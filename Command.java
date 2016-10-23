package pl.edu.agh.kis.woznwojc;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class that contains parsed information about user input
 *
 *  @author Wojciech Woźniczka
 */
public class Command
{
    /**
     *  determines if result will be only number or whole output string
     *  true for Raw output and false for Normal
     */
    boolean rawOut;

    /**
     *  Determines type of calculation eg. factorial, multiplication, etc.
     *  0 - factorial;
     */
    byte operationType;

    /**
     *  Return index of incorrect argument (user input), -1 for good input, -2 for no input, -3 for too few values for given function, -4 for too many values for given function
     */
    byte errArg = -1;

    /**
     *  Determines priority of task (-20 fastest to 19 slowest)
     */
    public byte priority = 10;

    /**
     *  Vector that hold values that will be used for calculation
     */
    Vector<String> operationData = new Vector<>(2);

    String[] userArgs;

    boolean errNaN = false;

    String funcName;

    Command(String[] usrInput)
    {
        userArgs = usrInput;
        parseUserInput(usrInput);
    }

    /**
     * Method parses user input arguments to Command object
     * @param usrInput command line arguments that user passes to server
     * @return Indicates occurrence of error. True if error occurred false if not.
     */
    public boolean parseUserInput(String[] usrInput)
    {
        if(usrInput.length == 0)
        {
            errArg = -2;
            return true;
        }

        if(!usrInput[0].equals("calc"))
        {
            errArg = 0;
            return true;
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
                return true;
            }
        }

        return checkCalculationFunction();
    }

    private boolean checkCalculationFunction()
    {
        switch(operationType)
        {
            case 0: if(operationData.size() < 1) errArg = -3; else if(operationData.size() > 1) errArg = -4; funcName = "Factorial"; break;
            default: if(operationData.size() < 2) errArg = -3; else if(operationData.size() > 2) errArg = -4; funcName = "Addition"; break;
        }

        if(errArg != 2)
            return true;
        else
            return false;
    }
}
