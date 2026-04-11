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

        return "Usage Charge      : " + usageCharge +
               "\nEnvironmental Fee : " + environmentalFee +
               "\nPrevious Balance  : " + getPreviousBalance() +
               "\n------------------------------------------------" +
               "\nTotal Bill        : " + getFinalBill(); // display of values only!!
    }
	
	@Override
	public String getServiceStatus() {
		if (getPreviousBalance() >= 12000) {
			return "Inactive";
		} else if (isActive()) {
			return "Active";
		} else {
			return "For Monitoring";
		}
	}
	
	@Override
    public boolean canReconnect() {
        return !isActive() && getPreviousBalance() < 12000;
    }

    @Override
    public void reconnectService() {
        if (canReconnect()) {
            setActive(true);
            System.out.println("Service reconnected.");
        } else {
            if (isActive()) {
                System.out.println("Service is already active.");
            } else {
                System.out.println("Reconnection not allowed due to unpaid balance.");
            }
        }
    }

    @Override
    public String getReconnectionMessage() {
        if (isActive()) {
            return "Service is active.";
        }
        if (canReconnect()) {
			return "Service is eligible for reconnection.";
		} else {
			return "Service inactive. Please settle your outstanding balance first.";
		}
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
	
	//overload
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
		return "------------------ WATER BILL ------------------";
	}

    @Override
    public String getStatementBody() {
        return getBillingBreakdown();
    }

    @Override
	public String getStatementFooter() {
		return "Total            : " + getFinalBill() +
			   "\n------------------------------------------------";
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
			System.out.println("Service Status : " + getServiceStatus());
			System.out.println("------------------------------------------------");
		} else {
        printStatement();
		}
	}	
	
	public void setWaterRatePerUnit(double waterRatePerUnit) { 
		this.waterRatePerUnit = waterRatePerUnit; 
	}
    
	public void setEnvironmentalFee(double environmentalFee) { 
		this.environmentalFee = environmentalFee; 
	}
}