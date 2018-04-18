package gui.playingView;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * Lays out components in a horizontal line, centered in the middle of
 * the parent container. This layout ensures that the minimum height of
 * the biggest component is not injured. If the containers width should go
 * under the minimal width, the components will be laid out overlapping.
 * !!!Note that this layout is optimized for components with same dimensions.
 * 
 * @author mstieger
 *
 */
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int minHeight = 0;
		int minWidth = 0;
		
		for(Component c : parent.getComponents()) {
			minWidth = Math.max(minWidth, c.getMinimumSize().width);
			minHeight += c.getMinimumSize().height;
		}
		try {
			return new Dimension((parent.getComponentCount() + 1) * minWidth/2 ,
					minHeight/parent.getComponentCount());
		}
		catch(ArithmeticException ex) {
			return new Dimension(-1, -1);
		}

	}

	@Override
	public void layoutContainer(Container parent) {
		//CALCULATE SCALE AND PARENT SIZE
		Dimension min = this.minimumLayoutSize(parent);
		Dimension act = parent.getSize();
		
		act.height = Math.max(act.height, min.height);	//Height defines the components size
		double scale = act.height/(double)min.height; 	//Scale can not go below 1.0
		act.width = Math.max(act.width, (int)(min.width*scale));	//Take biggest width		
		parent.setSize(act);
		
		//SETS BOUNDS OF COMPONENTS
		int minWidth = 0;
		for(Component c : parent.getComponents()) {
			minWidth += c.getMinimumSize().width;
		}
		//Components fit in parent
		if(minWidth*scale <= act.getWidth()) {
			int x = 0;
			boolean firstComponent = true;
			for(Component c : parent.getComponents()){
				if(firstComponent) {
					x = (int)(act.width - parent.getComponentCount() * c.getMinimumSize().getWidth()*scale)/2; 
					firstComponent = false;
				}
				c.setBounds(x, 0, (int)(c.getMinimumSize().width*scale), (int)(c.getMinimumSize().height*scale));
				x += c.getWidth();
			}
		}
		//Horizontally overlap components
		else if(parent.getComponentCount() > 0){
			int offset = (int)((act.width - parent.getComponent(parent.getComponentCount()-1).getMinimumSize().width*scale)/(parent.getComponentCount()-1));
			for(int i = 0; i < parent.getComponentCount(); i++) {
				Component c = parent.getComponent(i);
				c.setBounds(i*offset, 0, (int)(c.getMinimumSize().width*scale), (int)(c.getMinimumSize().height*scale));
			}
		}
	}

}
