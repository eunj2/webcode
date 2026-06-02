package entity;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import crypto.EnvelopeUtil;
import crypto.SerializeUtil;
import model.BaeminToCardData;
import model.BaeminToStoreData;
import model.DigitalEnvelope;
import model.UserToBaeminData;

class BaeminCryptoException extends Exception {
	private static final long serialVersionUID = 1L;
    public BaeminCryptoException(String message, Throwable cause) { 
    	super(message, cause); 
    	}
}

public class Baemin {
	private final PrivateKey privateKey;
	
	public Baemin(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 사용자가 보낸 전자봉투 개봉
	public UserToBaeminData openEnvelope(DigitalEnvelope baeminEnvelope) throws BaeminCryptoException {
		byte[] decrypted = null;
		try {
			decrypted = EnvelopeUtil.openEnvelope(baeminEnvelope, privateKey);
			return (UserToBaeminData) SerializeUtil.bytesToObject(decrypted);
		} catch (Exception e) {
			throw new BaeminCryptoException("배달의민족 수신 전자봉투 해독 중 오류가 발생했습니다.", e);
		} finally {
			if (decrypted != null) {
				Arrays.fill(decrypted, (byte) 0);
			}
		}
	}
	
	public BaeminToStoreData generateStoreData(UserToBaeminData data) {
		return new BaeminToStoreData(
			data.getOrderInfo(), 
			data.getPaymentInfoHash(), 
			data.getDigitalSignature()
		);
	}
	
	// 가맹점 전달 전자봉투 생성
	public DigitalEnvelope createStoreEnvelope(BaeminToStoreData storeData, PublicKey storePublicKey) throws BaeminCryptoException {
		byte[] storeDataBytes = null;
		try {
			storeDataBytes = SerializeUtil.objectToBytes(storeData);
			return EnvelopeUtil.sealEnvelope(storeDataBytes, storePublicKey);
		} catch (Exception e) {
			throw new BaeminCryptoException("가맹점용 전자봉투 생성 도중 오류가 발생했습니다.", e);
		} finally {
			if (storeDataBytes != null) {
				Arrays.fill(storeDataBytes, (byte) 0);
			}
		}
	}
	
	// 카드사 전달 데이터 생성
		public BaeminToCardData generateCardData(UserToBaeminData data) {
			return new BaeminToCardData(data.getCardEnvelope());
		}
		
		// 카드사 전달 전자봉투 생성
		public DigitalEnvelope createCardEnvelope(BaeminToCardData cardData, PublicKey cardPublicKey) throws BaeminCryptoException {
			byte[] cardDataBytes = null;
			try {
				cardDataBytes = SerializeUtil.objectToBytes(cardData);
				return EnvelopeUtil.sealEnvelope(cardDataBytes, cardPublicKey);
			} catch (Exception e) {
				throw new BaeminCryptoException("카드사용 전자봉투 생성 도중 오류가 발생했습니다.", e);
			} finally {
				if (cardDataBytes != null) {
					Arrays.fill(cardDataBytes, (byte) 0);
				}
			}
		}
}