package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom; 

public class HashUtil {
	//솔트(Salt) 생성 메서드
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
	// SHA-256 해시 생성
	public static byte[] hash(byte[] data, byte[] salt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(salt);
		md.update(data);
		
		return md.digest(); // 변수 선언 없이 즉시 반환
	}
	
	// byte[]를 hex 문자열로 변환 (성능 최적화 및 스레드 오버헤드 제거)
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
    
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x", bytes[i]));
        }        
        return sb.toString();
    }
    
    // 두 개의 해시 결합
    public static byte[] combineHash(byte[] hash1, byte[] hash2) {
        byte[] combined = new byte[hash1.length + hash2.length];
        
        System.arraycopy(hash1, 0, combined, 0, hash1.length);
        System.arraycopy(hash2, 0, combined, hash1.length, hash2.length);
        
        return combined;
    }
}
