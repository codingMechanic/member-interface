package de.lsvn.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.test.Test;

/*
 * AES Encryption and Decryption with Apache Base64
 */
public class CryptionHelper {
	
	private String encryptionKey = "";
	
	public static void main(String args[]) {
		CryptionHelper cryptionHelper = new CryptionHelper();
	    String encrypt = cryptionHelper.encrypt("mypassword");
	    System.out.println("decrypted value:" + cryptionHelper.decrypt(cryptionHelper.encryptionKey, encrypt));
	}
    
	public String encrypt(String stringValue) {
		try {
			// Generate and temporarily store secret key.
	        KeyGenerator keygen = KeyGenerator.getInstance("AES");
	        keygen.init(256);
	        SecretKey secretKey = keygen.generateKey();
	        byte[] rawKey = secretKey.getEncoded();
	        // Use Base64 to avoid conflict with non ASCII characters.
	        String key = Base64.encodeBase64String(rawKey);
	        this.encryptionKey = key;
	        System.out.println("------------------Key------------------");
	        System.out.println(key);
	        System.out.println("--------------End of Key---------------");
			SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
			
			// Prepare a cipher object for encrypting.
			Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
	        
	        // Encrypt the string from the method's argument as a byte array and encode with Base64, making it a string.
	        String encrypt = (new Base64()).encodeAsString(cipher.doFinal(stringValue.getBytes()));
	        System.out.println("encrypted string:" + encrypt);
	        return encrypt;
	        
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BadPaddingException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidKeyException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

public String decrypt(String encryptionKey, String encrypted) {
    try {
    	// Get the temporarily stored secret key and Base64 decode it
        Key key = new SecretKeySpec(new Base64().decode(encryptionKey), "AES");
        
        // Prepare a cipher object for decrypting.
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        // Decode the string from the method's argument with Base64, making it a byte array.
        byte[] decodedValue = new Base64().decode(encrypted);
        // Decrypt the byte array.
        byte[] decValue = cipher.doFinal(decodedValue);
        // Make the byte array a string.
        String decryptedValue = new String(decValue);
        return decryptedValue;
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}


}
