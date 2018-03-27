package gui.playingView;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;

public class PlayingFieldLayout implements LayoutManager2{

    public static final String CARPET  = "Carpet";
    public static final String BLACKBOARD  = "Blackboard";
    public static final String HAND = "Hand";
    
    private Component carpet;
    private Component blackboard;
    private Component hand;
    
	@Override
	public void addLayoutComponent(String name, Component comp) {
		synchronized (comp.getTreeLock()) {
	        /* Assign the component to one of the known regions of the layout.
	         */
	        if ("Carpet".equals(name) || name == null) {
	        	carpet = comp;
	        } else if ("Blackboard".equals(name)) {
	        	blackboard = comp;
	        } else if ("Hand".equals(name)) {
	        	hand = comp;
	        } else {
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
		Dimension carpet = this.carpet.getPreferredSize();
		Dimension blackboard = this.blackboard.getPreferredSize();
		Dimension hand = this.hand.getPreferredSize();
		
		//Calculate minimum height
		int height = Math.max(blackboard.height, carpet.height + hand.height);
		//Calculate minimum width
		int width = Math.max(carpet.width, hand.width) + blackboard.width;
		return new Dimension(width, height);
	}
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension carpet = this.carpet.getMinimumSize();
		Dimension blackboard = this.blackboard.getMinimumSize();
		Dimension hand = this.hand.getMinimumSize();
		
		//Calculate minimum height
		int height = carpet.height + hand.height;
		if(blackboard.height > height) {
			height = blackboard.height;
		}
		//Calculate minimum width
		int width = carpet.width;
		if(hand.width > width) {
			width = hand.width;
		}
		return new Dimension(width, height);
	}
	
	@Override
	public void layoutContainer(Container parent) {
		//TODO Not catched if components are null
		Dimension act = parent.getSize();
		Dimension min = this.minimumLayoutSize(parent);
		
		if(act.height < min.height) {
			act.height = min.height;
		}
		if(act.width < min.width) {
			act.width = min.width;
		}
		parent.setBounds(0, 0, act.width, act.height);
		blackboard.setBounds(act.width * 2/3, 0, act.width * 1/3, act.height);
		carpet.setBounds(0, 0, act.width * 2/3, act.height * 2/3);
		hand.setBounds(0, 2/3*act.height, 2/3 * act.width, 1/3 * act.height);
		
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
