package pl.edu.agh.kis.woznwojc;

import java.math.BigInteger;

/**
 * Class that handles calculations from parsed {@link Command commands}. Process of getting computation done for often
 * parameters is speeded up by using {@link Cache}.
 *
 * @author Wojciech Woźniczka
 *
 * @see Cache
 */
public class FunctionHandler
{
    /**
     * Method that prints out result of requested calculation ({@link Command}).
     * At first it checks if requested operation is not already cached in {@link Cache}
     * then if not - it calculate it calling proper methods and puts result in cache.
     *
     * @param userCommand Command that have all required information about calculation that needs to be done
     * @param dataCache Cache used to speed up calculation process
     *
     * @see Command
     * @see Cache
     */
    static void getFunction(Command userCommand, Cache dataCache)
    {
        String dataStr = concatenateDataVector(userCommand, "");
        String dataOutStr = concatenateDataVector(userCommand, " and ");
        String result = dataCache.get(userCommand.operationType + "f" + dataStr);

        if(result != null)
        {
            if(!userCommand.rawOut) System.out.print(userCommand.funcName + " of " + dataOutStr + " is ");
            System.out.println(result.substring(result.indexOf("f")+1, result.length()));
        }
        else
        {
            String output = computeFunction(userCommand);
            if(!userCommand.rawOut) System.out.print(userCommand.funcName + " of " + dataOutStr + " is ");
            System.out.println(output);
            dataCache.add(userCommand.operationType + "f" + userCommand.operationData.get(0), output);
        }

    }

    /**
     * Method chooses proper computation function using {@link Command#operationType} nad returns outcome from this function
     *
     * @param userCommand Command used to check what {@link Command#operationType} needs to be done
     * @return Result of calculating proper function
     *
     * @see #computeFactorial(String)
     * @see #computeAddition(String, String)
     * @see #computeSubtraction(String, String)
     * @see #computeMultiplication(String, String)
     * @see #computeDivision(String, String)
     */
    static String computeFunction(Command userCommand)
    {
        switch(userCommand.operationType)
        {
            case 0: return computeFactorial(userCommand.operationData.get(0));
            case 1: return computeAddition(userCommand.operationData.get(0), userCommand.operationData.get(1));
            case 2: return computeSubtraction(userCommand.operationData.get(0), userCommand.operationData.get(1));
            case 3: return computeMultiplication(userCommand.operationData.get(0), userCommand.operationData.get(1));
            case 4: return computeDivision(userCommand.operationData.get(0), userCommand.operationData.get(1));
            default: return null;
        }
    }

    /**
     * Method takes {@link Command#operationData} and concatenates each string to one separated by <b>split</b> argument
     *
     * @param userCommand Command used to get {@link Command#operationData calculation values}
     * @param split string that is inserted between words
     * @return concatenated String
     *
     * @see Command#operationData
     */
    static String concatenateDataVector(Command userCommand, String split)
    {
        String calcData = "";

        for (String singleData : userCommand.operationData)
        {
            calcData += singleData + split;
        }

        return calcData.substring(0, calcData.length() - split.length());
    }

    /**
     * Method that computes factorial
     *
     * @param param number from which factorial will be computed
     * @return factorial of <b>param</b>
     */
    private static String computeFactorial(String param)
    {
        BigInteger result = new BigInteger("1");

        for(int i=2; i<=Integer.parseInt(param); ++i)
        {
            result = result.multiply(new BigInteger("" + i));
        }

        return result.toString();
    }

    /**
     * Method that computes addition
     *
     * @param x summand
     * @param y summand
     * @return <b>x</b> + <b>y</b>
     */
    private static String computeAddition(String x, String y)
    {
        return (new BigInteger(x)).add(new BigInteger(y)).toString();
    }

    /**
     * Method that computes subtraction
     *
     * @param x minunend
     * @param y subtrahend
     * @return <b>x</b> - <b>y</b>
     */
    private static String computeSubtraction(String x, String y)
    {
        return (new BigInteger(x)).subtract(new BigInteger(y)).toString();
    }

    /**
     * Method that computes multiplication
     *
     * @param x factor
     * @param y factor
     * @return <b>x</b> * <b>y</b>
     */
    private static String computeMultiplication(String x, String y)
    {
        return (new BigInteger(x)).multiply(new BigInteger(y)).toString();
    }

    /**
     * Method that computes division
     *
     * @param x divident
     * @param y divisor
     * @return <b>x</b> / <b>y</b>
     */
    private static String computeDivision(String x, String y)
    {
        return (new BigInteger(x)).divide(new BigInteger(y)).toString();
    }
}