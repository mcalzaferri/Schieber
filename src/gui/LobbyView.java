package gui;

import javax.swing.*;
import client.ViewEnumeration;
import client.ViewObserver;
import client.shared.ClientModel;
import shared.Player;
import shared.Seat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class LobbyView extends ObservableView implements Viewable{

	//width, height and dimension of window
    private static final int width = 800;
    private static final int height = 600;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;
    
	//Component
	JPanel lobbyViewPanel;
	LobbyPanel lobbyPicturePanel;
	JPanel lobbyPanel;
	JPanel playerTablePanel;
	JPanel readyStatePanel;
	JScrollPane playerScrollPane;
	JOptionPane chooseSeatOptionPane;
	JTable playerTable;
	JLabel lobbyLabel;
	JButton chair1Button;
	JButton chair2Button;
	JButton chair3Button;
	JButton chair4Button;
	
	//local variables
	Map<Integer,Player> playersMap;
	boolean readyState = false;
	
	public LobbyView(ArrayList<ViewObserver> observers, ClientModel data) {
		super(data, observers);
		if(data == null) {
			throw new IllegalArgumentException("Fatal Error: Players must not be null");
		}
		playersMap = data.getPlayers();
		lobbyPicturePanel = new LobbyPanel();
		
		layoutLobbyView();
		
	}

	public void layoutLobbyView()
	{
		playerScrollPane = new JScrollPane();
		playerTablePanel = new JPanel();
		playerTablePanel.setLayout(new BorderLayout());
		layoutPlayerTable();
		layoutFillRemainingSeatsButton();
		layoutLobbyPanel();

		this.setLayout(new BorderLayout());
		this.add(playerTablePanel,BorderLayout.WEST);
		this.add(lobbyPanel,BorderLayout.CENTER);
		
		this.setPreferredSize(new Dimension(width,height));
		this.setLocation(left,top);
	}
	
	public void layoutPlayerTable()
	{
		//layout Table
		String[] columnNames = {"Name", "Sitzplatz"};
		Object[][] tableData = new Object[playersMap.size()][2];
		int rowCount=0;

		for(Map.Entry<Integer,Player> entry: playersMap.entrySet())
		{
			Player player = entry.getValue();
			tableData[rowCount][0]=player.getName();
			if(player.getSeatNr() == 0)
				tableData[rowCount][1]="kein Sitzplatz";
			else
				tableData[rowCount][1]=player.getSeatNr();
			rowCount++;
		}

		if(playerTable != null) {
			playerTablePanel.remove(playerTable);
		}

		playerTable	= new JTable(tableData,columnNames);
		playerScrollPane.setViewportView(playerTable);
		playerTablePanel.add(playerScrollPane, BorderLayout.CENTER);
		playerScrollPane.setPreferredSize(new Dimension(width/4,height));
	}
	
	public void layoutFillRemainingSeatsButton()
	{
	    JButton fillSeatsWithBotsButton = new JButton("restliche Plätze mit Computerspieler besetzen");
		playerTablePanel.add(fillSeatsWithBotsButton, BorderLayout.SOUTH);
		
	    fillSeatsWithBotsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer : observers) {
					observer.btnFillWithBots();
				}
			}
		});
	}

	public void layoutLobbyPanel()
	{
		layoutLobbyPictureButtons();
		layoutReadyStatePanel();
	    lobbyPanel = new JPanel();
	    lobbyPanel.setLayout(new BorderLayout());
	    lobbyPanel.add(lobbyPicturePanel, BorderLayout.CENTER);
	    lobbyPanel.add(readyStatePanel, BorderLayout.SOUTH);
	}
	
	public void layoutLobbyPictureButtons()
	{
		//Layout Buttons on Lobby Picture
		chair1Button = new JButton();
		chair1Button.setOpaque(false);	
		chair1Button.setToolTipText("Sitzplatz 1");
		
		chair2Button = new JButton();
		chair2Button.setOpaque(false);	
		chair2Button.setToolTipText("Sitzplatz 2");
		
		chair3Button= new JButton();
		chair3Button.setOpaque(false);
		chair3Button.setToolTipText("Sitzplatz 3");
		
		chair4Button = new JButton();
		chair4Button.setOpaque(false);
		chair4Button.setToolTipText("Sitzplatz 4");
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWeights = new double[] {0.0709,0.0921,0.1773,0.0567,0.1702,0.1773,0.1631,0.0921};
		gbl.rowWeights = new double[] {0.507,0.198,0.286};
		lobbyPicturePanel.setLayout(gbl);
		
	    GridBagConstraints c = new GridBagConstraints();

	    c.fill=GridBagConstraints.BOTH;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.gridwidth = 8;
	    lobbyPicturePanel.add(Box.createGlue(),c);
	    
	    c.gridx = 1; 
	    c.gridy = 1; 
	    c.gridwidth = 2;
	    lobbyPicturePanel.add(chair1Button,c); 
	    
	    c.gridx = 4; 
	    c.gridy = 1; 
	    c.gridwidth = 3;
	    lobbyPicturePanel.add(chair2Button, c);
	    
	    c.gridx = 5; 
	    c.gridy = 2; 
	    c.gridwidth = 1;
	    lobbyPicturePanel.add(chair3Button, c);
	    c.gridx = 2; 
	    lobbyPicturePanel.add(chair4Button, c);
	    
	    chooseSeatOptionPane = new JOptionPane();
	    
	    chair1Button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {

	    		for(ViewObserver observer : observers) {
					observer.btnJoinTableClick(Seat.SEAT1);
				}
				
				/* TODO REV Notify client anyways so messages can be displayed /Maurus
	    		if(isSeatFree(1))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT1);
					}
					update();
	    		}
	    		*/
	    	}
		});
	    
	    chair2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				for(ViewObserver observer : observers) {
					observer.btnJoinTableClick(Seat.SEAT2);
				}
				
				/* TODO REV Notify client anyways so messages can be displayed /Maurus
	    		if(isSeatFree(2))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT2);
					}
					update();
	    		}
	    		*/
			}
		});
	    
	    chair3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				for(ViewObserver observer : observers) {
					observer.btnJoinTableClick(Seat.SEAT3);
				}
				
				/* TODO REV Notify client anyways so messages can be displayed /Maurus
	    		if(isSeatFree(3))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT3);
					}
					update();
	    		}
	    		*/
			}
		});
	    
	    chair4Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				for(ViewObserver observer : observers) {
					observer.btnJoinTableClick(Seat.SEAT4);
				}
				
				/* TODO REV Notify client anyways so messages can be displayed /Maurus
	    		if(isSeatFree(4))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT4);
					}
					update();
	    		}
	    		*/
			}
		});
	}
	
	public void layoutReadyStatePanel()
	{
	    readyStatePanel = new JPanel();
	    readyStatePanel.setLayout(new FlowLayout());
	    
	    JButton readyStateButton = new JButton("Spieler ist bereit");
	    JPanel showStatePanel = new JPanel();
	    showStatePanel.setBackground(Color.red);
	    
	    readyStatePanel.add(showStatePanel);
	    readyStatePanel.add(readyStateButton);
	    
	    readyStateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer : observers) {
					observer.btnChangeStateClick();
				}
				readyState= !readyState;
				
				if(readyState)
				{
					showStatePanel.setBackground(Color.green);
				}
				else
				{
					showStatePanel.setBackground(Color.red);
				}
			}
		});
	}
	
	public boolean isSeatFree(int actSeatNumber)
	{	
		for(Map.Entry<Integer,Player> entry: playersMap.entrySet())
		{
			int seatNumber = entry.getValue().getSeatNr();

			if(seatNumber == actSeatNumber)
			{
				return false;
			}
		}
		return true;
	}
	
	private Player getActPlayer() {
		return data.getThisPlayer();
	}
	
	@Override
	public void update() {
		playersMap = data.getPlayers();
		layoutPlayerTable();
		
		if(!isValid()) {
			this.validate();
		}
		this.repaint();
	}
	
	@Override
	public JPanel getContent() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.LOBBYVIEW;
	}
	

}
