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
                + "  -r\n"
                + "    Return raw number instead of string, helpful for parsing output.\n\n"
                + "  -p [PRIORITY]\n"
                + "    Sets priority of task from -20 (done quickest) to 19 (done slowest)\n\n"
                + "  VALUEs are integer values used to compute given function\n\n\n"
                + "AUTHOR\n"
                + "  Wojciech Wozniczka");
    }

    public static void printServerErr(Command userCommand, String[] args)
    {
        if(userCommand.errArg == -1)
            System.out.println("No errors detected.");
        else
            System.out.println("Error in " + (userCommand.errArg+1) + " argument (" + args[userCommand.errArg] + ").");

        System.out.println("\n\nDISPLAYING HELP:\n\nNAME\n"
                + "  CalculationServer - cloud calculation platform\n\n"
                + "SYNOPSIS\n"
                + "  calc -t [OPERATION TYPE] [OPTIONS] VALUE1 [VALUE2] ..\n\n"
                + "DESCRIPTION\n"
                + "  Compute output from given function and values\n\n"
                + "  -f [OPERATION NUMBER]\n"
                + "    Possible operations:\n"
                + "      0 - factorial, require 1 VALUE\n\n"
                + "  -r\n"
                + "    Return raw number instead of string, helpful for parsing output.\n\n"
                + "  -p [PRIORITY]\n"
                + "    Sets priority of task from -20 (done quickest) to 19 (done slowest)\n\n"
                + "  VALUEs are integer values used to compute given function\n\n\n"
                + "AUTHOR\n"
                + "  Wojciech Wozniczka\n\n");
    }
}
