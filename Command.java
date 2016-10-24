package pl.edu.agh.kis.woznwojc;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that parse user calculation commands and store data about them
 *
 * @author Wojciech Wozniczka
 */
class Command
{
    /**
     *  Determines if result will be only number or whole output string (in {@link FunctionHandler#computeFunction(Command)}
     *  true for Raw output and false for Normal
     *
     *  @see FunctionHandler#computeFunction(Command)
     */
    boolean rawOut;

    /**
     * Determines type of calculation eg. factorial, multiplication, etc:
     * <ul>
     *     <li>0 - factorial</li>
     *     <li>1 - addition</li>
     *     <li>2 - subtraction</li>
     *     <li>3 - multiplication</li>
     *     <li>4 - division</li>
     * </ul>
     * Used for checking how much values function need in {@link #checkCalculationFunction()} and
     * to determine proper computation function in {@link FunctionHandler#computeFunction(Command)}
     *
     * @see #checkCalculationFunction()
     * @see FunctionHandler#computeFunction(Command)
     */
    byte operationType;

    /**
     * Return index of incorrect argument (user input)
     * <ul>
     *     <li>0 or higher for bad argument index</li>
     *     <li>-1 for good input</li>
     *     <li>-2 for no input</li>
     *     <li>-3 for too few values for given function</li>
     *     <li>-4 for too many values for given function</li>
     * </ul>
     * Used eventually be {@link Output#printUserErr(Command)}
     *
     * @see Output#printUserErr(Command)
     */
    byte errArg = -1;

    /**
     * Determines priority of task (-20 fastest to 19 slowest) to sort in {@link CalculationQueue}
     *
     * @see CalculationQueue
     */
    byte priority;

    /**
     * Vector that hold values that will be used for calculation in method called by {@link FunctionHandler#computeFunction(Command)}
     *
     * @see FunctionHandler#computeFunction(Command)
     */
    Vector<String> operationData = new Vector<>(2);

    /**
     * Stores arguments passed for parsing to {@link #parseUserInput(String[])}. Used eventually in
     * {@link Output#printUserErr(Command)} to print argument in which error occurred
     *
     * @see #parseUserInput(String[])
     * @see Output#printUserErr(Command)
     */
    String[] userArgs;

    /**
     * Determines if error from {@link #parseUserInput(String[])} was a argument error, or number error for example:<br>
     * <tt>-badarg 123</tt> is an argument error<br>
     * <tt>-p 1abc3</tt> is a number error
     *
     * @see Output#printUserErr(Command)
     * @see #parseUserInput(String[])
     */
    boolean errNaN = false;

    /**
     * Name of function like 'Factorial' or 'Multiplication'.
     * Used by {@link FunctionHandler#computeFunction(Command)} to print proper function name in result if
     * {@link #rawOut} is false.
     *
     * @see FunctionHandler#computeFunction(Command)
     * @see #rawOut
     */
    String funcName;

    /**
     * Constructor that fills {@link Command} object with data provided in constructor and by user using {@link #parseUserInput(String[])}
     *
     * @param usrInput user arguments array, like cmd arguments
     * @param defPriority default priority for {@link Command} if not provided by user in <b>usrInput</b>
     *
     * @see #parseUserInput(String[])
     */
    Command(String[] usrInput, byte defPriority)
    {
        userArgs = usrInput;
        parseUserInput(usrInput);
        priority = defPriority;
    }

    /**
     * Method that parse given arguments and sets {@link Command} parameters to them.
     * If an error occurs {@link Command#errArg} will be set to bad argument index and
     * {@link Command#errNaN} will be set to true if error occurred while parsing number.
     *
     * @param usrInput command line arguments that user passes to server
     *
     * @see #checkCalculationFunction()
     * @see #errArg
     * @see #errNaN
     */
    void parseUserInput(String[] usrInput)
    {
        if(usrInput.length == 0)
        {
            errArg = -2;
        }

        if(!usrInput[0].equals("calc"))
        {
            errArg = 0;
        }

        for(byte i=1; i<usrInput.length; ++i)
        {
            String arg = usrInput[i];

            int val = 0;

            try
            {
                Matcher matcherParam = Pattern.compile("^-?[0-9]+$").matcher(usrInput[i+1]);
                if(matcherParam.find())
                {
                    val = Integer.parseInt(usrInput[i+1]);
                    errNaN = false;
                }
                else
                    errNaN = true;
            }
            catch(Exception e)
            {
                errNaN = true;
            }

            Matcher matcherVal = Pattern.compile("^-?[0-9]+$").matcher(usrInput[i]);
            Matcher matcherFunc = Pattern.compile("^-f$").matcher(arg);
            Matcher matcherRaw = Pattern.compile("^-r$").matcher(arg);
            Matcher matcherPriority = Pattern.compile("^-p$").matcher(arg);

            if(matcherVal.find())
                operationData.add(arg);
            else if(matcherRaw.find())
                rawOut = true;
            else if(matcherFunc.find() && !errNaN)
            {
                operationType = (byte)val;
                ++i;
            }
            else if(matcherPriority.find() && !errNaN)
            {
                priority = (byte) val;
                if (priority < -20) priority = -20;
                else if (priority > 19) priority = 19;
                ++i;
            }
            else
            {
                errArg = i;
            }
        }
        checkCalculationFunction();
    }

    /**
     * Method that sets {@link #funcName} to proper function name and checks if {@link #operationData} size
     * (operation values) is proper for given {@link #operationType calcullation function}.
     * If not, {@link #errArg} is set to -3 if there is not enough values and -4 if too many.
     *
     * @see #funcName
     * @see #operationData
     * @see #errArg
     * @see Output#printUserErr(Command)
     */
    private void checkCalculationFunction()
    {
        switch(operationType)
        {
            case 0: funcName = "Factorial"; break;
            case 1: funcName = "Addition"; break;
            case 2: funcName = "Subtraction"; break;
            case 3: funcName = "Multiplication"; break;
            case 4: funcName = "Division"; break;
        }

        switch(operationType)
        {
            case 0: if(operationData.size() < 1) errArg = -3; else if(operationData.size() > 1) errArg = -4; break;
            default: if(operationData.size() < 2) errArg = -3; else if(operationData.size() > 2) errArg = -4; break;
        }
    }
}
