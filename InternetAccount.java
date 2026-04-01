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

		if (speedPlan.equals("Basic")) basePlan = 999;
		else if (speedPlan.equals("Standard")) basePlan = 1499;
		else if (speedPlan.equals("Premium")) basePlan = 1999; // based this on the standard internet plan (modify niyo nalang if may iba kayong gusto)
		
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

		if (speedPlan.equals("Basic")) basePlan = 999;
		else if (speedPlan.equals("Standard")) basePlan = 1499;
		else if (speedPlan.equals("Premium")) basePlan = 1999; // based this on the standard internet plan (modify niyo nalang if may iba kayong gusto)

		return "Plan: " + speedPlan +
           "\nBase Plan Fee: " + basePlan +
           "\nModem Fee: " + modemFee +
           "\nReconnection Charge: " + reconnectionCharge +
           "\nPrevious Balance: " + getPreviousBalance() +
           "\n----------------------" +
           "\nTotal Bill: " + getFinalBill();
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
        return "========= INTERNET BILL =========";
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
            System.out.println("Internet Bill: " + getFinalBill()); // simplified display
        } else {
            printStatement(); // all-out display
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