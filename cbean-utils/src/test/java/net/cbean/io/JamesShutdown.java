package net.cbean.io;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
 
/** Class to shutdown Apache James Mail Server via TCP/IP 
  * Copyright: Angsuman Chakraborty. All rights reserved.
*/
public class JamesShutdown {
    // Default values. Can be overridden by specifying the corresponding System property using -D option.
    // For example: To specify a different host use: -Dhost=samba
    private static String host = "localhost";
    private static int port = 4555;
    private static String login = "root";
    private static String password = "root";    
    
    private static final String CRLF = "\r\n"; // Carriage return followed by line feed
    private static final String SHUTDOWN = "shutdown";
    private static boolean DEBUG = false;
    
    /** Shuts down Apache James Mail Server by passing shutdown command via simulated telnet session. 
     *
     *  Error handling: The program terminates with error code 1 on error. A terse message indicating
     *  the error is displayed on console (System.out).
     *
     *  Options: -Dhost=hostname      <- Specify the host; default localhost
     *  Options: -Dport=portnumber    <- Specify the port; default 4555
     *  Options: -Dlogin=login        <- Specify the login; default root
     *  Options: -Dpassword=password  <- Specify the password; default root
     */
    public static void main(String args[]) {
        // Override with system properties, if available
        host = System.getProperty("host", host);
        Integer Port = Integer.getInteger("port");
        if(Port != null) port = Port.intValue();
        login = System.getProperty("login", login);
        password = System.getProperty("password", password);
        String debug = System.getProperty("debug", "false"); // Use -Ddebug=true for debugging
        if("true".equalsIgnoreCase(debug)) DEBUG = true;
        
        Socket socket = null;
        boolean error = false;
        try {
            socket = new Socket(host, port);
            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            // Conversation; simulating telnet session with James server
            readLines(r, 3);
            writeLine(w, login);
            readLines(r, 1);
            writeLine(w, password);
            readLines(r, 1);
            writeLine(w, SHUTDOWN);
            readLines(r, 1);
        } catch(IOException ioe) {
            System.out.println("I/O Error: " + ioe);
            error = true;
        } finally {        
            if(socket != null) {
                try {
                    socket.close();
                } catch(IOException ignore) {}
            }
        }
        if(error) System.exit(1);
    }
    
    /** Read and discard count lines from BufferedReader r */
    public static void readLines(BufferedReader r, int count) throws IOException {
        for(int i = 0;i < count;i++) {
            String t = r.readLine();
            if(DEBUG) System.out.println(t);
        }
    }
    
    /** Write out to BufferedWriter w and flush */
    public static void writeLine(BufferedWriter w, String out) throws IOException {
        w.write(out + CRLF, 0, (out + CRLF).length());
        w.flush();
    }
}