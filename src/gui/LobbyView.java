package gui;

import javax.swing.*;
import client.ViewEnumeration;
import client.ViewObserver;
import shared.Player;
import shared.Seat;
import shared.client.ClientModel;
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
		layoutButtons();
		playerScrollPane = new JScrollPane();
		layoutPlayerScrollPanel();

		this.setLayout(new BorderLayout());
		this.add(playerScrollPane,BorderLayout.WEST);
		this.add(lobbyPanel,BorderLayout.CENTER);
		
		this.setPreferredSize(new Dimension(width,height));
		this.setLocation(left,top);
	}
	
	public void layoutPlayerScrollPanel()
	{
		String[] columnNames = {"Name", "Sitzplatz"};
		Object[][] tableData = new Object[playersMap.size()+1][2];
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
		if(getActPlayer() != null) {
			tableData[rowCount][0]=getActPlayer().getName();
			if(getActPlayer().getSeatNr() == 0)
				tableData[rowCount][1]="kein Sitzplatz";
			else
				tableData[rowCount][1]=getActPlayer().getSeatNr();
		}else {
			//TODO getActPlayer() ist am Anfang noch nicht initialisiert
		}
		
		
		playerTable	= new JTable(tableData,columnNames);
		playerScrollPane.setViewportView(playerTable);
		playerScrollPane.setPreferredSize(new Dimension(width/4,height));
	}

	
	public void layoutButtons()
	{
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

	    		if(isSeatFree(1))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT1);
					}
					update();
	    		}
	    	}
		});
	    
	    chair2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	    		if(isSeatFree(2))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT2);
					}
					update();
	    		}
			}
		});
	    
	    chair3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	    		if(isSeatFree(3))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT3);
					}
					update();
	    		}
			}
		});
	    
	    chair4Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	    		if(isSeatFree(4))
	    		{
					for(ViewObserver observer : observers) {
						observer.btnJoinTableClick(Seat.SEAT4);
					}
					update();
	    		}
			}
		});
	    
	    
	    JPanel readyButtonPanel = new JPanel();
	    readyButtonPanel.setLayout(new FlowLayout());
	    
	    JButton readyButton = new JButton("Spieler ist bereit");
	    readyButtonPanel.add(readyButton);
	    
	    JPanel showStatePanel = new JPanel();
	    showStatePanel.setBackground(Color.red);
	    
	    readyButtonPanel.add(showStatePanel);
	    
	    lobbyPanel = new JPanel();
	    lobbyPanel.setLayout(new BorderLayout());
	    lobbyPanel.add(lobbyPicturePanel, BorderLayout.CENTER);
	    lobbyPanel.add(readyButtonPanel, BorderLayout.SOUTH);
	    
	    JOptionPane readyMessageOptionPane = new JOptionPane();
	    
	    readyButton.addActionListener(new ActionListener() {
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
		layoutPlayerScrollPanel();
		playerTable.repaint();
		playerScrollPane.repaint();
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
