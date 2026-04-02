public class AccountRegistryNode {  // yong private fields
	private SubscriptionAccount account;
	private AccountRegistryNode next;
	
	public AccountRegistryNode(SubscriptionAccount account) {
		this.account = account;
		this.next = null;
	}
	
	public SubscriptionAccount getAccount() {
		return account;
	}
	
	public AccountRegistryNode getNext() {
		return next;
	}
	
	public void setNext(AccountRegistryNode next) {
		this.next =  next;
	}
}