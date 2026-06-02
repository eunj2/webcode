package model;

import java.io.Serializable;

public class UserToBaeminData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private OrderInfo orderInfo; // 주문 정보
	private byte[] orderInfoHash; // 주문 정보 해시
	private byte[] paymentInfoHash; // 결제 정보 해시
	private byte[] digitalSignature; // 이중서명
	private DigitalEnvelope cardEnvelope; // 카드사용 전자봉투
	private byte[] orderInfoSalt;
    private byte[] paymentInfoSalt;
	
	public UserToBaeminData (OrderInfo orderInfo, byte[] orderInfoHash, byte[] paymentInfoHash,
            byte[] digitalSignature, DigitalEnvelope cardEnvelope, byte[] orderInfoSalt, byte[] paymentInfoSalt) {
        this.orderInfo = orderInfo;
        this.orderInfoHash = orderInfoHash != null ? orderInfoHash.clone() : null;
        this.paymentInfoHash = paymentInfoHash != null ? paymentInfoHash.clone() : null;
        this.digitalSignature = digitalSignature != null ? digitalSignature.clone() : null;
        this.cardEnvelope = cardEnvelope;
		this.orderInfoSalt = orderInfoSalt != null ? orderInfoSalt.clone() : null;
        this.paymentInfoSalt = paymentInfoSalt != null ? paymentInfoSalt.clone() : null;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public byte[] getOrderInfoHash() {
        return orderInfoHash != null ? orderInfoHash.clone() : new byte[0];
    }

    public byte[] getPaymentInfoHash() {
        return paymentInfoHash != null ? paymentInfoHash.clone() : new byte[0];
    }

    public byte[] getDigitalSignature() {
        return digitalSignature != null ? digitalSignature.clone() : new byte[0];
    }

    public DigitalEnvelope getCardEnvelope() {
        return cardEnvelope;
    }

	public byte[] getOrderInfoSalt() {
        return orderInfoSalt != null ? orderInfoSalt.clone() : new byte[0];
    }

    public byte[] getPaymentInfoSalt() {
        return paymentInfoSalt != null ? paymentInfoSalt.clone() : new byte[0];
    }
}
