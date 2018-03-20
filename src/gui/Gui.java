package gui;

import java.awt.List;
import java.util.ArrayList;

import client.AbstractClientView;
import client.ViewEnumeration;
import client.ViewObserver;
import gui.playingView.PlayingFieldView;
import shared.Card;
import shared.CardList;
import shared.Player;
import shared.Score;
import shared.client.ClientModel;

public class Gui extends AbstractClientView{
	public static PictureFactory pictureFactory = new PictureFactory();
	private ArrayList<ViewObserver> observers;
	private SelectHostView selectHostView;
	private PlayingFieldView playingFieldView;
	private TrumpView trumpView;
	private GameOverView gameOverView;
	private AbstractView currentView;
	
	
	public Gui(ClientModel data) {
		//Init fields
		super(data);
		this.observers = new ArrayList<>();
		this.selectHostView = new SelectHostView(ViewEnumeration.SELECTHOSTVIEW, observers);
		this.playingFieldView = new PlayingFieldView(ViewEnumeration.PLAYVIEW, observers, data);
		this.trumpView = new TrumpView(ViewEnumeration.SELECTTRUMPVIEW, observers);
		this.gameOverView = new GameOverView(ViewEnumeration.GAMEOVERVIEW, observers);
		
		//Init views
		this.playingFieldView.setVisible(true);
		this.currentView = this.playingFieldView;
		
		this.selectHostView.setVisible(false);
		this.trumpView.setVisible(false);
		this.gameOverView.setVisible(false);
	}

	@Override
	public void addObserver(ViewObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void changeView(ViewEnumeration view) {
		if(this.currentView.getViewType().equals(view)) {
			this.currentView.update();
		}
		switch(view) {
		case GAMEOVERVIEW:
			this.currentView.setVisible(false);
			this.currentView = this.gameOverView;
			this.currentView.setVisible(true);
			break;
		case PLAYVIEW:
			this.currentView.setVisible(false);
			this.currentView = this.playingFieldView;
			this.currentView.setVisible(true);
			
			break;
		case SELECTHOSTVIEW:
			this.currentView.setVisible(false);
			this.currentView = this.selectHostView;
			this.currentView.setVisible(true);
			
			break;
		case SELECTTRUMPVIEW:
			this.currentView.setVisible(false);
			this.currentView = this.trumpView;
			this.currentView.setVisible(true);
			
			break;
		default:
			break;
		
		}
	}

	@Override
	public ViewEnumeration getCurrentView() {
		return this.currentView.getViewType();
	}
}
