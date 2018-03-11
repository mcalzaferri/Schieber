package gui;

import java.awt.event.MouseEvent;

public interface PlayingFieldViewListener {
	public void cardSelected(PlayingFieldViewEvent event);
	public void mouseClicked(PlayingFieldViewEvent event);
}
