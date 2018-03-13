package client;

import shared.*;

public interface ViewObserver {
	public abstract void btnConnectClick(ServerAddress serverAddress);
	public abstract void btnRestartClick();
	public abstract void btnDisconnectClick();
	public abstract void btnTrumpfClick(Trumpf trumpf);
	public abstract void btnCardClick(Card card);
}
