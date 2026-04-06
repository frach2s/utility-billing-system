public class BundledAccount extends SubscriptionAccount implements Reconnectable {
	private String includedServices;
	private double bundleDiscountRate;
	private double electricityCharge;
	private double waterCharge;
	private double internetCharge;
	
	// Constructor 1 — empty
	public BundledAccount() {
		super();
		this.bundleDiscountRate = 0.10; 
	}

	// Constructor 2 — basic info
	public BundledAccount(String accountNumber, String customerName) {
		super(accountNumber, customerName);
		this.bundleDiscountRate = 0.10; 
	}

	// Constructor 3 — full details 
	public BundledAccount(String accountNumber, String customerName, String address,
			String householdType, String planTier, double previousBalance,
			double currentUsage, double bundleDiscountRate) {
		super(accountNumber, customerName, address, householdType, planTier, previousBalance, currentUsage);
		this.bundleDiscountRate = bundleDiscountRate; 
	}
	
	//Overriden Methods		
	@Override
	public double computeMonthlyBill() {

		// internet charge based on plan tier
		internetCharge = 999; // Basic default
		String plan = getPlanTier();
		if (plan.equalsIgnoreCase("Standard")) internetCharge = 1499;
		else if (plan.equalsIgnoreCase("Premium")) internetCharge = 1999;

		double subtotal = electricityCharge + waterCharge + internetCharge;

		double discount = 0;
		if (bundleDiscountRate > 0) {
			discount = subtotal * bundleDiscountRate;
		}

		double total = subtotal - discount + getPreviousBalance();
		setFinalBill(total);
		return total;
	}
	//=========================================
	
	@Override 
	public String getServiceType() {
		return "Bundle Service";
	}
	
	@Override
	public String getBillingBreakdown() {
		double subtotal = electricityCharge + waterCharge + internetCharge;
		double discount = subtotal * bundleDiscountRate;

		return "========= BUNDLE BREAKDOWN =========\n" +
			   "Usage Entered      : " + String.format("%.2f", getCurrentUsage()) + " units\n" +
			   "Electricity Charge : " + String.format("%.2f", electricityCharge) + "\n" +
			   "Water Charge       : " + String.format("%.2f", waterCharge) + "\n" +
			   "Internet Charge    : " + String.format("%.2f", internetCharge) + "\n" +
			   "----------------------\n" +
			   "Subtotal           : " + String.format("%.2f", subtotal) + "\n" +
			   "Bundle Discount    : -" + String.format("%.2f", discount) + "\n" +
			   "Previous Balance   : " + String.format("%.2f", getPreviousBalance()) + "\n" +
			   "----------------------\n" +
			   "Total Bill         : " + String.format("%.2f", getFinalBill());
	}
	
	
	@Override
	public String printStatementTitle() {
		return "========= BUNDLED UTILITY BILL =========";
	}

	@Override
	public String getStatementBody() {
		return getBillingBreakdown();
	}

	@Override
	public String getStatementFooter() {
		return "Total: " + getFinalBill();
	}
	
	
	//Reconnectable
	@Override
	public boolean canReconnect() {
		return getPreviousBalance() <= 0;
	}
	
	@Override
	public void reconnectService() {
		if (canReconnect()) {
			setActive(true); 
			setPaymentStatus("Paid");
			System.out.println("Service reconnected successfully.");
		} else {
			System.out.println("Cannot reconnect. Outstanding balance exists.");
	    }
    }
	
	@Override
	public String getReconnectionMessage() {
		return canReconnect () ? "Eligible for reconnection." : "Not eligible.";
	}
	
	//=======================================================//
	// overloaded methods
	public void requestPlanChange(String newPlan) {
    if (getPlanTier().equalsIgnoreCase(newPlan)) { //kukunin nya ung current plan through getplantier() tas compare sa nya sa newplan then if it is the same then mag-out na sa method
        System.out.println("Current plan is already " + newPlan + ". No changes made.");
        return;
    }
    String oldPlan = getPlanTier();
    String current = oldPlan.toLowerCase();
    String next = newPlan.toLowerCase();
	
	
	//isUpgrade is true if current is basic AND next is standard or premium, OR if current is standard AND next is premium
    boolean isUpgrade = (current.equals("basic") && (next.equals("standard") || next.equals("premium"))) || (current.equals("standard") && next.equals("premium"));

    boolean isDowngrade = (current.equals("premium") && (next.equals("standard") || next.equals("basic"))) || (current.equals("standard") && next.equals("basic"));

	//eto since di pede magsabay ang downgrade and upgradee eto ung magh-handle neto
    if (isUpgrade) {
        System.out.println("Upgrading plan from " + oldPlan + " to " + newPlan + ".");
    } else if (isDowngrade) {
        System.out.println("Downgrading plan from " + oldPlan + " to " + newPlan + ".");
    }

    setPlanTier(newPlan);
    setServiceRequestNote("Plan change requested: " + oldPlan + " to " + newPlan);
    System.out.println("Plan successfully changed to: " + newPlan);
	}
	
	//verload
	public void requestPlanChange(String newPlan, String effectiveCycle) {
		if (getPlanTier().equalsIgnoreCase(newPlan)) {
        System.out.println("Current plan is already " + newPlan + ". No changes made.");
        return;
    }
    String oldPlan = getPlanTier();
    String current = oldPlan.toLowerCase();
    String next = newPlan.toLowerCase();

    boolean isUpgrade = (current.equals("basic") && (next.equals("standard") || next.equals("premium"))) || (current.equals("standard") && next.equals("premium"));

    boolean isDowngrade = (current.equals("premium") && (next.equals("standard") || next.equals("basic"))) || (current.equals("standard") && next.equals("basic"));

    if (isUpgrade) {
        System.out.println("Upgrading plan from " + oldPlan + " to " + newPlan + ".");
    } else if (isDowngrade) {
        System.out.println("Downgrading plan from " + oldPlan + " to " + newPlan + ".");
    }

    setPlanTier(newPlan);
    setServiceRequestNote("Plan change requested: " + oldPlan + " to " + newPlan);
    System.out.println("Plan will take effect on: " + effectiveCycle);
    System.out.println("Plan successfully changed to: " + newPlan);
	}
	//=======================================================//
	
	@Override
	public void printStatement() {
		System.out.println("Full Statement:");
		System.out.println(getBillingBreakdown());
	}
	
	@Override
	public void printStatement(boolean shortMode) {
		if (shortMode) {
			System.out.println("Total Bill: " + getFinalBill());
		} else {
			printStatement();
		}
	}
	
	//Getters
	public String getIncludedServices() {
		return includedServices;
	}
	
	public void setIncludedServices(String includedServices) {
		this.includedServices = includedServices;
	}
	
	public double getBundleDiscountRate() {
		return bundleDiscountRate;
	}
	
	public void setBundleDiscountRate(double bundleDiscountRate){
		this.bundleDiscountRate = bundleDiscountRate;
	}
	
	public double getElectricityCharge() {
		return electricityCharge;
	}
    
	public void setElectricityCharge(double electricityCharge) {
		this.electricityCharge = electricityCharge;		
	}
	
	public double getWaterCharge() {
		return waterCharge;
	}
		
	public void	 setWaterCharge(double  waterCharge) {
		this.waterCharge = waterCharge;
	}
	
	public double getInternetCharge() {
		return internetCharge;
	}
	
	public void setInternetCharge(double internetCharge) {
		this.internetCharge = internetCharge;
	}
}