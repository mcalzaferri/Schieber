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
import gui.PictureFactory.Pictures;
import gui.playingView.PlayingFieldView;
import shared.client.ClientModel;

public class Gui extends AbstractClientView{
	public static PictureFactory pictureFactory = new PictureFactory();
	private ArrayList<ViewObserver> observers;
	private JFrame frame;
	private JInternalFrame iFrame;
	private PlayingFieldView main;
	private Viewable current;
	private ArrayList<Viewable> internals;
		
	public Gui(ClientModel data) {
		super(data);
		this.observers = new ArrayList<>();
		this.frame = new JFrame("Schieber");
		this.iFrame = new JInternalFrame("", false, false, false, false);
		this.main = new PlayingFieldView(data, observers);
		main.update();
		this.current = null;
		
		this.internals = new ArrayList<>();		
		internals.add(new SelectHostView(observers));
		//internals.add(new LobbyView(data, observers)); TODO Throws null pointer exception
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
	}
		
	@Override
	public void addObserver(ViewObserver observer) {
		this.observers.add(observer);
	}
	
	@Override
	public void changeView(ViewEnumeration view) {
		//Update view if current view is the one to be changed
		if(getCurrentView() == view) {
			if(view == main.getType())
				main.update();
			else
				current.update();
		}
		//Load new view in internal frame
		else {
			for(Viewable v : internals) {
				if(v.getType().equals(view)) {
					this.current = v;
					this.iFrame.setContentPane(v.getContent());
					this.iFrame.setSize(v.getContent().getMinimumSize());
					this.iFrame.setTitle(v.getType().toString());
					this.iFrame.setVisible(true);
					return;
				}
			}
			this.iFrame.setVisible(false);	//If view is not in internals close inner frame
		}

	}

	@Override
	public ViewEnumeration getCurrentView() {
		if(current == null) {
			return main.getType();
		}
		return current.getType();
	}

	@Override
	public void publishMessage(String text) {
		this.main.publish(text);
	}
}
