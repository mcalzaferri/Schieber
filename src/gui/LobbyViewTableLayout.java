package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

public class LobbyViewTableLayout implements LayoutManager2{

    public static final String NORTH  = "North";
    public static final String EAST  = "East";
    public static final String SOUTH = "South";
    public static final String WEST = "West";
    public static final String CENTER = "Center";
    
    private Component north;
    private Component east;
    private Component south;
    private Component west;
    private Component center;
    
    int hgap;
    int vgap;
    
    public LobbyViewTableLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }
    
	@Override
	public void addLayoutComponent(String name, Component comp) {
		synchronized (comp.getTreeLock()) {
	        /* Assign the component to one of the known regions of the layout.
	         */
	        if ("Center".equals(name) || name == null) {
	        	center = comp;
	        } else if ("North".equals(name)) {
	        	north = comp;
	        } else if ("East".equals(name)) {
	        	east = comp;
	        } else if ("South".equals(name)) {
	        	south = comp;
	        } else if ("West".equals(name)) {
	        	west = comp;
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
		Dimension center = this.center.getPreferredSize();
		Dimension north = this.north.getPreferredSize();
		Dimension east = this.east.getPreferredSize();
		Dimension south = this.south.getPreferredSize();
		Dimension west = this.west.getPreferredSize();
		
		//Calculate preferred height
		int height = north.height+south.height+center.height+4*vgap;
		//Calculate preferred width
		int width = west.width+east.width+center.width+4*hgap;
		return new Dimension(width, height);
	}
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension center = this.center.getMinimumSize();
		Dimension north = this.north.getMinimumSize();
		Dimension east = this.east.getMinimumSize();
		Dimension south = this.south.getMinimumSize();
		Dimension west = this.west.getMinimumSize();
		
		//Calculate minimum height
		int height = north.height+south.height+center.height+4*vgap;
		//Calculate minimum width
		int width = west.width+east.width+center.width+4*hgap;
		return new Dimension(width, height);
	}
    private Component getChild(String key) {
        Component result = null;

        if (key == NORTH) {
            result = north;
        }
        else if (key == SOUTH) {
            result = south;
        }
        else if (key == WEST) {
            result = west;
        }
        else if (key == EAST) {
            result = east;
        }
        else if (key == CENTER) {
            result = center;
        }
        return result;
    }
    
	@Override
	public void layoutContainer(Container target) {
	      synchronized (target.getTreeLock()) {
	          int top = 0;
	          int bottom = 0;
	          int left = 0;
	          int right = 0;
	          Component c = null;

	          if ((c=getChild(NORTH)) != null) {
	              Dimension d = c.getPreferredSize();
	              c.setBounds((int)(target.getWidth()-d.getWidth())/2, vgap, d.width, d.height);
	              top = d.height+2*vgap;
	          }
	          if ((c=getChild(SOUTH)) != null) {
	              Dimension d = c.getPreferredSize();
	              c.setBounds((int)(target.getWidth()-d.getWidth())/2, target.getHeight()-(vgap+d.height), d.width, d.height);
	              bottom = target.getHeight()-(d.height+2*vgap);
	          }
	          if ((c=getChild(WEST)) != null) {
	              Dimension d = c.getPreferredSize();
	              c.setBounds(hgap, (int)(target.getHeight()-d.getHeight())/2, d.width, d.height);
	              left = d.width+2*hgap;
	          }
	          if ((c=getChild(EAST)) != null) {
	              Dimension d = c.getPreferredSize();
	              c.setBounds(target.getWidth()-(hgap+d.width), (int)(target.getHeight()-d.getHeight())/2, d.width, d.height);
	              right = target.getWidth()-(d.width+2*hgap);
	          }
	          if ((c=getChild(CENTER)) != null) {
	              c.setBounds(left, top, right - left, bottom - top);
	          }
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
