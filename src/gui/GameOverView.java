package gui;
import javax.swing.*;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.ServerAddress;
import shared.client.ClientModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GameOverView extends ObservableView implements Viewable{

	private JButton newRoundButton;
	private JButton disconnectButton;
	private JPanel buttonPanel;
	
    private static final int width = 400;
    private static final int height = 200;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;
	
	public GameOverView(ArrayList<ViewObserver> observers) {
		super(null, observers);

		newRoundButton = new JButton("neue Runde");
		disconnectButton = new JButton("Beenden");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(newRoundButton);
		buttonPanel.add(disconnectButton);
		JPanel endViewPanel = new JPanel();
		endViewPanel.setLayout(new BorderLayout());
		endViewPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		newRoundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnRestartClick();
				}
			}
		});
		
		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnDisconnectClick();
				}
			}
		});
		
		

		
		add(endViewPanel);
		setSize(width,height);
		setLocation(left,top);

	}
	
	@Override
	public JPanel getContent() {
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.GAMEOVERVIEW;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
}
