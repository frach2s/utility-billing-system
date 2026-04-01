public interface PaymentProcessable{
	void printStatementTitle(double amount);
    void printStatementTitle(double amount, String paymentNote); // paymentnote is like "paid via gcash / partial payment 
	
	boolean validatePaymentStatus();
	double getOutstandingBalance();
}