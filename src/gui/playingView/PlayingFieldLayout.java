package gui.playingView;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

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
		
		//Calculate preferred height
		int height = Math.max(blackboard.height, carpet.height) + hand.height;
		//Calculate preferred width
		int width = Math.max(carpet.width + blackboard.width, hand.width);
		return new Dimension(width, height);
	}
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension carpet = this.carpet.getMinimumSize();
		Dimension blackboard = this.blackboard.getMinimumSize();
		Dimension hand = this.hand.getMinimumSize();
		
		//Calculate minimum height
		int height = Math.max(blackboard.height, carpet.height) + hand.height;
		//Calculate minimum width
		int width = Math.max(carpet.width + blackboard.width, hand.width);
		return new Dimension(width, height);
	}
	
	@Override
	public void layoutContainer(Container parent) {
		//TODO Not catched if components are null
		Dimension act = parent.getSize();
		Dimension min = this.minimumLayoutSize(parent);
		
		//Scaling according to width
		act.width = Math.max(act.width, min.width);
		double scaling = (double)act.width/(double)min.width;
		
		//Set bounds
		int x=0, y=0, w=(int)(scaling*min.width), h=(int)(scaling*min.height);
		parent.setBounds(x, y, w, h);
		
		x = 0; y=0; 
		w=(int)(scaling*carpet.getMinimumSize().width); h = (int)(scaling*carpet.getMinimumSize().height);
		carpet.setBounds(0, 0, w, h);
		
		x = w; y = 0; 
		w = (int)(scaling*blackboard.getMinimumSize().width); h = (int)(scaling*blackboard.getMinimumSize().height);
		blackboard.setBounds(x, y, w, h);
		
		x = 0; y = (int)(scaling*(min.height - hand.getMinimumSize().height));
		w = (int)(scaling*hand.getMinimumSize().width); h = (int)(scaling*hand.getMinimumSize().height);
		hand.setBounds(x, y, w, h);
		
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
