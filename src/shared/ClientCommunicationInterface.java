package shared;

public interface ClientCommunicationInterface {
	public abstract void publishChosenCard(Card card);
	public abstract void publishChosenTrump(Trump trump);
	public abstract void disconnect();
	/**
	 * @param serverAddress Address of the Game Server
	 * @return returns true if connected successfully
	 */
	public abstract boolean connect(ServerAddress serverAddress);
}
