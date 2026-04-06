import java.util.Scanner;

public class UtilityConsoleApp {

    private Scanner scanner = new Scanner(System.in);
    private AccountRegistry registry = new AccountRegistry();
    private SubscriptionAccount currentAccount;

    public static void main(String[] args) {
        UtilityConsoleApp app = new UtilityConsoleApp();
        app.showMenu();
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n=== Utility Console Menu ===\n");
            System.out.println("1.  Register Account");
            System.out.println("2.  Enter Usage");
            System.out.println("3.  Compute Bill");
            System.out.println("4.  Apply Late Penalty");
            System.out.println("5.  Process Payment");
            System.out.println("6.  Request Plan Change");
            System.out.println("7.  Check Reconnection");
            System.out.println("8.  Reconnect Service");
            System.out.println("9.  Print Detailed Statement");
            System.out.println("10. Print Short Summary");
            System.out.println("11. List All Accounts");
            System.out.println("12. Exit");
            System.out.print("\nEnter choice: ");
            choice = scanner.nextInt();

            System.out.println(); // pang space lang after ng choice input

            switch (choice) {
                case 1: registerAccount(); break;
                case 2: enterUsage(); break;
                case 3: computeBill(); break;
                case 4: applyLatePenalty(); break;
                case 5: processPayment(); break;
                case 6: requestPlanChange(); break;
                case 7: checkReconnection(); break;
                case 8: reconnectService(); break;
                case 9: printDetailedStatement(); break;
                case 10: printShortSummary(); break;
                case 11: listAllAccounts(); break;
                case 12: System.out.println("Thank you for trusting us!"); break;
                default: System.out.println("Invalid choice."); break;
            }

        } while (choice != 12);
    }

    private void registerAccount() { //REGISTRATION
        System.out.println("\n--- Register Account ---\n");
		
		System.out.println("Choose Service Type:");

		int type;

		while (true) {

			System.out.println("1. Electricity");
			System.out.println("2. Water");
			System.out.println("3. Internet");
			System.out.println("4. Bundle");
			System.out.print("Enter choice (1-4): ");

			type = scanner.nextInt();
			scanner.nextLine();

			if (type >= 1 && type <= 4) break; 

			System.out.println("Invalid choice. Please select only 1, 2, or 3.\n"); //para di makapindot ng ibang number 
		}
        System.out.print("Account Number: ");
        String accNum = scanner.nextLine();

        if (registry.findAccount(accNum) != null) {
            System.out.println("\nAccount already exists!");
            return;
        }

        System.out.print("Customer Name: ");
        String name = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Household Type: ");
        String house = scanner.nextLine();

        System.out.println("\nChoose Plan Tier:");
        System.out.println("1. Basic");
        System.out.println("2. Standard");
        System.out.println("3. Premium");
        System.out.print("Enter choice: ");
        
		int planChoice = scanner.nextInt();
		scanner.nextLine();

		String plan = "";

		if(planChoice == 1) plan = "Basic";
		else if(planChoice == 2) plan = "Standard";
		else if(planChoice == 3) plan = "Premium";

        SubscriptionAccount acc = null;

        if (type == 1) acc = new ElectricityAccount(accNum, name);
        else if (type == 2) acc = new WaterAccount(accNum, name);
        else if (type == 3) acc = new InternetAccount(accNum, name);
		else if (type == 4) acc = new BundledAccount(accNum, name);

		if (acc != null) {

			acc.setAddress(address);
			acc.setHouseholdType(house);
			acc.setPlanTier(plan);
			acc.setActive(true);
			acc.setPaymentStatus("Unpaid");


			// yong sa bundle inputs
			if (acc instanceof BundledAccount) {

				BundledAccount bundle = (BundledAccount) acc;

				System.out.println("\nEnter Bundle Service Charges\n");

				System.out.print("Electricity Charge: ");
				bundle.setElectricityCharge(scanner.nextDouble());

				System.out.print("Water Charge: ");
				bundle.setWaterCharge(scanner.nextDouble());

				System.out.print("Internet Charge: ");
				bundle.setInternetCharge(scanner.nextDouble());

				scanner.nextLine(); 
			}


			registry.addAccount(acc);

			System.out.println("\nAccount registered successfully.\n");
		}
    }

		private void enterUsage() {
		System.out.println("\n--- Enter Usage ---\n");

		System.out.print("Enter Account Number: ");
		String accNum = scanner.next();

		SubscriptionAccount acc = registry.findAccount(accNum);

		if (acc == null) {
			System.out.println("\nAccount not found.\n");
			return;
		}

		if (acc instanceof BundledAccount) {
			BundledAccount bundled = (BundledAccount) acc;

			System.out.print("Enter Electricity Usage (kWh): ");
			double elecUsage = scanner.nextDouble();

			System.out.print("Enter Water Usage (units): ");
			double waterUsage = scanner.nextDouble();

			// calculate charges immediately
			bundled.setElectricityCharge(elecUsage * 10); // rate: 10 per kWh
			bundled.setWaterCharge(waterUsage * 8);        // rate: 8 per unit
			// internet is flat rate — no usage needed

			System.out.println("\nUsage updated successfully.\n");

		} else {
			System.out.print("Enter Usage: ");
			double usage = scanner.nextDouble();
			acc.updateUsage(usage);
			System.out.println("\nUsage updated successfully.\n");
		}
	}

    private void computeBill() { //COMPUTATION
        System.out.println("\n--- Compute Bill ---\n");

        System.out.print("Enter Account Number: ");
        String accNum = scanner.next();

        SubscriptionAccount acc = registry.findAccount(accNum);

        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        double bill = acc.computeMonthlyBill();

        System.out.println("\n================================================");
		System.out.println("           MONTHLY SERVICE STATEMENT");
		System.out.println("================================================");
		System.out.println("Account No.    : " + acc.getAccountNumber());
		System.out.println("Customer Name  : " + acc.getCustomerName());
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

    private void applyLatePenalty() { //PENALTY KINEME
        System.out.println("\n--- Apply Late Penalty ---\n");

        System.out.print("Enter Account Number: ");
        String accNum = scanner.next();

        SubscriptionAccount acc = registry.findAccount(accNum);

        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        acc.applySharedPenaltyRule();
        System.out.println();
    }

   private void processPayment() { //fixed: dahil sa issue sa interface pinalitan ko na, so dun na sa parentclass ung math
      System.out.println("\n--- Process Payment ---\n");

      System.out.print("Enter Account Number: ");
      String accNum = scanner.next();
      scanner.nextLine();

      SubscriptionAccount acc = registry.findAccount(accNum);
      if (acc == null) {
          System.out.println("\nAccount not found.\n");
          return;
      }

      System.out.print("Enter Payment Amount: ");
      double payment = scanner.nextDouble();
      scanner.nextLine();

      System.out.print("Add payment note? (y/n): ");
      String hasNote = scanner.nextLine();

      if (hasNote.equalsIgnoreCase("y")) {
          System.out.print("Enter note: ");
          String note = scanner.nextLine();
        acc.processPayment(payment, note); // overloaded version
      } else {
        acc.processPayment(payment); // simple version
      }
    } 

    private void requestPlanChange() { //REQUESTPLANCHANGE	
        System.out.println("\n--- Request Plan Change ---\n");

        System.out.print("Enter Account Number: ");
        String accNum = scanner.next();

        SubscriptionAccount acc = registry.findAccount(accNum);

        if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
        }

        scanner.nextLine();

        System.out.println("Choose New Plan:");
        System.out.println("1. Basic");
        System.out.println("2. Standard");
        System.out.println("3. Premium");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        String newPlan = null;
        switch (choice) {
            case 1: newPlan = "Basic"; break;
            case 2: newPlan = "Standard"; break;
            case 3: newPlan = "Premium"; break;
            default:
                System.out.println("\nInvalid choice.\n");
                return;
        }

        if (acc instanceof ElectricityAccount) ((ElectricityAccount) acc).requestPlanChange(newPlan);
        else if (acc instanceof WaterAccount) ((WaterAccount) acc).requestPlanChange(newPlan);
		else if (acc instanceof InternetAccount) ((InternetAccount) acc).requestPlanChange(newPlan);
		else if (acc instanceof BundledAccount) ((BundledAccount) acc).requestPlanChange(newPlan);

        System.out.println("\nPlan changed to " + newPlan + ".\n");
    }

    private void checkReconnection() { //CONNECTION
        System.out.println("\n--- Check Reconnection ---\n");

        System.out.print("Enter Account Number: ");
        String accNum = scanner.next();
	

        SubscriptionAccount acc = registry.findAccount(accNum);
		
		if (acc == null) {
            System.out.println("\nAccount not found.\n");
            return;
         }


        if (acc instanceof Reconnectable) {
            Reconnectable r = (Reconnectable) acc;
            System.out.println("\n" + r.getReconnectionMessage() + "\n");
        }
    }

    private void reconnectService() {
		System.out.println("\n--- Reconnect Service ---\n");

		System.out.print("Enter Account Number: ");
		String accNum = scanner.next();

		SubscriptionAccount acc = registry.findAccount(accNum);

		if (acc == null) {
			System.out.println("\nAccount not found.\n");
			return;
		}

		if (acc instanceof Reconnectable) {
			Reconnectable r = (Reconnectable) acc;
			r.reconnectService();
			System.out.println();
		}
	}

    private void printDetailedStatement() { 
        System.out.println("\n--- Detailed Statement ---\n");

        System.out.print("Enter Account Number: ");
        String accNum = scanner.next();

        SubscriptionAccount acc = registry.findAccount(accNum);

        if (acc instanceof PrintableStatement) {
            PrintableStatement p = (PrintableStatement) acc;
            p.printStatement();
        }
        System.out.println();
    }

    private void printShortSummary() {
        System.out.println("\n--- Short Summary ---\n");

        System.out.print("Enter Account Number: ");
        String accNum = scanner.next();

        SubscriptionAccount acc = registry.findAccount(accNum);

        if (acc instanceof PrintableStatement) {
            PrintableStatement p = (PrintableStatement) acc;
            p.printStatement(true);
        }
        System.out.println();
    }

    private void listAllAccounts() {
        System.out.println("\n--- List of All Accounts ---\n");
        registry.listAllAccounts();
        System.out.println();
    }
}