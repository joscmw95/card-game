import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * The Class Game.
 */
public class Game extends Application implements IGame {
	
	/** The Constant suits. */
	private static final String[] suits = {"Diamonds", "Clubs", "Hearts", "Spades"};
	
	/** The Constant ranks. */
	private static final String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
	
	/** The game logs. */
	final ObservableList<String> gameLogs = FXCollections.observableArrayList();
	
	/** The log list. */
	final ListView<String> logList = new ListView<String>(gameLogs);
	
	/** The ImageView of the back of a card. */
	final ImageView cardBack = new ImageView();
	
	/** The layout */
	final Stage gameStage = new Stage();
	final StackPane pilePane = new StackPane();
	final BorderPane playTable = new BorderPane();
	final HBox mainLayout = new HBox();
	
	/** The game log label. */
	final Label gameLogLabel = new Label("Game Log:\n\n");
	
	/** The player label. */
	final Label playerLabel = new Label();
	
	/** The confirmation dialog. */
	final Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
	
	/** The error dialog. */
	final Alert errorDialog = new Alert(AlertType.INFORMATION);
	
	/** The stock. */
	final List<Card> stock = new ArrayList<Card>();
	
	/** The pile. */
	final List<Card> pile = new ArrayList<Card>();
	
	/** The reference to MouseGestures. */
	final MouseGestures mg = new MouseGestures(this);
	
	/** The array of Players. */
	Player[] players;
	
	/** The current player. */
	Player currentPlayer;
	
	/** The card at the top of the pile. */
	Card pileTop;
	
	/** The current player no. */
	int playerIndex=0;
	
	/** The counter to keep track of number of passes. */
	int counter=0;
	
	/** The number of players. */
	int playerNo;
	
	/** The boolean that states whether the play is in clockwise direction. */
	boolean clockwiseDirection = true;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// launch application
		launch(args);
	}
	
	/**
	 * Starts the card game, setup number of players and background color of game stage according to the parameters.
	 *
	 * @param numberOfPlayers the number of players
	 * @param backgroundColor the background color
	 */
	public void startGame(int numberOfPlayers, Color backgroundColor) {
		playerNo = numberOfPlayers;
		// load all cards into a list
	    shuffle();
	    
	    players = new Player[playerNo];
		// for each player
		for(int i=0; i<playerNo; i++) {
			players[i] = new Player();
			// add 5 cards
			for(int j=0; j<5; j++) {
				Card card = stock.remove(stock.size()-1);
				players[i].addCard(card);
				// attach mouse events on card
				mg.makeDraggable(card);
			}
		}
		// initialize first player
		currentPlayer = players[playerIndex];
		
		// get one card from stock to start the discard pile
		pile.add(stock.remove(stock.size()-1));
		pileTop = pile.get(0);
		
		// defining UI components
	    StackPane stockPane = new StackPane();
	    stockPane.getChildren().add(cardBack);
	    
	    pilePane.getChildren().add(pileTop.imageView);
	    
	    HBox center = new HBox(3);
	    center.getChildren().addAll(stockPane, pilePane);
	    center.setAlignment(Pos.CENTER);
	    
	    HBox top = new HBox();
	    top.getChildren().add(playerLabel);
	    
	    logList.setPrefHeight(900);
	    VBox gameLogBox = new VBox();
	    gameLogBox.setPrefWidth(435);
	    gameLogBox.getChildren().addAll(gameLogLabel, logList);
	    gameLogBox.setStyle("-fx-padding: 10;");
	    
	    playTable.setTop(top);
	    playTable.setCenter(center);
	    playTable.setBottom(currentPlayer.getHandGUI());
	    playTable.setPrefWidth(950);
	    playTable.setStyle("-fx-padding: 25");
	    
	    mainLayout.getChildren().addAll(playTable, gameLogBox);
	    mainLayout.setAlignment(Pos.CENTER);
	    mainLayout.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
	    mainLayout.setPrefHeight(1000);
	    mainLayout.setPrefWidth(1425);
		
	    playerLabel.setTextFill(backgroundColor.invert());
	    gameLogLabel.setTextFill(backgroundColor.invert());
	    
	    showMessage("The game has started.");
	    Scene scene = new Scene(mainLayout);
	    gameStage.setTitle("The Card Game");
		gameStage.setScene(scene);
		gameStage.setMaximized(true);
		gameStage.show();
		getUserSelection();
	}

	/**
	 * Initializes and shuffles the array list of the Card object using Collections.shuffle when its empty, 
	 * subsequent calls to the function puts cards from discard pile (except top) into stock and shuffle. 
	 */
	public void shuffle() {
		if (pile.size() > 0) {
			// stock pile exhausted, put all cards from discard pile except top into stock and shuffle
			for(int i=pile.size()-2; i>=0; i--) {
				stock.add(pile.remove(i));
			}
			Collections.shuffle(stock);
		} else {
			// game just started, generate cards
			for (int i = 0; i < 52; i++) {
				String suit = suits[i/13];
				String rank = ranks[i%13];
				switch (rank) {
					case "Ace":
						stock.add(new Ace(suit, rank, this));
						break;
					case "Queen":
						stock.add(new Queen(suit, rank, this));
						break;
					case "King":
						stock.add(new King(suit, rank, this));
						break;
					case "8":
						stock.add(new Eight(suit, rank, this));
						break;
					default:
						stock.add(new Card(suit, rank, this));
						break;
				}
			}
			// shuffle
			Collections.shuffle(stock);
		}
	}
	
	/**
	 * Moves playerIndex to next player, increases the index with game direction is clockwise else decreases.
	 */
	public void nextPlayer() {
		if (clockwiseDirection) {
			playerIndex = (playerIndex+1)%playerNo;
		} else {
			playerIndex--;
			if (playerIndex < 0) playerIndex = playerNo-1;
		}
		currentPlayer = players[playerIndex];
	}
	
	/**
	 * Ends the game. Calculates total points of all players and display final result.
	 */
	public void endGame() {
		String dialogString = "";
		int min = players[0].getTotalPoints();
		for(int i=1; i<playerNo; i++) {
			if (min > players[i].getTotalPoints()) {
				min = players[i].getTotalPoints();
			}
		}
		for(int i=0; i<playerNo; i++) {
			if (players[i].getTotalPoints() == min) {
				showMessage("Player " + (i+1) + " wins with " + players[i].getTotalPoints() + " points!");
				dialogString += "Player " + (i+1) + " wins with " + players[i].getTotalPoints() + " points!\n";
			} else {
				showMessage("Player " + (i+1) + " lost with " + players[i].getTotalPoints() + " points!");
				dialogString += "Player " + (i+1) + " lost with " + players[i].getTotalPoints() + " points!\n";
			}
		}
		
		confirmationDialog.setTitle("Message");
		confirmationDialog.setHeaderText(dialogString);
		confirmationDialog.setContentText("Are you up for another game?");
		confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = confirmationDialog.showAndWait();
		if (result.get() == ButtonType.YES){
			Game game = new Game();
			try {
				game.start(new Stage());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		gameStage.close();
	}
    
	/* (non-Javadoc)
	 * @see IGame#reverse()
	 */
	@Override
	public void reverse() {
		if (clockwiseDirection == true) {
			clockwiseDirection = false;
		} else {
			clockwiseDirection = true;
		}
	}

	/* (non-Javadoc)
	 * @see IGame#skipTurn()
	 */
	@Override
	public void skipTurn() {
		// check if hand is empty before skipping
		if (currentPlayer.getHand().isEmpty()) endGame();
		nextPlayer();
	}

	/* (non-Javadoc)
	 * @see IGame#drawCard(boolean)
	 */
	@Override
	public void drawCard(boolean trumpMode) {
		Player playerTemp = null;
		int playerIndexTemp = 0;
		if (trumpMode) {
			playerTemp = currentPlayer;
			playerIndexTemp = playerIndex;
			nextPlayer();
		}
		// draw card if hand has less than 5 cards
		if (currentPlayer.getHand().size() < 5) {
			counter=0;
			Card drawCard = stock.remove(stock.size()-1);
			showMessage("Player " + (playerIndex+1) + " draws a card.");
			currentPlayer.addCard(drawCard);
			// attach mouse event to card
			mg.makeDraggable(drawCard);
			// check if stock pile is exhausted
			if (stock.isEmpty()) {
				showMessage("Stock pile is exhausted, shuffling discard pile..");
				shuffle();
			}
		} else {
			if (trumpMode) {
				showMessage("Player " + (playerIndex+1) + " couldn't draw any more cards.");
			} else {
				counter++;
				showMessage("Player " + (playerIndex+1) + " passes the round.");
			}
		}
		if (trumpMode) {
			currentPlayer = playerTemp;
			playerIndex = playerIndexTemp;
		}
	}

	/* (non-Javadoc)
	 * @see IGame#getUserSelection()
	 */
	@Override
	public void getUserSelection() {
		showMessage("It is Player " + (playerIndex+1) + "'s turn.");
		playerLabel.setText("Player " + (playerIndex+1));
		playTable.setBottom(currentPlayer.getHandGUI());
		if (currentPlayer.isPlayable(pileTop)) {
			// reset pass counter to 0
			counter=0;
		} else {
			// call drawCard with trumpMode=false, trumpMode=true is used when a King is played.
			drawCard(false);
			if (counter == playerNo) endGame();
			else {
				nextPlayer();
				getUserSelection();
			}
		}
	}

	/* (non-Javadoc)
	 * @see IGame#showMessage(java.lang.String)
	 */
	@Override
	public void showMessage(String message) {
		gameLogs.add(message);
		logList.scrollTo(gameLogs.size()-1);
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage startStage) throws Exception {
		// initialization
		cardBack.setImage(new Image(getClass().getResource("resources/back.png").toExternalForm()));
		cardBack.setFitWidth(151);
		cardBack.setPreserveRatio(true);
		playerLabel.setStyle("-fx-font: 30 arial;");
		gameLogLabel.setStyle("-fx-font: 20 arial");
				
		startStage.setTitle("Welcome to The Card Game");
		
		// defining UI items
		ChoiceBox<Integer> choiceBox = new ChoiceBox<Integer>();
		Label labelPlayerNo = new Label("Number of Players:");
		labelPlayerNo.setStyle("-fx-font: 30 Arial; -fx-text-fill: white");
		choiceBox.getItems().addAll(2, 3, 4);
		choiceBox.setValue(2);
		choiceBox.setStyle("-fx-font: 20 Arial");
		Label labelColor = new Label("Background Color:");
		labelColor.setStyle("-fx-font: 30 Arial; -fx-text-fill: white");
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(Color.DARKSLATEGREY);
		Button buttonStart = new Button("START GAME");
		buttonStart.setOnAction(e -> {
			startGame(choiceBox.getValue(), colorPicker.getValue());
			startStage.close();
		});
		
		// defining UI layouts
		VBox outerLayout = new VBox(50);
		HBox innerLayout1 = new HBox(30);
		HBox innerLayout2 = new HBox(30);
		HBox innerLayout3 = new HBox(30);
		innerLayout1.getChildren().addAll(labelPlayerNo, choiceBox);
		innerLayout1.setAlignment(Pos.CENTER_LEFT);
		innerLayout2.getChildren().addAll(labelColor, colorPicker);
		innerLayout2.setAlignment(Pos.CENTER_LEFT);
		innerLayout3.getChildren().add(buttonStart);
		innerLayout3.setAlignment(Pos.CENTER_LEFT);

		outerLayout.getChildren().addAll(innerLayout1, innerLayout2, innerLayout3);
		outerLayout.setPrefHeight(1000);
		outerLayout.setPrefWidth(1420);
		outerLayout.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResource("resources/background.jpg").toExternalForm()), 
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		outerLayout.setStyle("-fx-font: 20 Arial; -fx-alignment: center; -fx-padding: 100;");
		
		Scene scene = new Scene(outerLayout);
		startStage.setScene(scene);
		startStage.setMaximized(true);
		startStage.show();
	}
}
