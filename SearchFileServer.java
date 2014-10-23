/**
 * SearchFileServer
 *
 * SearchFileServer performs logic requested by the program SearchFileClient.
 *
 * SearchFileServer takes input from Named Pipes, that input being a file name
 * and a target string. The file is then scanned for the target word.
 * For every line the target word will be printed out with how many times it
 * was found on that line, and a total of all the times the target word was found
 * will be calculated. All of this information will be passed to the server using a
 * Named Pipe. The input file and the target word are also passed through the pipe.
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class SearchFileServer
{
    private static BufferedReader reader;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        //Server output pipe
        NamedPipe serverPipe = new NamedPipe(SearchFileUtils.SERVER_OUTPUT_PIPE);
        String callSign = "";
        String fileName = "";
        String targetString = "";
        int timesFound = 0;
        int lineCount = 1;
        int exitVal = 0;
        boolean error = false;

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        df.setTimeZone(TimeZone.getTimeZone("US/Central"));

        //Process start message
        System.out.println(SearchFileUtils.SERVER_PREFIX + "Running on" + " " + df.format(date) + "\n");

        Runtime rt = Runtime.getRuntime();

        try
        {
            //Retrieve call sign from client to confirm client identity
            callSign = serverPipe.retrieveFromPipe(SearchFileUtils.CLIENT_OUTPUT_PIPE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Check call sign for identity confirmation
        if (callSign.equals(SearchFileUtils.SERVER_CONFIRMATION)) 
        {
            try 
            {
                //Send call sign to confirm to the client that this is the proper server
                System.out.println(SearchFileUtils.SERVER_PREFIX + SearchFileUtils.SYNCH_CLIENT);
                serverPipe.sendThroughPipe(SearchFileUtils.CLIENT_CONFIRMATION);
                serverPipe.waitFor();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //Retrieve File Name from the client
        fileName = serverPipe.retrieveFromPipe(SearchFileUtils.CLIENT_OUTPUT_PIPE);

        //Retrieve Target String from the client
        targetString = serverPipe.retrieveFromPipe(SearchFileUtils.CLIENT_OUTPUT_PIPE);

        ArrayList<String> list = new ArrayList<String>();

        int timesFoundTotal = 0;

        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
            String line = "";

            //Read in the file and search for the target word
            while((line = bufferedReader.readLine()) != null)
            {
                //Regular Expression to find the amount of occurences of a string in the current line of text
                timesFound = line.length() - line.replaceAll(Pattern.quote(targetString.substring(0,1)) + "(?=" + Pattern.quote(targetString.substring(1)) + ")", "").length();
                timesFoundTotal += timesFound;

                //If target word found on line, create report and add to list
                if(timesFound > 1)
                {
                    list.add("Target >>" + targetString + "<< Appeared on Line " + lineCount + " a Total of " + timesFound + " Times");
                }
                if(timesFound == 1)
                {
                    list.add("Target >>" + targetString + "<< Appeared on Line " + lineCount + " a Total of " + timesFound + " Time");
                }

                lineCount++;
            }
        }
        catch(FileNotFoundException e)
        {
            error = true;
        }
        catch(IOException e)
        {
            error = true;
        }

        //Total up the amount of times the target word was found and create a printout
        if(error)
        {
            list.add("The file >>>" + fileName + "<<< Could not be Found");
        }
        else if(timesFoundTotal > 1 || timesFoundTotal == 0)
        {
            list.add("Target >>" + targetString + "<< in File >>" + fileName + "<< Appeared a Total of " + timesFoundTotal + " Times");
        }
        else if(timesFoundTotal == 1)
        {
            list.add("Target >>" + targetString + "<< in File >>" + fileName + "<< Appeared a Total of " + timesFoundTotal + " Time");
        }

        //Send the printouts from the list to the client
        serverPipe.sendMultipleLinesThroughPipe(list);

        serverPipe.retrieveFromPipe(SearchFileUtils.CLIENT_OUTPUT_PIPE);

        serverPipe.sendThroughPipe(SearchFileUtils.END_OF_FILE);
        serverPipe.waitFor();

        date = new Date();
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        df.setTimeZone(TimeZone.getTimeZone("US/Central"));

        serverPipe.retrieveFromPipe(SearchFileUtils.CLIENT_OUTPUT_PIPE);

        //Terminate the server process
        System.out.println(SearchFileUtils.SERVER_PREFIX + "Terminated On " + df.format(date));

        //Remove the pipe
        serverPipe.destroyPipe();
    }
}

