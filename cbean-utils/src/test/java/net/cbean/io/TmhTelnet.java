package net.cbean.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

public class TmhTelnet implements Runnable, TelnetNotificationHandler {   
	  
    private static TelnetClient tc = null;   
    private InputStream in;   
    private PrintStream out;
    private Thread tThread;
    private boolean connected=true;
    
    public static void main(String args[]){
    	TmhTelnet telnet=new TmhTelnet ("125.46.86.123",23);
    	telnet.tThread = new Thread(telnet);   
    	telnet.tThread.start();
    
        telnet.write("******");   
        telnet.write("******");
        telnet.writescript("command.txt");   
    } 
    
    public TmhTelnet(String host, int port) {   
    	intconnect(host,port);
    }   
  
    private void writescript(String filename) {   
        try {   
            if(new File(filename).exists()){   
                FileReader f = new FileReader(filename);   
                BufferedReader reader = new BufferedReader(f);   
                String command = "";   
                while(command !=null){   
                    command = reader.readLine();   
                    if(command!=null)write(command);   
                }   
            }   
        } catch (FileNotFoundException e) {   
            // TODO Auto-generated catch block   
            e.printStackTrace();   
        } catch (IOException e) {   
            // TODO Auto-generated catch block   
            e.printStackTrace();   
        }
    }
    
    private void write(String command) {   
        try{   
            System.out.println("command:>>>>>>>>>"+command);   
            out.println(command);
            out.flush();
            while(!tThread.getState().equals(Thread.State.BLOCKED));
        }catch(Exception e){   
            e.printStackTrace();   
        }   
    }   
  
    private boolean intconnect(String host, int port) {   
        try {   
            tc = new TelnetClient();   
            TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler(   
                    "VT100", false, false, true, false);   
            EchoOptionHandler echoopt = new EchoOptionHandler(true, false,   
                    true, false);   
            SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true,   
                    true, true, true);   
  
            tc.addOptionHandler(ttopt);   
            tc.addOptionHandler(echoopt);   
            tc.addOptionHandler(gaopt);
            tc.connect(host, port);   
            in = tc.getInputStream();   
            out = new PrintStream(tc.getOutputStream());  
            return true;   
        } catch (Exception e) {   
            e.printStackTrace();   
            try {   
                tc.disconnect();   
            } catch (IOException e1) {   
                // TODO Auto-generated catch block   
                e1.printStackTrace();   
            }   
            return false;   
        }          
    }   
       
    public void receivedNegotiation(int negotiation_code, int option_code) {   
        String command = null;   
        if (negotiation_code == TelnetNotificationHandler.RECEIVED_DO) {   
            command = "DO";   
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_DONT) {   
            command = "DONT";   
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_WILL) {   
            command = "WILL";   
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_WONT) {   
            command = "WONT";   
        }   
        System.out.println("Received " + command + " for option code "  
                + option_code);   
    }   
  
    /***************************************************************************  
     * Reader thread. Reads lines from the TelnetClient and echoes them on the  
     * screen.  
     **************************************************************************/  
    public void run() {   
        InputStream instr = tc.getInputStream();   
  
        try {   
  
            byte[] buff = new byte[1024];   
            int ret_read = 0;   
            do {   
                ret_read = instr.read(buff);   
                if (ret_read > 0) {   
                    System.out.print(new String(buff, 0, ret_read));   
                }   
            } while (ret_read >= 0); 
        } catch (Exception e) {   
            System.err.println("Exception while reading socket:"  
                    + e.getMessage());   
        }   
  
        try {   
            tc.disconnect();   
        } catch (Exception e) {   
            System.err.println("Exception while closing telnet:"  
                    + e.getMessage());   
        }   
    }   
}  