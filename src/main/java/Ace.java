import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ChoiceDialog;

public class Ace extends Card {
	Ace(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	// By default, nominate original suit
	private String nominatedSuit = this.suit;
	
	public String getNominatedSuit() {
		return nominatedSuit;
	}

	public void setNominatedSuit(String nominatedSuit) {
		this.nominatedSuit = nominatedSuit;
	}

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
	
	@Override
	boolean match(Card card) {
    	return this.rank.equals(card.rank) || this.nominatedSuit.equals(card.suit);
    }
}
