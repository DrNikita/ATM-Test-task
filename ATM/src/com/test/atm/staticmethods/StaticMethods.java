package com.test.atm.staticmethods;

import java.math.BigDecimal;

public class StaticMethods {

	public static BigDecimal getRublesFormate(BigDecimal amount) {
		return amount.divide(new BigDecimal("100"));
	}
}
