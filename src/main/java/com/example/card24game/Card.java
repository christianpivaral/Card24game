package com.example.card24game;

public class Card {

    private String rank;
    private String suit;
    private int value;

    // Makes a card with its rank, suit, and game value
    public Card(String rank, String suit, int value) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // Gets the matching image for the card
    public String getImagePath() {
        return "/cards/" + rank + "_of_" + suit + ".png";
    }
}