package client;

import shared.*;

public interface ViewObserver {
	public abstract void btnConnectClick(ServerAddress serverAddress);
	public abstract void btnRestartClick();
	public abstract void btnDisconnectClick();
	public abstract void btnTrumpClick(Trump trump);
	public abstract void btnCardClick(Card card);
	public abstract void btnCloseWindowClick(ViewEnumeration view);
}
