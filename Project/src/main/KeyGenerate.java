package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import crypto.RSAUtil;

public class KeyGenerate {

	public static void main(String[] args) 
			throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		// 사용자 키 생성
		KeyPair userKeyPair = RSAUtil.generateKeyPair();
		RSAUtil.savePublicKey("user_public.key", userKeyPair.getPublic());
		RSAUtil.savePrivateKey("user_private.key", userKeyPair.getPrivate());
		
		// 배민 키 생성
		KeyPair baeminKeyPair = RSAUtil.generateKeyPair();
		RSAUtil.savePublicKey("baemin_public.key", baeminKeyPair.getPublic());
		RSAUtil.savePrivateKey("baemin_private.key", baeminKeyPair.getPrivate());
		
		// 카드사 키 생성
		KeyPair cardKeyPair = RSAUtil.generateKeyPair();
		RSAUtil.savePublicKey("card_public.key", cardKeyPair.getPublic());
		RSAUtil.savePrivateKey("card_private.key", cardKeyPair.getPrivate());
		
		// 가맹점 키 생성
		KeyPair storeKeyPair = RSAUtil.generateKeyPair();
		RSAUtil.savePublicKey("store_public.key", storeKeyPair.getPublic());
		RSAUtil.savePrivateKey("store_private.key", storeKeyPair.getPrivate());
		
		System.out.println("키 생성 완료");

	}

}
