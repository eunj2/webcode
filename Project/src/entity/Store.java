package entity;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util. Arrays;

import crypto.EnvelopeUtil;
import crypto.HashUtil;
import crypto.SerializeUtil;
import crypto.SignatureUtil;
import model.BaeminToStoreData;
import model.DigitalEnvelope;
import model.OrderInfo;

class StoreCryptoException extends Exception {
	private static final long serialVersionUID = 1L;
    public StoreCryptoException(String message, Throwable cause) { 
		super(message, cause); 
	}
}

public class Store {
	private  final PrivateKey privateKey;
	
	public Store (PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 배달의 민족이 보낸 전자봉투 개봉
	public BaeminToStoreData openEnvelope(DigitalEnvelope storeEnvelope) throws  StoreCryptoException{
		byte[] decrypted = null;
        try {
            decrypted = EnvelopeUtil.openEnvelope(storeEnvelope, privateKey);
            return (BaeminToStoreData) SerializeUtil.bytesToObject(decrypted);
        } catch (Exception e) {
            throw new StoreCryptoException("가맹점 전자봉투 개봉 중 오류가 발생했습니다.", e);
        } finally {
            if (decrypted != null) {
                Arrays.fill(decrypted, (byte) 0);
            }
        }
    }
	
	// 주문 정보 검증
	public boolean verifyOrder(BaeminToStoreData storeData, PublicKey userPublicKey) throws  StoreCryptoException {
		// 주문 정보 해시 생성
		byte[] orderInfoBytes = null;
        byte[] orderInfoHash = null;
        byte[] combined = null;
        byte[] combinedHash = null;
        
        try {
            // 직렬화
            OrderInfo orderInfo = storeData.getOrderInfo();
            orderInfoBytes = SerializeUtil.objectToBytes(orderInfo);
            
            byte[] salt = storeData.getOrderInfoSalt();
            orderInfoHash = HashUtil.hash(orderInfoBytes, salt);

			//주문정보-배민정보 해시 결합
            byte[] paymentInfoHash = storeData.getPaymentInfoHash();
            combined = HashUtil.combineHash(orderInfoHash, paymentInfoHash);
            
            // 최종 결합 해시 생성
            combinedHash = HashUtil.hash(combined, salt);
            
           	byte[] digitalSignature = storeData.getDigitalSignature();
            return SignatureUtil.digitalSignVerify(combinedHash, digitalSignature, userPublicKey);
            
        } catch (Exception e) {
            throw new StoreCryptoException("주문 정보 이중서명 검증 중 오류가 발생했습니다.", e);
        } finally {
            if (orderInfoBytes != null) Arrays.fill(orderInfoBytes, (byte) 0);
            if (orderInfoHash != null) Arrays.fill(orderInfoHash, (byte) 0);
            if (combined != null) Arrays.fill(combined, (byte) 0);
            if (combinedHash != null) Arrays.fill(combinedHash, (byte) 0);
        }
    }
}
