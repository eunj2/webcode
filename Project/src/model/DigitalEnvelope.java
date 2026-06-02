package model;

import java.io.Serializable;

public class DigitalEnvelope implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private byte[] encryptedData; // AES로 암호화한 결제 정보 데이터
	private byte[] encryptedKey; // RSA로 암호화된 AES 키
	
	public DigitalEnvelope(byte[] encryptedData, byte[] encryptedKey) {
        this.encryptedData = encryptedData != null ? encryptedData.clone() : null;
        this.encryptedKey = encryptedKey != null ? encryptedKey.clone() : null;
    }

    public byte[] getEncryptedData() {
        return encryptedData != null ? encryptedData.clone() : new byte[0];
    }

    public byte[] getEncryptedKey() {
        return encryptedKey != null ? encryptedKey.clone() : new byte[0];
    }
}