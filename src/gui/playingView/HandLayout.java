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
			if(c.isVisible()) {
				prefWidth += c.getPreferredSize().width;
				prefHeight += c.getPreferredSize().height;
			}
		}
		//Sets dimension to total components width and average components height
		return new Dimension(prefWidth, prefHeight/parent.getComponentCount());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void layoutContainer(Container parent) {
		//SETS BOUNDS OF COMPONENTS
		Dimension d = this.preferredLayoutSize(parent);
		Dimension p = parent.getSize();

		//Do the layout
		if(d.getWidth() <= parent.getWidth()) {
			int curWidth = (int) ((parent.getWidth() - d.getWidth())/2);
			for(Component c : parent.getComponents()){
				c.setBounds(curWidth, 0, c.getPreferredSize().width, c.getPreferredSize().height);
				curWidth += c.getWidth();
			}
		}
		else {
			int offset = (parent.getWidth() - parent.getComponent(parent.getComponentCount()-1).getWidth())/(parent.getComponentCount()-1);
			for(int i = 0; i < parent.getComponentCount(); i++) {
				Component c = parent.getComponent(i);
				c.setBounds(i*offset, 0, c.getPreferredSize().width, c.getPreferredSize().height);
			}
		}
	}

}
