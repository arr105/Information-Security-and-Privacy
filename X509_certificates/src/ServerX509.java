import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.JOptionPane;

public class ServerX509
{
	public static void main(String[] args) throws Exception 
	{   
		
		int port = 7999;
		ServerSocket ser = new ServerSocket(port);
		Socket ss = ser.accept();
		ObjectInputStream cipherClientStream = new ObjectInputStream(ss.getInputStream());
		String aliasName="arun";
        char[] pswd="1234567".toCharArray();
		
        KeyStore kst = KeyStore.getInstance("jks");
        kst.load(new FileInputStream("arun.jks"), pswd);
        PrivateKey serverPrivateKey = (PrivateKey)kst.getKey(aliasName, pswd);
        Cipher ciph = Cipher.getInstance("RSA");
        byte[] cipherTextClient = (byte[]) cipherClientStream.readObject(); // reading the ciphertext sent by the client
        ciph.init(Cipher.DECRYPT_MODE, serverPrivateKey); 
		byte[] decryptedMessage = ciph.doFinal(cipherTextClient);
		//decrypt message is displayed
		JOptionPane.showMessageDialog(null, "The Message is: " + new String(decryptedMessage));
		ser.close();
	}

}

