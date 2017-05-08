import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "LOCALHOST";
		int port = 7999;
		Socket s = new Socket(host, port);
		
		//A DES key is generated
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		SecureRandom rand = new SecureRandom();
		keyGen.init(rand);
		SecretKey sk = keyGen.generateKey();
		
	    //the key is stored in a file
		String fileName = "key.txt";
		ObjectOutputStream oS = new ObjectOutputStream( new FileOutputStream(fileName));
		oS.writeObject(sk);
		
		 // the key is sent to the server
		ObjectOutputStream sSoc = new ObjectOutputStream(s.getOutputStream());
	    sSoc.writeObject(sk);
	    sSoc.flush();
	    
	    Cipher dCiph = Cipher.getInstance("DES");
	    dCiph.init(Cipher.ENCRYPT_MODE, sk); // encrypt with the key
	    byte [] byteDataToEncrypt = message.getBytes();  
	    CipherOutputStream ciphOut = new CipherOutputStream(s.getOutputStream(), dCiph);
	    ciphOut.write(byteDataToEncrypt);  //data is sent
	    ciphOut.close();
	    
	    s.close();
	    oS.close();
	}
}
