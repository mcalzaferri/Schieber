package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.ServerAddress;
import shared.Card;
import shared.CardColor;
import shared.CardValue;

import javax.swing.*;

public class SelectHostView extends ObservableView implements Viewable{
	
	//Components
	JPanel serverPanel;
	
	private JTextField serverIPText; 
	private JTextField serverSocketText;
	private JButton startButton;
	
	//Window size and coordinates 
    private static final int width = 500;
    private static final int height = 300;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;
    
    


	public SelectHostView(ArrayList<ViewObserver> observers) {
		super(null, observers);
		
		layoutSelectHostView();
		
		
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					InetSocketAddress serverAddress = new InetSocketAddress(serverIPText.getText(), Integer.parseInt(serverSocketText.getText())); //TODO change port 
					observer. btnConnectClick(serverAddress, "YOU"); //TODO Feld zur eingabe des Benutzernamens (Darf nicht leer sein) /Maurus
				}
			}
		});
	}

	private void layoutSelectHostView() {
		
		//dimensions and location of window
		setMinimumSize(new Dimension(width, height)); //TODO Has ofach mol so gmacht /Maurus
		setSize(width,height);
		setLocation(left,top);
		
		JLabel serverLabel = new JLabel("Server");
		
		/*//Server-IP
		JLabel serverIPLabel = new JLabel("IP-Adresse:");
		serverIPText = new JTextField();
		JPanel serverIPPanel = new JPanel();
		serverIPPanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,0));
		serverIPPanel.add(serverIPLabel);
		serverIPPanel.add(serverIPText);
		
		//Server-Socket
		JLabel serverSocketLabel = new JLabel("Socket:");
		serverSocketText = new JTextField();
		JPanel serverSocketPanel = new JPanel();
		serverSocketPanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,0));
		serverSocketPanel.add(serverSocketLabel);
		serverSocketPanel.add(serverSocketText);*/
		
		//Server-IP and Server-Socket
		JLabel serverIPLabel = new JLabel("IP-Adresse:");
		serverIPText = new JTextField();
		JLabel serverSocketLabel = new JLabel("Socket:");
		serverSocketText = new JTextField();
		JPanel serverIPSocketPanel = new JPanel();
		serverIPSocketPanel.setLayout(new GridLayout(2,2,0,5));
		serverIPSocketPanel.add(serverIPLabel);
		serverIPSocketPanel.add(serverIPText);
		serverIPSocketPanel.add(serverSocketLabel);
		serverIPSocketPanel.add(serverSocketText);
		
		
		//Server IP and Socket
		serverPanel = new JPanel();
		serverPanel.setLayout(new GridLayout(2,1,0,20));
		serverPanel.add(serverLabel);
		serverPanel.add(serverIPSocketPanel);
		//serverPanel.add(serverSocketPanel);

		
		//Button
		JPanel startButtonPanel = new JPanel();
		startButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		startButton = new JButton("starte Schieber");
		startButtonPanel.add(startButton);
		
		//add Components 
		setLayout(new BorderLayout());
		add(serverPanel,BorderLayout.NORTH);
		add(startButtonPanel,BorderLayout.SOUTH);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getContent() {
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.SELECTHOSTVIEW;
	}

}
