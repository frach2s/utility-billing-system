public class InternetAccount extends SubscriptionAccount implements Reconnectable {
	// fields
	private String speedPlan;
	private double modemFee;
	private double reconnectionCharge; // int acc self declared variables
	
	// constructors
	public InternetAccount() {} // default constructor (empty)
	
	public InternetAccount(String accountNumber, String accountName) {
		super(accountNumber, accountName); // initialization of basic info only
	}
	
	public InternetAccount(String accountNumber, String customerName,
                        String address, String householdType,
						String planTier, double previousBalance,
                        double currentUsage, String speedPlan) {
		super(accountNumber, customerName, address, householdType,
			  planTier, previousBalance, currentUsage);
		this.speedPlan = speedPlan; // full constructor with the newly declared variable (speedPlan)
	}
	
	// methods
	 @Override
    public double computeMonthlyBill() {

        double basePlan = 0;

        String plan = getPlanTier();

        if (plan.equalsIgnoreCase("Basic")) basePlan = 999;
        else if (plan.equalsIgnoreCase("Standard")) basePlan = 1499;
        else if (plan.equalsIgnoreCase("Premium")) basePlan = 1999;

        double total = basePlan
                + modemFee
                + reconnectionCharge
                + getPreviousBalance();

        setFinalBill(total);
        return total;
    }

    @Override
    public String getServiceType() {
        return "Internet";
    }

    @Override
    public String getBillingBreakdown() {

        double basePlan = 0;

        String plan = getPlanTier();

        if (plan.equalsIgnoreCase("Basic")) basePlan = 999;
        else if (plan.equalsIgnoreCase("Standard")) basePlan = 1499;
        else if (plan.equalsIgnoreCase("Premium")) basePlan = 1999;

        return "Plan: " + plan +
                "\nBase Plan Fee: " + basePlan +
                "\nModem Fee: " + modemFee +
                "\nReconnection Charge: " + reconnectionCharge +
                "\nPrevious Balance: " + getPreviousBalance() +
               "\n---------------------------------------------" ;		   
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
    public String printStatementTitle() {
        return "========= INTERNET BILL =========";
    }

    @Override
    public String getStatementBody() {
        return getBillingBreakdown();
    }

    @Override
	public String getStatementFooter() {
		return "Total Bill         : " + String.format("%.2f", getFinalBill()) +
			   "\nPaid Amount        : " + String.format("%.2f", getPaidAmount()) +
			   "\nRemaining Balance  : " + String.format("%.2f", getFinalBill() - getPaidAmount());
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
			System.out.println("Final Bill     : " + String.format("%.2f", getFinalBill()- getPaidAmount()));
			System.out.println("Payment Status : " + getPaymentStatus());
			System.out.println("Service Status : " + (isActive() ? "Active" : "For Monitoring"));
			System.out.println("----------------------------------------");
		} else {
			printStatement();
		}
	}
	
	public void setSpeedPlan(String speedPlan) { 
		this.speedPlan = speedPlan; 
	}
    
	public void setModemFee(double modemFee) { 
		this.modemFee = modemFee; 
	}
	
	public void setReconnectionCharge(double reconnectionCharge) { 
		this.reconnectionCharge = reconnectionCharge; 
	}
}