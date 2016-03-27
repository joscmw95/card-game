/**
 * The Class Queen.
 */
public class Queen extends Card {
	
	/**
	 * Instantiates a new queen.
	 *
	 * @param suit the suit
	 * @param rank the rank
	 * @param iGame the i game
	 */
	Queen(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	/* (non-Javadoc)
	 * @see Card#action()
	 */
	@Override 
	void action() {
		iGame.showMessage("A queen is played! Skipped next player's turn.");
		iGame.skipTurn();
	}
}
