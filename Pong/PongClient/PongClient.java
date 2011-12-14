import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

public class PongClient extends PongNetworkingBase
{
  	private PongClientPanel pongClientPanel;

	public PongClient(PongFrame pf)
	{
		String serverName = JOptionPane.showInputDialog(null, "Input the name of the computer running the PongServer application on your network", host);
		host = serverName != null ? serverName : host;
		pongClientPanel = new PongClientPanel(pf, this);

		if(makeContact())
		{
			pf.add(pongClientPanel);
		}
	}

	protected boolean makeContact()
	{
		try
		{
      		sock = new Socket(host, PORT);
      		in  = new BufferedReader(
		  		new InputStreamReader( sock.getInputStream() ) );
      		out = new PrintWriter( sock.getOutputStream(), true );  // autoflush
      		new PongWatcher(this, in).start();
      		out.println("type join");
      		//JOptionPane.showMessageDialog(null, "Success! Connection has been made to: " + host + ". Unfortunately, the rest of feature hasn't been implemented yet! :)");
		}
    	catch(Exception e)
    	{
    		  JOptionPane.showMessageDialog(null, "Unable to connect to: " + host + ". Make sure the name of the computer running PongServer matches!");
    		  return false;
    	}
    	return true;
	}

	public boolean makeConnectionToHost(String hostName)
	{
		try
		{
			out.println("Chosen HOST: " + hostName);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public void disconnect()
	{
		try
		{
			out.println("disconnect");
			out.close();
			in.close();
			sock.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void processHostInfo(String hostName)
	{
		pongClientPanel.addHostInfoToList(hostName);
	}

	public void requestUpdatedHostInfo()
	{
		try
		{
			out.println("fetch host info");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
