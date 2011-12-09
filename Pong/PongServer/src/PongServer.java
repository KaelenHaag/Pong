import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PongServer
{
	static final int PORT = 1234;
	private JFrame frame;
	private volatile JTextArea msgArea;
	private JScrollPane scrollPane;

	private volatile ArrayList<HostInfo> hosts;
	int handlerCount = 0;

	public PongServer()
	{
		frame = new JFrame();
		frame.setSize(650,300);

		msgArea = new JTextArea();
		msgArea.setEditable(false);
		scrollPane = new JScrollPane(msgArea);
		scrollPane.setBounds(0,0,650,300);
		frame.getContentPane().add(scrollPane);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		hosts = new ArrayList<HostInfo>();

		try
		{
			final ServerSocket serverSock = new ServerSocket(PORT);
			Socket client;

			frame.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent event)
				{
					try
					{
						System.out.println("Closing!");
						serverSock.close();
						System.exit(0);
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
			});

			frame.setTitle("Host name: " + InetAddress.getLocalHost().getHostName());
			//System.out.println("Host name: " + InetAddress.getLocalHost().getHostName());
			while(true)
			{
				msgArea.append("Waiting for a client\n");
				client = serverSock.accept();
				msgArea.append("Contact made with " + client+"\n");
				new PongPlayerHandler(client, this).start();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public synchronized JTextArea getMessageArea()
	{
		return msgArea;
	}

	public synchronized void addNewPotentialHost(HostInfo hi)
	{
		msgArea.append(hi.getComputerName()+"\n");
		hosts.add(hi);
	}

	public synchronized ArrayList<HostInfo> getPotentialHosts()
	{
		return hosts;
	}

	public static void main(String args[])
	{
		new PongServer();
	}
}
