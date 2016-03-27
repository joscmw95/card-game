/**
 * The Class Eight.
 */
public class Eight extends Card {
	
	/**
	 * Instantiates a new eight.
	 *
	 * @param suit the suit
	 * @param rank the rank
	 * @param iGame the i game
	 */
	Eight(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	/* (non-Javadoc)
	 * @see Card#action()
	 */
	@Override 
	void action() {
		iGame.showMessage("An Eight is played! The direction of play is reversed.");
		iGame.reverse();
	}
}
