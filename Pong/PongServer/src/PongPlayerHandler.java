
import java.net.*;
import java.io.*;
import java.util.*;

public class PongPlayerHandler extends Thread
{
	PongServer server;
	Socket client;
	BufferedReader in;
	PrintWriter out;
	
	public PongPlayerHandler(Socket client, PongServer ps)
	{
		this.client = client;
    	server = ps;
    	System.out.println("Player connection request");
    	try
    	{
     	 	in  = new BufferedReader(
			    new InputStreamReader( client.getInputStream() ) );
     	 	out = new PrintWriter( client.getOutputStream(), true );  // autoflush
    	}
    	catch(Exception e)
   		{  System.out.println(e);  }
	}

	public void run()
	{
		String line;
		boolean done = false;
		try
		{
			while(!done)
			{
				if((line = in.readLine()) == null)
				{
					done = true;
				}
				else
				{
					System.out.println(line);
				}
			}
		}
		catch(IOException e){}

		try
		{
			client.close();
			in.close();
			out.close();
			System.out.println("Player disconnected");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
