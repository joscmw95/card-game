/**
 * The Class King.
 */
public class King extends Card {
	
	/**
	 * Instantiates a new king.
	 *
	 * @param suit the suit
	 * @param rank the rank
	 * @param iGame the i game
	 */
	King(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	/* (non-Javadoc)
	 * @see Card#action()
	 */
	@Override 
	void action() {
		iGame.showMessage("A King is played! The next player is forced to draw a card.");
		iGame.drawCard(true);
	}
}
