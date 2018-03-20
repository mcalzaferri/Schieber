package gui;
import javax.swing.*;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.ServerAddress;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GameOverView extends AbstractView{

	private JButton newRoundButton;
	private JButton disconnectButton;
	
    private static final int width = 400;
    private static final int height = 200;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;
	
	public GameOverView(ViewEnumeration viewType, ArrayList<ViewObserver> observers) {
		super(viewType, observers);
		setTitle("Spiel beendet");
		
		newRoundButton = new JButton("neue Runde");
		disconnectButton = new JButton("Beenden");
		JPanel endViewPanel = new JPanel();
		endViewPanel.setLayout(new GridLayout(2,1));
		
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
		
		
		endViewPanel.add(newRoundButton);
		endViewPanel.add(disconnectButton);
		
		add(endViewPanel);
		setSize(width,height);
		setLocation(left,top);

	}
	
	public void update()
	{
		
	}
	
}
