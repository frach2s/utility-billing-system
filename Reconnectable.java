public interface Reconnectable{
	boolean canReconnect();
	void reconnectService();
	String getReconnectionMessage();
}