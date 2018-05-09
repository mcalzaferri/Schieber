package gui.playingView;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

/**
 * This class layouts its component in order to fit a playing view
 * containing the following components:
 * * carpet:	In the upper left corner
 * * blackboard:	In the upper right corner
 * * hand:	below the carpet
 * 
 * This layout manager therefore respects each components minimum size.
 * 
 * @author mstieger
 *
 */
public class PlayingFieldLayout implements LayoutManager2{

    public static final String CARPET  = "Carpet";
    public static final String BLACKBOARD  = "Blackboard";
    public static final String HAND = "Hand";
    public static final String INFO = "Info";
    public static final String ANIMATIONREGION = "AnimationRegion";
    
    private Component carpet;
    private Component blackboard;
    private Component hand;
    private Component info;
    private Component animationRegion;
    
	@Override
	public void addLayoutComponent(String name, Component comp) {
		synchronized (comp.getTreeLock()) {
	        /* Asign the component to one of the known regions of the layout.
	         */
	        if (CARPET.equals(name) || name == null) {
	        	carpet = comp;
	        } else if (BLACKBOARD.equals(name)) {
	        	blackboard = comp;
	        } else if (HAND.equals(name)) {
	        	hand = comp;
	        } else if (INFO.equals(name)){
	        	info = comp;
	        } else if (ANIMATIONREGION.equals(name)) {
	        	animationRegion = comp;
            }else {
            	throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
            }
	      }
		
	}
	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return this.minimumLayoutSize(parent);	//Ensures that calling pack method on the frame does not result in an exception
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		if(isValid()) {
			Dimension carpet = this.carpet.getMinimumSize();
			Dimension blackboard = this.blackboard.getMinimumSize();
			Dimension hand = this.hand.getMinimumSize();
			Dimension info = this.info.getMinimumSize();
			
			//Calculate minimum height
			int height = Math.max(carpet.height + hand.height, blackboard.height + info.height);
			//Calculate minimum width
			int width = Math.max(carpet.width + blackboard.width, info.width);
			return new Dimension(width, height);
		}
		return new Dimension(-1, -1); //Some default layout to prevent exceptions if returned null
		
	}
	
	@Override
	public void layoutContainer(Container parent) {
		//Only works if all components are not null
		if(isValid()) {
			Dimension act = parent.getSize();
			Dimension min = minimumLayoutSize(parent);
			
			/*
			 * Scaling
			 * Take minimal scale of horizontal and vertical scale to ensure that the components always fit
			 * in the parent's bounds. Because the components height and width are scaled with the same factor,
			 * their aspect ration stays the same		
			 */
			double scaling = Math.min((double)act.width/min.width, (double)act.height/min.height);
			scaling = Math.max(scaling, 1);	//Prevents parent's bounds to go below minimum layout size
			
			//Set bounds of components
			//Set parents bounds 
			int x=0, y=0, w=(int)(scaling*min.width), h=(int)(scaling*min.height);
			parent.setBounds(x, y, w, h);
			
			//Carpet located in the upper left
			x = 0; y=0; 
			w=(int)(scaling*carpet.getMinimumSize().width); h = (int)(scaling*carpet.getMinimumSize().height);
			carpet.setBounds(0, 0, w, h);
			
			//Blackboard located in the upper right, immediately after carpet
			x = w; y = 0; 
			w = (int)(scaling*blackboard.getMinimumSize().width); h = (int)(scaling*blackboard.getMinimumSize().height);
			blackboard.setBounds(x, y, w, h);
			
			//Hand located in the lower left below carpet
			x = 0; y = (int)(scaling*(carpet.getMinimumSize().height));
			w = (int)(scaling*carpet.getMinimumSize().width); h = (int)(scaling*hand.getMinimumSize().height);
			hand.setBounds(x, y, w, h);	
			
			//Infoboard located in the lower right
			x = (int)(scaling*blackboard.getMinimumSize().height); y = (int)(scaling*(blackboard.getMinimumSize().height));
			w = (int)(scaling*info.getMinimumSize().width); h = (int)(scaling*info.getMinimumSize().height);
			info.setBounds(x, y, w, h);
			
			//AnimationRegion located above Blackboard and Hand
			x = 0; y = 0;
			w=(int)(scaling*carpet.getMinimumSize().width); h = (int)(scaling*carpet.getMinimumSize().height) + (int)(scaling*hand.getMinimumSize().height);
			animationRegion.setBounds(x, y, w, h);
		}
		
	}
	
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		synchronized (comp.getTreeLock()) {
	        if ((constraints == null) || (constraints instanceof String)) {
	            addLayoutComponent((String)constraints, comp);
	        } else {
	            throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
	        }
	      }	
	}
	private boolean isValid() {
		return (carpet != null && blackboard != null && hand != null && info != null);
	}
	@Override
	public Dimension maximumLayoutSize(Container target) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub
		
	}
    

}
