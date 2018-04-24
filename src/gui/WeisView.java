package gui;

import java.util.ArrayList;

import client.ViewEnumeration;
import client.ViewObserver;


import java.util.ArrayList;

import client.*;
import shared.client.*;
import shared.*;

import javax.swing.*;

import ch.ntb.jass.common.entities.TrumpEntity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;

public class WeisView extends ObservableView implements Viewable{
	
	//components
	JPanel weisViewPanel;
	JPanel buttonsPanel;
	JPanel cardsPanel;
	JButton weisenButton;
	JButton verwerfenButton;
	JButton swipeLeftButton;
	JButton swipeRightButton;

	//width, height and dimension of window
    private static final int width = 500;
    private static final int height = 600;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;
    
    private static final int hgap = 10;
    private static final int vgap = 20;
    
    //Card width, height and scale
    private static final int cardWidth = 224;
    private static final int cardHeight = 349;
    private static final double scale = 0.4;
    
    private int index = 0;
    
    //list of possible Wiis
    private Weis[] weisList;
        
	public WeisView(ClientModel data, ArrayList<ViewObserver> observers) {	
		super(data, observers);
		if(data == null) {
			throw new IllegalArgumentException("Fatal Error: Weis must not be null");
		}
		weisList = data.getPossibleWiis();
		if(weisList != null)
			layoutWeisView(); //TODO Wenn das GUI erstellt wird ist getPossibleWiis null... /Maurus
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}
	
	public void layoutWeisView()
	{
		weisViewPanel = new JPanel();
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,30));
		weisenButton = new JButton("Weisen");
		verwerfenButton = new JButton("Verwerfen");
		buttonsPanel.add(weisenButton);
		buttonsPanel.add(verwerfenButton);
		
		cardsPanel = new JPanel();
		layoutCardsPanel();
		
		swipeLeftButton = new JButton("<");
		swipeLeftButton.setEnabled(false);
		swipeRightButton = new JButton(">");
		
		weisViewPanel.setLayout(new BorderLayout());
		weisViewPanel.add(cardsPanel,BorderLayout.CENTER);
		weisViewPanel.add(buttonsPanel, BorderLayout.SOUTH);
		if(weisList.length>1)
		{
			weisViewPanel.add(swipeLeftButton, BorderLayout.WEST);
			weisViewPanel.add(swipeRightButton, BorderLayout.EAST);
		}

		add(weisViewPanel);
		setSize(width,height);
		setLocation(left,top);
		
		swipeLeftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				swipeRightButton.setEnabled(true);
				index--;
				if(index>0)
				{
					swipeLeftButton.setEnabled(true);
				}
				else
				{
					swipeLeftButton.setEnabled(false);
				}
				layoutCardsPanel();
			}
		});
		swipeRightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				swipeLeftButton.setEnabled(true);
				index++;
				if(index<weisList.length-1)
				{
					swipeRightButton.setEnabled(true);
				}
				else
				{
					swipeRightButton.setEnabled(false);
				}
				layoutCardsPanel();
			}
		});
		
		weisenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnWeisActionChosen(true);
				}
			}
		});
		
		verwerfenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnWeisActionChosen(false);
				}
			}
		});
	}
	
	public void layoutCardsPanel()
	{	
		cardsPanel.setLayout(new GridLayout((getCardCount(weisList[index].getType())+3-1)/3,3,hgap,vgap));
		cardsPanel.removeAll();
		CardList cardList = getCardsFromWeis(weisList[index]);
		try{
			for(int i=0; i<cardList.size();i++)
			{
				cardsPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(cardList.get(i),new Dimension((int)(cardWidth*scale),(int)(cardHeight*scale))))));
			}
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cardsPanel.validate();
		this.repaint();
	}
	
	public int getCardCount(WeisType type)
	{
		switch(type)
		{
			case STOECK:
				return 2;
			case DREIBLATT:
				return 3;
			case VIERBLATT:
			case VIERGLEICHE:
			case VIERNELL:
			case VIERBAUERN:
				return 4;
			case FUENFBLATT:
				return 5;
			case SECHSBLATT:
				return 6;
			case SIEBENBLATT:
				return 7;
			case ACHTBLATT:
				return 8;
			case NEUNBLATT:
				return 9;
			default:
				return 0;
		}
	}
	
	public CardList getCardsFromWeis(Weis weis)
	{
		CardList cardList = new CardList();
		Card originCard = weis.getOriginCard();
		WeisType type = weis.getType();
		if(data.getTrump().getEntity()==TrumpEntity.UNEUFE)
		{
			//originCard is the lowest card 
			switch(type)
			{
				case DREIBLATT:
				case VIERBLATT:
				case FUENFBLATT:
				case SECHSBLATT:
				case SIEBENBLATT:
				case ACHTBLATT:
				case NEUNBLATT:
					for(int i = 0; i<getCardCount(weis.getType());i++)
					{
						cardList.add(new Card(originCard.getColor() , CardValue.getById(originCard.getValue().getId()+i) ));
					}
					break;
				case VIERGLEICHE:
				case VIERNELL:
				case VIERBAUERN:
					for(int i = 1; i<getCardCount(weis.getType())+1;i++)
					{
						cardList.add(new Card(CardColor.getById(i),originCard.getValue()));
					}
					break;
			}
			
		}
		else
		{
			//originCard is the highest card
			
			switch(type)
			{
				case STOECK:
				case DREIBLATT:
				case VIERBLATT:
				case FUENFBLATT:
				case SECHSBLATT:
				case SIEBENBLATT:
				case ACHTBLATT:
				case NEUNBLATT:
					for(int i = 0; i<getCardCount(type);i++)
					{
						cardList.add(new Card(originCard.getColor(), CardValue.getById(originCard.getValue().getId()-i) ));
					}
					break;
				case VIERGLEICHE:
				case VIERNELL:
				case VIERBAUERN:
					for(int i = 1; i<getCardCount(type)+1;i++)
					{
						cardList.add(new Card(CardColor.getById(i) , originCard.getValue() ));
					}
					break;
			}
		}
		
		return cardList;
	}

	@Override
	public JPanel getContent() {
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.WEISVIEW;
	}
	

}
