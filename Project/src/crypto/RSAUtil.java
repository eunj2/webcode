package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {
	private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
	private static final int KEY_SIZE = 2048;
	// 비대칭 키 생성
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {		
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(KEY_SIZE); 
        return keyPairGen.generateKeyPair(); // 중간 변수 제거
    }
	// 공개 키 암호화
    public static byte[] encrypt(byte[] data, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION); 
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }
	// 개인 키 복호화
	public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData); 
    }
	
	// 공개 키 저장
	public static void savePublicKey(String fname, PublicKey publicKey) throws IOException {
        File directory = new File("keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fname);
        
        //
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(publicKey);
        }
    }
	// 개인 키 저장
    public static void savePrivateKey(String fname, PrivateKey privateKey) throws IOException {
        File directory = new File("keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fname);
        
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(privateKey);
        }
    }
	
	// 공개 키 불러오기
	public static PublicKey loadPublicKey(String fname) throws FileNotFoundException, IOException, ClassNotFoundException {
		File directory = new File("keys");
		File file = new File(directory, fname);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			PublicKey publicKey = (PublicKey) ois.readObject();
			return publicKey;
		}
	}
	
	// 개인 키 불러오기
	public static PrivateKey loadPrivateKey(String fname) throws FileNotFoundException, IOException, ClassNotFoundException {
		File directory = new File("keys");
		File file = new File(directory, fname);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			PrivateKey privateKey = (PrivateKey) ois.readObject();
			return privateKey;
		}
	}
}
