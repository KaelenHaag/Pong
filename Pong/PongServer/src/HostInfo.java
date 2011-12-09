import java.net.*;
public class HostInfo
{
	private InetAddress hostAddress;
	private String computerName;
	private PongPlayerHandler playerHandler;

	public HostInfo(InetAddress ha, String name, PongPlayerHandler pph)
	{
		hostAddress = ha;
		computerName = name;
	}

	public InetAddress getHostAddress()
	{
		return hostAddress;
	}

	public String getComputerName()
	{
		return computerName;
	}

	public PongPlayerHandler getPongPlayerHandler()
	{
		return playerHandler;
	}

	public String toString()
	{
		return "Computer name: " + computerName;
	}
}
