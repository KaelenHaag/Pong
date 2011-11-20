import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

public class PongHost
{
	private static final int PORT = 1234;     //server details
  	private static String host = "localhost";

	private Socket sock;
  	private PrintWriter out;

	public PongHost()
	{
		host = JOptionPane.showInputDialog(null, "Input the name of the computer running the PongServer application on your network", host);
		
		makeContact();
	}

	private void makeContact()
	{
		try
		{
      		sock = new Socket(host, PORT);
      		BufferedReader in  = new BufferedReader(
		  		new InputStreamReader( sock.getInputStream() ) );
      		out = new PrintWriter( sock.getOutputStream(), true );  // autoflush
      		out.println("host");
      		JOptionPane.showMessageDialog(null, "Success! Connection has been made to: " + host + ". Unfortunately, the rest of feature hasn't been implemented yet! :)");
    	}
    	catch(Exception e)
    	{  
    		  JOptionPane.showMessageDialog(null, "Unable to connect to: " + host + ". Make sure the name of the computer running PongServer matches!");
    	}
    	
	}
}
