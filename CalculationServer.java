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
    PriorityQueue<Command> pQueue;
    Cache dataCache;
    byte defaultPriority = 10;
    byte serverErrArg = -1;
    int maxCacheSize = 1000;
    int cacheTTLms = 15000;
    boolean errNaN = false;


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
        pQueue = new PriorityQueue<Command>(10, new Comparator<Command>()
        {
            public int compare(Command x, Command y)
            {
                if(x.priority < y.priority) return -1;
                if(x.priority > y.priority) return 1;
                return 0;
            }
        });

        setServerParams(args);

        dataCache = new Cache(maxCacheSize, cacheTTLms);
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
                Matcher matcherVal = Pattern.compile("^[0-9]+$").matcher(args[i+1]);
                if(matcherVal.find())
                {
                    val = Integer.parseInt(args[i+1]);
                    errNaN = false;
                }
                else
                {
                    errNaN = true;
                }
            }

            catch(Exception e)
            {
                e.printStackTrace();
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
        for(int i=0; i<pQueue.size(); ++i)
        {
            Command userCommand = pQueue.poll();

            if(userCommand.errArg != -1)
                Output.printUserErr(userCommand);
            else
            {
                runProperFunction(userCommand);
            }
        }
    }

    void runProperFunction(Command userCommand)
    {
        switch(userCommand.operationType)
        {
            case 0: FunctionHandler.getFactorial(userCommand, dataCache);
        }
    }

}
