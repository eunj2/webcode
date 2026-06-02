package model;

import java.io.Serializable;

public class BaeminToStoreData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private OrderInfo orderInfo;
	private byte[] paymentInfoHash;
	private byte[] digitalSignature;
	private byte[] orderInfoSalt;
	
	public BaeminToStoreData(OrderInfo orderInfo, byte[] paymentInfoHash, byte[] digitalSignature) {
		this.orderInfo = orderInfo;
		this.paymentInfoHash = paymentInfoHash != null ? paymentInfoHash.clone() : null;
        this.digitalSignature = digitalSignature != null ? digitalSignature.clone() : null;
		this.orderInfoSalt = orderInfoSalt != null ? orderInfoSalt.clone() : null;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public byte[] getPaymentInfoHash() {
        return paymentInfoHash != null ? paymentInfoHash.clone() : new byte[0];
    }

    public byte[] getDigitalSignature() {
        return digitalSignature != null ? digitalSignature.clone() : new byte[0];
    }

	public byte[] getOrderInfoSalt() { 
        return orderInfoSalt != null ? orderInfoSalt.clone() : new byte[0]; 
    }
}
