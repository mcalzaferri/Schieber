package gui.animation;

public class AnimationEvent {
	private Object animatedObject;
	
	public AnimationEvent(Object animatedObject) {
		this.animatedObject = animatedObject;
	}
	
	public Object getAnimatedObject() {
		return animatedObject;
	}
}
