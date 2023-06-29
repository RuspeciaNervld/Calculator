package com.calculator.models;

import com.calculator.CalculatorModel;

import java.math.BigDecimal;

class Number implements Expression {
    private final double value;

    public Number(double value) {
        this.value = value;
    }

    public double evaluate() {
        return value;
    }
}

class BinaryOperation implements Expression {
    private final char operator;
    private final Expression leftOperand;
    private final Expression rightOperand;

    public BinaryOperation(char operator, Expression leftOperand, Expression rightOperand) {
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public double evaluate() {
        switch (operator) {
            case '+' -> {
                return leftOperand.evaluate() + rightOperand.evaluate();
            }
            case '-' -> {
                return leftOperand.evaluate() - rightOperand.evaluate();
            }
            case '×' -> {
                return leftOperand.evaluate() * rightOperand.evaluate();
            }
            case '÷' -> {
                double divisor = rightOperand.evaluate();
                if (divisor == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return leftOperand.evaluate() / divisor;
            }
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}

public class CalculatorParserModel implements CalculatorModel {
    boolean isError = false;
    String errorMessage = "";
    private String expression = "";
    private int position = 0;

    public String getResultString(String expression) {
        this.expression = expression;
        this.position = 0;
        Expression parsedExpression = parseExpression();
        BigDecimal bd = new BigDecimal(Double.toString(parsedExpression.evaluate()));

        return String.valueOf(bd.stripTrailingZeros());
    }

    private Expression parseExpression() {
        Expression leftOperand = parseTerm();

        while (position < expression.length()) {
            char operator = expression.charAt(position);
            if (operator == '+' || operator == '-') {
                position++;
                Expression rightOperand = parseTerm();
                leftOperand = new BinaryOperation(operator, leftOperand, rightOperand);
            } else {
                break;
            }
        }

        return leftOperand;
    }

    private Expression parseTerm() {
        Expression leftOperand = parseFactor();

        while (position < expression.length()) {
            char operator = expression.charAt(position);
            if (operator == '×' || operator == '÷') {
                position++;
                Expression rightOperand = parseFactor();
                leftOperand = new BinaryOperation(operator, leftOperand, rightOperand);
            } else {
                break;
            }
        }

        return leftOperand;
    }

    private Expression parseFactor() {
        char currentChar = expression.charAt(position);
        if (currentChar == '(') {
            position++;
            Expression expressionInParentheses = parseExpression();
            if (expression.charAt(position) != ')') {
                isError = true;
                errorMessage = "错误，括号不匹配";
            }
            position++;
            return expressionInParentheses;
        } else if (Character.isDigit(currentChar) || currentChar == '.') {
            StringBuilder sb = new StringBuilder();

            while (position < expression.length() &&
                    (Character.isDigit(expression.charAt(position)) || expression.charAt(position) == '.')) {
                sb.append(expression.charAt(position));
                position++;
            }

            double value = Double.parseDouble(sb.toString());
            return new Number(value);
        } else {
            isError = true;
            errorMessage = "错误，无效的字符";
            return null;
        }
    }

}
