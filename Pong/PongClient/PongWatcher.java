import java.net.*;
import java.io.*;
import java.util.Scanner;

public class PongWatcher extends Thread
{
	private PongNetworkingBase receiver;
	private BufferedReader in;

	public PongWatcher(PongNetworkingBase pnb, BufferedReader i)
	{
		receiver = pnb;
		in = i;
	}

	public void run()
	{
		String line;
		try
		{
			while((line = in.readLine()) != null)
			{
				if(line.startsWith("NumberOfHosts"))
				{
					System.out.println("NumberOfHosts!");
					System.out.println(line.substring(14));
					processIncomingHostInfo(Integer.parseInt(line.substring(14)));
					continue;
				}
			}
		}
		catch(Exception e){System.out.println(e);}
	}

	private void processIncomingHostInfo(int numberOfHosts)
	{
		System.out.println("Processing " + numberOfHosts);
		if(numberOfHosts > 0)
		{
			Scanner hostScanner = new Scanner(in);
			while(numberOfHosts > 0)
			{
				try
				{
					String line = in.readLine();
					System.out.println(line);
					((PongClient)receiver).processHostInfo(line.substring(15));
					numberOfHosts--;
				}
				catch(IOException e){System.out.println(e);}
			}
		}
		else
		{
			System.out.println("There are no potential hosts available!");
		}
	}
}
