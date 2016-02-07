import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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

public class Game extends Application implements IGame {
	final ObservableList<String> gameLogs = FXCollections.observableArrayList();
	final ListView<String> logList = new ListView<String>(gameLogs);
	final ImageView cardBack = new ImageView();
	final StackPane pilePane = new StackPane();
	final BorderPane playTable = new BorderPane();
	final HBox mainLayout = new HBox();
	final Label gameLogLabel = new Label("Game Log:\n\n");
	final Label playerLabel = new Label();
	final Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
	final Alert errorDialog = new Alert(AlertType.INFORMATION);
	Stage gameStage = new Stage();
	Player[] players;
	Player currentPlayer;
	List<Card> stock = new ArrayList<Card>();
	List<Card> pile = new ArrayList<Card>();
	Card pileTop;
	int playerIndex=0, counter=0, playerNo;
	boolean clockwiseDirection = true;
	static final String[] suits = {"Diamonds", "Clubs", "Hearts", "Spades"};
	static final String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

	public static void main(String[] args) {
		// launch application
		launch(args);
	}
	
	void shuffle() {
		if (pile.size() > 0) {
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
	
	void startGame(int numberOfPlayers, Color backgroundColor) {
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
				// attach onClick listener to card
				card.imageView.setOnMouseClicked(e -> UserSelectionDialog(card));
			}
		}
		
		currentPlayer = players[playerIndex];
		
		// get one card from stock to start the discard pile
		pile.add(stock.remove(stock.size()-1));
		pileTop = pile.get(0);
		
		// initialize UI components
        StackPane stockPane = new StackPane();
        stockPane.getChildren().add(cardBack);
        
        pilePane.getChildren().add(pileTop.imageView);
        
        HBox center = new HBox(3);
        center.setCenterShape(true);
        center.getChildren().addAll(stockPane, pilePane);
        center.setStyle("-fx-alignment: center");
        
        HBox top = new HBox();
        top.getChildren().add(playerLabel);
        
        logList.setPrefHeight(900);
        VBox gameLogBox = new VBox();
        gameLogBox.setPrefWidth(400);
        gameLogBox.getChildren().addAll(gameLogLabel, logList);
        gameLogBox.setStyle("-fx-padding: 10;");
        
        playTable.setTop(top);
        playTable.setCenter(center);
        playTable.setBottom(currentPlayer.handGUI);
        playTable.setPrefWidth(950);
        playTable.setStyle("-fx-padding: 25");
        
        mainLayout.getChildren().addAll(playTable, gameLogBox);
        mainLayout.setStyle("-fx-alignment: center");
        mainLayout.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        mainLayout.setPrefHeight(1000);
        mainLayout.setPrefWidth(1420);
		
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
	
	void nextPlayer() {
		if (clockwiseDirection) {
			playerIndex = (playerIndex+1)%playerNo;
		} else {
			playerIndex--;
			if (playerIndex < 0) playerIndex = playerNo-1;
		}
		currentPlayer = players[playerIndex];
	}
	
	void end() {
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
	
	void UserSelectionDialog(Card playerCard) {
		confirmationDialog.setTitle("Confirmation Dialog");
		confirmationDialog.setHeaderText("You are about to play the " + playerCard + ".");
		confirmationDialog.setContentText("Confirm playing this card?");
		confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
		Optional<ButtonType> result = confirmationDialog.showAndWait();
		if (result.get() == ButtonType.YES){
			if (pileTop.match(playerCard)) {
				showMessage("Player " + (playerIndex+1) + " discarded " + playerCard);
				currentPlayer.playCard(playerCard);
				pile.add(playerCard);
				pileTop = pile.get(pile.size()-1);
				pilePane.getChildren().add(pileTop.imageView);
				
				// end game if player's hand is empty
				if (currentPlayer.hand.isEmpty()) end();
				else {
					nextPlayer();
					getUserSelection();
				}
			}
			else {
				errorDialog.setTitle("Message");
				errorDialog.setHeaderText("Cannot play this card.");
				errorDialog.setContentText("Your card does not match the rank and suit.");
				errorDialog.showAndWait();
			}
		}
	}
		
	@Override
	public void reverse() {
		if (clockwiseDirection == true) {
			clockwiseDirection = false;
		} else {
			clockwiseDirection = true;
		}
	}

	@Override
	public void skipTurn() {
		nextPlayer();
	}

	@Override
	public void getUserSelection() {
		showMessage("It is Player " + (playerIndex+1) + "'s turn.");
		playerLabel.setText("Player " + (playerIndex+1));
		if (currentPlayer.isPlayable(pileTop)) {
			counter=0;
			playTable.setBottom(currentPlayer.handGUI);
		} else {
			counter++;
			if (currentPlayer.hand.size() < 5) {
				Card drawCard = stock.remove(stock.size()-1);
				showMessage("Player " + (playerIndex+1) + " draws a card.");
				currentPlayer.addCard(drawCard);
				drawCard.imageView.setOnMouseClicked(e -> UserSelectionDialog(drawCard));
				if (stock.isEmpty()) {
					showMessage("Stock pile is exhausted, shuffling discard pile..");
					shuffle();
				}
			}
			playTable.setBottom(currentPlayer.handGUI);
			showMessage("Player " + (playerIndex+1) + " passes the round.");
			if (counter == playerNo) end();
			else {
				nextPlayer();
				getUserSelection();
			}
		}
	}

	@Override
	public void showMessage(String message) {
		gameLogs.add(message);
	}

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
		colorPicker.setValue(Color.MAROON);
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
		innerLayout1.setStyle("-fx-alignment: center-left;");
		innerLayout2.getChildren().addAll(labelColor, colorPicker);
		innerLayout2.setStyle("-fx-alignment: center-left;");
		innerLayout3.getChildren().add(buttonStart);
		innerLayout3.setStyle("-fx-alignment: center-left;");

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
