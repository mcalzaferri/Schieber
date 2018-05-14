package gui.playingView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.Gui;
import gui.ObservableView;
import gui.PictureFactory.Pictures;
import shared.Player;
import shared.Team;
import shared.client.ClientModel;

/**
 * This class represents a blackboard and displays the score of all
 * teams and the rounds current trump.
 * 
 * @author mstieger
 *
 */
public class BlackBoardPane extends ObservableView{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7805022564116303797L;
	private BlackBoardDrawer drawer;
	private BufferedImage img;
	
	public static final Font font = new Font("MV Boli" ,Font.PLAIN, 28);
	public static final Dimension minSize = new Dimension(350, 500);
	public static final Dimension minTrumpSize = new Dimension(50,70);
	public static final Rectangle minInnerBounds = new Rectangle(50, 50, minSize.width - 100, minSize.height -100);
	
	public BlackBoardPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super(data, observers);
		if(data == null) {
			throw new IllegalArgumentException("Fatal Error: Card must not be null");
		}
		this.drawer = new BlackBoardDrawer(25);
		try {
			this.img = Gui.pictureFactory.getPicture(Pictures.BlackBoard);
		} catch (IOException e) {
			// Fatal error => Should not happen
			e.printStackTrace();
		}
		setOpaque(false);
		this.setMinimumSize(minSize);
	}
		
	Rectangle r = new Rectangle();
	Dimension dim = new Dimension();
	Font f = font;
	@Override
	public void paint(Graphics g) {
		//Calculating scale factor and bounds
		double scale = this.getSize().getHeight()/minSize.getHeight();
		r.setRect(scale*minInnerBounds.x, scale*minInnerBounds.y,
				scale*minInnerBounds.width, scale*minInnerBounds.height);
		dim.setSize(scale*minTrumpSize.width, scale*minTrumpSize.height);
		
		//Getting scores for teams
		int myScore = 0;
		int enemyScore = 0;
		if(data.getScore() != null && data.getTeams() != null) {
			Team myTeam = Team.getTeamThatContainsPlayer(data.getTeams().values(), data.getThisPlayer());
			for(Team team : data.getTeams().values()) {
				if(myTeam == null)
					myTeam = team;
				if(myTeam == team) {
					myScore = data.getScore().getScore(team);
				}else {
					enemyScore = data.getScore().getScore(team);
				}
			}
		}
		
		//Draw content on blackboard
		g.setColor(Color.WHITE);
		g.setFont(new Font(font.getFontName(), font.getStyle(), (int)(scale*font.getSize())));
		g.drawImage(Gui.pictureFactory.getScaledPicture(img, this.getSize()), 0, 0, null);
		
		drawer.setBounds(r);
		drawer.drawTitle(g, "Schieber");
		drawer.drawText(g, new String[]{"Wir;" + myScore,
				"Gegner;" + enemyScore});
		drawer.drawTrump(g, data.getTrump(), dim);
	}
}
