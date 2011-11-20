import java.net.*;
import java.io.*;

public class PongServer
{
	static final int PORT = 1234;

	public PongServer()
	{
		try
		{
			ServerSocket serverSock = new ServerSocket(PORT);
			Socket client;
			while(true)
			{
				System.out.println("Waiting for a client");
				client = serverSock.accept();
				System.out.println("Contact made with" + client);
				new PongPlayerHandler(client, this).start();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public static void main(String args[])
	{
		System.out.println("Server");
		new PongServer();
	}
}
