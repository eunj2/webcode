package entity;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import crypto.EnvelopeUtil;
import crypto.HashUtil;
import crypto.SerializeUtil;
import crypto.SignatureUtil;
import model.CardData;
import model.DigitalEnvelope;
import model.OrderInfo;
import model.PaymentInfo;
import model.UserToBaeminData;

class UserCryptoException extends Exception {
    private static final long serialVersionUID = 1L;
	public UserCryptoException(String message, Throwable cause) { 
		super(message, cause); 
	}
}

public class User {
	private final PrivateKey privateKey;
	
	public User(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 사용자가 배달의 민족으로 보낼 전자봉투 생성
	public DigitalEnvelope createEnvelope(OrderInfo orderInfo, PaymentInfo paymentInfo, PublicKey cardPublicKey, PublicKey baeminPublicKey) throws UserCryptoException {
		byte[] orderInfoBytes = null;
        byte[] orderInfoHash = null;
        byte[] paymentInfoBytes = null;
        byte[] paymentInfoHash = null;
        byte[] combinedHash = null;
        byte[] pomd = null;
        byte[] digitalSignature = null;
        byte[] cardDataBytes = null;
        byte[] baeminDataBytes = null;

		byte[] orderSalt = HashUtil.generateSalt();
        byte[] paymentSalt = HashUtil.generateSalt();
        
        try {
            orderInfoBytes = SerializeUtil.objectToBytes(orderInfo);
            orderInfoHash = HashUtil.hash(orderInfoBytes, orderSalt);
            
            paymentInfoBytes = SerializeUtil.objectToBytes(paymentInfo);
            paymentInfoHash = HashUtil.hash(paymentInfoBytes, paymentSalt);
        
            combinedHash = HashUtil.combineHash(orderInfoHash, paymentInfoHash);
            pomd = HashUtil.hash(combinedHash, paymentSalt); // 결제 검증용 타겟 솔트 지정
            
            digitalSignature = SignatureUtil.digitalSign(pomd, privateKey);
            
            
            CardData cardData = new CardData(paymentInfo, orderInfoHash, digitalSignature, paymentSalt);
            cardDataBytes = SerializeUtil.objectToBytes(cardData);
            
            DigitalEnvelope cardEnvelope = EnvelopeUtil.sealEnvelope(cardDataBytes, cardPublicKey);
             
            UserToBaeminData baeminData = new UserToBaeminData(orderInfo, orderInfoHash, paymentInfoHash, digitalSignature, cardEnvelope, orderSalt, paymentSalt);
            baeminDataBytes = SerializeUtil.objectToBytes(baeminData);
            
            return EnvelopeUtil.sealEnvelope(baeminDataBytes, baeminPublicKey);
            
        } catch (Exception e) {
            throw new UserCryptoException("사용자 이중서명 및 전자봉투 생성 도중 오류가 발생했습니다.", e);
        } finally {
            if (orderInfoBytes != null) Arrays.fill(orderInfoBytes, (byte) 0);
            if (orderInfoHash != null) Arrays.fill(orderInfoHash, (byte) 0);
            if (paymentInfoBytes != null) Arrays.fill(paymentInfoBytes, (byte) 0);
            if (paymentInfoHash != null) Arrays.fill(paymentInfoHash, (byte) 0);
            if (combinedHash != null) Arrays.fill(combinedHash, (byte) 0);
            if (pomd != null) Arrays.fill(pomd, (byte) 0);
            if (digitalSignature != null) Arrays.fill(digitalSignature, (byte) 0);
            if (cardDataBytes != null) Arrays.fill(cardDataBytes, (byte) 0);
            if (baeminDataBytes != null) Arrays.fill(baeminDataBytes, (byte) 0);
            
            if (orderSalt != null) Arrays.fill(orderSalt, (byte) 0);
            if (paymentSalt != null) Arrays.fill(paymentSalt, (byte) 0);
          
            if (paymentInfo != null) {
                paymentInfo.clearCardNumber();
            }
        }
    }
}

