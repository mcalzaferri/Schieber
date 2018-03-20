package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.ServerAddress;

import javax.swing.*;

public class SelectHostView extends AbstractView{
	
	private JTextField serverIPText; 
	private JButton startButton;
	
    private static final int width = 500;
    private static final int height = 300;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;



	public SelectHostView(ViewEnumeration viewType, ArrayList<ViewObserver> observers) {
		super(viewType, observers);
		// TODO Auto-generated constructor stub
		setTitle("Spiel starten");
		
		JPanel serverIPPanel = new JPanel();
		serverIPPanel.setLayout(new BorderLayout());
		JLabel serverIPLabel = new JLabel("Server-IP:");
		serverIPText = new JTextField();
		serverIPPanel.add(serverIPLabel, BorderLayout.NORTH);
		serverIPPanel.add(serverIPText, BorderLayout.CENTER);
		
		JPanel startButtonPanel = new JPanel();
		startButtonPanel.setLayout(new BorderLayout());
		startButton = new JButton("starte Schieber");
		startButtonPanel.add(startButton, BorderLayout.EAST);
		
		JPanel startViewPanel = new JPanel();
		startViewPanel.setLayout(new BorderLayout());
		startViewPanel.add(serverIPPanel,BorderLayout.NORTH);
		startViewPanel.add(startButtonPanel,BorderLayout.SOUTH);
		
		add(startViewPanel);
		setSize(width,height);
		setLocation(left,top);
		
		
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					ServerAddress serverAddress= new ServerAddress(Integer.parseInt(serverIPText.getText()));
					observer. btnConnectClick(serverAddress);
				}
			}
		});
		
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
