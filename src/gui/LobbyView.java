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
    
	//components
	private JPanel tablePanel;
	private JPanel playingFieldPanel;
	private JPanel lobbyViewPanel;
	private JPanel continueButtonPanel;
	private Box player1Box;
	private Box player2Box;
	private Box player3Box;
	private Box player4Box;
	private JScrollPane playerScrollPane;
	private JOptionPane chooseSeatOptionPane;
	private JTable playerTable;
	private JButton player1Button;
	private JButton player2Button;
	private JButton player3Button;
	private JButton player4Button;
	private JButton continueButton;
	private JLabel player1Label;
	private JLabel player2Label;
	private JLabel player3Label;
	private JLabel player4Label;
	
	Map<Integer,Player> playersMap;
	Player actPlayer;
	
	
	public LobbyView(ClientModel data, ArrayList<ViewObserver> observers) {
		super(data, observers);
		
		playersMap = data.getPlayers();
		actPlayer = data.getThisPlayer();
		
		layoutLobbyView();
		
	}
	
	public void layoutLobbyView()
	{
		layoutPlayerTablePanel();
		layoutTablePanel();
		lobbyViewPanel = new JPanel();
		lobbyViewPanel.setLayout(new BorderLayout());
		lobbyViewPanel.add(playerScrollPane,BorderLayout.WEST);
		lobbyViewPanel.add(tablePanel,BorderLayout.CENTER);
		
		add(lobbyViewPanel);
		setSize(width,height);
		setLocation(left,top);
	}
	
	public void layoutPlayerTablePanel()
	{
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
		
		playerTable	= new JTable(tableData,columnNames);
		playerScrollPane = new JScrollPane(playerTable);
		playerScrollPane.setPreferredSize(new Dimension(width/4,height));
	}
	
	public void layoutTablePanel()
	{
		playingFieldPanel = new JPanel();
		playingFieldPanel.setLayout(new LobbyViewTableLayout(10,10));
		try{
			playingFieldPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Pictures.Table))),LobbyViewTableLayout.CENTER);
		}
		catch(IOException e1)
		{
		}
		
		layoutPlayerPanel();
		
		playingFieldPanel.add(player1Box,LobbyViewTableLayout.NORTH);
		playingFieldPanel.add(player2Box,LobbyViewTableLayout.EAST);
		playingFieldPanel.add(player3Box,LobbyViewTableLayout.SOUTH);
		playingFieldPanel.add(player4Box,LobbyViewTableLayout.WEST);
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(playingFieldPanel, BorderLayout.CENTER);
		continueButton = new JButton("weiter");
		continueButtonPanel = new JPanel();
		continueButtonPanel.setLayout(new BorderLayout());
		continueButtonPanel.add(continueButton, BorderLayout.EAST);
		tablePanel.add(continueButtonPanel,BorderLayout.SOUTH);
		
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					actPlayer.setReady(true);
			}
		});
	}
	
	public void layoutPlayerPanel()
	{
		//Buttons
		player1Button = new JButton("Spieler platzieren");
		player1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
		player2Button = new JButton("Spieler platzieren");
		player2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
		player3Button = new JButton("Spieler platzieren");
		player3Button.setAlignmentX(Component.CENTER_ALIGNMENT);
		player4Button = new JButton("Spieler platzieren");
		player4Button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		player1Label = new JLabel("Sitzplatz 1");
		player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		player2Label = new JLabel("Sitzplatz 2");
		player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		player3Label = new JLabel("Sitzplatz 3");
		player3Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		player4Label = new JLabel("Sitzplatz 4");
		player4Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		checkFreeSeats();
		
		
		
		/*player1Panel = new JPanel();
		player1Panel.setLayout(new GridLayout(2,1));
		player1Panel.add(player1Label);
		player1Panel.add(player1Button);*/
		
		player1Box = Box.createVerticalBox();
		player1Box.add(player1Label);
		player1Box.add(player1Button);
		
		player2Box = Box.createVerticalBox();
		player2Box.add(player2Label);
		player2Box.add(player2Button);

		player3Box = Box.createVerticalBox();
		player3Box.add(player3Label);
		player3Box.add(player3Button);
		
		player4Box = Box.createVerticalBox();
		player4Box.add(player4Label);
		player4Box.add(player4Button);
		
		
		player1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseSeatOptionPane = new JOptionPane();
				int response = chooseSeatOptionPane.showConfirmDialog(null,"Möchten Sie Sitzplatz 1 auswählen?", "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.YES_NO_OPTION)
				{
					actPlayer.setSeatNr(1);
					actPlayer.setAtTable(true);
					update();
				}
			}
		});
		
		player2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseSeatOptionPane = new JOptionPane();
				int response = chooseSeatOptionPane.showConfirmDialog(null,"Möchten Sie Sitzplatz 2 auswählen?", "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.YES_NO_OPTION)
				{
					actPlayer.setSeatNr(2);
					actPlayer.setAtTable(true);
					update();
				}
			}
		});
		
		player3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseSeatOptionPane = new JOptionPane();
				int response = chooseSeatOptionPane.showConfirmDialog(null,"Möchten Sie Sitzplatz 3 auswählen?", "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.YES_NO_OPTION)
				{
					actPlayer.setSeatNr(3);
					actPlayer.setAtTable(true);
					update();
				}
			}
		});
		
		player4Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseSeatOptionPane = new JOptionPane();
				int response = chooseSeatOptionPane.showConfirmDialog(null,"Möchten Sie Sitzplatz 4 auswählen?", "Sitzplatz wählen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(response == JOptionPane.YES_NO_OPTION)
				{
					actPlayer.setSeatNr(4);
					actPlayer.setAtTable(true);
					update();
				}
			}
		});
	}
	
	public void setLabels(String playerName, int seatNumber)
	{
		switch(seatNumber)
		{
			case 1:
				player1Label.setText("hier spielt "+playerName);
				break;
			case 2:
				player2Label.setText("hier spielt "+playerName);
				break;
			case 3:
				player3Label.setText("hier spielt "+playerName);
				break;
			case 4:
				player4Label.setText("hier spielt "+playerName);
				break;
			
		}
	}
	
	public void checkFreeSeats()
	{
		player1Button.setVisible(true);
		player2Button.setVisible(true);
		player3Button.setVisible(true);
		player4Button.setVisible(true);
		
		player1Label.setText("Sitzplatz 1");
		player2Label.setText("Sitzplatz 2");
		player3Label.setText("Sitzplatz 3");
		player4Label.setText("Sitzplatz 4");
		
		for(Map.Entry<Integer,Player> entry: playersMap.entrySet())
		{
			int seatNumber = entry.getValue().getSeatNr();
			Player player = entry.getValue();
			
			if(seatNumber>0 && seatNumber<=4)
			{
				setButtonInvisible(seatNumber);
				setLabels(player.getName(), seatNumber);
			}
		}
		
		int actSeatNumber = actPlayer.getSeatNr();
		if(actSeatNumber>0 && actSeatNumber<=4)
		{
			setButtonInvisible(actSeatNumber);
			setLabels(actPlayer.getName(), actSeatNumber);
		}
	}
	
	public void setButtonInvisible(int freeSeatNumber)
	{
		switch(freeSeatNumber)
		{
			case 1:
				player1Button.setVisible(false);
				break;
			case 2:
				player2Button.setVisible(false);
				break;
			case 3:
				player3Button.setVisible(false);
				break;
			case 4:
				player4Button.setVisible(false);
				break;
			
		}
	}
	
	@Override
	public void update() {
		playersMap = data.getPlayers();
		layoutPlayerTablePanel();
		playerTable.repaint();
		playerScrollPane.repaint();
		checkFreeSeats();
		player1Box.validate();
		player2Box.validate();
		player3Box.validate();
		player4Box.validate();
		this.repaint();
	}

	@Override
	public JPanel getContent() {
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.LOBBYVIEW;
	}
	
	
	

}
