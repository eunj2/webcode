package entity;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import crypto.EnvelopeUtil;
import crypto.HashUtil;
import crypto.SerializeUtil;
import crypto.SignatureUtil;
import model.BaeminToCardData;
import model.CardData;
import model.DigitalEnvelope;
import model.PaymentInfo;

class CardCompanyException extends Exception {
	private static final long serialVersionUID = 1L;
    public CardCompanyException(String message, Throwable cause) { 
		super(message, cause); 
	}
}

public class CardCompany {
	private final PrivateKey privateKey;
	
	public CardCompany(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 배달의 민족에서 보낸 2차 전자봉투 개봉
	public BaeminToCardData openEnvelope(DigitalEnvelope cardEnvelope) throws CardCompanyException {
		byte[] decrypted = null;
        try {
            decrypted = EnvelopeUtil.openEnvelope(cardEnvelope, privateKey);
            return (BaeminToCardData) SerializeUtil.bytesToObject(decrypted);
        } catch (Exception e) {
            // 예외 추상화로 시스템 정보 보호
            throw new CardCompanyException("2차 전자봉투 개봉 중 오류가 발생했습니다.", e);
        } finally {
            //데이터 제거
            if (decrypted != null) {
                Arrays.fill(decrypted, (byte) 0);
            }
        }
    }
	
	// 1차 전자봉투 개봉
	public CardData openCardEnvelope(BaeminToCardData cardData) throws CardCompanyException {
		byte[] decrypted = null;
        try {
            DigitalEnvelope cardEnvelope = cardData.getCardEnvelope();
            decrypted = EnvelopeUtil.openEnvelope(cardEnvelope, privateKey);
            return (CardData) SerializeUtil.bytesToObject(decrypted);
        } catch (Exception e) {
            throw new CardCompanyException("1차 결제 전자봉투 개봉 중 오류가 발생했습니다.", e);
        } finally {
            if (decrypted != null) {
                Arrays.fill(decrypted, (byte) 0);
            }
        }
    }
	
	// 결제 정보 검증
	public boolean verifyPayment(CardData cardData, PublicKey userPublicKey) throws CardCompanyException{
		// 결제 정보 해시 생성
		byte[] paymentInfoBytes = null; 
		byte[] paymentInfoHash = null;
    	byte[] combined = null; 
		byte[] combinedHash = null;
	    try {
			//직렬화
	        PaymentInfo paymentInfo = cardData.getPaymentInfo();
	        paymentInfoBytes = SerializeUtil.objectToBytes(paymentInfo);
	        
	        byte[] salt = cardData.getPaymentInfoSalt(); 
	        paymentInfoHash = HashUtil.hash(paymentInfoBytes, salt);
			
			byte[] orderInfoHash = cardData.getOrderInfoHash();
	        combined = HashUtil.combineHash(orderInfoHash, paymentInfoHash);
	        
			// 최종 해시 값
			combinedHash = HashUtil.hash(combined, salt); 
	        
	        byte[] digitalSignature = cardData.getDigitalSignature();
	        return SignatureUtil.digitalSignVerify(combinedHash, digitalSignature, userPublicKey);
	    
	    } catch (Exception e) {
	        throw new CardCompanyException("결제 정보 이중서명 검증 중 오류가 발생했습니다.", e);
	    } finally {
	        if (paymentInfoBytes != null) Arrays.fill(paymentInfoBytes, (byte) 0);
	        if (paymentInfoHash != null) Arrays.fill(paymentInfoHash, (byte) 0);
	        if (combined != null) Arrays.fill(combined, (byte) 0);
	        if (combinedHash != null) Arrays.fill(combinedHash, (byte) 0);
	        
	        if (cardData != null && cardData.getPaymentInfo() != null) {
	            cardData.getPaymentInfo().clearCardNumber();
	        }
	    }
	}
}

