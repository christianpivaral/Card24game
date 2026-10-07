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

    private ArrayList<Card> deck;
    private ArrayList<Card> currentCards;

    @FXML
    public void initialize() {
        createDeck();
        generateCards();
    }

    // Makes the deck of 52 cards
    private void createDeck() {
        deck = new ArrayList<>();

        String[] suits = {"clubs", "diamonds", "hearts", "spades"};

        String[] ranks = {
                "ace", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "jack", "queen", "king"
        };

        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                int value = i + 1;
                deck.add(new Card(ranks[i], suit, value));
            }
        }
    }

    // Picks 4 random cards
    private void generateCards() {
        Collections.shuffle(deck);

        currentCards = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            currentCards.add(deck.get(i));
        }

        displayCards();
    }

    // Shows the card images
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

    // Checks the expression when Verify is clicked
    @FXML
    private void verifyExpression() {
        String expression = expressionField.getText();

        if (expression == null || expression.trim().isEmpty()) {
            showAlert("Invalid Expression", "Please enter an expression.");
            return;
        }

        // Only allows the math symbols used in the game
        if (!expression.matches("[0-9+\\-*/()\\s]+")) {
            showAlert(
                    "Invalid Expression",
                    "Only numbers, +, -, *, /, and parentheses are allowed."
            );
            return;
        }

        // Makes sure all 4 card values are used only once
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

    // Checks if the user used the same numbers as the cards
    private boolean usesCorrectCards(String expression) {
        ArrayList<Integer> cardValues = new ArrayList<>();

        for (Card card : currentCards) {
            cardValues.add(card.getValue());
        }

        ArrayList<Integer> enteredValues = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\d+").matcher(expression);

        while (matcher.find()) {
            enteredValues.add(Integer.parseInt(matcher.group()));
        }

        Collections.sort(cardValues);
        Collections.sort(enteredValues);

        return cardValues.equals(enteredValues);
    }

    // Makes whole numbers display without .0
    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        }

        return String.valueOf(result);
    }

    // Gets 4 new cards and clears the text field
    @FXML
    private void refreshCards() {
        generateCards();
        expressionField.clear();
    }

    // Shows a message to the user
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}