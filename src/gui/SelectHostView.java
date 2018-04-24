package gui;

import java.awt.*;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import javax.swing.*;

import client.ViewEnumeration;
import client.ViewObserver;

public class SelectHostView extends ObservableView implements Viewable{
	
	//Components
	JPanel serverPanel;
	
	private JTextField serverIPText; 
	private JTextField serverSocketText;
	private JTextField usernameText;
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
					observer.btnConnectClick(serverAddress,usernameText.getText());
				}
			}
		});
	}

	private void layoutSelectHostView() {
		
		//dimensions and location of window

		this.setPreferredSize(new Dimension(width,height));
		setLocation(left,top);
		
		JLabel serverLabel = new JLabel("Server");
		serverLabel.setFont(new Font(serverLabel.getFont().getFontName(), Font.BOLD, 24));
		serverLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//Server-IP and Server-Socket
		JLabel serverIPLabel = new JLabel("IP-Adresse:");
		serverIPLabel.setFont(new Font(serverIPLabel.getFont().getFontName(), Font.PLAIN, 12));
		serverIPText = new JTextField();
		JLabel serverSocketLabel = new JLabel("Socket:");
		serverSocketLabel.setFont(new Font(serverSocketLabel.getFont().getFontName(), Font.PLAIN, 12));
		serverSocketText = new JTextField();
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font(serverSocketLabel.getFont().getFontName(), Font.PLAIN, 12));
		usernameText = new JTextField();
		
		JPanel serverIPSocketPanel = new JPanel();
		serverIPSocketPanel.setLayout(new GridLayout(3,2,0,5));
		serverIPSocketPanel.add(serverIPLabel);
		serverIPSocketPanel.add(serverIPText);
		serverIPSocketPanel.add(serverSocketLabel);
		serverIPSocketPanel.add(serverSocketText);
		serverIPSocketPanel.add(usernameLabel);
		serverIPSocketPanel.add(usernameText);
		
		
		//Server IP and Socket
		serverPanel = new JPanel();
		serverPanel.setLayout(new BorderLayout(0,50));
		serverPanel.add(serverLabel,BorderLayout.NORTH);
		serverPanel.add(serverIPSocketPanel,BorderLayout.CENTER);
		//serverPanel.add(serverSocketPanel);

		
		//Button
		JPanel startButtonPanel = new JPanel();
		startButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		startButton = new JButton("starte Schieber");
		startButton.setFont(new Font(startButton.getFont().getFontName(), Font.PLAIN, 12));
		startButtonPanel.add(startButton);
		
		//add Components 
		this.setLayout(new BorderLayout());
		this.add(serverPanel,BorderLayout.NORTH);
		this.add(startButtonPanel,BorderLayout.SOUTH);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getContent() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		// TODO Auto-generated method stub
		return ViewEnumeration.SELECTHOSTVIEW;
	}
}
