public abstract class SubscriptionAccount implements PrintableStatement, PaymentProcessable{
	//FIELDS
	private String accountNumber;
	private String customerName;
	private String address;
	private String householdType;
	private String planTier;
	private String paymentStatus;
	private String serviceRequestNote;
	private boolean active;
	private double previousBalance;
	private double currentUsage;
	private double lateFee;
	private double discountAmount;
	private double finalBill;
	
	public SubscriptionAccount(){
		
	}; //just incase if sa main method magc-create kayo ng empty object like new SubscriptionAccount ()----
	
	public SubscriptionAccount(String accountNumber, String customerName){
		this.accountNumber = accountNumber;
		this.customerName = customerName;
	}
	
	public SubscriptionAccount(String accountNumber, String customerName, String address, String householdType,String planTier, double previousBalance, double currentUsage){
		this.accountNumber = accountNumber;
		this.customerName = customerName;
		this.address = address;
		this.householdType = householdType;
		this.planTier = planTier;
		this.previousBalance = previousBalance;
		this.currentUsage = currentUsage;
	}
	
	//ABSTRACT METHOD need override sa mga subclasses
	abstract double computeMonthlyBill();
	abstract String getServiceType();
	abstract String getBillingBreakdown();
	
	
	//============  GETTERS ======
	public String getAccountNumber(){
		return accountNumber;
	}
	
	public String getCustomerName(){
		return customerName;
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getHouseholdType(){
		return householdType;
	}
	
	public String getPlanTier(){
		return planTier;
	}
	
	public String getPaymentStatus(){
		return paymentStatus;
	}
	
	public String getServiceRequestNote(){
		return serviceRequestNote;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public double getPreviousBalance(){
		return previousBalance;
	}
	
	public double getCurrentUsage(){
		return currentUsage;
	}
	
	public double getLateFee(){
		return lateFee;
	}
	
	public double getDiscountAmount(){
		return discountAmount;
	}
	
	public double getFinalBill(){
		return finalBill;
	}
	
	// ========== SETTERS ==========
	
	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}
	
	public void setCustomerName(String customerName){
		this.customerName =  customerName;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public void setHouseholdType(String householdType){
		this.householdType = householdType;
	}
	
	public void setPlanTier(String planTier){
		this.planTier = planTier;
	}
	
	public void setPaymentStatus(String paymentStatus){
		this.paymentStatus = paymentStatus;
	}
	
	public void setServiceRequestNote(String serviceRequestNote){
		this.serviceRequestNote = serviceRequestNote;
	}
	
	public void setActive (boolean active){
		this.active = active;
	}
	
	public void setPreviousBalance(double previousBalance){
		this.previousBalance = previousBalance;
	}
	
	public void setCurrentUsage(double currentUsage){
		this.currentUsage = currentUsage;
	}
	
	public void setLateFee(double lateFee){
		this.lateFee = lateFee;
	}
	
	public void setDiscountAmount(double discountAmount){
		this.discountAmount = discountAmount;
	}
	
	public void setFinalBill (double finalBill){
		this.finalBill = finalBill;
	}
	
	//ADDITONAL METHODS FOR UPDATING DATA
	public void updateUsage(double newUsage){
		this.currentUsage = newUsage;
	} 
	
	public void updatePaymentStatus(String status){
		this.paymentStatus = status;
	}
	
	public void applySharedPenaltyRule(){
		if (this.paymentStatus.equals("Unpaid")) {
             this.lateFee = this.finalBill * 0.05; 
             System.out.println("Late fee applied: " + this.lateFee);
        } else {
        System.out.println("No penalty. Account is paid.");
        }
	}
	
	public String buildBasicSummary() {
        return "Account: " + this.accountNumber +
               "\nCustomer: " + this.customerName +
               "\nPlan: " + this.planTier +
               "\nStatus: " + this.paymentStatus +
               "\nFinal Bill: " + this.finalBill;
        }
	
	public String toString() { // para to if magcreate ka ng object tas need mo ecall out ng mabilisan eto gagamitin mo for example: 
	// SubscriptionAccount acc1 = new ElectricAccount();
	//System.out.print(acc); ka nlng tas map-print na yung nsa loob ng build summary withgout using acc.BuildSummary
        return buildBasicSummary();  
        }
	//abstract methods from interface printable statement- subclasses will override these
    public abstract String printStatementTitle();
    public abstract String getStatementBody();
    public abstract String getStatementFooter();
    public abstract void printStatement();
    public abstract void printStatement(boolean shortMode);
  
    @Override //fixed: nagkamali ako sa interface kaya nag e-error -xtian
    public void processPayment(double amount) {
       this.finalBill -= amount;
       if (this.finalBill <= 0) {
         this.finalBill = 0;
         this.paymentStatus = "Paid";
        } else {
        this.paymentStatus = "Partial";
        }
        System.out.println("Payment of " + amount + " processed.");
    }

    @Override
    public void processPayment(double amount, String paymentNote) {
    processPayment(amount);
      this.serviceRequestNote = paymentNote;
      System.out.println("Note: " + paymentNote);
    }

    @Override
    public boolean validatePaymentStatus() {
    return this.paymentStatus.equals("Paid");
    }

    @Override
    public double getOutstandingBalance() {
    return this.finalBill;
    }

}