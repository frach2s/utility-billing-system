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
	
	public void listAllAccounts() {
		AccountRegistryNode current = head;
		while (current != null) {
			System.out.println(current.getAccount());
			current = current.getNext();
		}
	}
	
	public int getSize() {
		return size;
	}
}
