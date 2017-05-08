import java.io.*;
import java.net.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import javax.crypto.*;
import javax.swing.JOptionPane;

public class ClientX509 
{
	public static void main(String[] args) throws Exception 
	{
		String host = "LOCALHOST";
		int port = 7999;
		Socket ss = new Socket(host, port);
	    ObjectOutputStream cipherOutStream = new ObjectOutputStream(ss.getOutputStream());
        InputStream inStreamCert = new FileInputStream("arun.cer");
        CertificateFactory cff = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)cff.generateCertificate(inStreamCert);
        Date date = certificate.getNotAfter(); 
        inStreamCert.close();
        
        if(date.after(new Date())) 
        	JOptionPane.showMessageDialog(null, "This certificate is still valid. \n Valid from: " 
        			+ certificate.getNotBefore() + " to " + certificate.getNotAfter());
        else  
        	JOptionPane.showMessageDialog(null, "This certificate has expired");
        
        try
        {
    	   certificate.checkValidity();
    	   JOptionPane.showMessageDialog(null, "This certificate is valid");
        }
        catch (Exception e)
        {
    	   System.out.println(e);   
        }
        System.out.println(certificate.toString());
        JOptionPane.showMessageDialog(null, "Certificate Description Available in Console Area..."); 
        String message = JOptionPane.showInputDialog("please enter a message");
        RSAPublicKey publicKeyServer = (RSAPublicKey) certificate.getPublicKey();
        //message is encrypted with public key
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyServer);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        cipherOutStream.writeObject(cipherText);
		cipherOutStream.flush();
		cipherOutStream.close();
	    ss.close();
	}
}

