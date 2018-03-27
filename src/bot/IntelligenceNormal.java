package bot;

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

	@Override
	public Card getNextCard() {
		// TODO implement (for now just something useful is returned to make test class work)
		return new Card(CardColor.EICHEL, CardValue.SECHS);
	}

	@Override
	public Trump selectTrump(boolean canSwitch) {
		int[] values = this.getValueOnHand();
		int bestTrumpID = 3, maxValue = values[3];
		
		int[] sichereStich = {getSichereStich(CardColor.EICHEL),
								getSichereStich(CardColor.ROSE),
								getSichereStich(CardColor.SCHILTE),
								getSichereStich(CardColor.SCHELLE),
								getSichereStichUndenufeObenabe(false),
								getSichereStichUndenufeObenabe(true)
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
				combinedValues[i] = sichereStich[i]*values[i];
			} else {
				combinedValues[i] = sichereStich[i]*obenabeUndenufeMultiplicator;
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
	public int getSichereStichUndenufeObenabe(boolean undenufe) {
		int[] ids = cardsToIds(cardsInHand);
		int sichereStich = 0;
		Arrays.sort(ids);
		
		int noOfCards = 1;
		boolean last = false;
		if(undenufe) {
			for(int i = 1; i<10; i++) {
				if(i==9) {
					last = true;	// final round for checking
				}
				if(!last && (ids[i]-ids[i-1])==1) { //consecutive cards, suppress check of non-existent index
					noOfCards++;
				} else {
					Card card;
					card = Card.getCardById(ids[i-noOfCards]);
					if(card.getValue()==CardValue.SECHS) {
						sichereStich += noOfCards;
					}
					noOfCards = 1;
				}
		}
		} else {
			for(int i = 1; i<10; i++) {
				if(i==9) {
					last = true;	// final round for checking
				}
				if(!last && (ids[i]-ids[i-1])==1) { //consecutive cards, suppress check of non-existent index
					noOfCards++;
				} else {
					Card card;
					card = Card.getCardById(ids[i-1]);
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
	public int getSichereStich(CardColor color) {
		int[] ids = cardsToIds(cardsInHand);
		int sichereStich = 0;
		
		//assign new values
		for(int i = 0; i<9; i++) {
			switch(ids[i]%10) {
			case 0:
			case 1:
			case 2:
				break;
			case 3:
				ids[i] += 4;
				break;
			case 4:
				ids[i] -= 1;
				break;
			case 5:
				ids[i] += 3;
				break;
			case 6:
			case 7:
			case 8:
				ids[i] -= 2;
			default:
			}
		}
		
		Arrays.sort(ids);
		boolean last = false;
		int noOfCards = 1;
		for(int i = 1; i<10; i++) {
			if(i==9) {
				last = true;	// final round for checking
			}
			if(!last && (ids[i]-ids[i-1])==1) { //consecutive cards, suppress check of non-existent index
				noOfCards++;
			} else {
				Card card;
				card = Card.getCardById(ids[i-1]);
				if(card.getValue()==CardValue.ASS && card.getColor().equals(color)) {
					sichereStich += noOfCards;
				}
				noOfCards = 1;
			}
		}
		return sichereStich;
		
	}

}
