package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;


public class SocketOutputInputTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 23;
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("Starting CMD Server...");
			while(true){
				final Socket s = server.accept();
				Thread client = new Thread(new Runnable(){
					
					public void run() {
						
						CommandRunner crunner = new CommandRunner(new Dispatcher("cmd.properties"));
						try {
							PrintStream out = new PrintStream(s.getOutputStream());
							BufferedReader bin = new VT100BufferedReader(new InputStreamReader(s.getInputStream()), out);
							
//							CharBuffer target = CharBuffer.allocate(255);  // for putty client
//							bin.read(target);
							out.print("\rLogin User: ");
							String username = bin.readLine();
							out.print("Password: ");
							String password = bin.readLine();
							if(username.endsWith("admin") &&password.equals("dsmart")){
								out.print("\33[2J");
								crunner.run(bin, out);
							}else{
								out.println("Invalid Username or Password");
							}
							out.println("Disconnected");
							s.shutdownInput();
							s.shutdownOutput();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}});
				client.start();
				System.out.println("Console Server started");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
