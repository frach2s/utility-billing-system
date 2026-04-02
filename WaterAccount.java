public class WaterAccount extends SubscriptionAccount implements Reconnectable {
	// fields
	private double waterRatePerUnit = 8;
	private double environmentalFee = 30; // water acc self declared variables
	
	// constructors
	public WaterAccount() {} // default constructor (empty)
	
	public WaterAccount(String accountNumber, String accountName) {
		super(accountNumber, accountName); // initialization of basic info only
	}
	
	public WaterAccount(String accountNumber, String customerName,
                        String address, String householdType,
						String planTier, double previousBalance,
                        double currentUsage, double waterRatePerUnit) {
		super(accountNumber, customerName, address, householdType,
			  planTier, previousBalance, currentUsage);
		this.waterRatePerUnit = waterRatePerUnit; // full constructor with the newly declared variable (waterRatePerUnit)
	}
	
	// methods
	@Override
    public double computeMonthlyBill() {

        double usageCharge = getCurrentUsage() * waterRatePerUnit;

        double total = usageCharge
                     + environmentalFee
                     + getPreviousBalance(); // returning multiple fields as bill is composed of all of the following (for ex. getPreviousBalance value adds up if it is unpaid)
        setFinalBill(total); 

        return total;
    }

    @Override
    public String getServiceType() {
        return "Water";
    }

    @Override
    public String getBillingBreakdown() {

        double usageCharge = getCurrentUsage() * waterRatePerUnit;

        return "Usage Charge: " + usageCharge +
               "\nEnvironmental Fee	: " + environmentalFee +
               "\nPrevious Balance: " + getPreviousBalance() +
               "\n----------------------" +
               "\nTotal Bill: " + getFinalBill(); // display of values only!!
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
	
	// overloaded methods
	public void requestPlanChange(String newPlan) {
		setPlanTier(newPlan);
		setServiceRequestNote("Plan change requested");
		System.out.println("Plan changed to: " + newPlan); // the new plan depends on the inseted value of the user
	}

	public void requestPlanChange(String newPlan, String effectiveCycle) {
		setPlanTier(newPlan);
		setServiceRequestNote("Plan change requested");
		System.out.println("Plan will change to " + newPlan + " on " + effectiveCycle); // same output plus the new parameter
	}
	
	@Override
    public String printStatementTitle() {
        return "========= WATER BILL =========";
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
            System.out.println("Water Bill: " + getFinalBill()); // simplified display
        } else {
            printStatement(); // all-out display
        }
    }
	
	public void setWaterRatePerUnit(double waterRatePerUnit) { 
		this.waterRatePerUnit = waterRatePerUnit; 
	}
    
	public void setEnvironmentalFee(double environmentalFee) { 
		this.environmentalFee = environmentalFee; 
	}
}