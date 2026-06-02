package main;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.util.Scanner;
import java.util.Arrays;

import crypto.RSAUtil;
import entity.Baemin;
import entity.CardCompany;
import entity.Store;
import entity.User;
import model.BaeminToCardData;
import model.BaeminToStoreData;
import model.CardData;
import model.DigitalEnvelope;
import model.OrderInfo;
import model.PaymentInfo;
import model.UserToBaeminData;

public class Main {

	public static void main(String[] args) 
			throws Exception{
		PublicKey userPublicKey = RSAUtil.loadPublicKey("user_public.key");
		PrivateKey userPrivateKey = RSAUtil.loadPrivateKey("user_private.key");
		
		PublicKey baeminPublicKey = RSAUtil.loadPublicKey("baemin_public.key");
		PrivateKey baeminPrivateKey = RSAUtil.loadPrivateKey("baemin_private.key");
		
		PublicKey cardPublicKey = RSAUtil.loadPublicKey("card_public.key");
		PrivateKey cardPrivateKey = RSAUtil.loadPrivateKey("card_private.key");			
		
		PublicKey storePublicKey = RSAUtil.loadPublicKey("store_public.key");
		PrivateKey storePrivateKey = RSAUtil.loadPrivateKey("store_private.key");
		
		// 사용자 객체 생성
		User user = new User(userPrivateKey);
		// 배민 객체 생성
		Baemin baemin = new Baemin(baeminPrivateKey);
		// 카드사 객체 생성
		CardCompany cardCompany = new CardCompany(cardPrivateKey);
		// 가맹점 객체 생성
		Store store = new Store(storePrivateKey);
		
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("---- 배달의 민족 ----");
			System.out.println("---- 엽기 떡볶이 주문 정보 입력 ----");
			System.out.print("메뉴명을 입력하세요: ");
			String menu = sc.nextLine();
			System.out.print("수량을 입력하세요: ");
			int quantity = sc.nextInt();
			System.out.print("가격을 입력하세요: ");
			int price = sc.nextInt();
			
			System.out.println();
			System.out.println("---- 결제 정보 입력 ----");
			System.out.print("카드 번호를 입력하세요: ");
			char[] inputCardNumber = sc.next().toCharArray();
			System.out.println();
		
			// 주문 정보 생성
			OrderInfo orderInfo = new OrderInfo(menu, quantity, price);
			// 결제 정보 생성
			PaymentInfo paymentInfo = new PaymentInfo(inputCardNumber, price);
			
			// 사용자 -> 배민
			DigitalEnvelope baeminEnvelope = user.createEnvelope(orderInfo, paymentInfo, cardPublicKey, baeminPublicKey);
			System.out.println("사용자가 배민에게 전자봉투 전송 완료");
			Arrays.fill(inputCardNumber, '0');
			// 배민에서 전자봉투 개봉
			UserToBaeminData baeminData = baemin.openEnvelope(baeminEnvelope);
			System.out.println("배민 전자봉투 개봉 완료");
		
			// 결과 출력
			 System.out.println();
	         System.out.println("===== 주문 정보 =====");
	         System.out.println("메뉴: " + baeminData.getOrderInfo().getMenu());
	         System.out.println("수량: " + baeminData.getOrderInfo().getQuantity());
	         System.out.println("가격 : " + baeminData.getOrderInfo().getPrice());
	         System.out.println();
	         System.out.println("주문 정보 해시 길이 : " + baeminData.getOrderInfoHash().length);
	         System.out.println("결제 정보 해시 길이 : " + baeminData.getPaymentInfoHash().length);
	         System.out.println("전자서명 길이 : " + baeminData.getDigitalSignature().length);
	         System.out.println();
	         System.out.println("카드사용 전자봉투 존재 여부 : " + (baeminData.getCardEnvelope() != null));
			
         
	         // 배민 -> 가맹점
	         BaeminToStoreData storeData = baemin.generateStoreData(baeminData);
	         DigitalEnvelope storeEnvelope = baemin.createStoreEnvelope(storeData, storePublicKey);
	         
	         // 배민 -> 카드사
	         BaeminToCardData cardData = baemin.generateCardData(baeminData);
	         DigitalEnvelope cardEnvelope = baemin.createCardEnvelope(cardData, cardPublicKey);
	         
	         // 가맹점에서 주문 정보 검증
	         BaeminToStoreData storeReceived = store.openEnvelope(storeEnvelope);
	         boolean storeVerify = store.verifyOrder(storeReceived, userPublicKey);
	         System.out.println("가맹점 주문 정보 검증 결과: " + storeVerify);
	         
	         System.out.println();
	         System.out.println("===== 가맹점이 확인한 정보 =====");
	
	         System.out.println("메뉴 : " + storeData.getOrderInfo().getMenu());
	
	         System.out.println("수량 : " + storeData.getOrderInfo().getQuantity());
	
	         System.out.println("가격 : " + storeData.getOrderInfo().getPrice());
	
	         System.out.println();
	
	         System.out.println("결제 정보 해시 길이 : " + storeData.getPaymentInfoHash().length);
	
	         System.out.println("전자서명 길이 : " + storeData.getDigitalSignature().length);
	
	         System.out.println();
	
	         System.out.println("가맹점 검증 결과 : " + storeVerify);
	         
	         // 카드사에서 결제 정보 검증
	         BaeminToCardData cardReceived = cardCompany.openEnvelope(cardEnvelope);
	         System.out.println();
	         System.out.println("===== 카드사가 확인한 외부 전자봉투 =====");
	         System.out.println(	"내부 카드사용 전자봉투 존재 여부 : " + (cardReceived.getCardEnvelope() != null));
	         
	         CardData cardReceivedData = cardCompany.openCardEnvelope(cardReceived);
	         boolean cardCompanyVerify = cardCompany.verifyPayment(cardReceivedData, userPublicKey);
	         System.out.println("카드사 결제 정보 검증 결과: " + cardCompanyVerify);
	         
	         System.out.println();
	         System.out.println("===== 카드사가 확인한 정보 =====");
	         System.out.println("카드번호 : " + cardReceivedData.getPaymentInfo().getCardNumber());
	         System.out.println("결제금액 : " + cardReceivedData.getPaymentInfo().getPrice());
	         System.out.println();
	         System.out.println("주문정보 해시 길이 : " + cardReceivedData.getOrderInfoHash().length);
	         System.out.println("전자서명 길이 : " + cardReceivedData.getDigitalSignature().length);
	         System.out.println();
	         System.out.println("카드사 검증 결과 : " + cardCompanyVerify);
		}
         
	}
}
