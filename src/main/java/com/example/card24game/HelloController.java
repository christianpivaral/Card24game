package com.example.card24game;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Controls the Card 24 game and handles the user interactions
public class HelloController {

    @FXML
    private ImageView card1;

    @FXML
    private ImageView card2;

    @FXML
    private ImageView card3;

    @FXML
    private ImageView card4;

    @FXML
    private TextField expressionField;

    // Stores the full deck and the four cards currently being displayed
    private ArrayList<Card> deck;
    private ArrayList<Card> currentCards;

    // Sets up the deck and displays the first four cards when the game starts
    @FXML
    public void initialize() {
        createDeck();
        generateCards();
    }

    // Creates all 52 cards and gives each rank its Card 24 value
    private void createDeck() {
        deck = new ArrayList<>();

        String[] suits = {"clubs", "diamonds", "hearts", "spades"};

        String[] ranks = {
                "ace", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "jack", "queen", "king"
        };

        // The array position gives Ace a value of 1 through King at 13
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                int value = i + 1;
                deck.add(new Card(ranks[i], suit, value));
            }
        }
    }

    // Shuffles the deck and selects four random cards
    private void generateCards() {
        Collections.shuffle(deck);

        currentCards = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            currentCards.add(deck.get(i));
        }

        displayCards();
    }

    // Displays the images for the four currently selected cards
    private void displayCards() {
        ImageView[] cardViews = {card1, card2, card3, card4};

        for (int i = 0; i < currentCards.size(); i++) {
            Card card = currentCards.get(i);

            Image image = new Image(
                    getClass().getResourceAsStream(card.getImagePath())
            );

            cardViews[i].setImage(image);
        }
    }

    // Checks the player's expression when the Verify button is clicked
    @FXML
    private void verifyExpression() {
        String expression = expressionField.getText();

        // Prevents an empty expression from being submitted
        if (expression == null || expression.trim().isEmpty()) {
            showAlert("Invalid Expression", "Please enter an expression.");
            return;
        }

        // Only allows numbers and the math symbols supported by the game
        if (!expression.matches("[0-9+\\-*/()\\s]+")) {
            showAlert(
                    "Invalid Expression",
                    "Only numbers, +, -, *, /, and parentheses are allowed."
            );
            return;
        }

        // Makes sure the player used all four displayed card values exactly once
        if (!usesCorrectCards(expression)) {
            showAlert(
                    "Invalid Expression",
                    "You must use all four card values exactly once."
            );
            return;
        }

        try {
            Evaluator evaluator = new Evaluator();
            double result = evaluator.evaluate(expression);

            // A small tolerance is used because division can create decimal values
            if (Math.abs(result - 24.0) < 0.000001) {
                showAlert("Correct!", "Your expression equals 24!");
            } else {
                showAlert(
                        "Incorrect",
                        "Your expression equals " + formatResult(result) + ", not 24."
                );
            }

        } catch (ArithmeticException e) {
            showAlert("Invalid Expression", e.getMessage());

        } catch (Exception e) {
            showAlert("Invalid Expression", "The expression is not valid.");
        }
    }

    // Compares the numbers entered by the player with the four card values
    private boolean usesCorrectCards(String expression) {
        ArrayList<Integer> cardValues = new ArrayList<>();

        for (Card card : currentCards) {
            cardValues.add(card.getValue());
        }

        ArrayList<Integer> enteredValues = new ArrayList<>();

        // Finds every complete number in the expression
        Matcher matcher = Pattern.compile("\\d+").matcher(expression);

        while (matcher.find()) {
            enteredValues.add(Integer.parseInt(matcher.group()));
        }

        // Sorting lets both lists be compared even if the cards were used
        // in a different order in the expression
        Collections.sort(cardValues);
        Collections.sort(enteredValues);

        return cardValues.equals(enteredValues);
    }

    // Displays whole-number results without an unnecessary .0
    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        }

        return String.valueOf(result);
    }

    // Generates four new cards and clears the previous expression
    @FXML
    private void refreshCards() {
        generateCards();
        expressionField.clear();
    }

    // Displays a dialog box with a message for the player
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}