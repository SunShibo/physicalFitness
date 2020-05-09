package com.ichzh.physicalFitness.util.mail;

public class MailUtil {

	public static void sendErrorMail(){
		ProductPriceObserver ps =new ProductPriceObserver();
		ps.update(null, null);
	}
	public static void main(String[] args) {
		sendErrorMail();
	}
}
