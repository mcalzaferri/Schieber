package gui;

import java.util.ArrayList;

import client.ViewEnumeration;
import client.ViewObserver;
import client.shared.*;

import shared.*;

import javax.swing.*;

import ch.ntb.jass.common.entities.TrumpEntity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class WeisView extends ObservableView implements Viewable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6918985557295103342L;
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
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(width,height));
	}

	@Override
	public void update() {
		weisList = data.getPossibleWiis();	//Get new data
		if(weisList != null) {
			//Only display cards if list contains cards
			layoutWeisView();
		}
		if(!isValid()) {
			validate();
		}
		repaint();
	}
	
	public void layoutWeisView()
	{
		if(weisViewPanel != null) {
			remove(weisViewPanel);
		}

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
		swipeRightButton.setEnabled(false);
		
		weisViewPanel.setLayout(new BorderLayout());
		weisViewPanel.add(cardsPanel,BorderLayout.CENTER);
		weisViewPanel.add(buttonsPanel, BorderLayout.SOUTH);
		if(weisList.length>1)
		{
			weisViewPanel.add(swipeLeftButton, BorderLayout.WEST);
			weisViewPanel.add(swipeRightButton, BorderLayout.EAST);
			swipeRightButton.setEnabled(true);
		}

		add(weisViewPanel, BorderLayout.CENTER);	//This overwrites the current component in the center
		setLocation(left,top);
		
		swipeLeftButton.addActionListener(new ActionListener() {
			@Override
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
			@Override
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
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnWeisActionChosen(true);
				}
			}
		});
		
		verwerfenButton.addActionListener(new ActionListener() {
			@Override
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
		int columnCount= (getCardCount(weisList[index].getType())+3-1)/3;
		int rowCount = 3;
		Dimension cardDimension = new Dimension((int)(cardWidth*scale),(int)(cardHeight*scale));
		Dimension cardsPanelDimension = new Dimension((int)(columnCount*(cardDimension.getWidth()+hgap)),(int)(rowCount*(cardDimension.getHeight()+vgap)));
		
		cardsPanel.setPreferredSize(cardsPanelDimension);
		cardsPanel.setLayout(new GridLayout((getCardCount(weisList[index].getType())+3-1)/3,3,hgap,vgap));
		cardsPanel.removeAll();
		CardList cardList = getCardsFromWeis(weisList[index]);
		try{
			for(int i=0; i<cardList.size();i++)
			{
				cardsPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(cardList.get(i),cardDimension))));
			}
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!isValid()) {
			validate();
		}
		repaint();//Needed because after klicking a button the whole view has to be repainted
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
			default:
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
