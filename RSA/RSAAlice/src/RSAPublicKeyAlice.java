import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;

public class RSAPublicKeyAlice extends JFrame {

	//graphical phase is initialized
	private Container content; 	
	private JLabel welcome, typeOfServiceLabel;
	private JLabel enterStringLabel, infoLabel;
	private JTextField enterStringField;
	private JPanel servicePanel, inputPanel;
	private JRadioButton auth, conf, both; 
	private ButtonGroup group; 
	
	static String message;
	
	public RSAPublicKeyAlice ()
	{
		content = getContentPane();
		content.setLayout(new FlowLayout());
		welcome = new JLabel("Welcome to the Public Key System App...                ");
		typeOfServiceLabel = new JLabel("Please Choose the type of service you want to achieve...");
		enterStringLabel = new JLabel ("Enter a String         ");
		enterStringField = new JTextField(20);
		
		auth = new JRadioButton("Integrity & Authentication       ");
		conf = new JRadioButton("Confidentiality       ");
		both = new JRadioButton("Both      ");
		
		group = new ButtonGroup();
		
		servicePanel = new JPanel();
		inputPanel = new JPanel();
				
		servicePanel.setLayout(new BoxLayout(servicePanel, BoxLayout.X_AXIS));
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		
		group.add(auth);
		group.add(conf);
		group.add(both);
		
		servicePanel.add(auth);
		servicePanel.add(conf);
		servicePanel.add(both);
		inputPanel.add(enterStringLabel);
		inputPanel.add(enterStringField);
		 
		RadioButtonHandler rbh = new RadioButtonHandler();
						
		auth.addItemListener(rbh);
		conf.addItemListener(rbh);
		both.addItemListener(rbh);
	
		content.add(welcome);
		content.add(inputPanel);
		content.add(typeOfServiceLabel);
		content.add(servicePanel);
	}
	private class RadioButtonHandler implements ItemListener
	{
			public void itemStateChanged(ItemEvent ie)
			{
			message = enterStringField.getText();
			
			String host = "LOCALHOST";
			int port = 6867;
			try {
				Socket ss = new Socket(host, port);
				ObjectOutputStream bobPublicKey = new ObjectOutputStream(ss.getOutputStream());
				ObjectInputStream alicePublicKey = new ObjectInputStream(ss.getInputStream());
				
				// Alice public and private key is generated. 
				KeyPairGenerator  kPG = KeyPairGenerator.getInstance("RSA");
				kPG.initialize(1024, new SecureRandom());
				KeyPair keyP = kPG.genKeyPair();
				RSAPublicKey APKey = (RSAPublicKey) keyP.getPublic();
				RSAPrivateKey APriKey = (RSAPrivateKey) keyP.getPrivate();
				
				//sending Alice Public Key to Bob
				bobPublicKey.writeObject(APKey);
				
				//Bob public key received
				RSAPublicKey BobPR = (RSAPublicKey) alicePublicKey.readObject();
				Cipher ciph = Cipher.getInstance("RSA");
			
			  if(ie.getSource() == conf) 
			  {
				enterStringField.setEditable(false);
			    infoLabel = new JLabel("Confidentiality: The Message will be encrypted using Bob's public Key");
				content.add(infoLabel);
				setSize(480,170);
				conf.setEnabled(false);
				auth.setEnabled(false);
				both.setEnabled(false);
				  
				  ciph.init(Cipher.ENCRYPT_MODE, BobPR);
			      byte[] encryptedMessage = ciph.doFinal(message.getBytes());
			      bobPublicKey.writeInt(1);
				  bobPublicKey.writeObject(encryptedMessage);
				  bobPublicKey.flush();
				  bobPublicKey.close(); 
			  }
			  
			  if(ie.getSource() == auth) 
			  {
				enterStringField.setEditable(false);
				infoLabel = new JLabel("Integrity & Authentication: The Message will be encrypted " +
				 		"using Alice's private key");
				  
				content.add(infoLabel);
				setSize(480,170);
				conf.setEnabled(false);
				auth.setEnabled(false);
				both.setEnabled(false);
				  
				  ciph.init(Cipher.ENCRYPT_MODE, APKey);
			      byte[] encryptedMessage = ciph.doFinal(message.getBytes());
			      bobPublicKey.writeInt(2);
			      bobPublicKey.writeObject(encryptedMessage);
			      bobPublicKey.flush();
			      bobPublicKey.close();  
			  }
			  
			  if(ie.getSource() == both) 
			  {
				  enterStringField.setEditable(false);
				  infoLabel = new JLabel("Both : The Message will be firstly encrypted " +
				  		"using Alice's private key and then using bob's public key");
				  content.add(infoLabel);
				  setSize(480,170);
				  
				  conf.setEnabled(false);
				  auth.setEnabled(false);
				  both.setEnabled(false);
				  ciph.init(Cipher.ENCRYPT_MODE, APKey);
			      byte[] encryptedMessage = new byte[message.length()];
			      encryptedMessage = ciph.doFinal(message.getBytes());
			      ciph.init(Cipher.ENCRYPT_MODE, BobPR);
			      byte[] encryptedMessage2 = ciph.doFinal(encryptedMessage);
			      bobPublicKey.writeInt(3);
			      bobPublicKey.writeObject(encryptedMessage2);
			      bobPublicKey.flush();
			      bobPublicKey.close();
			  }
			  	  ss.close();
			}
			catch (UnknownHostException e)
			{
			e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			} 
			catch (NoSuchAlgorithmException e)
			{
			e.printStackTrace();
			} 
			catch (NoSuchPaddingException e)
			{
			e.printStackTrace();
			}
			catch (ClassNotFoundException e) 
			{
			e.printStackTrace();
			} 
			catch (InvalidKeyException e) 
			{
			e.printStackTrace();
			}
			catch (IllegalBlockSizeException e)
			{
			e.printStackTrace();
			}
			catch (BadPaddingException e)
			{
			e.printStackTrace();
			}
						
		}
	}
	public static void main(String[] args) throws Exception
	{
		RSAPublicKeyAlice a = new RSAPublicKeyAlice();
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.setSize(480, 150);
		a.setVisible(true);
		a.setResizable(false);
		a.setTitle("RSA Public Key System: Alice");
	}

}
