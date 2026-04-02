public class BundledAccount extends SubscriptionAccount implements Reconnectable {
	private String includedServices;
	private double bundleDiscountRate;
	private double electricityCharge;
	private double waterCharge;
	private double internetCharge;
	
	//Constructors
	public BundledAccount() {
		super();
	}
	
	public BundledAccount(String accountNumber, String customerName) {
		super(accountNumber, customerName);
	}
	
	public BundledAccount(String accountNumber, String customerName, String address,
			String householdType, String planTier, double previousBalance,
			double currentUsage, double bundleDiscountRate) {
				super(accountNumber, customerName, address, householdType, planTier, previousBalance, currentUsage);
				
				this.bundleDiscountRate = bundleDiscountRate;
			}
	
	//Overriden Methods		
	 @Override
    public double computeMonthlyBill() {

        if (electricityCharge < 0 || waterCharge < 0 || internetCharge < 0) {
            System.out.println("Invalid charges detected.");
            return 0;
        }

        double subtotal = electricityCharge + waterCharge + internetCharge;

        double discount = 0;
        if (bundleDiscountRate > 0) {
            discount = subtotal * bundleDiscountRate;
        }

        double total = subtotal - discount + getPreviousBalance();

        setFinalBill(total);
        return total;
    }
	
	@Override 
	public String getServiceType() {
		return "Internet Bundle Servvice";
	}
	
	  @Override
    public String getBillingBreakdown() {

        double subtotal = electricityCharge + waterCharge + internetCharge;
        double discount = subtotal * bundleDiscountRate;

        return "========= BUNDLE BREAKDOWN =========" +
               "\nElectricity: " + electricityCharge +
               "\nWater: " + waterCharge +
               "\nInternet: " + internetCharge +
               "\n----------------------" +
               "\nSubtotal: " + subtotal +
               "\nDiscount (" + (bundleDiscountRate * 100) + "%): -" + discount +
               "\nPrevious Balance: " + getPreviousBalance() +
               "\n----------------------" +
               "\nTotal Bill: " + getFinalBill();
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
			System.out.println("Service reconnected successfully.");
		} else {
			System.out.println("Cannot reconnect. Outstanding balance exist.");
		}
	}
	
	@Override
	public String getReconnectionMessage() {
		return canReconnect () ? "Eligible for reconnection." : "Not eligible.";
	}
	
	//Overloaded Metho
	public void requestPlanChange(String newPlan) {
		System.out.println("Requsted plan change to:" + newPlan);
	}
	
	public void requestPlanChange(String newPlan, String effectiveCycle) {
		System.out.println("Plan:" + newPlan + "Will take effect on:" + effectiveCycle);
	}
	
	public void printStatement() {
		System.out.println("Full Statement:");
		System.out.println(getBillingBreakdown());
	}
	
	public void printStatement(boolean shortMode) {
		if (shortMode) {
			System.out.println("Total Bill: " + computeMonthlyBill());
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