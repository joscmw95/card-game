/**
 * The Interface IGame.
 */
interface IGame {
	
	/**
	 * Reverse the game direction.
	 */
	void reverse();
	
	/**
	 * Skip the current player.
	 */
	void skipTurn();
	
	/**
	 * Draw card for current player.
	 *
	 * @param trumpMode the trump mode
	 */
	void drawCard(boolean trumpMode);
	
	/**
	 * Gets the user selection.
	 *
	 */
	void getUserSelection();
	
	/**
	 * Adds a String into the game log ListView.
	 *
	 * @param message the message
	 */
	void showMessage(String message);
}
