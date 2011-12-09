
import java.net.*;
import java.io.*;
import java.util.*;

public class PongPlayerHandler extends Thread
{
	private PongServer server;
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private String type;
	private HostInfo hi;

	public PongPlayerHandler(Socket client, PongServer ps)
	{
		this.client = client;
    	server = ps;
    	server.getMessageArea().append("Player connection request\n");
    	try
    	{
     	 	in  = new BufferedReader(
			    new InputStreamReader( client.getInputStream() ) );
     	 	out = new PrintWriter( client.getOutputStream(), true );  // autoflush
    	}
    	catch(Exception e)
   		{  server.getMessageArea().append(e.getMessage()+"\n");  }
	}

	public void run()
	{
		String line;
		boolean done = false;
		try
		{
			while(!done)
			{
				line = in.readLine();
				if(line == null || line.equals("disconnect"))
				{
					server.getMessageArea().append("We're done!\n");
					done = true;
				}
				else
				{
					server.getMessageArea().append(line+"\n");

					//The type of connection that is being made to either a potential host or one who wants to join
					if(line.trim().startsWith("type"))
					{
						if(line.contains("host"))
						{
							type = "host";
							hi = new HostInfo(client.getInetAddress(), client.getInetAddress().getLocalHost().getHostName(), this);
							server.addNewPotentialHost(hi);
							server.getMessageArea().append("Potential host has joined!\n");
							continue;
						}
						else
						{
							if(line.contains("join"))
							{
								type = "join";
								sendHostInfo();
								continue;
							}
							server.getMessageArea().append("unrecognized \"type\" command!\n");
							out.println("unrecognized \"type\" command!");
							continue;
						}
					}

					if(line.trim().startsWith("fetch"))
					{
						server.getMessageArea().append("FETCH request!\n");
						sendHostInfo();
					}

				}
			}
		}
		catch(IOException e){}

		closeConnection();
	}

	private void sendHostInfo()
	{
		server.getMessageArea().append("Sending host info!\n");
		ArrayList<HostInfo> hosts = server.getPotentialHosts();
		try
		{
			out.println("NumberOfHosts " + hosts.size());
			for(int i = 0; i < hosts.size(); i++)
			{
				server.getMessageArea().append(hosts.get(i).getComputerName()+"\n");
				out.println(hosts.get(i));
			}
		}
		catch(Exception e){}
	}

	public void closeConnection()
	{
		try
		{
			//If this handler is for a host, we need to remove the HostInfo from the hosts list owned by the server.
			if(type.equals("host") && hi != null)
			{
				server.getPotentialHosts().remove(hi);
			}
			client.close();
			in.close();
			out.close();
			server.getMessageArea().append("Player disconnected\n");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public Socket getClient()
	{
		return client;
	}
}
