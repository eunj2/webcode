package crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class SignatureUtil {
	// 전자서명 생성 (이중 서명에서 사용)
	public static byte[] digitalSign(byte[] data, PrivateKey privateKey) 
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initSign(privateKey);
		sig.update(data);
		byte[] signature = sig.sign();
		return signature;
	}
	
	// 전자서명 검증
	public static boolean digitalSignVerify(byte[] data, byte[] digitalSign, PublicKey publicKey) 
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initVerify(publicKey);
		sig.update(data);
		boolean rslt = sig.verify(digitalSign);
		return rslt;
	}
}
