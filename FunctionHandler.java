package pl.edu.agh.kis.woznwojc;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        /*else if(userCommand.enableClose && ((result = dataCache.getClose(userCommand.operationType + "f" + userCommand.operationData.get(0))) != null))
        {
            //currently factorial exclusive feature
            String output = computeFactorial(userCommand.operationData.get(0), result.substring(result.indexOf("f") + 1, result.indexOf(" ")), result.substring(result.indexOf(" ") + 1, result.length()));
            if(!userCommand.rawOut) System.out.print(userCommand.funcName + " of " + dataOutStr + " is ");
            System.out.println(output);
            dataCache.add("0f" + userCommand.operationData.get(0),output);
        }*/
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

    /*static void getFactorial(Command userCommand, Cache dataCache)
    {
        String result = dataCache.get(userCommand.operationType + "f" + userCommand.operationData.get(0));

        if(result != null)
        {
            if(!userCommand.rawOut) System.out.print("Factorial of " + userCommand.operationData.get(0) + " is ");
            System.out.println(result.substring(result.indexOf("f")+1, result.length()));
        }
        else if((result = dataCache.getClose(userCommand.operationType + "f" + userCommand.operationData.get(0))) != null)
        {
            String output = computeFactorial(userCommand.operationData.get(0), result.substring(result.indexOf("f") + 1, result.indexOf(" ")), result.substring(result.indexOf(" ") + 1, result.length()));
            if(!userCommand.rawOut) System.out.print("Factorial of " + userCommand.operationData.get(0) + " is ");
            System.out.println(output);
            dataCache.add("0f" + userCommand.operationData.get(0),output);
        }
        else
        {
            String output = computeFactorial(userCommand.operationData.get(0), "1", "1");
            if(!userCommand.rawOut) System.out.print("Factorial of " + userCommand.operationData.get(0) + " is ");
            System.out.println(output);
            dataCache.add("0f" + userCommand.operationData.get(0),output);
        }
    }*/

    private static String computeFactorial(String param, String startPoint, String startVal)
    {
        BigInteger result = new BigInteger(startVal);

        for(int i=Integer.parseInt(startPoint)+1; i<=Integer.parseInt(param); ++i)
        {
            result = result.multiply(new BigInteger("" + i));
        }

        return result.toString();
    }

    public static void main(String[] args)
    {
        Cache test = new WojcWoznCache(100, 100);
        String[] testStr = {"calc", "-f", "0", "5"};
        System.out.println(FunctionHandler.computeFactorial("20", "4", "24"));

        Matcher testMatch = Pattern.compile("[^([0-9]+f)]").matcher("0f213123");
        System.out.println(testMatch.find());

        System.out.println(testMatch.group(0));

        String testCase = "0f123123";

        System.out.println(testCase.substring(testCase.indexOf("f")+1, testCase.length()));

        testCase = "0f123123 32323";

        System.out.println(testCase.substring(testCase.indexOf("f")+1, testCase.indexOf(" ")));
        System.out.println(testCase.substring(testCase.indexOf(" ")+1, testCase.length()));
    }
}