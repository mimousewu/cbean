package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

class TelnetServer
{
	public static void main(String args[]) throws Exception
	{
		ServerSocket Soc=new ServerSocket(23);
		while(true)
		{
			Socket CSoc=Soc.accept();
			AcceptTelnetClient ob=new AcceptTelnetClient(CSoc);
		}
	}
}

class AcceptTelnetClient extends Thread
{
	Socket ClientSocket;
	DataInputStream din;
	DataOutputStream dout;
	String LoginName;
	String Password;

	AcceptTelnetClient(Socket CSoc) throws Exception
	{
		ClientSocket=CSoc;
		System.out.println("Client Connected ...");
		DataInputStream din=new DataInputStream(ClientSocket.getInputStream());
		DataOutputStream dout=new DataOutputStream(ClientSocket.getOutputStream());

		dout.writeUTF("Hello ");
		System.out.println("Waiting for UserName And Password");
		
//		LoginName=din.readUTF();
//		Password=din.readUTF();
			
		start();		
	}
	public void run()
	{
		try
		{	
		DataInputStream din=new DataInputStream(ClientSocket.getInputStream());
		DataOutputStream dout=new DataOutputStream(ClientSocket.getOutputStream());

		dout.writeBytes("Thank you");
		BufferedReader brFin=new BufferedReader(new FileReader("Passwords.txt"));

		String LoginInfo=new String("");
		boolean allow=false;
		
		while((LoginInfo=brFin.readLine())!=null)
		{
			StringTokenizer st=new StringTokenizer(LoginInfo);
			if(LoginName.equals(st.nextToken()) && Password.equals(st.nextToken()))
			{
				dout.writeUTF("ALLOWED");
				allow=true;
				break;
			}
		}
		
		brFin.close();

		if (allow==false)
		{
			dout.writeUTF("NOT_ALLOWED");			
		}
		
	

		while(allow)
		{
			String strCommand;
			strCommand=din.readUTF();
			if(strCommand.equals("quit"))
			{
				allow=false;
			}
			else
			{
				Runtime rt=Runtime.getRuntime();
			
				Process p=rt.exec("TelnetServer.bat " + strCommand);
				
				String stdout=new String("");
				String st;
				DataInputStream dstdin=new DataInputStream(p.getInputStream());
				while((st=dstdin.readLine())!=null)
				{
					stdout=stdout +st + "\n";
				}
				dstdin.close();
				dout.writeUTF(stdout);
			}						
		}
		ClientSocket.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	} 
}
