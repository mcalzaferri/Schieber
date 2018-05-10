package server;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import ch.ntb.jass.common.entities.WeisEntity;
import shared.Player;
import shared.Seat;
import shared.Trump;
import shared.Weis;

public class WeisToScoreBoardHandler {

	private LinkedHashMap<Player, WeisEntity[]> declaredWeise;
	private LinkedHashMap<Player, Weis> highestWeise;
	private Player weisWinningPlayer;
	private Trump trump;
	private int weisWinningTeamId;
	private int team1Score;
	private int team2Score;
		
	public WeisToScoreBoardHandler(LinkedHashMap<Player, WeisEntity[]> declaredWeise, Trump trump){
		this.declaredWeise = declaredWeise;
		this.trump = trump;
		highestWeise = new LinkedHashMap<>();
		weisWinningPlayer = null;
	}
	
	/**
	 * This method decides which team gets the Weis-Score.
	 */
	public void execute(){
		
		// Iterate over each Weis on Table.
		for(Map.Entry<Player, WeisEntity[]> entry : declaredWeise.entrySet()){
						
			for(WeisEntity weisEntity : entry.getValue()){

				Weis reviewedWeis = new Weis(weisEntity);
				// Check for highest Weis of each Player.
				if(highestWeise.containsKey(entry.getKey())){
					
					int originCardId = highestWeise.get(entry.getKey()).getOriginCard().getValue().getId();
					if( originCardId < reviewedWeis.getOriginCard().getValue().getId() ){
						highestWeise.replace(entry.getKey(), reviewedWeis);
					}					
				} else {
					highestWeise.put(entry.getKey(), reviewedWeis);
				}
				setTeamScores(entry, reviewedWeis);								
			}
		}
		
		if(highestWeisCounts(highestWeise)){						
			weisWinningTeamId = getWinningTeamId();
		} else {
			if(amountOfCardsCounts(highestWeise)){
				weisWinningTeamId = getWinningTeamId();
			} else {
				if(trumpCounts(highestWeise)){
					weisWinningTeamId = getWinningTeamId();
				} else {
					Map.Entry<Player,Weis> entry = highestWeise.entrySet().iterator().next();
					weisWinningPlayer = entry.getKey();
					weisWinningTeamId = getWinningTeamId();
				}				
			}
		} 
	}
	
	private boolean highestWeisCounts(Map<Player, Weis> currentWeise){
		boolean weisCounts = false;		
		int highestCardId = 0;		

		for(Map.Entry<Player, Weis> entry : currentWeise.entrySet()){
			int currentOriginCardId = entry.getValue().getOriginCard().getValue().getId();			
			if(currentOriginCardId == highestCardId){
				weisCounts = false;
			}				
			else if(currentOriginCardId > highestCardId){
				highestCardId = currentOriginCardId;
				weisCounts = true;
				weisWinningPlayer = entry.getKey();				
			}
		}
		removeLowerWeise();
		
		return weisCounts;
	}
	
	/**
	 * Remove every Weis form Map except the highest of each Player.
	 */
	private void removeLowerWeise() {		
		Map.Entry<Player, Weis> comparableWeis = null;	
		for(Iterator<Map.Entry<Player, Weis>> it = highestWeise.entrySet().iterator(); it.hasNext();){
			Map.Entry<Player, Weis> entry = it.next();
			if(comparableWeis == null){
				comparableWeis = entry;				
			}
			else if(entry.getValue().compareTo(comparableWeis.getValue(), trump) > 0){
				highestWeise.remove(comparableWeis);
				comparableWeis = entry;
			}
			else if(entry.getValue().compareTo(comparableWeis.getValue(), trump) < 0){
				it.remove();
			}
		}
	}

	private boolean amountOfCardsCounts(Map<Player, Weis> currentWeise){
		boolean amountCounts = false;
		int lastCardAmount = 0;
		
		for(Map.Entry<Player, Weis> entry : currentWeise.entrySet()){
			int currentCardAmount = 0;
			
			switch(entry.getValue().getType()) {
			case STOECK:
				currentCardAmount = 3;
				break;
			case DREIBLATT:
				currentCardAmount = 3;
				break;
			case VIERBLATT:
				currentCardAmount = 4;
				break;
			case VIERGLEICHE:
				currentCardAmount = 4;
				break;
			case VIERNELL:
				currentCardAmount = 4;
				break;
			case VIERBAUERN:
				currentCardAmount = 4;
				break;
			case FUENFBLATT:
				currentCardAmount = 5;
				break;
			case SECHSBLATT:
				currentCardAmount = 6;
				break;
			case SIEBENBLATT:
				currentCardAmount = 7;
				break;
			case ACHTBLATT:
				currentCardAmount = 8;
				break;
			case NEUNBLATT:				
				currentCardAmount = 9;
				break;
			}
			if(currentCardAmount == lastCardAmount){
				amountCounts = false;
			}
			if(currentCardAmount > lastCardAmount){
				lastCardAmount = currentCardAmount;
				weisWinningPlayer = entry.getKey();
				amountCounts = true;
			}			
		}		
		return amountCounts;
	}
	
	private boolean trumpCounts(Map<Player, Weis> currentWeise) {
		boolean trumpCounts = false;		
		
		Map.Entry<Player, Weis> comparableWeis = null;
		
		for(Map.Entry<Player, Weis> entry : currentWeise.entrySet()){
			if(comparableWeis == null){
				comparableWeis = entry;				
			}
			else if(entry.getValue().getOriginCard().getColor().equals(trump.getTrumpfColor()) 
					&& !comparableWeis.getValue().getOriginCard().getColor().equals(trump.getTrumpfColor())){
				comparableWeis = entry;
				weisWinningPlayer = entry.getKey();
				trumpCounts = true;
				
			} else {
				trumpCounts = false;
			}
		}
		return trumpCounts;
	}
	
	private int getWinningTeamId() {
		if(weisWinningPlayer.getSeat() == Seat.SEAT1 || 
				weisWinningPlayer.getSeat() == Seat.SEAT3){
			return  1;
		}
		else if(weisWinningPlayer.getSeat() == Seat.SEAT2 || 
				weisWinningPlayer.getSeat() == Seat.SEAT4){
			return  2;								
		}
		else {
			System.err.println("Unimplemented Seat");
			return 0;
		}
	}
	
	private void setTeamScores(Map.Entry<Player, WeisEntity[]> currentEntry, Weis currentWeis){
		switch(currentEntry.getKey().getSeat()){
		case SEAT1:
		case SEAT3:
			team1Score += currentWeis.getType().getScore();
			break;
		case SEAT2:
		case SEAT4:	
			team2Score += currentWeis.getType().getScore();	
			break;
		default:
			System.err.println("Unimplemented Seat");
		}
	}
	
	public int getTeamId(){
		return weisWinningTeamId;
	}
	
	public int getWeisScore(){
		if(weisWinningTeamId == 1){
			return team1Score;
		}
		else if(weisWinningTeamId == 2){
			return team2Score;
		} else {
			System.err.println("Unimplemented TeamId");
			return 0;
		}
	}
}
