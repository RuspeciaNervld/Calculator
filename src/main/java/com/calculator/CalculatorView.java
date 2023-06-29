package com.calculator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CalculatorView {
    String expression = "";
    String result = "";
    boolean calculated = false;
    boolean error = false;

    // 返回表达式
    public String getExpression() {
        return expression;
    }

    // 添加运算符或操作符
    public void appendOperatorOrDigit(String operator) {
        if(calculated){
            this.clear();
        }
        expression +=  operator ;
        showExpressionAndResult();
    }

    // 清空表达式和结果
    public void clear() {
        calculated = false;
        expression = "";
        result = "";
        showExpressionAndResult();
    }

    // 删除一个字符
    public void backspace() {
        if (calculated) {
            result = "";
            calculated = false;
        }else if (expression.length() > 0) {
            expression = expression.substring(0, expression.length() - 1);
        }
        showExpressionAndResult();
    }

    public void updateResult(String result){
        calculated = true;
        this.result = result;
    }

    // 在图形化界面展示表达式和结果
    public void showExpressionAndResult() {
        if(error) {
            CalculatorApplication.showText(result + "!请按退格键修改");
        }else{
            if(calculated){
                CalculatorApplication.showText(expression + "=" + result);
            } else {
                CalculatorApplication.showText(expression);
            }
        }
    }

    public void updateRecords(String records) {
        CalculatorApplication.showHistory(records);
    }

}