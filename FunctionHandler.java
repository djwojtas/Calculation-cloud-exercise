package pl.edu.agh.kis.woznwojc;

import java.math.BigInteger;

/** Class that handles process of calculating factorial
 *
 *  @author Wojciech Woźniczka
 */
public class FunctionHandler
{
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

    static String computeFunction(Command userCommand)
    {
        switch(userCommand.operationType)
        {
            case 0: return computeFactorial(userCommand.operationData.get(0), "1", "1");
            case 1: return computeAddition(userCommand.operationData.get(0), userCommand.operationData.get(1));
            case 2: return computeSubtraction(userCommand.operationData.get(0), userCommand.operationData.get(1));
            case 3: return computeMultiplication(userCommand.operationData.get(0), userCommand.operationData.get(1));
            case 4: return computeDivision(userCommand.operationData.get(0), userCommand.operationData.get(1));
            default: return null;
        }
    }

    static String concatenateDataVector(Command userCommand, String split)
    {
        String calcData = "";

        for (String singleData : userCommand.operationData)
        {
            calcData += singleData + split;
        }

        return calcData.substring(0, calcData.length() - split.length());
    }

    private static String computeFactorial(String param, String startPoint, String startVal)
    {
        BigInteger result = new BigInteger(startVal);

        for(int i=Integer.parseInt(startPoint)+1; i<=Integer.parseInt(param); ++i)
        {
            result = result.multiply(new BigInteger("" + i));
        }

        return result.toString();
    }

    private static String computeAddition(String x, String y)
    {
        return (new BigInteger(x)).add(new BigInteger(y)).toString();
    }

    private static String computeSubtraction(String x, String y)
    {
        return (new BigInteger(x)).subtract(new BigInteger(y)).toString();
    }

    private static String computeMultiplication(String x, String y)
    {
        return (new BigInteger(x)).multiply(new BigInteger(y)).toString();
    }

    private static String computeDivision(String x, String y)
    {
        return (new BigInteger(x)).divide(new BigInteger(y)).toString();
    }
}