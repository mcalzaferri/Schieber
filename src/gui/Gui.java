package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import client.AbstractClientView;
import client.ViewEnumeration;
import client.ViewObserver;
import gui.playingView.PlayingFieldView;
import shared.client.ClientModel;

public class Gui extends AbstractClientView{
	public static PictureFactory pictureFactory = new PictureFactory();
	private ArrayList<ViewObserver> observers;
	private JFrame frame;
	private JInternalFrame iFrame;
	private ArrayList<Viewable> internals;
		
	public Gui(ClientModel data) {
		super(data);
		this.observers = new ArrayList<>();
		this.frame = new JFrame("Schieber");
		this.iFrame = new JInternalFrame("", false, false, false, false);
		
		this.internals = new ArrayList<>();		
		internals.add(new SelectHostView(observers));
		internals.add(new LobbyView(data, observers));
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
		PlayingFieldView p = new PlayingFieldView(data, observers);
		frame.setLayout(new BorderLayout());
		frame.add(p.getContent(), BorderLayout.CENTER);
		frame.setMinimumSize(p.getContent().getMinimumSize());
		frame.setVisible(true);
	}
		
	@Override
	public void addObserver(ViewObserver observer) {
		this.observers.add(observer);
	}
	
	@Override
	public void changeView(ViewEnumeration view) {
		for(Viewable v : internals) {
			if(v.getType().equals(view)) {
				this.iFrame.setContentPane(v.getContent());
				this.iFrame.setSize(v.getContent().getMinimumSize());
				this.iFrame.setTitle(v.getType().toString());
				this.iFrame.setVisible(true);
				return;
			}
		}
		this.iFrame.setVisible(false);
	}

	@Override
	public ViewEnumeration getCurrentView() {
		return null;
	}

	@Override
	public void publishMessage(String text) {
		// TODO Auto-generated method stub
		
	}
}
