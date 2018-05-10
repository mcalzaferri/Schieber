package gui.animation;

import java.io.IOException;

import gui.Gui;
import shared.Card;

public class MoveCardAnimation extends MovePictureAnimation{

	public MoveCardAnimation(Card card, int duration, AnimationProperty start, AnimationProperty end, AnimationListener listener) throws IOException {
		super(Gui.pictureFactory.getPicture(card), duration, start, end,listener);
	}

}
