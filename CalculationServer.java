package pl.edu.agh.kis.woznwojc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class that handles communication between user and app.
 *
 *  @author Wojciech Woźniczka
 */
public class CalculationServer
{
    Cache dataCache;
    byte defaultPriority = 10;
    byte serverErrArg = -1;
    int maxCacheSize = 1000;
    int cacheTTLms = 1500000;
    boolean errNaN = false;
    CalculationQueue<Command> pQueue;


    public static void main(String args[])
    {
        CalculationServer calcSrv = new CalculationServer(args);

        for(;;)
        {
            calcSrv.getUserInput();
            calcSrv.runCalculations();
            calcSrv.dataCache.cleanUp();
        }
    }

    CalculationServer(String[] args)
    {
        setServerParams(args);
        dataCache = new WojcWoznCache(maxCacheSize, cacheTTLms);
        pQueue = new WojcWoznCalculationQueue();
    }

    public void getUserInput()
    {
        System.out.println("Enter number of calculations to enqueue:");
        Scanner usrInput = new Scanner(System.in);
        int inputNum;

        for(;;)
        {
            try{inputNum = Integer.parseInt(usrInput.nextLine()); break;}
            catch(Exception e){System.out.println("Please enter correct number");}
        }

        if(inputNum < 1) inputNum = 1;

        for(int i=0; i<inputNum; ++i)
        {
            System.out.println("Enter calculation command:");
            String[] usrArgs = usrInput.nextLine().split(" +");
            pQueue.add(new Command(usrArgs));
        }
    }

    boolean setServerParams(String[] args)
    {
        for (byte i = 1; i < args.length; ++i)
        {
            int val = 0;

            try
            {
                Matcher matcherVal = Pattern.compile("^-?[0-9]+$").matcher(args[i+1]);
                if(matcherVal.find())
                {
                    val = Integer.parseInt(args[i+1]);
                    errNaN = false;
                }
                else
                    errNaN = true;
            }
            catch(Exception e)
            {
                errNaN = true;
            }

            Matcher matcherPriority = Pattern.compile("^-n$").matcher(args[i]);
            Matcher matcherTTL = Pattern.compile("^-t$").matcher(args[i]);
            Matcher matcherCacheSize = Pattern.compile("^-s$").matcher(args[i]);

            if (matcherPriority.find() && !errNaN)
                defaultPriority = (byte) val;
            if (matcherTTL.find() && !errNaN)
                cacheTTLms = val;
            if (matcherCacheSize.find() && !errNaN)
                maxCacheSize = val;
            else
            {
                serverErrArg = i;
                return true;
            }
        }

        return false;
    }

    void runCalculations()
    {
        for(; 0<pQueue.size();)
        {
            Command userCommand = pQueue.pull();

            if(userCommand.errArg != -1)
                Output.printUserErr(userCommand);
            else
            {
                FunctionHandler.getFunction(userCommand, dataCache);
            }
        }
    }

}
