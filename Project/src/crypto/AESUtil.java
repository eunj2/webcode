package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class AESUtil {
	// 대칭 키 생성
	public static SecretKey generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128); // 256으로 해도 됨
		SecretKey secretKey = keyGen.generateKey();
		return secretKey;
	}
	
	// 대칭 키 암호화 (IV를 생성하여 암호문 앞에 붙여서 반환하는 방식)
    public static byte[] encrypt(byte[] data, SecretKey secretKey) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        
        // CBC 모드를 위한 무작위 IV 생성
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom(); // SecureRandom 사용
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(data);
        
        // 복호화할 때 IV가 필요하므로 [ IV(16바이트) + 암호문 ] 형태로 결합하여 반환
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        
        return combined;
    }
    
    // 대칭 키 복호화
    public static byte[] decrypt(byte[] combinedData, SecretKey secretKey) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
        // 암호문 앞부분에서 IV 추출
        byte[] iv = new byte[16];
        System.arraycopy(combinedData, 0, iv, 0, iv.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        // 실제 암호문 데이터 추출
        int encryptedSize = combinedData.length - 16;
        byte[] encrypted = new byte[encryptedSize];
        System.arraycopy(combinedData, 16, encrypted, 0, encryptedSize);
        
        // "RSA" 오류 수정 -> ALGORITHM("AES/CBC/PKCS5Padding") 사용
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        
        return cipher.doFinal(encrypted);
    }
	
	public static void saveKey(String fname, SecretKey secretKey) throws FileNotFoundException, IOException {
		File directory = new File("keys");
		
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		File file = new File(directory, fname);
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(secretKey);
		}
	}
	
	public static SecretKey loadKey(String fname) throws FileNotFoundException, IOException, ClassNotFoundException {
		File directory = new File("keys");
		File file = new File(directory, fname);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			SecretKey key = (SecretKey) ois.readObject();
			return key;
		}
	}
}
