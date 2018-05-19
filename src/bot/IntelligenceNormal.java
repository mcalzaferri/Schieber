package bot;

import java.util.ArrayList;
import java.util.Arrays;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.GameMode;
import shared.Trump;
import shared.Weis;
import shared.WeisType;

public class IntelligenceNormal extends BotIntelligence {
	
	private final int awesomeTrumpSelectThreshhold = 70; //equals a bit more than 75% of maximum value (90)
	private final int goodTrumpSelectThresshold = 45; // equals 50%
	private final int obenabeUndenufeMultiplicator = 25; // used for combined Value
	private ArrayList<Card> winningCards = new ArrayList<>();
	
	@Override
	public Card getNextCard() {
		
		ArrayList<Card> allowedCards = getAllowedCards();
		
		if (allowedCards.isEmpty())
		{
			//TODO FKaiser Errorhandling - what to do?
			System.out.println("Error - AllowedCards is empty");
		}
		
		// check if trumps are out to make sure bot doesn't play it unnecessarily
		int trumpsOnHand = getAmountOfTrumpCards(cardsInHand);
		boolean noMoreTrump = ((trumpsOnHand + trumpCardsPlayed) == 9);
		
		if(noMoreTrump) {
			enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1] = true;
			enemyRightOutOfColor[trump.getTrumpfColor().getId()-1] = true;
			partnerOutOfColor[trump.getTrumpfColor().getId()-1] = true;
		}
		
		winningCards = this.getSicherenStichDuringPlay(allowedCards);
			
		if(deck.isEmpty()) { //I'm first to go
			
			//austrumpfen if we made trumpf and enemy still has them
			if(trumpfGemacht && (trump.getTrumpfColor() != null) && !(enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1] && enemyRightOutOfColor[trump.getTrumpfColor().getId()-1])) {
				for(Card c : allowedCards) {
					if(c.isBuur(trump)) {
						return c;
					}
				}
				for(Card c : allowedCards) {
					if(c.getColor() == trump.getTrumpfColor()) {
						return c;
					}
				}
			}
			
			for(Card wc : winningCards) {
				if(wc.getColor() == trump.getTrumpfColor() && enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1] && enemyRightOutOfColor[trump.getTrumpfColor().getId()-1]) {
					// Don't play trump card if enemy has none
				} else {
					return wc;
				}
			}
			
			// check if partner has a winning card and play a card of this colour if possible
			for(Card pc : this.getSicherenStichDuringPlay(partnerCards)) {
				for(Card oc : allowedCards) {
					if(pc.getColor() == oc.getColor()) {
						if(pc.getColor() == trump.getTrumpfColor() && enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1] && enemyRightOutOfColor[trump.getTrumpfColor().getId()-1]) {
							// Don't play trump card if enemy has none
						} else {
							return oc;
						}
					}
				}
			}
			
			// play color owned by partner but not by enemies (except Trump)
			for(Card oc : allowedCards) {
				if(oc.getColor() == trump.getTrumpfColor()) {
					// don't do "austrumpfen"
				} else {
					int ocID = oc.getColor().getId()-1;
					if(!partnerOutOfColor[ocID] && enemyLeftOutOfColor[ocID] && enemyRightOutOfColor[ocID]) {
						return oc;
					}
				}
			}
			
			//play color owned by partner and not owned by one enemy (50% chance), make sure the enemy doesn't have the highest
			for(Card oc : allowedCards) {
				int ocID = oc.getColor().getId()-1;
				if(!partnerOutOfColor[ocID] && (enemyLeftOutOfColor[ocID] || enemyRightOutOfColor[ocID])) {
					for(Card ec : this.getSicherenStichDuringPlay(enemyCards)) {
						if(ec.getColor() != oc.getColor()) {
							return oc;
						}
					}
				}
			}
			
			// try not to play a card that the enemy surely wins, but don't trumpf
			for(Card ec : this.getSicherenStichDuringPlay(enemyCards)) {
				for(Card oc : allowedCards) {
					if(ec.getColor() != oc.getColor()) {
						if(oc.getColor() == trump.getTrumpfColor()) {
							// don't do "austrumpfen"
						} else {
							return oc;
						}
					}
				}
			}
			
		} else if(deck.size() == 1) { // I'm second to go
			
			Card firstCard = deck.get(0);
			
			//play a highscoring card if partner has the best card
			for(Card pc : this.getSicherenStichDuringPlay(partnerCards)) {
				if(pc.getColor() == firstCard.getColor()) {
					for(Card oc : allowedCards) {
						if((oc.getColor() != trump.getTrumpfColor()) && (getScore(oc) >= 8)) {
							return oc;
						}
					}
				}
			}
			
			// Stich with Farbe if possible and enemy out of this color
			if(enemyLeftOutOfColor[firstCard.getColor().getId()-1]) { 
				for(Card c : allowedCards) {
					if(c.getColor() == firstCard.getColor() && getValue(c) > getValue(firstCard)) {
						return c;
					}
				}
			}
			
			// Stich with Farbe if possible and enemy out of Trumpf
			if(trump.getTrumpfColor() != null) {
				if(enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1]) { 
					for(Card wc : winningCards) {
						if(wc.getColor() == firstCard.getColor()) {
							return wc;
						}
					}
				}
			}
			
			int score = getScore(firstCard);
			//stich if high scoring card (with Trumpf)
			if(score >= 8) {
				for(Card wc : winningCards) {
					return wc;
				}
			}
			
		} else if(deck.size() == 2) { // I'm third to go (partner started)
			
			// if partner starts in first round - we made trumpf
			if(cardsInHand.size() == 9) {
				trumpfGemacht = true;
			}
			
			Card partnerCard = deck.get(0); // also first card
			Card enemyRightCard = deck.get(1);
			
			int partnerScore = getScore(partnerCard);
			int enemyRightScore = getScore(enemyRightCard);
			
			int partnerValue = getValue(partnerCard);
			int enemyRightValue;
			
			if((enemyRightCard.getColor() == partnerCard.getColor()) || (enemyRightCard.getColor() == trump.getTrumpfColor())) {
				enemyRightValue = getValue(enemyRightCard);
			} else {
				enemyRightValue = 0;
			}
			
			if(enemyRightValue > partnerValue) { // enemy has higher card

				if(enemyRightCard.getColor() == partnerCard.getColor()) { // enemy didn't play trumpf (or trumpf is current round color)
					
					// Stich with Farbe if possible and enemy out of this color
					if(enemyLeftOutOfColor[partnerCard.getColor().getId()-1]) { 
						for(Card c : allowedCards) {
							if(c.getColor() == partnerCard.getColor() && getValue(c) > getValue(partnerCard)) {
								return c;
							}
						}
					}
					
					// Stich with Farbe if possible and enemy out of Trumpf
					if(trump.getTrumpfColor() != null) {
						if(enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1]) { 
							for(Card wc : winningCards) {
								if(wc.getColor() == partnerCard.getColor()) {
									return wc;
								}
							}
						}
					}
					
				} else { // enemy played Trumpf
					
					// left enemy out of trumpf -> stich if good 
					if(enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1]) {
						for(Card c : allowedCards) {
							if((getValue(c) > enemyRightValue)) {
								// good score, play low trumpf
								if(partnerScore + enemyRightScore >= 10) {
									if(getScore(c) <= 10) {
										return c;
									}
								}
								
								// awesome score, play any Trumpf
								if(partnerScore + enemyRightScore >= 20) {
									return c;
								}
							}
						}
					}
				}
				
			} else {
			
				// partner has max card
				if(partnerCard.equals(maxCardsInPlay[partnerCard.getColor().getId()-1])) {
					if(partnerCard.getColor() == trump.getTrumpfColor() || trump.getTrumpfColor() == null || enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1]) {
						for(Card c : allowedCards) {
							if(getScore(c) == 8 || getScore(c) == 10) {
								if(partnerCard.getColor() == trump.getTrumpfColor()) {
									return c;
								} else if(c.getColor() != trump.getTrumpfColor()) { // don't schmier Trumpf
									return c;
								}
							}
						}
					}
				}

				// partner has higher card than enemy and other enemy is out of color
				if(enemyLeftOutOfColor[partnerCard.getColor().getId()-1]) {
					if(trump.getTrumpfColor() != null && enemyLeftOutOfColor[trump.getTrumpfColor().getId()-1]) {
						for(Card c : allowedCards) {
							if(getScore(c) == 8 || getScore(c) == 10) {
								if(partnerCard.getColor() == trump.getTrumpfColor()) {
									return c;
								} else if(c.getColor() != trump.getTrumpfColor()) { // don't schmier Trumpf
									return c;
								}
							}
						}
					}
				}
			}
			
		} else if(deck.size() == 3) { // last to go

			Card enemyLeftCard = deck.get(0); // is also first card
			Card partnerCard = deck.get(1);
			Card enemyRightCard = deck.get(2);
			
			int enemyLeftScore = getScore(enemyLeftCard);
			int partnerScore = getScore(partnerCard);
			int enemyRightScore = getScore(enemyRightCard);
			
			int enemyLeftValue = getValue(enemyLeftCard);
			int partnerValue;
			int enemyRightValue;
			
			// check if partner and enemycards match the first played color or trumpf
			if((partnerCard.getColor() == enemyLeftCard.getColor()) || (partnerCard.getColor() == trump.getTrumpfColor())) {
				partnerValue = getValue(partnerCard);
			} else {
				partnerValue = 0;
			}
			
			if((enemyRightCard.getColor() == enemyLeftCard.getColor()) || (enemyRightCard.getColor() == trump.getTrumpfColor())) {
				enemyRightValue = getValue(enemyRightCard);
			} else {
				enemyRightValue = 0;
			}
			
			// partner is winning, try to schmier a 10 (or an 8 in Obenabe/undenufe)
			if((partnerValue > enemyLeftValue) && (partnerValue > enemyRightValue)) {
				for(Card c : allowedCards) {
					if(getScore(c) == 8 || getScore(c) == 10) {
						if(enemyLeftCard.getColor() == trump.getTrumpfColor()) {
							return c;
						} else if(c.getColor() != trump.getTrumpfColor()) { // don't schmier Trumpf
							return c;
						}
					}
				}
			}
			
			// I can win with color
			for(Card c : allowedCards) {
				if(c.getColor() == enemyLeftCard.getColor()) {
					if((getValue(c) > enemyLeftValue) && (getValue(c) > enemyRightValue)) {
						return c;
					}
				}
			}
			
			// I can win with a Trumpf (and the score is good)
			if(enemyLeftCard.getColor() != trump.getTrumpfColor()) {
				for(Card c : allowedCards) {
					if((getValue(c) > enemyLeftValue) && (getValue(c) > enemyRightValue)) {
						// good score, play low trumpf
						if(enemyLeftScore + partnerScore + enemyRightScore >= 10) {
							if(getScore(c) <= 10) {
								return c;
							}
						}
						
						// awesome score, play any Trumpf
						if(enemyLeftScore + partnerScore + enemyRightScore >= 20) {
							return c;
						}
					}
				}
			}
			
		}
		
		// 1st fallback, apparently no good card on hand, don't play a scoring card & don't play Trumpf
		for(Card c : allowedCards) {
			if(getScore(c) == 0 && c.getColor() != trump.getTrumpfColor()) {
				return c;
			}
		}
		
		// 2nd fallback, play a low scoring card (not Trumpf)
		for(Card c : allowedCards) {
			if(getScore(c) < 8 && c.getColor() != trump.getTrumpfColor()) {
				return c;
			}
		}
		
		// emergency fallback, play first allowed card on hand
		return allowedCards.get(0);
	}

	@Override
	public Trump selectTrump(boolean canSwitch) {
		int[] values = this.getValueOnHand();
		int bestTrumpID = 3, maxValue = values[3];
		
		int[] sichereStich = {getSichereStichBeginning(CardColor.EICHEL),
								getSichereStichBeginning(CardColor.ROSE),
								getSichereStichBeginning(CardColor.SCHILTE),
								getSichereStichBeginning(CardColor.SCHELLE),
								getSichereStichUndenufeObenabeBeginning(false),
								getSichereStichUndenufeObenabeBeginning(true)
		};
		int bestStichID = 5, maxStichValue = sichereStich[5];
		
		// find highest values for most Stich and it's corresponding color/mode
		// it's done in reverse direction because in case of equality the higher scoring gamemodes should be chosen
		for(int i = 4; i>=0; i--) {
			if(sichereStich[i]>maxStichValue) {
				maxStichValue = sichereStich[i];
				bestStichID = i;
			}
		}
		
		// 8 or 9 sichere Stich - go for it
		if(maxStichValue > 7) {
			switch(bestStichID) {
			case 0:
				return Trump.EICHEL;
			case 1: 
				return Trump.ROSE;
			case 2:
				return Trump.SCHILTE;
			case 3:
				return Trump.SCHELLE;
			case 4:
				return Trump.OBENABE;
			case 5:
				return Trump.UNEUFE;
			default:		// should not happen
				return null;
			}
		}
		
		// find highest value for any color and it's ID
		for(int i = 2; i >= 0; i--) {
			if(values[i]>maxValue) {
				maxValue = values[i];
				bestTrumpID = i;
			}
		}
		
		//awesome Trump -> go for it
		if(maxValue>awesomeTrumpSelectThreshhold) { 
			switch(bestTrumpID) {
			case 0:
				return Trump.EICHEL;
			case 1: 
				return Trump.ROSE;
			case 2:
				return Trump.SCHILTE;
			case 3:
				return Trump.SCHELLE;
			default:		// should not happen
				return null;
			} 
		} 
		
		
		// 5 or more sichere Stich - go for it
		if(maxStichValue > 4) {
			switch(bestStichID) {
			case 0:
				return Trump.EICHEL;
			case 1: 
				return Trump.ROSE;
			case 2:
				return Trump.SCHILTE;
			case 3:
				return Trump.SCHELLE;
			case 4:
				return Trump.OBENABE;
			case 5:
				return Trump.UNEUFE;
			default:		// should not happen
				return null;
			}
		}
		
		//still somewhat good Trump
		if(maxValue>goodTrumpSelectThresshold) {
			switch(bestTrumpID) {
			case 0:
				return Trump.EICHEL;
			case 1: 
				return Trump.ROSE;
			case 2:
				return Trump.SCHILTE;
			case 3:
				return Trump.SCHELLE;
			default:		// should not happen
				return null;
			} 	
		}
		
		//hand not that good, try to Schieben
		if(canSwitch) {
			return Trump.SCHIEBEN;
		}
		
		// here comes the emergency selection
		// use a combined value of sichere Stich and Value
		int[] combinedValues = new int[6];
		for(int i = 0; i<6; i++) {
			if(i<4) {
				combinedValues[i] = (sichereStich[i]+1)*values[i]; // even with 0 sichere stich the color can be good
			} else {
				combinedValues[i] = (sichereStich[i])*obenabeUndenufeMultiplicator;
			}
		}
		int maxCombinedValue = combinedValues[5];
		int bestCombinedID = 5;
		// find highest value for any color and it's ID
		for(int i = 4; i >= 0; i--) {
			if(combinedValues[i]>maxCombinedValue) {
				maxCombinedValue = combinedValues[i];
				bestCombinedID = i;
			}
		}
		
		switch(bestCombinedID) {
		case 0:
			return Trump.EICHEL;
		case 1: 
			return Trump.ROSE;
		case 2:
			return Trump.SCHILTE;
		case 3:
			return Trump.SCHELLE;
		case 4:
			return Trump.OBENABE;
		case 5:
			return Trump.UNEUFE;
		default:		// should not happen
			return null;
		}
		
		
	}
	
	/**
	 * This function returns the amount of sichere Stich for Obenabe and Undenufe games
	 * @param true if undenufe
	 * @return amount of sichere stich
	 */
	public int getSichereStichUndenufeObenabeBeginning(boolean undenufe) {
		int[] ids = cardsToIds(cardsInHand);
		int sichereStich = 0;
		Arrays.sort(ids);
		
		int noOfCards = 1;
		boolean last = false;
		if(undenufe) {
			for(int i = 1; i<ids.length+1; i++) {
				if(i==ids.length) {
					last = true;	// final round for checking
				}
				if(!last && (ids[i]-ids[i-1])==1) { //consecutive cards, suppress check of non-existent index
					noOfCards++;
				} else {
					Card card;
					card = new Card(ids[i-noOfCards]);
					if(card.getValue()==CardValue.SECHS) {
						sichereStich += noOfCards;
					}
					noOfCards = 1;
				}
		}
		} else {
			for(int i = 1; i<ids.length+1; i++) {
				if(i==ids.length) {
					last = true;	// final round for checking
				}
				if(!last && (ids[i]-ids[i-1])==1) { //consecutive cards, suppress check of non-existent index
					noOfCards++;
				} else {
					Card card;
					card = new Card(ids[i-1]);
					if(card.getValue()==CardValue.ASS) {
						sichereStich += noOfCards;
					}
					noOfCards = 1;
				}
			}
		}
		
		return sichereStich;
			
	}
	

	/**
	 * This function returns the amount of sichere Stich for a certain Trumpf color
	 * @param color
	 * @return sichere stich if this color is chosen as Trumpf
	 */
	public int getSichereStichBeginning(CardColor color) {
		int[] ids = cardsToIds(cardsInHand);
		int sichereStich = 0;
		
		//assign new values
		for(int i = 0; i<ids.length; i++) {
			switch(ids[i]%10) {
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				ids[i] += 4;
				break;
			case 5:
				ids[i] -= 1;
				break;
			case 6:
				ids[i] += 3;
				break;
			case 7:
			case 8:
			case 9:
				ids[i] -= 2;
			default:
			}
		}
		
		Arrays.sort(ids);
		boolean last = false;
		int noOfCards = 1;
		for(int i = 1; i<ids.length+1; i++) {
			if(i==9) {
				last = true;	// final round for checking
			}
			if(!last && (ids[i]-ids[i-1])==1) { //consecutive cards, suppress check of non-existent index
				noOfCards++;
			} else {
				Card card;
				card = new Card(ids[i-1]);
				if(card.getValue()==CardValue.ASS && card.getColor().equals(color)) {
					sichereStich += noOfCards;
				}
				noOfCards = 1;
			}
		}
		return sichereStich;
	}
	
	/**
	 * This function returns a card that is guaranteed to win
	 * @param allowedCards
	 * @return sicherer Stich, can return null!
	 */
	public ArrayList<Card> getSicherenStichDuringPlay(ArrayList<Card> allowedCards) {
		ArrayList<Card> winningCards = new ArrayList<>();
		for(Card c : allowedCards) {
			if(trump.getGameMode() == GameMode.TRUMPF) {
				if(c.equals(maxCardsInPlay[4])) {
					winningCards.add(c);
				}
			} else {
				if(c.equals(maxCardsInPlay[c.getColor().getId()-1])) {
					winningCards.add(c);
				}
			}
		}
		return winningCards;
	}
	
	/**
	 * This function returns the score of a card (depending on the current Gamemode/Trump
	 * @param card
	 * @return score
	 */
	public int getScore(Card c) {
		int score = 0;
		switch(trump.getGameMode()) {
		case TRUMPF:
			if(trump.getTrumpfColor() == c.getColor()) {
				score = c.getValue().getTrumpScore();
			} else {
				score = c.getValue().getGeneralScore();
			}
			break;
		case OBENABE:
			score = c.getValue().getObenabeScore();
			break;
		case UNEUFE:
			score = c.getValue().getUneufeScore();
			break;
		default:
			break;
		}
		return score;
	}
	
	/**
	 * This function returns the value of a card (depending on the current Gamemode/Trump
	 * @param card
	 * @return value
	 */
	public int getValue(Card c) {
		int value = 0;
		switch(trump.getGameMode()) {
		case TRUMPF:
			if(trump.getTrumpfColor() == c.getColor()) {
				value = c.getValue().getTrumpValue() + 10; // make Trump more valueable than other colors
			} else {
				value = c.getValue().getDefaultValue();
			}
			break;
		case OBENABE:
			value = c.getValue().getDefaultValue();
			break;
		case UNEUFE:
			value = 16 - c.getValue().getDefaultValue(); // invert value for UNEUFE
			break;
		default:
			break;
		}
		return value;
	}
	
}
