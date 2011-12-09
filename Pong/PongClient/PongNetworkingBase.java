import java.io.*;
import java.net.*;
public abstract class PongNetworkingBase
{
	protected static final int PORT = 1234;     //server details
  	protected String host = "005-20092000598";

	protected Socket sock;
	protected BufferedReader in;
  	protected PrintWriter out;

  	protected abstract boolean makeContact();
}
