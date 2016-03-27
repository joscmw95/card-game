import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The Class Card.
 */
public class Card implements Comparable<Card>{
	
	/** The reference to IGame. */
	protected IGame iGame;
	
	/** The image view. */
	protected ImageView imageView = new ImageView();;
	
	/** The suit. */
	protected String suit;
	
	/** The rank. */
	protected String rank;
	
	/** The Constant cardValue. */
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
    
    /** The Constant cardScore. */
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

	/**
	 * Instantiates a new card.
	 *
	 * @param suit the suit
	 * @param rank the rank
	 * @param iGame the i game
	 */
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
	
    /**
     * Matches the card with the parameter. Returns true if has equal rank or suit, false otherwise.
     *
     * @param card the card to be matched with
     * @return true, if cards matches
     */
    boolean match(Card card) {
    	return this.rank.equals(card.rank) || this.suit.equals(card.suit);
    }
    
    /**
     * Gets the score of the card.
     *
     * @return the score
     */
    int getScore() {
    	return cardScore.get(rank);
    }
    
	/**
	 * Calls the action of the card.
	 */
	void action() {
	}
	
	/**
	 * Sets the reference to IGame.
	 *
	 * @param iGame the new IGame
	 */
	void setIGame(IGame iGame) {
		this.iGame = iGame;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.rank + " of " + this.suit;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Card card) {
		if (this.suit.equals(card.suit)) {
			return cardValue.get(this.rank) - cardValue.get(card.rank);
		} else {
			return cardValue.get(this.suit) - cardValue.get(card.suit);
		}
	}
}
