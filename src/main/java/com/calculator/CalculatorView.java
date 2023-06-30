package com.calculator;

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
            if(result.startsWith("错误")) {
                expression = "";
            }else {
                expression = result;
            }
            calculated = false;
        }
        // 避免出现不可连接在一起的运算符相连的情况
        if((expression.endsWith("×") || expression.endsWith("÷")) && (operator.equals("×") || operator.equals("÷") || operator.equals("+"))){
            expression = expression.substring(0, expression.length() - 1);
        }else if((expression.endsWith("+") || expression.endsWith("-")) && (operator.equals("+") || operator.equals("-") || operator.equals("×") || operator.equals("÷"))){
            // 如果最后是乘除号 和 加减号，就删掉两个符号，加上新的符号
            if(expression.charAt(expression.length() - 2) == '×' || expression.charAt(expression.length() - 2) == '÷'){
                expression = expression.substring(0, expression.length() - 1);
            }
            expression = expression.substring(0, expression.length() - 1);
        }

        // 找到最后一个符号位置
        char[] targetChar = {'+', '-', '×', '÷', '(', ')'};
        int lastIndex = -1;
        for (char c : targetChar) {
            // 找到最后一个符号位置
            int index = expression.lastIndexOf(c);
            lastIndex = Math.max(lastIndex, index);
        }

        if(operator.equals(".")){
            if (expression.lastIndexOf(".") <= lastIndex && !expression.isEmpty()) {
                // 允许输入小数点
                expression += operator;
                showExpressionAndResult();
            }
            // 数字已经有小数点了, 或者表达式为空, 不允许再输入小数点
            return;
        } else if(operator.equals("0")) { // 数字0
            int zeroNum = 0;
            for(int i = lastIndex + 1; i < expression.length(); i++){
                if(expression.charAt(i) == '0'){
                    zeroNum++;
                }
            }
            if (zeroNum != 1 || expression.charAt(lastIndex + 1) != '0') {
                // 允许输入0
                expression += operator;
                showExpressionAndResult();
            }
            // 表达式中已经有一个0了, 不允许再输入0
            return;
        } else if(operator.charAt(0) > '0' && operator.charAt(0) <= '9') { // 其他数字
            if(expression.length() > 0 ){
                if(expression.charAt(expression.length() - 1) == ')'){
                    // 表达式最后一个字符是右括号, 不允许输入数字
                    return;
                }else if (expression.length() >= lastIndex + 2 && expression.charAt(lastIndex+1) == '0' && expression.lastIndexOf(".") <= lastIndex){
                    // 表达式最后一个字符是0，且不是小数点后面的0, 不允许输入数字，只能更改字符
                    expression = expression.substring(0, expression.length() - 1);
                }
            }
        }
        expression += operator;
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