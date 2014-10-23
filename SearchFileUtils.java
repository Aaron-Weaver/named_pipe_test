/**
 * SearchFileUtils
 *
 * SearchFileUtils is simply a utility class containing Strings to be used throughout the
 * programs SearchFileClient and SearchFileServer.
 *
 * For a more descriptive use of what each String is used look at the comments below or see
 * the following classes:
 * @see SearchFileClient
 * @see SearchFileServer
 *
 * @author Aaron Weaver     (waaronl@okstate.edu)
 * @since 2014-09-30
 * @version 1.0
 */

import java.lang.management.ManagementFactory;

public class SearchFileUtils {

    //Confirmation Strings
    public static String SERVER_CONFIRMATION = "serverStart";
    public static String CLIENT_CONFIRMATION = "waitingForInputFromClient";

    //Pipe making commands
    public static String MKFIFO_COMMAND = "mkfifo";
    public static String CLIENT_OUTPUT_PIPE = "clientOutput";
    public static String SERVER_OUTPUT_PIPE = "serverOutput";

    //Command for running the server process (Legacy)
    public static String RUN_SERVER_COMMAND = "java SearchFileServer";

    //Message for connection failure
    public static String FAILURE_MESSAGE = "Connection failed";

    //Prefixes before each command line print
    public static String CLIENT_PREFIX = ("Client : PID " + ManagementFactory.getRuntimeMXBean().getName().substring(0, 4) + " - ");
    public static String SERVER_PREFIX = ("Server : PID " + ManagementFactory.getRuntimeMXBean().getName().substring(0, 4) + " - ");

    //User input prompts
    public static String ENTER_FILENAME = "Enter File Name : ";
    public static String ENTER_TARGET_STRING = "Enter Target : ";

    //Synchronization messages
    public static String SYNCH_SERVER = "Synchronized to Server";
    public static String SYNCH_CLIENT = "Synchronized to Client";

    //End of file reached message
    public static String END_OF_FILE = "Server-EOF";

    //Client is done printing message
    public static String CLIENT_DONE_PRINTING = "clientDone";

}
