package gui;

import java.awt.List;
import java.util.ArrayList;

import client.AbstractClientView;
import client.ViewEnumeration;
import client.ViewObserver;
import shared.Card;
import shared.CardList;
import shared.Player;
import shared.Score;
import shared.client.ClientModel;

public class Gui extends AbstractClientView{
	private ArrayList<ViewObserver> observers;
	private SelectHostView selectHostView;
	private PlayingFieldView playingFieldView;
	private TrumpView trumpView;
	private GameOverView gameOverView;
	
	
	public Gui(ClientModel data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addObserver(ViewObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeView(ViewEnumeration view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ViewEnumeration getCurrentView() {
		// TODO Auto-generated method stub
		return null;
	}
}
