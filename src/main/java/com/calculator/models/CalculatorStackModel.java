package com.calculator.models;

import com.calculator.CalculatorModel;

import java.math.BigDecimal;
import java.util.Stack;

public class CalculatorStackModel implements CalculatorModel {
    boolean isError = false;
    String errorMessage = "";
    public String getResultString(String expression) {
        char[] expressionCharArray = expression.toCharArray();

        // 两个栈，一个用于操作数，一个用于运算符
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expressionCharArray.length; i++) {
            if (expressionCharArray[i] == ' ')
                continue;

            if (Character.isDigit(expressionCharArray[i]) || expressionCharArray[i] == '.') {
                StringBuilder sb = new StringBuilder();
                // 读取操作数
                while (i < expressionCharArray.length && (Character.isDigit(expressionCharArray[i]) || expressionCharArray[i] == '.')) {
                    sb.append(expressionCharArray[i++]);
                }
                // 将操作数转为double类型，压入操作数栈
                operandStack.push(Double.parseDouble(sb.toString()));
                // 恢复到操作数的最后一个字符
                i--;
            } else if (expressionCharArray[i] == '(') {
                operatorStack.push(expressionCharArray[i]);
            } else if (expressionCharArray[i] == ')') {
                while (operatorStack.peek() != '(') {
                    if (operandStack.size() == 1){
                        operandStack.push(operandStack.pop());
                        break;
                    }else if (operandStack.size() == 0){
                        break;
                    }
                    double result = calculateOnce(operatorStack.pop(), operandStack.pop(), operandStack.pop());
                    operandStack.push(result);
                }
                operatorStack.pop(); // 弹出 '('
            } else if (expressionCharArray[i] == '+' || expressionCharArray[i] == '-' || expressionCharArray[i] == '×' || expressionCharArray[i] == '÷') {
                // 当前符号和栈顶符号优先级比较，如果当前符号优先级低于栈顶符号，则先计算栈顶符号，否则直接压入栈
                while (!operatorStack.isEmpty() && hasPrecedence(expressionCharArray[i], operatorStack.peek())) {
                    if (operandStack.size() < 2)
                        return "错误，操作数与操作符不匹配"; // 操作数不足，表达式无效
                    double result = calculateOnce(operatorStack.pop(), operandStack.pop(), operandStack.pop());
                    operandStack.push(result);
                }
                // 当前符号优先级高于栈顶符号，或者栈为空，直接压入栈
                operatorStack.push(expressionCharArray[i]);
            } else {
                return "错误，无法识别的符号";
            }
        }

        // 计算剩余的操作符，直到操作符栈为空
        while (!operatorStack.isEmpty()) {
            if (operandStack.size() < 2)
                return "错误，操作数与操作符不匹配，表达式无效"; // 操作数不足，表达式无效
            double result = calculateOnce(operatorStack.pop(), operandStack.pop(), operandStack.pop());
            operandStack.push(result);
        }

        // 返回最终结果
        if (operandStack.isEmpty())
            return "错误，表达式为空"; // 表达式无效
        double res = operandStack.pop();
        BigDecimal bd = new BigDecimal(Double.toString(res));
        if(isError){
            isError = false;
            return errorMessage;
        }
        return String.valueOf(bd.stripTrailingZeros());
    }

    private int getPriority(char op){
        return switch (op) {
            case '(', ')' -> 0;
            case '+', '-' -> 1;
            case '×', '÷' -> 2;
            default -> -1;
        };
    }

    public boolean hasPrecedence(char op1, char op2) {
        return getPriority(op1) < getPriority(op2);
    }

    public double calculateOnce(char operator, double b, double a) {
        switch (operator) {
            case '+' -> {
                return a + b;
            }
            case '-' -> {
                return a - b;
            }
            case '×' -> {
                return a * b;
            }
            case '÷' -> {
                if (b == 0){
                    isError = true;
                    errorMessage = "错误，除数不能为0";
                    return 0;
                }
                return a / b;
            }
        }
        isError = true;
        errorMessage = "错误，无法识别的符号";
        return 0;
    }
}
