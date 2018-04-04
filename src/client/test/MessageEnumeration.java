package client.test;

public enum MessageEnumeration {
	Message(MessageType.Abstract),
	ToPlayerMessage(MessageType.Abstract),
	ToServerMessage(MessageType.Abstract),
	
	ChangeStateMessage(MessageType.ToServerMessage),
	ChosenTrumpMessage(MessageType.ToServerMessage),
	JoinLobbyMessage(MessageType.ToServerMessage),
	JoinTableMessage(MessageType.ToServerMessage),
	LeaveLobbyMessage(MessageType.ToServerMessage),
	LeaveTableMessage(MessageType.ToServerMessage),
	PlaceCardMessage(MessageType.ToServerMessage),
	
	ChosenTrumpInfoMessage(MessageType.InfoMessage),
	EndOfRoundInfoMessage(MessageType.InfoMessage),
	GameStartedInfoMessage(MessageType.InfoMessage),
	NewRoundInfoMessage(MessageType.InfoMessage),
	NewTurnInfoMessage(MessageType.InfoMessage),
	PlayerChangedStateMessage(MessageType.InfoMessage),
	PlayerLeftLobbyInfoMessage(MessageType.InfoMessage),
	PlayerMovedToLobbyInfoMessage(MessageType.InfoMessage),
	PlayerMovedToTableInfoMessage(MessageType.InfoMessage),
	TurnInfoMessage(MessageType.InfoMessage),
	WiisInfoMessage(MessageType.InfoMessage),
	
	ChooseTrumpMessage(MessageType.ToPlayerMessage),
	GameStateMessage(MessageType.ToPlayerMessage),
	HandOutCardsMessage(MessageType.ToPlayerMessage),
	LobbyStateMessage(MessageType.ToPlayerMessage),
	WrongCardMessage(MessageType.ToPlayerMessage);
	
	
	private final MessageType type;
	
	MessageEnumeration(MessageType type){
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}
}
