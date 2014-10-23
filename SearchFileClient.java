/**
 * SearchFileClient
 *
 * SearchFileClient launches the SearchFileServer process, sends user input information to the process,
 * the prints out the results of SearchFileServer.
 *
 * SearchFileClient starts a named pipe for its own output to the server then spawns the server process
 * (SearchFileServer). It then sends a "call sign" to the server to confirm its identity and receives
 * one from the server in turn. After which, it will take two separate inputs from the keyboard.
 * A File Name and a Target Word. It will then send those pieces of input to the server using the
 * NamedPipe. The server will then perform its logic (@see SearchFileServer) using those inputs and
 * send it back to the client to be printed. This process will terminate when the server sends a specific
 * command (@see SearchFileUtils.END_OF_FILE).
 *
 * The server confirms the client using call signs that are stored in SearchFileUtils.
 *
 * This class requires SearchFileUtils and NamedPipe to run, as well as the imports below.
 *
 * @author Aaron Weaver         (waaronl@okstate.edu)
 * @version 1.0
 * @since 2014-09-30
 */

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SearchFileClient
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        //Create client's named pipe
        NamedPipe clientPipe = new NamedPipe(SearchFileUtils.CLIENT_OUTPUT_PIPE);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        df.setTimeZone(TimeZone.getTimeZone("US/Central"));

        System.out.println("\n");

        System.out.println(SearchFileUtils.CLIENT_PREFIX + "Running On" + " " +  df.format(date));

        //Start the server
        ProcessBuilder processBuilder = new ProcessBuilder("java", "SearchFileServer");
        processBuilder.inheritIO();
        processBuilder.start();

        Process server = null;
        Runtime rt = null;

        try
        {
            //Send the server confirmation through the pipe
            clientPipe.sendThroughPipe(SearchFileUtils.SERVER_CONFIRMATION);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //Retrieve server confirmation
        String callSign = clientPipe.retrieveFromPipe(SearchFileUtils.SERVER_OUTPUT_PIPE);

        if (callSign.equals(SearchFileUtils.CLIENT_CONFIRMATION))
        {
            System.out.println(SearchFileUtils.CLIENT_PREFIX + SearchFileUtils.SYNCH_SERVER + "\n");
        }
        else
        {
            System.out.println(SearchFileUtils.CLIENT_PREFIX + SearchFileUtils.FAILURE_MESSAGE);
            System.exit(1);
        }

        String fileName = "";
        String targetString = "";

        System.out.println(SearchFileUtils.CLIENT_PREFIX + SearchFileUtils.ENTER_FILENAME);

        //Retrieve user input: File Name
        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine())
        {
            fileName = scan.nextLine();
            System.out.println("");
            clientPipe.sendThroughPipe(fileName);
        }

        //Retrieve user input: Target String
        System.out.println(SearchFileUtils.CLIENT_PREFIX + SearchFileUtils.ENTER_TARGET_STRING);
        if (scan.hasNextLine())
        {
            targetString = scan.nextLine();
            System.out.println("");
            clientPipe.sendThroughPipe(targetString);
        }

        System.out.println(SearchFileUtils.CLIENT_PREFIX + "Input File >>>" + fileName + "<<<");
        System.out.println(SearchFileUtils.CLIENT_PREFIX + "Target >>>" + targetString + "<<<\n");
        //String serverOutput = clientPipe.retrieveFromPipe(SearchFileUtils.SERVER_OUTPUT_PIPE);

        //Retrieve logic output from server
        ArrayList<String> list = clientPipe.retrieveMultipleLinesFromPipe(SearchFileUtils.SERVER_OUTPUT_PIPE);
        clientPipe.waitFor();
        for (int i = 0; i < list.size(); i++)
        {
            //Print logic output from server
            System.out.println(SearchFileUtils.CLIENT_PREFIX + list.get(i));
        }

        //Confirm the client is done printing
        clientPipe.sendThroughPipe(SearchFileUtils.CLIENT_DONE_PRINTING);

        System.out.println("");
        //Retrieve confirmation that the end of the file has been reached
        String eofSend = clientPipe.retrieveFromPipe(SearchFileUtils.SERVER_OUTPUT_PIPE);
        clientPipe.waitFor();

        //Retrieving "Server-EOF" end of file command
        if(eofSend.equals(SearchFileUtils.END_OF_FILE))
        {
            date = new Date();
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            df.setTimeZone(TimeZone.getTimeZone("US/Central"));
            System.out.println(SearchFileUtils.CLIENT_PREFIX + "Terminated On " + df.format(date));
            clientPipe.sendThroughPipe(SearchFileUtils.CLIENT_DONE_PRINTING);
            //Remove the pipe
            clientPipe.destroyPipe();
            System.exit(0);
        }
    }
}
