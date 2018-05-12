package gui.animation;

import java.io.IOException;

import gui.Gui;
import shared.Card;

/** Very specific animation which is only good for displaying animations of the 36 card covers or the back cover of a card
 * The animation will start at a given location and then move to an end location. Start and end location can have different size and rotation
 */
public class MoveCardAnimation extends MovePictureAnimation{

	/** Create a new MoveCardAnimation specified by the given parameters
	 * @param card The card which should be animated
	 * @param duration How long this animation should last
	 * @param start Start location of this animation. The animation will be painted there when the paint method is called for the first time
	 * @param end End location of this animation. The animation will be painted there until it is removed
	 * @param listener Optional AnimationListener which will be called when an AnimationEvent occurs
	 * @throws IOException As the picture of the card must be loaded from the file system, IOExceptions can occur
	 */
	public MoveCardAnimation(Card card, int duration, AnimationProperty start, AnimationProperty end, AnimationListener listener) throws IOException {
		super(Gui.pictureFactory.getPicture(card), duration, start, end,listener);
	}

}
