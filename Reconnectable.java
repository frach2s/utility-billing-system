public interface Reconnectable{
	boolean canReconnect();
	void reconnectService();
	String getRecconectionMessage();
}