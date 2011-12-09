import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;
import java.awt.event.*;

public class PongHost extends PongNetworkingBase
{
  	private PongFrame pf;

	public PongHost(PongFrame pf)
	{
		this.pf = pf;
		String serverName = JOptionPane.showInputDialog(null, "Input the name of the computer running the PongServer application on your network", host);
		host = serverName != null ? serverName : host;

		makeContact();
	}

	protected boolean makeContact()
	{
		try
		{
      		sock = new Socket(host, PORT);
      		in  = new BufferedReader(
		  		new InputStreamReader( sock.getInputStream() ) );
      		out = new PrintWriter( sock.getOutputStream(), true );  // autoflush
      		new PongWatcher(this, in);
      		out.println("type host");
      		pf.add(new PongPanel(pf, pf.getPeriod(), StartPanel.Choice.ONLINE));
      		pf.getContentPane().addContainerListener(new ContainerAdapter()
      		{
      			public void componentRemoved(ContainerEvent e)
      			{
      				//We want to watch when the PongPanel is removed because that means the player has pressed the back button
      				//So we want to disconnect the player from the server
      				//Added a check to make sure that it was the PongPanel we added
      				if(e.getChild() instanceof PongPanel)
      				{
      					if(((PongPanel)e.getChild()).getChoice() == StartPanel.Choice.ONLINE)
      					{
      						disconnect();
      						pf.getContentPane().removeContainerListener(this);
      					}
      				}

      			}
      		});
      		//JOptionPane.showMessageDialog(null, "Success! Connection has been made to: " + host + ". Unfortunately, the rest of feature hasn't been implemented yet! :)");
    	}
    	catch(Exception e)
    	{
    		  JOptionPane.showMessageDialog(null, "Unable to connect to: " + host + ". Make sure the name of the computer running PongServer matches!");
    		  return false;
    	}
    	return true;
	}

	private void disconnect()
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
}
