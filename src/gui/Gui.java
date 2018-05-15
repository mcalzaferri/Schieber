package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import client.AbstractClientView;
import client.ViewEnumeration;
import client.ViewObserver;
import client.shared.ClientModel;
import gui.animation.AnimationListener;
import gui.playingView.PlayingFieldView;
import shared.Card;

public class Gui extends AbstractClientView{
	public static PictureFactory pictureFactory = PictureFactory.instance;
	private ArrayList<ViewObserver> observers;
	private JFrame frame;
	private JInternalFrame iFrame;
	private Dialog dialog;
	private PlayingFieldView main;
	private Viewable current;
	private ArrayList<Viewable> internals;
	
	public Gui(ClientModel data) {
		super(data);
		this.observers = new ArrayList<>();
		this.frame = new JFrame("Schieber");
		this.iFrame = new JInternalFrame("", false, false, false, false);
		this.main = new PlayingFieldView(data, observers);
		this.dialog = new Dialog(frame, ModalityType.MODELESS);
		
		this.internals = new ArrayList<>();		
		internals.add(new SelectHostView(observers));
		internals.add(new LobbyView(observers, data));
		internals.add(new TrumpView(observers));
		internals.add(new WeisView(data, observers));
		internals.add(new GameOverView(observers));
		
		initializeGui();
	}
	private void initializeGui() {
		//Set up internal frame
        JDesktopPane desktop = new JDesktopPane(); //a specialized layered pane
		desktop.add(iFrame);
        frame.setContentPane(desktop);
        
		//Set up main frame	
		frame.setLayout(new BorderLayout());
		frame.add(main.getContent(), BorderLayout.CENTER);
		frame.setVisible(true);	//!!!Set visible has to stand before calculating the minimum size => Creating insets
		frame.setMinimumSize(getFramesizeForContent(main.getContent().getMinimumSize()));
		frame.pack();
		
		//Change close operation
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to exit the application?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

		        	for(ViewObserver observer: observers)
					{
						observer.btnCloseWindowClick(getCurrentView());
					}
		        }
		    }
		});
		
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				for(ViewObserver vo : observers) {
					vo.playViewClick();
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//Refresh all components
		changeView(ViewEnumeration.PLAYVIEW);
		updateAll();
	}
		
	@Override
	public void addObserver(ViewObserver observer) {
		this.observers.add(observer);
	}
	
	@Override
	public void changeView(ViewEnumeration view) {
		//Set internal frame
		for(Viewable v : internals) {
			if(v.getType().equals(view)) {
				this.current = v;
				this.iFrame.setContentPane(v.getContent());
				this.iFrame.pack();

				this.iFrame.setTitle(v.getType().toString());
				this.iFrame.setVisible(true);
				Container c = iFrame.getContentPane();
				//Enlarge frame that iFrame can fit
				if(frame.getContentPane().getWidth() < iFrame.getWidth()) {
					frame.setSize(getFramesizeForContent(new Dimension(iFrame.getWidth(), frame.getContentPane().getHeight())));
				}
				if(frame.getContentPane().getHeight() < iFrame.getHeight()) {
					frame.setSize(getFramesizeForContent(new Dimension(frame.getContentPane().getWidth(), iFrame.getHeight())));
				}
				
				return; 
			}
		}
		//Set main as current view
		this.current = main;
		this.iFrame.setVisible(false);	//Close inner frame
	}

	@Override
	public ViewEnumeration getCurrentView() {
		return current.getType();
	}

	@Override
	public void publishMessage(String text) {
		this.main.publish(text);
	}
	@Override
	public void updateView(ViewEnumeration view) {
		for(Viewable v: internals) {
			if(v.getType() == view) {
				v.update();
				return; //Leave method if first element has been found
			}
		}
		main.update();	//Update  main if no other view has been found
	}
	
	@Override
	public void updateAll() {
		for(Viewable v: internals) {
			v.update();
		}
		main.update();
	}
	
	@Override
	public void closeDialog() {
		dialog.dispose();		
	}
	
	@Override
	public void showDialog(String message, gui.Dialog.MessageType type) {
		/*Makes a dialog from a new thread => Needed because JOptionPane stops the
		 * execution of the calling thread and therefore the calling program
		 */	
		closeDialog();
		dialog.showDialog(message, type);
	}
	
	private Dimension getFramesizeForContent(Dimension content) {
		return new Dimension(frame.getInsets().left + frame.getInsets().right + content.width,
				frame.getInsets().top + frame.getInsets().bottom + content.height);
	}
	
	@Override
	public void showMoveCardAnimation(Card card,int duration,int source,int sourcePos, int sourceCount ,int destination,int destinationPos, int destinationCount, AnimationListener listener) {
		main.getAnimationRegion().showMoveCardAnimation(card, duration, source, sourcePos, sourceCount, destination, destinationPos, destinationCount, listener);
	}
	
	@Override
	public void sleepAnimationFinished() {
		while(main.getAnimationRegion().animationIsRunning()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void removeFinishedAnimations() {
		main.getAnimationRegion().removeFinishedAnimations();
		
	}
	@Override
	public boolean hasAnimations() {
		return main.getAnimationRegion().animationIsRunning();
		
	}
}
