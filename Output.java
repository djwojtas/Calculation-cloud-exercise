package pl.edu.agh.kis.woznwojc;

/** Class that handles process of passing correct form of calculation outcome to user
 *
 *  @author Wojciech Woźniczka
 */
public class Output
{
    public static void printUserErr(Command userCommand)
    {
        if(userCommand.errArg == -1)
            System.out.println("No errors detected.");
        else if(userCommand.errArg == -2)
            System.out.println("Error: no input arguments detected!");
        else if(userCommand.errArg == -3)
            System.out.println("Error: not enough values to compute given function.");
        else if(userCommand.errArg == -4)
            System.out.println("Error: too many values to compute given function.");
        else if(userCommand.errNaN)
            System.out.println("Error: argument " + (userCommand.errArg+2) + " (" + userCommand.userArgs[userCommand.errArg+1] + ") is not a number.");
        else
            System.out.println("Error in " + (userCommand.errArg+1) + " argument (" + userCommand.userArgs[userCommand.errArg] + ").");

        System.out.println("\n\nDISPLAYING HELP:\n\nNAME\n"
                + "  CalculationServer - cloud calculation platform\n\n"
                + "SYNOPSIS\n"
                + "  calc -t [OPERATION TYPE] [OPTIONS] VALUE1 [VALUE2] ..\n\n"
                + "DESCRIPTION\n"
                + "  Compute output from given function and values\n\n"
                + "  -f [OPERATION NUMBER]\n"
                + "    Possible operations:\n"
                + "      0 - factorial, require 1 VALUE, returns factorial of 1st VALUE\n\n"
                + "      1 - addition, require 2 VALUEs, returns addition of 1st VALUE + 2nd VALUE\n\n"
                + "      2 - subtraction, require 2 VALUEs, returns subtraction of 1st VALUE - 2nd VALUE\n\n"
                + "      3 - multiplication, require 2 VALUEs, returns multiplication of 1st VALUE * 2nd VALUE\n\n"
                + "      4 - division, require 2 VALUEs, returns division of 1st VALUE / 2nd VALUE\n\n"
                + "  -r\n"
                + "    Return raw number instead of string, helpful for parsing output.\n\n"
                + "  -p [PRIORITY]\n"
                + "    Sets priority of task from -20 (done quickest) to 19 (done slowest)\n\n"
                + "  VALUEs are integer values used to compute given function\n\n\n"
                + "AUTHOR\n"
                + "  Wojciech Wozniczka\n\n");
    }

    public static void printServerErr(CalculationServer server, String[] args)
    {
        if(server.serverErrArg == -1)
            System.out.println("No errors detected.");
        else if(server.errNaN)
            System.out.println("Error: argument " + (server.serverErrArg+2) + " (" + args[server.serverErrArg+1] + ") is not a number.");
        else
            System.out.println("Error in " + (server.serverErrArg+1) + " argument (" + args[server.serverErrArg] + ").");

        System.out.println("\n\nDISPLAYING HELP:\n\nNAME\n"
                + "  CalculationServer startup - set basic values\n\n"
                + "SYNOPSIS\n"
                + "  java CalculationServer [OPTIONS]\n\n"
                + "DESCRIPTION\n"
                + "  On a startup you can set parameters for CalculationServer\n\n"
                + "  -t [NUMBER]\n"
                + "    Set cache record time to live in ms, default is 15000\n\n"
                + "  -p [NUMBER]\n"
                + "    Set default calc command priority from -20 (done quickest) to 19 (done slowest), default is 10\n\n"
                + "  -s [NUMBER]\n"
                + "    Sets cache max capacity, default is 100\n\n"
                + "AUTHOR\n"
                + "  Wojciech Wozniczka\n\n");
    }
}
