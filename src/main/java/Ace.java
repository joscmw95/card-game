import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ChoiceDialog;

/**
 * The Class Ace.
 */
public class Ace extends Card {
	
	/**
	 * Instantiates a new ace.
	 *
	 * @param suit the suit
	 * @param rank the rank
	 * @param iGame the i game
	 */
	Ace(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	/** The nominated suit. */
	// By default, nominate original suit
	private String nominatedSuit = this.suit;
	
	/**
	 * Gets the nominated suit.
	 *
	 * @return the nominated suit
	 */
	public String getNominatedSuit() {
		return nominatedSuit;
	}

	/**
	 * Sets the nominated suit.
	 *
	 * @param nominatedSuit the new nominated suit
	 */
	public void setNominatedSuit(String nominatedSuit) {
		this.nominatedSuit = nominatedSuit;
	}

	/* (non-Javadoc)
	 * @see Card#action()
	 */
	@Override 
	void action() {
		final String [] arrayData = {"Clubs", "Diamonds", "Hearts", "Spades"};
		List<String> dialogData;

		dialogData = Arrays.asList(arrayData);
		 
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(dialogData.get(0), dialogData);
		dialog.setTitle("Message");
		dialog.setHeaderText("Select suit to nominate");
		dialog.setContentText("Cancel for default suit: " + this.suit);
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			this.nominatedSuit = result.get();
		}
		iGame.showMessage("An Ace is played! The next card played should follow the nominated suit.");
		iGame.showMessage(this.nominatedSuit + " is nominated.");
	}
	
	/* (non-Javadoc)
	 * @see Card#match(Card)
	 */
	@Override
	boolean match(Card card) {
    	return this.rank.equals(card.rank) || this.nominatedSuit.equals(card.suit);
    }
}
