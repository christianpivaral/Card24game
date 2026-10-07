package com.example.card24game;

// Evaluates the arithmetic expression entered by the player
public class Evaluator {

    private String expression;
    private int position;

    // Starts evaluating the expression and returns the final result
    public double evaluate(String expression) {
        this.expression = expression.replaceAll("\\s+", "");
        this.position = 0;

        double result = parseExpression();

        // Makes sure the entire expression was read
        if (position != this.expression.length()) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return result;
    }

    // Handles addition and subtraction
    private double parseExpression() {
        double result = parseTerm();

        while (position < expression.length()) {
            char operator = expression.charAt(position);

            if (operator == '+') {
                position++;
                result += parseTerm();

            } else if (operator == '-') {
                position++;
                result -= parseTerm();

            } else {
                break;
            }
        }

        return result;
    }

    // Handles multiplication and division
    private double parseTerm() {
        double result = parseFactor();

        while (position < expression.length()) {
            char operator = expression.charAt(position);

            if (operator == '*') {
                position++;
                result *= parseFactor();

            } else if (operator == '/') {
                position++;

                double divisor = parseFactor();

                // Stops the player from dividing by zero
                if (divisor == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }

                result /= divisor;

            } else {
                break;
            }
        }

        return result;
    }

    // Handles numbers and expressions inside parentheses
    private double parseFactor() {
        if (position >= expression.length()) {
            throw new IllegalArgumentException("Invalid expression");
        }

        if (expression.charAt(position) == '(') {
            position++;

            double result = parseExpression();

            // Makes sure an opening parenthesis has a closing parenthesis
            if (position >= expression.length()
                    || expression.charAt(position) != ')') {
                throw new IllegalArgumentException("Missing parenthesis");
            }

            position++;
            return result;
        }

        return parseNumber();
    }

    // Reads the next number from the expression
    private double parseNumber() {
        int start = position;

        while (position < expression.length()
                && Character.isDigit(expression.charAt(position))) {
            position++;
        }

        // No number was found where one was expected
        if (start == position) {
            throw new IllegalArgumentException("Expected a number");
        }

        return Double.parseDouble(
                expression.substring(start, position)
        );
    }
}