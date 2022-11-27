package com.test.atm.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.test.atm.model.ATM;
import com.test.atm.model.CreditCard;
import com.test.atm.staticmethods.StaticMethods;
import com.test.atm.thread.BlockingCardThread;

public class Runner {

	public static void main(String[] args) {

		final Pattern cardNumberPattern = Pattern.compile("\\d{4}([-]|)\\d{4}([-]|)\\d{4}([-]|)\\d{4}");

		ATM atm = new ATM(new BigDecimal("10000000"), new ArrayList<CreditCard>());
		readDataFromFile(atm);

		String cardNumber = null;
		CreditCard creditCard = null;
		boolean accountAccess = false;
		int attempts = 0;

		while (true) {

			if (!accountAccess) {
				Scanner cardNumberScanner = new Scanner(System.in);
				System.out.print("Enter card number: ");

				if (cardNumberScanner.hasNext()) {
					cardNumber = cardNumberScanner.next();
					Matcher cardNumberMatcher = cardNumberPattern.matcher(cardNumber);
					if (cardNumberMatcher.matches()) {
						creditCard = findCardByName(atm, cardNumber);
					} else {
						System.out.println("Incorrect format of number.");
					}
				}
			}

			if (creditCard != null) {
				if (!creditCard.isBlocked()) {

					while (!accountAccess) {
						System.out.print("Enter password: ");
						Scanner cardPasswordScanner = new Scanner(System.in);

						if (creditCard.getPassword().equals(cardPasswordScanner.next())) {
							if (!creditCard.isBlocked()) {
								System.out.println("Account accessed.");
								accountAccess = true;
								attempts = 0;
							} else {
								System.out.println("This card is blocked.");
							}
						} else {
							attempts++;
							if (attempts > 2) {
								System.out.println("Card was blocked.");
								BlockingCardThread blockCard = new BlockingCardThread(creditCard);
								blockCard.run();
								atm.getCards().remove(findCardByName(atm, creditCard.getNumber()));
								atm.getCards().add(creditCard);
								attempts = 0;
								writeDataInFile(atm);
								break;
							}
							System.out.println(
									"Incorrect password. You have left " + (3 - attempts) + " attempts. Try again.");
						}
					}

					if (accountAccess) {
						System.out.println("0. Check card balance\n1. Withdraw funds from the account\n"
								+ "2. Peplenish the balance\n3. Exit\nEnter number of the operation: ");
						Scanner opNumber = new Scanner(System.in);

						switch (opNumber.nextInt()) {

						case 0:
							System.out.println("You have " + StaticMethods.getRublesFormate(creditCard.getBalance())
									+ " rubles on your card.");
							break;

						case 1:
							System.out.println("Enter amount: ");
							Scanner amountSc = new Scanner(System.in);
							BigDecimal amount = new BigDecimal(amountSc.next());

							if (amount.compareTo(atm.getBallance()) == -1) {
								if (amount.compareTo(creditCard.getBalance()) == -1) {
									atm.setBallance(atm.getBallance().subtract(amount));
									creditCard.setBalance(creditCard.getBalance().subtract(amount));
									System.out.println("You withdrew: " + StaticMethods.getRublesFormate(amount)
											+ " rubles from your account. Now you have: "
											+ StaticMethods.getRublesFormate(creditCard.getBalance())
											+ " rubles on your card.");
								} else {
									System.out.println("You have no enough money on your ballance.");
								}
							} else {
								System.out.println("There is not enough money in the ATM.");
							}
							writeDataInFile(atm);
							break;

						case 2:
							System.out.println("Enter the replenishment amount: ");
							Scanner replenishmentSc = new Scanner(System.in);
							BigDecimal replenishmentAmount = new BigDecimal(replenishmentSc.next());

							if (replenishmentAmount.compareTo(new BigDecimal("1000000")) == 1) {
								System.out.println("The replenishment amount can't be higher than 1 000 000.");
							} else {
								atm.setBallance(atm.getBallance().add(replenishmentAmount));
								creditCard.setBalance(creditCard.getBalance().add(replenishmentAmount));
								System.out.println("You replenished the balance by the amount: "
										+ StaticMethods.getRublesFormate(replenishmentAmount)
										+ " rubles. Now you have: "
										+ StaticMethods.getRublesFormate(creditCard.getBalance())
										+ " rubles on your card.");
							}
							writeDataInFile(atm);
							break;
						case 3:
							accountAccess = false;
							break;
						}
					}
				} else {
					System.out.println("Card is blocked.");
				}
			} else {
				System.out.println("Card wasn't found.");
			}
		}
	}

	private static void readDataFromFile(ATM atm) {

		final String FILEPATH = "D:\\JavaIntensive\\data.txt";
		File file = new File(FILEPATH);

		try (Scanner fileReader = new Scanner(file)) {

			while (true) {
				if (fileReader.hasNext()) {
					String[] cardData = fileReader.nextLine().split(" ");
					atm.addCard(new CreditCard(cardData[0], new BigDecimal(cardData[1]), cardData[2],
							Boolean.valueOf(cardData[3])));
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}

	private static void writeDataInFile(ATM atm) {
		final File FILENAME = new File("data.txt");
		try (FileWriter writer = new FileWriter(FILENAME, false)) {
			StringBuffer cards = new StringBuffer();
			atm.getCards().stream().forEach(card -> cards.append(card));
			writer.write(cards.toString());
			writer.flush();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static CreditCard findCardByName(ATM atm, String cardNumber) {
		return atm.getCards().stream().filter(card -> card.getNumber().equals(cardNumber)).findAny().orElse(null);
	}
}
