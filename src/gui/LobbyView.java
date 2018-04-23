package gui;

import javax.swing.*;
import java.io.IOException;

import client.ViewEnumeration;
import client.ViewObserver;
import gui.PictureFactory.Pictures;
import shared.Player;
import shared.Trump;
import shared.client.ClientModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
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
	ViewEnumeration viewType;
	ArrayList<ViewObserver> observers;
	ClientModel data;
	Map<Integer,Player> playersMap;
	Player actPlayer;
	int optionType1;
	int optionType2;
	int optionType3;
	int optionType4;
	
	String optionString1;
	String optionString2;
	String optionString3;
	String optionString4;
	
	public LobbyView(ArrayList<ViewObserver> observers, ClientModel data) {
		super(null, observers);
		
		this.viewType = viewType;
		this.observers = observers;
		this.data = data;
		
		playersMap = data.getPlayers();
		actPlayer = data.getThisPlayer();
		
		lobbyPicturePanel = new LobbyPanel();
		
		layoutLobbyView();
		
	}

	public void layoutLobbyView()
	{
		layoutButtons();
		playerScrollPane = new JScrollPane();
		layoutPlayerScrollPanel();
		lobbyViewPanel = new JPanel();
		lobbyViewPanel.setLayout(new BorderLayout());
		lobbyViewPanel.add(playerScrollPane,BorderLayout.WEST);
		lobbyViewPanel.add(lobbyPanel,BorderLayout.CENTER);
		
		add(lobbyViewPanel);
		setSize(width,height);
		setLocation(left,top);
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
		
		tableData[rowCount][0]=actPlayer.getName();
		if(actPlayer.getSeatNr() == 0)
			tableData[rowCount][1]="kein Sitzplatz";
		else
			tableData[rowCount][1]=actPlayer.getSeatNr();
		
		playerTable	= new JTable(tableData,columnNames);
		playerScrollPane.setViewportView(playerTable);
		playerScrollPane.setPreferredSize(new Dimension(width/4,height));
	}

	
	public void layoutButtons()
	{
		chair1Button = new JButton("Sitzplatz 1");
		chair1Button.setOpaque(false);		
		chair2Button = new JButton();
		chair2Button.setOpaque(false);	
		chair3Button= new JButton();
		chair3Button.setOpaque(false);	
		chair4Button = new JButton();
		chair4Button.setOpaque(false);	
		
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
		    	switch(optionType1)
		    	{
			    	case JOptionPane.QUESTION_MESSAGE:
							int response = chooseSeatOptionPane.showConfirmDialog(null,optionString1, "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(response == JOptionPane.YES_NO_OPTION)
							{
								actPlayer.setSeatNr(1);
								update();
							}
						
			    		break;
			    	case JOptionPane.INFORMATION_MESSAGE:
			    			if(actPlayer.getSeatNr() == 1)
			    				optionString1="Sitzplatz 1 wurde reserviert";
			    			
			    			chooseSeatOptionPane.showMessageDialog(null, optionString1);
			    		break;
		    	}
	    	}
		});
	    
	    chair2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		    	switch(optionType2)
		    	{
			    	case JOptionPane.QUESTION_MESSAGE:
							int response = chooseSeatOptionPane.showConfirmDialog(null,optionString2, "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(response == JOptionPane.YES_NO_OPTION)
							{
								actPlayer.setSeatNr(2);
								update();
							}
						
			    		break;
			    	case JOptionPane.INFORMATION_MESSAGE:
			    			if(actPlayer.getSeatNr() == 2)
			    				optionString2="Sitzplatz 2 wurde reserviert";
			    			
			    			chooseSeatOptionPane.showMessageDialog(null, optionString2);
			    		break;
		    	}
			}
		});
	    
	    chair3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		    	switch(optionType3)
		    	{
			    	case JOptionPane.QUESTION_MESSAGE:
							int response = chooseSeatOptionPane.showConfirmDialog(null,optionString3, "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(response == JOptionPane.YES_NO_OPTION)
							{
								actPlayer.setSeatNr(3);
								update();
							}
						
			    		break;
			    	case JOptionPane.INFORMATION_MESSAGE:
		    			if(actPlayer.getSeatNr() == 3)
		    				optionString3="Sitzplatz 3 wurde reserviert";
		    			
			    			chooseSeatOptionPane.showMessageDialog(null, optionString3);
			    		break;
		    	}
			}
		});
	    
	    chair4Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		    	switch(optionType4)
		    	{
			    	case JOptionPane.QUESTION_MESSAGE:
							int response = chooseSeatOptionPane.showConfirmDialog(null,optionString4, "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(response == JOptionPane.YES_NO_OPTION)
							{
								actPlayer.setSeatNr(4);
								update();
							}
						
			    		break;
			    	case JOptionPane.INFORMATION_MESSAGE:
			    			if(actPlayer.getSeatNr() == 4)
			    				optionString4="Sitzplatz 4 wurde reserviert";
			    			
			    			chooseSeatOptionPane.showMessageDialog(null, optionString4);
			    		break;
		    	}
			}
		});
	    
	    checkFreeSeats();
	    
	    JPanel continueButtonPanel = new JPanel();
	    continueButtonPanel.setLayout(new BorderLayout());
	    
	    JButton continueButton = new JButton("weiter");
	    continueButtonPanel.add(continueButton, BorderLayout.SOUTH);
	    
	    lobbyPanel = new JPanel();
	    lobbyPanel.setLayout(new BorderLayout());
	    lobbyPanel.add(lobbyPicturePanel, BorderLayout.CENTER);
	    lobbyPanel.add(continueButtonPanel, BorderLayout.SOUTH);
	    
	    continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actPlayer.setReady(true);
			}
		});
	}
	
	public void checkFreeSeats()
	{	
		optionType1 = JOptionPane.QUESTION_MESSAGE;
		optionType2 = JOptionPane.QUESTION_MESSAGE;
		optionType3 = JOptionPane.QUESTION_MESSAGE;
		optionType4 = JOptionPane.QUESTION_MESSAGE;
		
		optionString1 = "Möchten Sie Sitzplatz 1 auswählen?";
		optionString2 = "Möchten Sie Sitzplatz 2 auswählen?";
		optionString3 = "Möchten Sie Sitzplatz 3 auswählen?";
		optionString4 = "Möchten Sie Sitzplatz 4 auswählen?";
				
		for(Map.Entry<Integer,Player> entry: playersMap.entrySet())
		{
			int seatNumber = entry.getValue().getSeatNr();
			Player player = entry.getValue();
			
			if(seatNumber>0 && seatNumber<=4)
			{
				setOptionPaneType(player);
			}
		}
		
		int actPlayerseatNumber = actPlayer.getSeatNr();
		
		if(actPlayerseatNumber>0 && actPlayerseatNumber<=4)
		{
			setOptionPaneType(actPlayer);
		}
	}
	
	public void setOptionPaneType(Player p)
	{
		switch(p.getSeatNr())
		{
			case 1:
				optionType1 = JOptionPane.INFORMATION_MESSAGE;
				optionString1 = "Dieser Platz ist schon besetzt von "+p.getName();
				break;
			case 2:
				optionType2 = JOptionPane.INFORMATION_MESSAGE;
				optionString2 = "Dieser Platz ist schon besetzt von "+p.getName();
				break;
			case 3:
				optionType3 = JOptionPane.INFORMATION_MESSAGE;
				optionString3 = "Dieser Platz ist schon besetzt von "+p.getName();
				break;
			case 4:
				optionType4 = JOptionPane.INFORMATION_MESSAGE;
				optionString4 = "Dieser Platz ist schon besetzt von "+p.getName();
				break;
			
		}
	}
	
	@Override
	public void update() {
		playersMap = data.getPlayers();
		layoutPlayerScrollPanel();
		playerTable.repaint();
		playerScrollPane.repaint();
		checkFreeSeats();
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
