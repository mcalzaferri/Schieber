package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.AbstractClientView;
import client.ViewEnumeration;
import client.ViewObserver;
import client.test.MessageType;
import gui.PictureFactory.Pictures;
import gui.playingView.PlayingFieldView;
import shared.client.ClientModel;

public class Gui extends AbstractClientView implements Runnable{
	public static PictureFactory pictureFactory = new PictureFactory();
	private ArrayList<ViewObserver> observers;
	private JFrame frame;
	private JInternalFrame iFrame;
	Thread dialog;
	private PlayingFieldView main;
	private Viewable current;
	private ArrayList<Viewable> internals;
		
	public Gui(ClientModel data) {
		super(data);
		this.dialog = new Thread(this);
		this.observers = new ArrayList<>();
		this.frame = new JFrame("Schieber");
		this.iFrame = new JInternalFrame("", false, false, false, false);
		this.main = new PlayingFieldView(data, observers);
		
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
		frame.setMinimumSize(main.getContent().getMinimumSize());
		frame.setVisible(true);
		
		//Change close operation
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to exit the application?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            System.exit(0);
		        }
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
	
	/**This enumeration resembles the values to configure a JOptionPane
	 * 
	 * @author mstieger
	 *
	 */
	public enum MessageType{
		/*
		 * Index values of elements must agree with the values for message types
		 * of JOptionPane
		 */
		ERROR(0), 
		INFORMATION(1), 
		WARNING(2), 
		QUESTION(3), 
		PLAIN(-1);
		
		private final int index;
		
		MessageType(int index){
			this.index = index;
		}
	};
	
	String message;
	MessageType type;
	@Override
	public void showDialog(String message, MessageType type) {
		/*Makes a dialog from a new thread => Needed because JOptionPane stops the
		 * execution of the calling thread and therefore the calling program
		 */	
		this.message = message;
		this.type = type;
		
		//Ensure that only one dialog thread is active at the time
		try {
			dialog.stop();	//Forcefully stops the thread (not safe!!!)
		} catch (Exception e) {
			//Do nothing
		}
			
		if(dialog.isAlive()) {
			//If thread could not be stopped or is not stopped yet => wait for thread to die
			try {
				dialog.join();
			} catch (InterruptedException e) {
				//Fatal error => should not happen
				e.printStackTrace();
				return;	//If thread could not be stopped or joined, a new thread must not be created => leave method
			}
		}
		
		//If thread is not alive => start a new thread
		dialog = new Thread(this);
		dialog.start();	
	}
	
	@Override
	public void run() {
		//Creates an option pane with OK button
				JOptionPane.showConfirmDialog(frame, 
			            message, type.toString(), 
			            JOptionPane.DEFAULT_OPTION,
			            type.index);
	}
	
}
