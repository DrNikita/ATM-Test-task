package com.test.atm.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import com.test.atm.model.CreditCard;

public class BlockingCardThread implements Runnable {

	private CreditCard creditCard;

	public BlockingCardThread(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Override
	public void run() {
		creditCard.block();
		Date date = new Date();
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(date);
		currentDate.add(Calendar.MILLISECOND, 5000);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				creditCard.unblock();
			}
		}, currentDate.getTime());
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creditCard);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockingCardThread other = (BlockingCardThread) obj;
		return Objects.equals(creditCard, other.creditCard);
	}

	@Override
	public String toString() {
		return "WaitingThread [creditCard=" + creditCard + "]";
	}
}
