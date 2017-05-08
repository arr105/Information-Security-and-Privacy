import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{

		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.
		boolean isItTheSame = true;
		int shaDigestLength;
		long tS1, tS2;
		double rN1, rN2;
		String user = in.readUTF(); //get username
		String password = lookupPassword(user); //get password
		
		// read time stamp value
		tS1 = in.readLong(); 
		tS2 = in.readLong();
		//read random number
		rN1 = in.readDouble(); 
		rN2 = in.readDouble();  
		
		// calculating first sha digest & second sha digest 
		byte[] shaDigestServer1 = Protection.makeDigest(user, password, tS1, rN1);
		byte[] shaDigestServer2 = Protection.makeDigest(shaDigestServer1, tS2, rN2);
		shaDigestLength = in.readInt(); // for memory allocation, length is read
		byte[] shaClient = new byte [shaDigestLength];
		in.readFully(shaClient);
		isItTheSame = MessageDigest.isEqual(shaClient, shaDigestServer2); //comparing value from client and server
	
		return isItTheSame;
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		
		ServerSocket s = new ServerSocket(port);
		System.out.println("Server listening...");
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
		System.out.println("connection closed.... ");
	}
}
