public interface PaymentProcessable{
	void processPayment(double amount);
    void processPayment(double amount, String paymentNote);
    boolean validatePaymentStatus();
    double getOutstandingBalance();
}