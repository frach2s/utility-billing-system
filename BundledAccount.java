public class BundledAccount extends SubscriptionAccount implements Reconnectable {

    // ===================== EXTRA PRIVATE FIELDS =====================
    private String includedServices;
    private double bundleDiscountRate;
    private double electricityCharge;
    private double waterCharge;
    private double internetCharge;

    // ===================== CONSTRUCTORS =====================

    // Constructor 1 — empty
    public BundledAccount() {
        super();
        this.bundleDiscountRate = 0.10;
    }

    // Constructor 2 — basic info only
    public BundledAccount(String accountNumber, String customerName) {
        super(accountNumber, customerName);
        this.bundleDiscountRate = 0.10;
    }

    // Constructor 3 — full details
    public BundledAccount(String accountNumber, String customerName, String address,String householdType, String planTier, double previousBalance,double currentUsage, double bundleDiscountRate) {
        super(accountNumber, customerName, address, householdType, planTier, previousBalance, currentUsage);
        this.bundleDiscountRate = bundleDiscountRate;
    }

    // ===================== OVERRIDDEN METHODS =====================

    @Override
    public double computeMonthlyBill() {
        // internet charge based on plan tier
        internetCharge = 999; // Basic default
        String plan = getPlanTier();
        if (plan != null) {
            if (plan.equalsIgnoreCase("Standard")) internetCharge = 1499;
            else if (plan.equalsIgnoreCase("Premium")) internetCharge = 1999;
        }

        double subtotal = electricityCharge + waterCharge + internetCharge;
        double discount = subtotal * bundleDiscountRate;
        double total = subtotal - discount + getPreviousBalance() + getLateFee();

        setFinalBill(total);
        return getFinalBill();
    }

    @Override
    public String getServiceType() {
        return "Bundle Service";
    }

    @Override
    public String getBillingBreakdown() {
        String plan = getPlanTier();
        double basePlan = 999;
        if (plan != null) {
            if (plan.equalsIgnoreCase("Standard")) basePlan = 1499;
            else if (plan.equalsIgnoreCase("Premium")) basePlan = 1999;
        }

        double subtotal = electricityCharge + waterCharge + basePlan;
        double discount = subtotal * bundleDiscountRate;

        return "=============== BUNDLE BREAKDOWN ===============\n" +
               "Electricity Charge : " + String.format("%.2f", electricityCharge) + "\n" +
               "Water Charge       : " + String.format("%.2f", waterCharge) + "\n" +
               "Internet Charge    : " + String.format("%.2f", basePlan) + "\n" +
               "------------------------------------------------\n" +
               "Subtotal           : " + String.format("%.2f", subtotal) + "\n" +
               "Bundle Discount    : -" + String.format("%.2f", discount) + "\n" +
               "Previous Balance   : " + String.format("%.2f", getPreviousBalance()) + "\n" +
               "Late Fee           : " + String.format("%.2f", getLateFee()) + "\n" +
               "------------------------------------------------\n" +
               "Total Bill         : " + String.format("%.2f", getFinalBill()) + "\n" +
               "Paid Amount        : " + String.format("%.2f", getPaidAmount()) + "\n" +
               "Remaining Balance  : " + String.format("%.2f", getOutstandingBalance()) +
			   "\n================================================\n";
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

    // ===================== FROM RECONNECTABLE (INTERFACE) =====================

    @Override
    public boolean canReconnect() {
        
        return !isActive() && getOutstandingBalance() <= 12000;
    }

    @Override
    public void reconnectService() {
        if (canReconnect()) {
            setActive(true);
            setPaymentStatus("Paid");
            System.out.println("Service reconnected successfully.");
        } else if (isActive()) {
            System.out.println("Service is already active.");
        } else {
            System.out.println("Cannot reconnect. Please settle outstanding balance first.");
        }
    }

    @Override
    public String getReconnectionMessage() {
        if (isActive()) return "Service is currently active.";
        if (canReconnect()) return "Eligible for reconnection.";
        
        return "Not eligible. Outstanding balance: " + String.format("%.2f", getOutstandingBalance());
    }

    // ===================== OVERLOADED METHODS =====================

    // requestPlanChange 
    public void requestPlanChange(String newPlan) {
        if (getPlanTier() != null && getPlanTier().equalsIgnoreCase(newPlan)) {
            System.out.println("Current plan is already " + newPlan + ". No changes made.");
            return;
        }

        String oldPlan = getPlanTier();
        String current = oldPlan != null ? oldPlan.toLowerCase() : "";
        String next = newPlan.toLowerCase();

        boolean isUpgrade = (current.equals("basic") && (next.equals("standard") || next.equals("premium")))
                         || (current.equals("standard") && next.equals("premium"));

        boolean isDowngrade = (current.equals("premium") && (next.equals("standard") || next.equals("basic")))
                           || (current.equals("standard") && next.equals("basic"));

        if (isUpgrade) {
            System.out.println("Upgrading plan from " + oldPlan + " to " + newPlan + ".");
        } else if (isDowngrade) {
            System.out.println("Downgrading plan from " + oldPlan + " to " + newPlan + ".");
        }

        setPlanTier(newPlan);
        setServiceRequestNote("Plan change requested: " + oldPlan + " to " + newPlan);
        setFinalBill(0); // reset bill so it recomputes on next billing cycle
        System.out.println("Plan successfully changed to: " + newPlan);
    }

    // requestPlanChange —
    public void requestPlanChange(String newPlan, String effectiveCycle) {
        if (getPlanTier() != null && getPlanTier().equalsIgnoreCase(newPlan)) {
            System.out.println("Current plan is already " + newPlan + ". No changes made.");
            return;
        }

        String oldPlan = getPlanTier();
        String current = oldPlan != null ? oldPlan.toLowerCase() : "";
        String next = newPlan.toLowerCase();

        boolean isUpgrade = (current.equals("basic") && (next.equals("standard") || next.equals("premium")))
                         || (current.equals("standard") && next.equals("premium"));

        boolean isDowngrade = (current.equals("premium") && (next.equals("standard") || next.equals("basic")))
                           || (current.equals("standard") && next.equals("basic"));

        if (isUpgrade) {
            System.out.println("Upgrading plan from " + oldPlan + " to " + newPlan + ".");
        } else if (isDowngrade) {
            System.out.println("Downgrading plan from " + oldPlan + " to " + newPlan + ".");
        }

        setPlanTier(newPlan);
        setServiceRequestNote("Plan change requested: " + oldPlan + " to " + newPlan);
        setFinalBill(0); // reset bill so it recomputes on next billing cycle
        System.out.println("Plan will take effect on: " + effectiveCycle);
        System.out.println("Plan successfully changed to: " + newPlan);
    }

    // printStatement
    @Override
    public void printStatement() {
        System.out.println(printStatementTitle());
        System.out.println(getStatementBody());
        System.out.println(getStatementFooter());
    }

    // printStatement 
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
			System.out.println("------------------------------------------------");
		} else {
			printStatement();
		}
	}

    // ===================== FROM PRINTABLESTATEMENT (INTERFACE) =====================

    @Override
    public String printStatementTitle() {  
		return "-------------- BUNDLED UTILITY BILL --------------" +
			   "\n";
    }

    @Override
    public String getStatementBody() {
        return getBillingBreakdown();
    }

    @Override
    public String getStatementFooter() {
        return "Remaining Balance: " + String.format("%.2f", getOutstandingBalance());
    }

    // ===================== EXTRA GETTERS AND SETTERS =====================

    public String getIncludedServices() {
        return includedServices;
    }

    public void setIncludedServices(String includedServices) {
        this.includedServices = includedServices;
    }

    public double getBundleDiscountRate() {
        return bundleDiscountRate;
    }

    public void setBundleDiscountRate(double bundleDiscountRate) {
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

    public void setWaterCharge(double waterCharge) {
        this.waterCharge = waterCharge;
    }

    public double getInternetCharge() {
        return internetCharge;
    }

    public void setInternetCharge(double internetCharge) {
        this.internetCharge = internetCharge;
    }
}