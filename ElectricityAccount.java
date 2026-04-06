public class ElectricityAccount extends SubscriptionAccount implements Reconnectable {
    // fields
    private double meterRate = 10;
    private double generationCharge = 150;
    private double serviceAvailabilityFee = 75; // elec acc self declared variables

    // constructors
    public ElectricityAccount() {} // default constructor (empty)

    public ElectricityAccount(String accountNumber, String customerName) {
        super(accountNumber, customerName); // initialization of basic info only
    }

    public ElectricityAccount(String accountNumber, String customerName,String address, String householdType,String planTier, double previousBalance,double currentUsage, double meterRate) {
        super(accountNumber, customerName, address, householdType,
              planTier, previousBalance, currentUsage);
        this.meterRate = meterRate; // full constructor with the newly declared variable (meterRate)
    }

    // methods
    @Override
    public double computeMonthlyBill() {

        double usageCharge = getCurrentUsage() * meterRate;

        double total = usageCharge
                     + generationCharge
                     + serviceAvailabilityFee
                     + getPreviousBalance(); // returning multiple fields as bill is composed of all of the following (for ex. getPreviousBalance value adds up if it is unpaid)
        setFinalBill(total); 

        return total;
    }

    @Override
    public String getServiceType() {
        return "Electricity";
    }

    @Override
    public String getBillingBreakdown() {

        double usageCharge = getCurrentUsage() * meterRate;

        return "Usage Charge: " + usageCharge +
               "\nGeneration Charge: " + generationCharge +
               "\nService Fee: " + serviceAvailabilityFee +
               "\nPrevious Balance: " + getPreviousBalance() +
               "\n----------------------" +
               "\nTotal Bill: " + getFinalBill(); // display of values only
    }

    // reconnectable
    @Override
    public boolean canReconnect() {
        return !isActive(); // can only apply for reconnection if the account is inactive
    }

    @Override
    public void reconnectService() {
        if (canReconnect()) {
            setActive(true);
            System.out.println("Service reconnected.");
        } else {
            System.out.println("Service is already active.");
        }
    }

    @Override
    public String getReconnectionMessage() {
        return isActive() ? "Service is active." : "Service is eligible for reconnection."; // status
    }
	
	//=======================================================//
	// overloaded methods
	//kukunin nya ung current plan through getplantier() tas compare sa nya sa newplan then if it is the same then mag-out na sa method
	public void requestPlanChange(String newPlan) {
    if (getPlanTier().equalsIgnoreCase(newPlan)) { 
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
    public String printStatementTitle() {
        return "========= ELECTRICITY BILL =========";
    }

    @Override
    public String getStatementBody() {
        return getBillingBreakdown();
    }

    @Override
	public String getStatementFooter() {
		return "Total: " + getFinalBill();
	}

    @Override
    public void printStatement() {
        System.out.println(printStatementTitle());
        System.out.println(getStatementBody());
        System.out.println(getStatementFooter());
    }

    @Override
	public void printStatement(boolean shortMode) {
    if (shortMode) {
        System.out.println("Account No.    : " + getAccountNumber());
        System.out.println("Customer Name  : " + getCustomerName());
			System.out.println("Service Type   : " + getServiceType());
        System.out.println("Plan Tier      : " + getHouseholdType() + " " + getPlanTier());
			System.out.println("Final Bill     : " + String.format("%.2f", getFinalBill()));
			System.out.println("Payment Status : " + getPaymentStatus());
			System.out.println("Service Status : " + (isActive() ? "Active" : "For Monitoring"));
			System.out.println("----------------------------------------");
		} else {
			printStatement();
		}
	}
	
	public void setMeterRate(double meterRate) { 
		this.meterRate = meterRate; 
	}
    
	public void setGenerationCharge(double generationCharge) { 
		this.generationCharge = generationCharge; 
	}
    
	public void setServiceAvailabilityFee(double serviceAvailabilityFee) { 
		this.serviceAvailabilityFee = serviceAvailabilityFee; 
	}
}