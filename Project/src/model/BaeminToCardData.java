package model;

import java.io.Serializable;

public class BaeminToCardData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DigitalEnvelope cardEnvelope; // 카드사용 전자봉투
	
	public BaeminToCardData(DigitalEnvelope cardEnvelope) {
		this.cardEnvelope = cardEnvelope;
	}

	public DigitalEnvelope getCardEnvelope() {
		return cardEnvelope;
	}
	
}
