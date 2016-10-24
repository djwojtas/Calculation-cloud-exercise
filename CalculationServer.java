package pl.edu.agh.kis.woznwojc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

/**
 * Class offering user possibility to do calculations on big integer numbers using simple command line interface.
 * <p>At initialization phase options (<b>defaultPriority</b>, <b>maxCacheSize</b> and <b>cacheTTLms</b>, see {@link #setServerParams(String[])})
 * can be set, or if not provided they are set to default values. If incorrect arguments are given, help will
 * be displayed with proper error information.
 *
 * <p>While class is running it will first ask for number of calculation commands we want to enqueue (see {@link #getUserInput()})
 * and then it will ask line by line for calculation commands.
 *
 * @author Wojciech Wozniczka
 */
public class CalculationServer
{
    /**
     * {@link Cache} used to store often calculated functions
     *
     * @see Cache
     */
    private Cache dataCache;

    /**
     * Default priority of calculation commands enqueued without -p argument in {@link #getUserInput()}
     *
     * @see #getUserInput()
     */
    private byte defaultPriority = 10;

    /**
     * Determines bad argument index from parsing {@link #setServerParams(String[])}
     *
     * @see #setServerParams(String[])
     */
    byte serverErrArg = -1;

    /**
     * Determines maximum capacity of {@link Cache} (in count of elements)
     *
     * @see Cache
     */
    private int maxCacheSize = 100;

    /**
     * Determines maximum TTL (time ti live) for record in {@link Cache}
     *
     * @see Cache
     */
    private int cacheTTLms = 15000;

    /**
     * Determines if error from {@link #setServerParams(String[])} was a argument error, or number error for example:<br>
     * <tt>-badarg 123</tt> is an argument error<br>
     * <tt>-p 1abc3</tt> is a number error
     *
     * @see Output#printServerErr(CalculationServer, String[])
     * @see #setServerParams(String[])
     */
    boolean errNaN = false;

    /**
     * Priority Queue used to enqueue user calculation commands
     *
     * @see CalculationQueue
     * @see #getUserInput()
     */
    private CalculationQueue<Command> pQueue;

    /**
     * Main method of whole application. Creates instance of {@link CalculationServer} and then runs infinite loop
     * of asking user for input (see {@link #getUserInput()}) and cleaning up cache (see {@link Cache#cleanUp()})
     *
     * @param args Command line arguments, used to set server parameters in {@link #setServerParams(String[])}
     *
     * @see #setServerParams(String[])
     * @see #getUserInput()
     * @see Cache#cleanUp()
     */
    public static void main(String[] args)
    {
        CalculationServer calcSrv = new CalculationServer(args);

        for(;;)
        {
            calcSrv.getUserInput();
            calcSrv.runCalculations();
            calcSrv.dataCache.cleanUp();
        }
    }

    /**
     * Constructor that takes array of string arguments and parses them using {@link #setServerParams(String[] args)}
     * Then creates {@link Cache} and {@link CalculationQueue} with given arguments (or default ones).
     *
     * @param args  arguments from which server parameters will be parsed.
     *
     * @see #main(String[])
     * @see #setServerParams(String[] args)
     * @see Cache
     * @see CalculationQueue
     */
    private CalculationServer(String[] args)
    {
        if(setServerParams(args))
        {
            Output.printServerErr(this, args);
            exit(0);
        }
        dataCache = new WojcWoznCache(maxCacheSize, cacheTTLms);
        pQueue = new WojcWoznCalculationQueue();
    }

    /**
     * Method that fills {@link CalculationQueue} with proper arguments using {@link Command#parseUserInput(String[])}.
     * Number of record pushed to queue is dependant from number that user gives at first.
     * <p>If user will enter incorrect number at first, method will keep asking.
     * <p>If user will enter good number of calculation commands but will make mistake in calc command itself
     *    function will call {@link Output#printUserErr(Command)} which print adequate error and help.
     *
     * @see CalculationQueue
     * @see Command#parseUserInput(String[])
     * @see Output#printUserErr(Command)
     */
    private void getUserInput()
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
            pQueue.add(new Command(usrArgs, defaultPriority));
        }
    }

    /**
     * Method that parse given arguments and sets {@link CalculationServer} parameters to them.
     * If an error occurs {@link CalculationServer#serverErrArg} will be set to bad argument index and
     * {@link CalculationServer#errNaN} will be set to true if error occurred while parsing number.
     *
     * @param args arguments that method will parse
     * @return Method return true if error occurred and false if not
     *
     * @see #serverErrArg
     * @see #errNaN
     */
    private boolean setServerParams(String[] args)
    {
        for (int i = 0; i < args.length; ++i)
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

            Matcher matcherPriority = Pattern.compile("^-p$").matcher(args[i]);
            Matcher matcherTTL = Pattern.compile("^-t$").matcher(args[i]);
            Matcher matcherCacheSize = Pattern.compile("^-s$").matcher(args[i]);

            if (matcherPriority.find() && !errNaN)
            {
                defaultPriority = (byte) val; ++i;
            }
            else if (matcherTTL.find() && !errNaN)
            {
                cacheTTLms = val; ++i;
            }
            else if (matcherCacheSize.find() && !errNaN)
            {
                maxCacheSize = val; ++i;
            }
            else
            {
                serverErrArg = (byte)i;
                return true;
            }
        }

        return false;
    }

    /**
     * Method that will run calculations until {@link CalculationQueue} is empty.
     * If calculation will be done without errors it will print result. If error will occur {@link Output#printUserErr(Command)}
     * will be used to print proper error and help.
     *
     * @see CalculationQueue
     * @see Output#printUserErr(Command)
     */
    private void runCalculations()
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
