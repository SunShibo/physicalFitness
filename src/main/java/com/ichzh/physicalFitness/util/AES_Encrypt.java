package com.ichzh.physicalFitness.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Base64;

public class AES_Encrypt {
	 

    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String AESDecrypt(String sessionKey,String encryptedData, String iv){
    	
    	byte[]  bSessionKey =   Base64.decode(sessionKey.getBytes());
    	byte[]  bEncryptedData = Base64.decode(encryptedData.getBytes());
    	byte[]  bIv = Base64.decode(iv.getBytes());
    	
    	SecretKeySpec k = new SecretKeySpec(bSessionKey,"AES"); 
    	IvParameterSpec ivParam = new IvParameterSpec(bIv);
    	try {
			Cipher cp = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cp.init(Cipher.DECRYPT_MODE, k, ivParam);
			
			byte[] ptext = cp.doFinal(bEncryptedData);
			
			return new String(ptext, "UTF-8");
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}  catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return "";   
    }
    
    public static void main(String[] args) {
       
    	String encryptedData = "Yu+AqJDNQaWuXRczZOs84Sxmvuv68kHlUpzK/0jg1pFL9TMTMKOOcdlt/1D7wZbTfO8NhJcdvdhbMDxlcZjQ05SFZjWYEW3Nt/7dEAyNT7nNcjZc33kBNFBToS/NDyblY1KMWa8L8UsgDIA1oQrTCdm42CZXsKITb8ubz2MSU/eGC4TElY3yTe8+NbQTmd3A++1xAdMMFGsnAkPsF8WfbtBscwrX1ySANoFsay7duCjDx8XDzQmWZZEMJWkWjU9L0w1rhy4dIK2BRi0RHF9KoXxcbs6cyDobtY9J4rKRlPXMZFAFjmQcf8qYxBe46CvvRxVoV7LFDgpiL5vtibeXUZw9HVhU6RQdVVJmtUhwvgACFPy0I+6rU04J6+RivIEBt2ba/Z1Az67QDgJvRzm7omjMgS+5YWkjTlHLIabVrrLe4ONUjDp9rcCh+JAABoOsin9nCbrtYiPO95xk3p3yeA==";
    	String iv = "gWgyrKQRSXOJyD6+vON78g==";
    	String sessionKey = "83zI+GUonaSQlTlahbCfyA==";

    	String  plainTxt = AESDecrypt(sessionKey, encryptedData, iv);
    	System.out.println(plainTxt);
    	
    } 
}
