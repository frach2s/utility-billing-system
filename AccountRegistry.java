public class AccountRegistry { //YONG MGA PRIVATE FIELDS
	private AccountRegistryNode head;
	private int size;
	

	//METHODDS
	public AccountRegistry() {
		head = null;
		size = 0;
		
	}
	
	public void addAccount(SubscriptionAccount account) {
		AccountRegistryNode newNode = new AccountRegistryNode(account);
		newNode.setNext(head);
		head = newNode;
		size++;
	}
	
	public SubscriptionAccount findAccount(String accountNumber) {
		AccountRegistryNode current = head;
		
		while (current != null) {
			if(current.getAccount().getAccountNumber().equals(accountNumber)) {
				return current.getAccount();
			}
			current = current.getNext();
		}
		return null;
	}
	
	public void listAllAccounts() { //spacing don sa listing

	if (head == null) {
		System.out.println("No accounts registered.");
		return;
	}

	AccountRegistryNode current = head;
	int count = 1;

	while (current != null) {

		System.out.println("----------------------------------------");
		System.out.println("Account #" + count);
		System.out.println("----------------------------------------");

		System.out.println(current.getAccount());

		System.out.println("----------------------------------------");
		System.out.println(); 

		current = current.getNext();
		count++;
	}
}
	
	public int getSize() {
		return size;
	}
}
