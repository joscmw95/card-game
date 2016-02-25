import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card implements Comparable<Card>{
	protected IGame iGame;
	protected ImageView imageView = new ImageView();;
	protected String suit;
	protected String rank;
	
	// Constructor
	Card(String suit, String rank, IGame iGame) {
		String fileName = "resources/" + (rank + "_of_" + suit + ".png").toLowerCase();
		this.suit = suit;
		this.rank = rank;
		this.iGame = iGame;
		this.imageView.setImage(new Image(getClass().getResource(fileName).toExternalForm()));
		this.imageView.setFitWidth(150);
		this.imageView.setPreserveRatio(true);
	}
	
	private static final Map<String, Integer> cardValue;
    static
    {
    	cardValue = new HashMap<String, Integer>();
    	// suits
    	cardValue.put("Clubs", 1);
    	cardValue.put("Diamonds", 2);
    	cardValue.put("Hearts", 3);
    	cardValue.put("Spades", 4);
    	// ranks
    	cardValue.put("Ace", 1);
    	cardValue.put("2", 2);
    	cardValue.put("3", 3);
    	cardValue.put("4", 4);
    	cardValue.put("5", 5);
    	cardValue.put("6", 6);
    	cardValue.put("7", 7);
    	cardValue.put("8", 8);
    	cardValue.put("9", 9);
    	cardValue.put("10", 10);
    	cardValue.put("Jack", 11);
    	cardValue.put("Queen", 12);
    	cardValue.put("King", 13);
    }
    
    private static final Map<String, Integer> cardScore;
    static
    {
    	cardScore = new HashMap<String, Integer>();
    	// ace
    	cardScore.put("Ace", 20);
    	// numeric cards
    	cardScore.put("2", 2);
    	cardScore.put("3", 3);
    	cardScore.put("4", 4);
    	cardScore.put("5", 5);
    	cardScore.put("6", 6);
    	cardScore.put("7", 7);
    	cardScore.put("8", 8);
    	cardScore.put("9", 9);
    	cardScore.put("10", 10);
    	// face cards
    	cardScore.put("Jack", 10);
    	cardScore.put("Queen", 10);
    	cardScore.put("King", 10);
    }
    
    boolean match(Card card) {
    	return this.rank.equals(card.rank) || this.suit.equals(card.suit);
    }
    
    int getScore() {
    	return cardScore.get(rank);
    }
    
	void action() {
	}
	
	void setIGame(IGame iGame) {
		this.iGame = iGame;
	}
	
	@Override
	public String toString() {
		return this.rank + " of " + this.suit;
	}
	
	@Override
	public int compareTo(Card card) {
		if (this.suit.equals(card.suit)) {
			return cardValue.get(this.rank) - cardValue.get(card.rank);
		} else {
			return cardValue.get(this.suit) - cardValue.get(card.suit);
		}
	}
}
