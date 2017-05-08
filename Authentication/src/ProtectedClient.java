import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.
		long tS1, tS2;
		double rN1, rN2;
		Date dt = new Date();
		
		tS1 = dt.getTime(); //timestamp is generated
		rN1 = Math.random(); //A random number is generated
		byte[] shaDigest1 = Protection.makeDigest(user, password, tS1, rN1);
		
		
		tS2 = dt.getTime(); //timestamp is generated
		rN2 = Math.random(); //A random number is generated
		byte[] shaDigest2 = Protection.makeDigest(shaDigest1, tS2, rN2);
				
		out.writeUTF(user); 
		out.writeLong(tS1);
		out.writeLong(tS2);
		out.writeDouble(rN1);
		out.writeDouble(rN2);
		out.writeInt(shaDigest1.length); 
		out.write(shaDigest2);
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		//String host = "paradox.sis.pitt.edu";
		String host = "LOCALHOST";
		int port = 7999;
		String user = "George";
		String password = "ab23";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}
