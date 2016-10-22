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
    static void getFactorial(Command userCommand, Cache dataCache)
    {
        String result = dataCache.get(userCommand.operationType + "f" + userCommand.operationData.get(0));

        if(result != null)
            System.out.println(result.substring(result.indexOf("f")+1, result.length()));
        else if((result = dataCache.getClose(userCommand.operationType + "f" + userCommand.operationData.get(0))) != null)
        {
            String output = computeFactorial(userCommand.operationData.get(0), result.substring(result.indexOf("f") + 1, result.indexOf(" ")), result.substring(result.indexOf(" ") + 1, result.length()));
            System.out.println(output);
            dataCache.add("0f" + userCommand.operationData.get(0),output);
        }
        else
        {
            String output = computeFactorial(userCommand.operationData.get(0));
            System.out.println(output);
            dataCache.add("0f" + userCommand.operationData.get(0),output);
        }
    }

    public static String computeFactorial(String param)
    {
        BigInteger result = new BigInteger("1");

        for(int i=2; i<=Integer.parseInt(param); ++i)
        {
            result = result.multiply(new BigInteger(("" + i)));
        }

        return result.toString();
    }

    public static String computeFactorial(String param, String startPoint, String startVal)
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
        Cache test = new Cache(100, 100);
        String[] testStr = {"calc", "-f", "0", "5"};
        getFactorial(new Command(testStr), test);
        System.out.println(FunctionHandler.computeFactorial("20"));
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