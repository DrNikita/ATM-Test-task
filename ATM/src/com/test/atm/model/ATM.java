package com.test.atm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ATM {
	private BigDecimal ballance;
	private List<CreditCard> cards;

	public ATM(BigDecimal ballance, ArrayList<CreditCard> arrayList) {
		this.ballance = ballance;
		this.cards = arrayList;
	}

	public BigDecimal getBallance() {
		return ballance;
	}

	public void setBallance(BigDecimal ballance) {
		this.ballance = ballance;
	}

	public List<CreditCard> getCards() {
		return cards;
	}

	public void setCards(List<CreditCard> cards) {
		this.cards = cards;
	}

	public void addCard(CreditCard card) {
		this.cards.add(card);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ballance, cards);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ATM other = (ATM) obj;
		return Objects.equals(ballance, other.ballance) && Objects.equals(cards, other.cards);
	}

	@Override
	public String toString() {
		return "ATM [ballance=" + ballance + ", cards=" + cards + "]";
	}

}
