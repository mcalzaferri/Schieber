package gui.playingView;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class HandLayout implements LayoutManager{

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		//CALCULATES THE PREFFERED SIZE OF THE CONTAINER
		int prefWidth = 0, prefHeight = 0;
		for(Component c : parent.getComponents()) {
			prefWidth += c.getPreferredSize().width;
			prefHeight += c.getPreferredSize().height;
		}
		//Sets dimension to total components width and average components height
		return new Dimension(prefWidth, prefHeight/parent.getComponentCount());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int minHeight = 0;
		int minWidth = 0;
		for(Component c : parent.getComponents()) {
			minWidth = Math.max(minWidth, c.getMinimumSize().width);
			minHeight += c.getMinimumSize().height;
		}
		return new Dimension(minWidth, minHeight/parent.getComponentCount());
	}

	@Override
	public void layoutContainer(Container parent) {
		//SETS BOUNDS OF COMPONENTS
		Dimension pref = this.preferredLayoutSize(parent);
		Dimension min = this.minimumLayoutSize(parent);
		Dimension act = parent.getSize();	
		act.height = Math.max(act.height, min.height);
		double scale = act.height/(double)min.height;
		act.width = Math.max(act.width, (int)(min.width*scale));
		
		parent.setBounds(parent.getBounds().x, parent.getBounds().y, act.width, act.height);
		
		//Do the layout
		if(pref.getWidth()*scale <= act.getWidth()) {
			int x = (int) ((act.getWidth() - pref.getWidth()*scale)/2);
			for(Component c : parent.getComponents()){
				c.setBounds(x, 0, (int)(c.getMinimumSize().width*scale), (int)(c.getMinimumSize().height*scale));
				x += c.getWidth();
			}
		}
		else {
			int offset = (int)((act.width - parent.getComponent(parent.getComponentCount()-1).getMinimumSize().width*scale)/(parent.getComponentCount()-1));
			for(int i = 0; i < parent.getComponentCount(); i++) {
				Component c = parent.getComponent(i);
				c.setBounds(i*offset, 0, (int)(c.getMinimumSize().width*scale), (int)(c.getMinimumSize().height*scale));
			}
		}
	}

}
