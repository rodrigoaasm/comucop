/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Maia
 */
public class MyRSAKey {

    private final static String ALGORITHM = "RSA";
    private final static String NAMEFILES = "ccr";
    private PrivateKey aPrivate;
    private PublicKey aPublic;

    public MyRSAKey(PrivateKey aPrivate, PublicKey aPublic) {
        this.aPrivate = aPrivate;
        this.aPublic = aPublic;
    }    

    /*Instancia um objeto MyRSAKey, gerando uma chave publica e privada*/
    public static MyRSAKey newInstance(){
        KeyPairGenerator keyGen = null;
        try {  
            keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        }
        keyGen.initialize(2048);//Definindo o tamanho da chave publica    
        KeyPair key = keyGen.generateKeyPair();//gerado as chaves
        
        return (new MyRSAKey(key.getPrivate(),key.getPublic()));
    }

    //Recuperando a chave publica
    public static PublicKey loadInstancePublicKey(){
        ObjectInputStream ois = null;
        PublicKey ch = null;
        try {
            ois = new ObjectInputStream (new FileInputStream (NAMEFILES+".public"));//Abrir arquivo de entrada
            ch = (PublicKey) ois.readObject();//Faz a leitura do objeto "chave publica"
        ois.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) { 
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ch;
    }
    
    //Recuperando chave privada
    public static PrivateKey loadInstancePrivateKey(){
        ObjectInputStream ois;
        PrivateKey ch = null;
        try {
            ois = new ObjectInputStream (new FileInputStream (NAMEFILES+".private"));//Abri arquivo de entrada
            ch = (PrivateKey) ois.readObject();//Faz a leitura do objeto "chave privada"
        ois.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return ch;
    }
    
    //Persiste em arquivo a chave publica
    public void storePublicKey(){
         //Abrindo arquivo e salvando a chave publica que será distribuida
        FileOutputStream fileOutput;       
        try {//Se o houve algum erro ele não grava a informação
            fileOutput = new FileOutputStream(NAMEFILES+".public");
            ObjectOutputStream objfileOutput = new ObjectOutputStream(fileOutput);
            objfileOutput.writeObject(aPublic);//Escrevendo Objeto em arquivo
            objfileOutput.flush();
            objfileOutput.close();
        } catch (Exception ex) {
             Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Persiste em arquivo a chave privada
    public void storePrivateKey(){
         //Abrindo arquivo e salvando a chave privada 
        FileOutputStream fileOutput;       
        try {//Se o houve algum erro ele não grava a informação.
            fileOutput = new FileOutputStream(NAMEFILES+".private");
            ObjectOutputStream objfileOutput = new ObjectOutputStream(fileOutput);
            objfileOutput.writeObject(aPrivate);//Escrevendo Objeto em arquivo
            objfileOutput.flush();
            objfileOutput.close();
        } catch (Exception ex) {
             Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Retorna chave publica em modo texto.
    public String getPublicKeyStringMode() {
        byte[] keyInByte = aPublic.getEncoded();//Converte key em um array de bytes
        try {
            //Converte Array de bytes em String
            return new String(keyInByte,"ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyRSAKey.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //Retorna objeto publickey
    public PublicKey getPublicKey(){
        return aPublic;
    }
    
    //Retorna chave 
    public PrivateKey getPrivateKey(){
        return aPrivate;
    }
    
    
    
}
