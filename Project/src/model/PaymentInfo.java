package model;

import java.io.Serializable;
import java.util.Arrays;

public class PaymentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private char[] cardNumber;
	private int price;
	
	public PaymentInfo(char[] cardNumber, int price) {
		this.cardNumber = cardNumber != null ? cardNumber.clone() : null;
		this.price = price;
	}

	public char[] getCardNumber() {
		return cardNumber != null ? cardNumber.clone() : new char[0];
	}

	public int getPrice() {
		return price;
	}
	//카드번호 즉시 지우기
	public void clearCardNumber() {
        if (this.cardNumber != null) {
            Arrays.fill(this.cardNumber, '0'); // 메모리 공간을 문자 '0'으로 덮어씀
        }
    }
	@Override
    public String toString() {
		if (cardNumber == null || cardNumber.length == 0) {
            return "카드번호: ****, 결제금액: " + price;
        }
        
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < cardNumber.length; i++) {
            if (i < 4) {
                masked.append(cardNumber[i]); // 앞 4자리만 평문 노출
            } else {
                if (cardNumber[i] == '-') {
                    masked.append('-'); // 포맷용 대시 기호는 유지
                } else {
                    masked.append('*'); // 나머지 금융 데이터는 전부 마스킹
                }
            }
        }
        return "카드번호: " + masked.toString() + ", 결제금액: " + price;
	}	
}
