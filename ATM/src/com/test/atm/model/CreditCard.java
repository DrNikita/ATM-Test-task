package com.test.atm.model;

import java.math.BigDecimal;
import java.util.Objects;

public class CreditCard {

	private String number;
	private BigDecimal balance;
	private String password;
	private Boolean isBlocked = false;

	public CreditCard(String number, BigDecimal balance, String password, boolean isBlocked) {
		this.number = number;
		this.balance = balance;
		this.password = password;
		this.isBlocked = isBlocked;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public void block() {
		this.isBlocked = true;
	}

	public void unblock() {
		this.isBlocked = false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(balance, isBlocked, number, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCard other = (CreditCard) obj;
		return Objects.equals(balance, other.balance) && isBlocked == other.isBlocked
				&& Objects.equals(number, other.number) && Objects.equals(password, other.password);
	}

	@Override
	public String toString() {
		return number + " " + balance + " " + password + " " + isBlocked + "\n";
	}
}
