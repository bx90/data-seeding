package encryption;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class Encryption 
{  
	public static String decrypt(byte[] content)
	{  
		try 
		{  
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			byte[] result = cipher.doFinal(content);  
			return new String(result);
		} 
		catch (Throwable e)
		{  
			e.printStackTrace();  
		}  
		return null;  
	}

	private static String key  = "12345678";

	public static byte[] StringtoByte(String str)
	{
		if(str == null || str.length() == 0)
		{
			return null;
		}
		String[] byteValues = str.split("%");
		byte[] bytes = new byte[byteValues.length];
		for (int i=0, len=bytes.length; i<len; i++) 
		{
			bytes[i] = Byte.parseByte(byteValues[i].trim());
		}
		return bytes;
	}  
}
