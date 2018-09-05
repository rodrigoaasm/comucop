/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripth;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Rodrigo Maia
 */
public class MyEncrypt {
    
    public static byte[][] encrypt(PublicKey pk,String text) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchProviderException{
        byte [] encryptedText;
        byte [] encryptedKeyAES;
        //Gerando chave AES
        KeyGenerator kgen = KeyGenerator.getInstance("AES");//Instaciando gerador AES
        kgen.init(128);
        SecretKey aesKey = kgen.generateKey();//Gerando a chave AES
        
        //Criptografando a informação em AES
        Cipher encryptCipherAES = Cipher.getInstance("AES");//Instaciando o AES
        encryptCipherAES.init(Cipher.ENCRYPT_MODE, aesKey);//Preparando o objeto AES
        encryptedText = encryptCipherAES.doFinal(text.getBytes("ISO-8859-1"));//Criptografa a informação.
        
        //Criptografando a chave AES em RSA
        Cipher rsaEncode = Cipher.getInstance ("RSA");//Instaciando RSA
        rsaEncode.init (Cipher.ENCRYPT_MODE, pk);//Preparando o objeto Rsa
        encryptedKeyAES = rsaEncode.doFinal(aesKey.getEncoded());//Criptografando chave AES
        
        return(new byte[][]{encryptedText,encryptedKeyAES});        
    }
    
    public static byte[] decrypt(PrivateKey pk,byte [] encryptedText, byte[] encryptedKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchProviderException{
        
        byte [] infoDecrypted;
        byte [] keyDecrypted;
        
        //Descifragem Da chave AES em RSA
        Cipher rsaEncode = Cipher.getInstance ("RSA");//Instaciando RSA
        rsaEncode.init (Cipher.DECRYPT_MODE, pk);//Preparando o objeto RSA
        keyDecrypted  = rsaEncode.doFinal(encryptedKey);//Descriptografando chave AES
       
         //Descifragem Da informação em AES
        Cipher decryptCipherAES = Cipher.getInstance("AES");
        decryptCipherAES.init(Cipher.DECRYPT_MODE, new SecretKeySpec (keyDecrypted, "AES"));
        infoDecrypted = decryptCipherAES.doFinal(encryptedText);
        return infoDecrypted;        
    }
}





