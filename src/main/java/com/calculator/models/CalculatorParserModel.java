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
    private boolean isNegativeSign = false; // 新增：用于区分负号和减号

    public String getResultString(String expression) {
        this.expression = expression;
        this.position = 0;
        this.isNegativeSign = false; // 新增：重置负号标志
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
                if (operator == '-' && !isNegativeSign) {
                    // 当前位置的减号是减号，而不是负号
                    Expression rightOperand = parseTerm();
                    leftOperand = new BinaryOperation(operator, leftOperand, rightOperand);
                } else {
                    // 当前位置的减号是负号
                    isNegativeSign = false; // 重置负号标志
                    Expression rightOperand = parseFactor(); // 解析负号后的因子
                    leftOperand = new BinaryOperation('×', leftOperand, rightOperand); // 使用乘法代替负号
                }
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
            if (isNegativeSign) {
                value = -value; // 当前位置的减号是负号，将值取负数
                isNegativeSign = false; // 重置负号标志
            }
            return new Number(value);
        } else if (currentChar == '-') {
            // 当前位置的减号是负号
            isNegativeSign = true;
            position++;
            return parseFactor();
        } else {
            isError = true;
            errorMessage = "错误，无效的字符";
            return null;
        }
    }
}

