package com.example.card24game;

// Represents one playing card used in the Card 24 game
public class Card {

    private String rank;
    private String suit;
    private int value;

    // Creates a card with its rank, suit, and game value
    public Card(String rank, String suit, int value) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    // Returns the card's rank
    public String getRank() {
        return rank;
    }

    // Returns the card's suit
    public String getSuit() {
        return suit;
    }

    // Returns the number value used for the Card 24 game
    public int getValue() {
        return value;
    }

    // Returns the path to the matching card image
    public String getImagePath() {
        return "/cards/" + rank + "_of_" + suit + ".png";
    }
}