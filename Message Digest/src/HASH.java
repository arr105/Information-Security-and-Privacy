import java.security.MessageDigest;
import java.util.Scanner;
import javax.xml.bind.DatatypeConverter;
public class HASH 
{
	public static void main(String[] args)
	{
		try
		{
			String ip;           
			Scanner scan = new Scanner(System.in);
			System.out.print("Enter a string in the next given space: ");
			ip = scan.nextLine();
			scan.close();
			System.out.println("The string is displayed as SHA-1 Hash:\n" + generateSHA1(ip));
			System.out.println("The string is displayed as SHA-256 Hash:\n" + generateSHA256(ip));
			System.out.println("The string is displayed as MD5 Hash:\n" + generateMD5(ip));	
		} 
		catch (Exception excep)
		{
			System.out.println(excep.getMessage());
		}
	}
	static String Stringhashed(String msg, String alg) 
			throws Exception 
	{
	try
	{
		MessageDigest instance = MessageDigest.getInstance(alg);
		byte[] hashedBytes = instance.digest(msg.getBytes("UTF-32"));
		return DatatypeConverter.printHexBinary(hashedBytes);
	}
	catch (Exception ex)
	{
		throw new Exception("Hash from String can't be generated", ex);
	}
}
	static String generateSHA1(String message) 
			throws Exception
	{
		return Stringhashed(message,"SHA-1");
		//generates SHA-1 Hash
	}
	static String generateSHA256(String message)
			throws Exception
	{
		return Stringhashed(message,"SHA-256");
		//generates SHA-256 Hash
	}
	static String generateMD5(String message)
			throws Exception 
	{
		return Stringhashed(message,"MD5");
		//generates MD5 Hash
	}
}
