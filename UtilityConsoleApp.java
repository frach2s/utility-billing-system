import java.util.Scanner;

public class UtilityConsoleApp {

    private Scanner scanner = new Scanner(System.in);
    private AccountRegistry registry = new AccountRegistry();
    private SubscriptionAccount currentAccount;

    public static void main(String[] args) {
        UtilityConsoleApp app = new UtilityConsoleApp();
        app.showMenu();
    }

    // ===================== MAIN MENU =====================
    public void showMenu() {
        int choice;
        do {
            System.out.println("\n================================================");
            System.out.println("       UTILITY SUBSCRIPTION CONSOLE");
            System.out.println("================================================");
            System.out.println(" 1.  Register Account");
            System.out.println(" 2.  Enter / Update Usage");
            System.out.println(" 3.  Compute Monthly Bill");
            System.out.println(" 4.  Apply Late Penalty");
            System.out.println(" 5.  Process Payment");
            System.out.println(" 6.  Request Plan Change");
            System.out.println(" 7.  Check Reconnection Eligibility");
            System.out.println(" 8.  Reconnect Service");
            System.out.println(" 9.  Print Detailed Service Statement");
            System.out.println(" 10. Print Short Summary");
            System.out.println(" 11. List All Accounts");
            System.out.println(" 12. Exit");
            System.out.println("================================================");
            System.out.print(" Enter choice: ");
            choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1:  registerAccount();        break;
                case 2:  enterUsage();             break;
                case 3:  computeBill();            break;
                case 4:  applyLatePenalty();       break;
                case 5:  processPayment();         break;
                case 6:  requestPlanChange();      break;
                case 7:  checkReconnection();      break;
                case 8:  reconnectService();       break;
                case 9:  printDetailedStatement(); break;
                case 10: printShortSummary();      break;
                case 11: listAllAccounts();        break;
                case 12: System.out.println("Thank you for trusting us!"); break;
                default: System.out.println("Invalid choice. Please try again."); break;
            }

        } while (choice != 12);
    }

    // ===================== REGISTER ACCOUNT =====================
    private void registerAccount() {
        System.out.println("--- Register Account ---\n");

        // --- service type selection with validation loop ---
        int type;
        while (true) {
            System.out.println("Choose Service Type:");
            System.out.println("  1. Electricity");
            System.out.println("  2. Water");
            System.out.println("  3. Internet");
            System.out.println("  4. Bundle (Electricity + Water + Internet)");
            System.out.print("Enter choice (1-4): ");
            type = scanner.nextInt();
            scanner.nextLine();
            if (type >= 1 && type <= 4) break;
            System.out.println("Invalid. Please select 1, 2, 3, or 4.\n");
        }

        // --- basic account info ---
        System.out.print("\nAccount Number    : ");
        String accNum = scanner.nextLine();

        if (registry.findAccount(accNum) != null) {
            System.out.println("\nAccount number already exists. Registration cancelled.\n");
            return;
        }

        System.out.print("Customer Name     : ");
        String name = scanner.nextLine();

        System.out.print("Address           : ");
        String address = scanner.nextLine();

        System.out.print("Household Type    : ");
        String house = scanner.nextLine();

        System.out.print("Previous Balance  : ");
        double prevBal = scanner.nextDouble();
        scanner.nextLine();

      
        // Bundle asks for usage separately per service inside enterUsage()
        // Internet is flat rate so opening usage is not needed
        double openingUsage = 0;
        if (type == 1) {
            System.out.print("Opening Usage (kWh)   : ");
            openingUsage = scanner.nextDouble();
            scanner.nextLine();
        } else if (type == 2) {
            System.out.print("Opening Usage (units) : ");
            openingUsage = scanner.nextDouble();
            scanner.nextLine();
        }

        //  plan tier selection with validation loop 
        // All four service types use Basic / Standard / Premium
        // For Internet and Bundle this maps directly to the monthly plan price (999/1499/1999)
        // For Electricity and Water this represents the service level
        String plan = "";
        while (true) {
            System.out.println("\nChoose Plan Tier:");
            System.out.println("  1. Basic");
            System.out.println("  2. Standard");
            System.out.println("  3. Premium");
            System.out.print("Enter choice (1-3): ");
            int planChoice = scanner.nextInt();
            scanner.nextLine();
            if      (planChoice == 1) { plan = "Basic";    break; }
            else if (planChoice == 2) { plan = "Standard"; break; }
            else if (planChoice == 3) { plan = "Premium";  break; }
            else System.out.println("Invalid. Please select 1, 2, or 3.");
        }

        //  create the correct subclass  
        SubscriptionAccount acc = null;

        if      (type == 1) acc = new ElectricityAccount(accNum, name);
        else if (type == 2) acc = new WaterAccount(accNum, name);
        else if (type == 3) acc = new InternetAccount(accNum, name);
        else if (type == 4) acc = new BundledAccount(accNum, name);

        if (acc != null) {
            acc.setAddress(address);
            acc.setHouseholdType(house);
            acc.setPlanTier(plan);
            acc.setPreviousBalance(prevBal);
            acc.setCurrentUsage(openingUsage); // opening usage set at registration
            acc.setActive(true);               // new accounts start as active
            acc.setPaymentStatus("Unpaid");    // new accounts start as unpaid

            registry.addAccount(acc);
            currentAccount = acc;

           
            System.out.println("\n================================================");
            System.out.println("        ACCOUNT REGISTERED SUCCESSFULLY");
            System.out.println("================================================");
            System.out.println("Account No.    : " + acc.getAccountNumber());
            System.out.println("Customer Name  : " + acc.getCustomerName());
            System.out.println("Service Type   : " + acc.getServiceType());
            System.out.println("Plan Tier      : " + acc.getPlanTier());
            System.out.println("Household Type : " + acc.getHouseholdType());
            System.out.println("Address        : " + acc.getAddress());
            System.out.println("Prev. Balance  : " + String.format("%.2f", acc.getPreviousBalance()));
            System.out.println("Opening Usage  : " + acc.getCurrentUsage());
            System.out.println("Payment Status : " + acc.getPaymentStatus());
            System.out.println("Service Status : Active");
            System.out.println("================================================\n");
        }
    }

    // =====================  ENTER / UPDATE USAGE =====================
    private void enterUsage() {
        System.out.println("--- Enter / Update Usage ---\n");

        System.out.print("Account Number: ");
        String accNum = scanner.next();
        scanner.nextLine();

        SubscriptionAccount acc = registry.findAccount(accNum);
        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        if (acc instanceof BundledAccount) {
            // Bundle needs separate electricity and water usage
            // Internet portion is flat rate — no usage input
            BundledAccount bundled = (BundledAccount) acc;

            System.out.print("Electricity Usage (kWh) : ");
            double elecUsage = scanner.nextDouble();

            System.out.print("Water Usage (units)     : ");
            double waterUsage = scanner.nextDouble();
            scanner.nextLine();

            bundled.setElectricityCharge(elecUsage * 10); // 10.00 per kWh
            bundled.setWaterCharge(waterUsage * 8);        // 8.00 per unit

            System.out.println("\n------------------------------------------------");
            System.out.println("Usage updated successfully.");
            System.out.println("  Electricity : " + elecUsage + " kWh   = Charge: " + String.format("%.2f", elecUsage * 10));
            System.out.println("  Water       : " + waterUsage + " units = Charge: " + String.format("%.2f", waterUsage * 8));
            System.out.println("  Internet    : Flat rate (based on plan tier)");
            System.out.println("------------------------------------------------\n");

        } else if (acc instanceof InternetAccount) {
            // Internet has no usage , flat monthly rate based on plan tier only
            System.out.println("\nInternet is billed at a flat monthly rate.");
            System.out.println("No usage input required. Use option 3 to compute the bill.\n");

        } else {
            // Electricity or Water ,,, single usage value
            String unit = (acc instanceof ElectricityAccount) ? "kWh" : "units";
            System.out.print("Enter Usage (" + unit + "): ");
            double usage = scanner.nextDouble();
            scanner.nextLine();

            acc.updateUsage(usage); // parent method

            System.out.println("\n------------------------------------------------");
            System.out.println("Usage updated to " + usage + " " + unit + ".");
            System.out.println("Account No. : " + acc.getAccountNumber());
            System.out.println("Customer    : " + acc.getCustomerName());
            System.out.println("------------------------------------------------\n");
        }
    }

    // =====================  COMPUTE MONTHLY BILL =====================
    private void computeBill() {
        System.out.println("--- Compute Monthly Bill ---\n");

        System.out.print("Account Number: ");
        String accNum = scanner.next();
        scanner.nextLine();

        SubscriptionAccount acc = registry.findAccount(accNum);
        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        // polymorphism — correct subclass computeMonthlyBill() runs automatically
        acc.computeMonthlyBill();

        System.out.println("\n================================================");
        System.out.println("           MONTHLY SERVICE STATEMENT");
        System.out.println("================================================");
        System.out.println("Account No.    : " + acc.getAccountNumber());
        System.out.println("Customer Name  : " + acc.getCustomerName());
        System.out.println("Address        : " + acc.getAddress());
        System.out.println("Service Type   : " + acc.getServiceType());
        System.out.println("Plan Tier      : " + acc.getPlanTier());
        System.out.println("Household Type : " + acc.getHouseholdType());
        System.out.println("------------------------------------------------");
        System.out.println(acc.getBillingBreakdown());
        System.out.println("------------------------------------------------");
        System.out.println("Payment Status : " + acc.getPaymentStatus());
        System.out.println("Service Status : " + (acc.isActive() ? "Active" : "Inactive"));
        System.out.println("================================================\n");
    }

    // =====================  APPLY LATE PENALTY =====================
    private void applyLatePenalty() {
        System.out.println("--- Apply Late Penalty ---\n");

        System.out.print("Account Number: ");
        String accNum = scanner.next();
        scanner.nextLine();

        SubscriptionAccount acc = registry.findAccount(accNum);
        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        
        if (acc.getFinalBill() == 0) {
            System.out.println("\nNo bill computed yet. Please use option 3 first.\n");
            return;
        }

        
        if (acc.getPaymentStatus() == null) {
            System.out.println("\nPayment status not set. Please compute the bill first.\n");
            return;
        }

        acc.applySharedPenaltyRule(); // parent method — 5% late fee if status is Unpaid
        System.out.println();
    }

    // ===================== PROCESS PAYMENT =====================
    private void processPayment() {
        System.out.println("--- Process Payment ---\n");

        System.out.print("Account Number: ");
        String accNum = scanner.next();
        scanner.nextLine();

        SubscriptionAccount acc = registry.findAccount(accNum);
        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        System.out.println("Current Outstanding Balance : " + String.format("%.2f", acc.getOutstandingBalance()));
        System.out.print("Payment Amount              : ");
        double payment = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Add a payment note? (y/n)   : ");
        String hasNote = scanner.nextLine();

        if (hasNote.equalsIgnoreCase("y")) {
            System.out.print("Enter note: ");
            String note = scanner.nextLine();
            acc.processPayment(payment, note); // overloaded — amount + note
        } else {
            acc.processPayment(payment); // simple — amount only
        }

        // payment confirmation output 
        System.out.println("\n================================================");
        System.out.println("             PAYMENT CONFIRMATION");
        System.out.println("================================================");
        System.out.println("Account No.        : " + acc.getAccountNumber());
        System.out.println("Customer Name      : " + acc.getCustomerName());
        System.out.println("Service Type       : " + acc.getServiceType());
        System.out.println("Amount Paid        : " + String.format("%.2f", payment));
        System.out.println("Total Paid So Far  : " + String.format("%.2f", acc.getPaidAmount()));
        System.out.println("Remaining Balance  : " + String.format("%.2f", acc.getOutstandingBalance()));
        System.out.println("Payment Status     : " + acc.getPaymentStatus());
        System.out.println("Service Status     : " + (acc.isActive() ? "Active" : "Inactive"));
        System.out.println("================================================\n");
    }

    // =====================  REQUEST PLAN CHANGE =====================
    private void requestPlanChange() {
        System.out.println("--- Request Plan Change ---\n");

        System.out.print("Account Number: ");
        String accNum = scanner.next();
        scanner.nextLine();

        SubscriptionAccount acc = registry.findAccount(accNum);
        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        System.out.println("Current Plan : " + acc.getPlanTier());
        System.out.println("\nChoose New Plan:");
        System.out.println("  1. Basic");
        System.out.println("  2. Standard");
        System.out.println("  3. Premium");
        System.out.print("Enter choice (1-3): ");
        int planChoice = scanner.nextInt();
        scanner.nextLine();

        String newPlan;
        switch (planChoice) {
            case 1: newPlan = "Basic";    break;
            case 2: newPlan = "Standard"; break;
            case 3: newPlan = "Premium";  break;
            default:
                System.out.println("\nInvalid choice.\n");
                return;
        }

      
        System.out.print("Set an effective cycle date? (y/n): ");
        String hasCycle = scanner.nextLine();

        if (hasCycle.equalsIgnoreCase("y")) {
            System.out.print("Enter effective cycle (e.g. June 2025): ");
            String cycle = scanner.nextLine();

           
            if      (acc instanceof ElectricityAccount) ((ElectricityAccount) acc).requestPlanChange(newPlan, cycle);
            else if (acc instanceof WaterAccount)        ((WaterAccount)        acc).requestPlanChange(newPlan, cycle);
            else if (acc instanceof InternetAccount)     ((InternetAccount)     acc).requestPlanChange(newPlan, cycle);
            else if (acc instanceof BundledAccount)      ((BundledAccount)      acc).requestPlanChange(newPlan, cycle);

        } else {
            // simple requestPlanChange(newPlan)
            if      (acc instanceof ElectricityAccount) ((ElectricityAccount) acc).requestPlanChange(newPlan);
            else if (acc instanceof WaterAccount)        ((WaterAccount)        acc).requestPlanChange(newPlan);
            else if (acc instanceof InternetAccount)     ((InternetAccount)     acc).requestPlanChange(newPlan);
            else if (acc instanceof BundledAccount)      ((BundledAccount)      acc).requestPlanChange(newPlan);
        }

        System.out.println();
    }

    // =====================  CHECK RECONNECTION ELIGIBILITY =====================
    private void checkReconnection() {
        System.out.println("--- Check Reconnection Eligibility ---\n");

        System.out.print("Account Number: ");
        String accNum = scanner.next();
        scanner.nextLine();

        SubscriptionAccount acc = registry.findAccount(accNum);
        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        // all four subclasses implement Reconnectable
        if (acc instanceof Reconnectable) {
            Reconnectable r = (Reconnectable) acc;
            System.out.println("\n------------------------------------------------");
            System.out.println("Account No.  : " + acc.getAccountNumber());
            System.out.println("Customer     : " + acc.getCustomerName());
            System.out.println("Service Type : " + acc.getServiceType());
            System.out.println("Status       : " + (acc.isActive() ? "Active" : "Inactive"));
            System.out.println("Eligibility  : " + r.getReconnectionMessage());
            System.out.println("------------------------------------------------\n");
        }
    }

    // =====================  RECONNECT SERVICE =====================
    private void reconnectService() {
        System.out.println("--- Reconnect Service ---\n");

        System.out.print("Account Number: ");
        String accNum = scanner.next();
        scanner.nextLine();

        SubscriptionAccount acc = registry.findAccount(accNum);
        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        if (acc instanceof Reconnectable) {
            Reconnectable r = (Reconnectable) acc;
            r.reconnectService(); // each subclass checks canReconnect() internally
            System.out.println();
        }
    }

    // =====================  PRINT DETAILED STATEMENT =====================
    private void printDetailedStatement() {
		System.out.println("--- Detailed Statement ---\n");

		System.out.print("Account Number: ");
		String accNum = scanner.next();
		scanner.nextLine();
	
		SubscriptionAccount acc = registry.findAccount(accNum);
		if (acc == null) {
			System.out.println("\nAccount not found.\n");
		return;
		}

		acc.computeMonthlyBill();

		if (acc instanceof PrintableStatement) {
			PrintableStatement p = (PrintableStatement) acc;
			p.printStatement();
		}
		
		System.out.println();
	}

    // =====================  PRINT SHORT SUMMARY =====================
    private void printShortSummary() {
		System.out.println("----------------------------------------");
		System.out.println("ACCOUNT SUMMARY");
		System.out.println("----------------------------------------");
	
		System.out.print("Account Number: ");
		String accNum = scanner.next();
		scanner.nextLine();

		SubscriptionAccount acc = registry.findAccount(accNum);
		if (acc == null) {
			System.out.println("\nAccount not found.\n");
        return;
		}

		acc.computeMonthlyBill();

		if (acc instanceof PrintableStatement) {
			PrintableStatement p = (PrintableStatement) acc;
			p.printStatement(true);
		}
		
		System.out.println();
	}

    // =====================  LIST ALL ACCOUNTS =====================
    private void listAllAccounts() {
        System.out.println("--- List of All Registered Accounts ---\n");
        registry.listAllAccounts(); 
        System.out.println();
    }
}