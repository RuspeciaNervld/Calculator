package com.calculator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class FileIO {
    // 读取文件内容，返回字符串
    public static String readFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    // 将字符串写入文件，在原有内容后追加
    public static void writeFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 清空文件内容
    public static void clearFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class CalculatorController {
    String fileName;
    private static CalculatorModel model = null;
    protected static final CalculatorView view = new CalculatorView();

    CalculatorController(String calculationRecordsFilePath, CalculatorModel model) {
        fileName = calculationRecordsFilePath;
        CalculatorController.model = model;
    }

    // 当用户点击“=”按钮时，调用此方法
    public void calculate() {
        String expression = view.getExpression();
        String result = model.getResultString(expression);
        if (!result.startsWith("错误")) {
            // 将此次记录存储到文件末尾
            FileIO.writeFile(fileName,expression + "=" + result);
            this.updateRecords();
        }
        view.updateResult(result);
        view.showExpressionAndResult();
    }

    // 展示所有历史记录
    public void updateRecords(){
        view.updateRecords(FileIO.readFile(fileName));
    }

    public void clearHistory() {
        FileIO.clearFile(fileName);
        view.updateRecords("");
    }
}
