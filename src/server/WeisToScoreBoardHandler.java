package server;

import ch.ntb.jass.common.entities.WeisEntity;
import java.util.*;
import shared.*;

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
		
		if(highestWeisCounts()){						
			weisWinningTeamId = getWinningTeamId();
		} else {
			if(amountOfCardsCounts()){
				weisWinningTeamId = getWinningTeamId();
			} else {
				if(trumpCounts()){
					weisWinningTeamId = getWinningTeamId();
				} else {
					Map.Entry<Player,Weis> entry = highestWeise.entrySet().iterator().next();
					weisWinningPlayer = entry.getKey();
					weisWinningTeamId = getWinningTeamId();
				}				
			}
		} 
	}

	/**
	 * Checks for the highest Value of each Weis and removes tho lower ones.
	 *  @return	true when one player has the Weis with the highest value.
	 */
	private boolean highestWeisCounts(){
		boolean weisCounts = false;	
		boolean isFirstRound = true;
		LinkedHashMap<Player, Weis> selectedHighestWeise = new LinkedHashMap<>();;
		Map.Entry<Player, Weis> comparableWeis = null;	
		
		//Iterate over highestWeise and remove lower Weise.
		for(Iterator<Map.Entry<Player, Weis>> it = highestWeise.entrySet().iterator(); it.hasNext();){
			Map.Entry<Player, Weis> entry = it.next();
			if(comparableWeis == null){
				comparableWeis = entry;				
			}
			else if(entry.getValue().compareTo(comparableWeis.getValue(), trump) > 0){
				selectedHighestWeise.put(entry.getKey(), entry.getValue());
				comparableWeis = entry;
				weisWinningPlayer = entry.getKey();
				isFirstRound = false;
			}
			else if(entry.getValue().compareTo(comparableWeis.getValue(), trump) < 0){
				selectedHighestWeise.put(comparableWeis.getKey(), comparableWeis.getValue());
				weisWinningPlayer = comparableWeis.getKey();
				isFirstRound = false;
			}
			else if(entry.getValue().compareTo(comparableWeis.getValue(), trump) == 0) {
				
				if(isFirstRound) {
					selectedHighestWeise.put(comparableWeis.getKey(), comparableWeis.getValue());
				}
				isFirstRound = false;
				selectedHighestWeise.put(entry.getKey(), entry.getValue());
				comparableWeis = null;
			}
		}				
		highestWeise = selectedHighestWeise;		
		if(highestWeise.size() > 1) {
			weisCounts = false;
		}
		else {
			weisCounts = true;
		}		
		return weisCounts;
	}

	/**
	 * Checks for the highest amount of Cards.
	 * @return 	true when one player has the highest number of cards.
	 */
	private boolean amountOfCardsCounts(){
		boolean amountCounts = false;
		int lastCardAmount = 0;
		
		for(Map.Entry<Player, Weis> entry : highestWeise.entrySet()){
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
	
	/**
	 * Checks for a Weis with trump.
	 * @return	true when one player has a Weis of Trump.
	 */
	private boolean trumpCounts() {
		boolean trumpCounts = false;		
		
		Map.Entry<Player, Weis> comparableWeis = null;
		
		for(Map.Entry<Player, Weis> entry : highestWeise.entrySet()){
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
	
	/**
	 * @return 	0 Error
	 * 			1 for Team 1
	 * 			2 for Team 2
	 */
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
	
	/**
	 * Sets the given Weis score to the given players seat.
	 * @param currentEntry
	 * @param currentWeis
	 */
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
	
	/**
	 * @return 	1 for Team 1
	 * 			2 for Team 2
	 */
	public int getTeamId(){
		return weisWinningTeamId;
	}
	
	/**
	 * @return The winning team score from the correct team.
	 */
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
