import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.JOptionPane;
public class CipherServer 
{
	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream inputStream = new ObjectInputStream(s.getInputStream());
		Key k = (Key) inputStream.readObject();	
				
		// process of deciphering is done
		Cipher deCipher = Cipher.getInstance("DES");
		deCipher.init(Cipher.DECRYPT_MODE, k);
		CipherInputStream tobeDecryptedStream = new CipherInputStream(s.getInputStream(),deCipher);
		int decryptedMessage = tobeDecryptedStream.read();
				StringBuilder dMessage = new StringBuilder();
		while( decryptedMessage != -1)
			     {	
					dMessage.append((char) decryptedMessage);
			    	decryptedMessage= tobeDecryptedStream.read();
			     }
				JOptionPane.showMessageDialog(null, dMessage.toString());
				inputStream.close();
				tobeDecryptedStream.close();
				server.close();
	}
}
