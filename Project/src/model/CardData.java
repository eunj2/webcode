package model;

import java.io.Serializable;

public class CardData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private PaymentInfo paymentInfo; // 결제 정보
	private byte[] orderInfoHash; // 주문 정보 해시
	private byte[] digitalSignature; // 이중서명
	private byte[] paymentInfoSalt;
	
	public CardData(PaymentInfo paymentInfo, byte[] orderInfoHash, byte[] digitalSignature, byte[] paymentInfoSalt) {
		this.paymentInfo = paymentInfo;
		this.orderInfoHash = orderInfoHash != null ? orderInfoHash.clone() : null;
        this.digitalSignature = digitalSignature != null ? digitalSignature.clone() : null;
		this.paymentInfoSalt = paymentInfoSalt != null ? paymentInfoSalt.clone() : null;
	}

	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public byte[] getOrderInfoHash() {
		return orderInfoHash != null ? orderInfoHash.clone() : new byte[0];
	}

	public byte[] getDigitalSignature() {
		return digitalSignature != null ? digitalSignature.clone() : new byte[0];
	}

	public byte[] getPaymentInfoSalt() { 
        return paymentInfoSalt != null ? paymentInfoSalt.clone() : new byte[0];
	}
}
