public class Queen extends Card {
	Queen(String suit, String rank, IGame iGame) {
		super(suit, rank, iGame);
	}

	@Override 
	void action() {
		iGame.showMessage("A queen is played! Skipped next player's turn.");
		iGame.skipTurn();
	}
}
