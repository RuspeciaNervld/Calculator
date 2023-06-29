package com.calculator;

import com.calculator.models.CalculatorStackModel;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class CalculatorApplication extends Application {

    private final CalculatorController calculatorController = new CalculatorController("records.txt",new CalculatorStackModel());
    private static final TextArea displayField = new TextArea(); // 显示框
    private static final TextArea historyField = new TextArea(); // 历史记录框
    private static final double DRAWER_WIDTH = 200.0;
    private boolean drawerVisible = false;

    public static void showText(String content) {
        displayField.setText(content);
    }

    public static void showHistory(String content) {
        historyField.setText(content);
    }

    // 创建根布局
    private final BorderPane root = new BorderPane();

    @Override
    public void start(Stage primaryStage) {
        // 创建顶部面板，并添加到根布局中
        HBox topPanel = new HBox();
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        topPanel.setPadding(new Insets(10));
        topPanel.setStyle("-fx-background-color: linear-gradient(to bottom, #ffcc99, #ff9966); -fx-background-radius: 5px;");
        root.setTop(topPanel);

        // 创建顶部按钮，并添加到顶部面板中
        Button historyButton = new Button("历史");
        historyButton.setStyle("-fx-background-color: linear-gradient(#d9e4f7, #f3e9d2);" +
                "-fx-background-radius: 20;" +
                "-fx-border-color: #c3c3c3;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 20;" +
                "-fx-padding: 10 20;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #333333;" +
                "-fx-rotate: 90;" +
                "-fx-text-alignment: center;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0.0, 0, 1);");
        topPanel.getChildren().add(historyButton);

        // 创建背景面板，并添加毛玻璃效果和背景颜色，然后将其添加到根布局中
        Pane backgroundPane = new Pane();
        backgroundPane.setStyle("-fx-background-color: linear-gradient(to bottom, #f7e9d9, #fdf6e3); -fx-background-radius: 10px;");
        backgroundPane.setEffect(new DropShadow());
        root.setCenter(backgroundPane);

        Pane drawerPane = createDrawerContent();


        historyButton.setOnAction(event -> toggleDrawer(backgroundPane,drawerPane));
        historyButton.setOnMouseEntered(event -> {
            ScaleTransition scaleTransitionBtn = new ScaleTransition(Duration.millis(100), historyButton);
            scaleTransitionBtn.setToX(1.2);
            scaleTransitionBtn.setToY(1.2);
            scaleTransitionBtn.play();
        });
        historyButton.setOnMouseExited(event -> {
            ScaleTransition scaleTransitionBtn = new ScaleTransition(Duration.millis(100), historyButton);
            scaleTransitionBtn.setToX(1);
            scaleTransitionBtn.setToY(1);
            scaleTransitionBtn.play();
        });

        // 创建文本框并设置样式和高度
        displayField.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");
        displayField.setEditable(false);
        displayField.setPrefHeight(50);
        displayField.setPrefWidth(300);
        displayField.setStyle("-fx-background-color: #333333;" +
                "-fx-font-size: 20px;" +
                "-fx-border-width: 4px;" +
                "-fx-border-color: linear-gradient(to bottom, #bfbfbf, #5e5e5e);");

        // 将文本框添加到顶部
        topPanel.getChildren().add(displayField);

        // 创建中心网格布局，并添加到背景面板中
        GridPane centerGrid = new GridPane();
        centerGrid.setPadding(new Insets(20, 20, 40, 20));
        centerGrid.setHgap(10);
        centerGrid.setVgap(10);
        centerGrid.setAlignment(Pos.CENTER);
        backgroundPane.getChildren().add(centerGrid);

        // 创建按钮数组
        String[][] buttons = {
                {"7", "8", "9", "Del", "AC"},
                {"4", "5", "6", "×", "÷"},
                {"1", "2", "3", "+", "-"},
                {"(", "0", ")", ".", "="}
        };

        // 添加按钮到网格布局中
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                Button button = new Button(buttons[row][col]);
                button.setPrefSize(60, 60);
                button.setStyle("-fx-font-size: 20px; -fx-background-color: #232932; -fx-text-fill: white;");
                button.setEffect(new DropShadow());
                button.setOnMouseEntered(event -> {
                    ScaleTransition scaleTransitionBtn = new ScaleTransition(Duration.millis(100), button);
                    scaleTransitionBtn.setToX(1.2);
                    scaleTransitionBtn.setToY(1.2);
                    scaleTransitionBtn.play();
                });
                button.setOnMouseExited(event -> {
                    ScaleTransition scaleTransitionBtn = new ScaleTransition(Duration.millis(100), button);
                    scaleTransitionBtn.setToX(1);
                    scaleTransitionBtn.setToY(1);
                    scaleTransitionBtn.play();
                });
                switch (buttons[row][col]) {
                    case "=" -> {
                        button.setStyle("-fx-font-size: 20px; -fx-background-color: #276ba3; -fx-text-fill: white;");
                        button.setOnMouseClicked(event -> calculatorController.calculate());
                    }
                    case "AC" -> {
                        button.setStyle("-fx-font-size: 20px; -fx-background-color: #ad002e; -fx-text-fill: white;");
                        button.setOnMouseClicked(event -> CalculatorController.view.clear());
                    }
                    case "Del" ->{
                        button.setStyle("-fx-font-size: 20px; -fx-background-color: #cc411c; -fx-text-fill: white;");
                        button.setOnMouseClicked(event -> CalculatorController.view.backspace());
                    }
                    default ->{
                        if (button.getText().charAt(0) >= '0' && button.getText().charAt(0) <= '9' ||button.getText().equals("(") || button.getText().equals(")")) {
                            button.setStyle("-fx-font-size: 20px; -fx-background-color: #363c47; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, #000000, 10, 0, 0, 0);");
                        }
                        button.setOnMouseClicked(event -> CalculatorController.view.appendOperatorOrDigit(button.getText()));
                    }
                }
                centerGrid.add(button, col, row);
            }
        }

        // 设置背景面板的大小和位置
        backgroundPane.setMaxSize(387, 399);
        backgroundPane.setMinSize(387, 399);
        backgroundPane.setLayoutX(0);
        backgroundPane.setLayoutY(0);

        // 创建场景并设置根布局
        Scene scene = new Scene(root, 387, 399);

        scene.getRoot().setStyle("-fx-background-color: linear-gradient(to bottom, #ff9d6a,#ff9d6a, #FFC4CB, #FFB0BE, #FF97AC);");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // 禁止窗口缩放
        // 显示窗口
        primaryStage.setTitle("计算器");
        // 添加计算器图标
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/calculator/calculator_icon2.png")));
        primaryStage.getIcons().add(icon);


        primaryStage.getIcons().add(icon);

        primaryStage.show();
    }

    private Pane createDrawerContent() {
        // 创建历史记录文本区域
        historyField.setId("historyField"); // 设置唯一的ID
        // 设置历史记录文本区域样式
        historyField.setEditable(false);
        historyField.setMaxHeight(250);
        historyField.setMinHeight(250);
        historyField.setWrapText(true);
        historyField.setStyle("-fx-font-size: 17px; -fx-text-fill: black; -fx-font-family: 'Arial';" +
                "-fx-border-width: 2px;" +
                "-fx-background-color: linear-gradient(to bottom, #ff7f00, #ffcc00);");


        // 设置历史记录文本区域的滚动条样式
        ScrollPane scrollPane = (ScrollPane) historyField.lookup("#historyField .scroll-pane");
        if (scrollPane != null) {
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #F0F4F8;");
        }
        // 创建清空历史记录按钮
        Button clearButton = new Button("清空历史记录");
        clearButton.setStyle("-fx-background-color: linear-gradient(#da6666, #b50000);" +
                "-fx-background-radius: 5;" +
                "-fx-border-color: #c3c3c3;" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 5;" +
                "-fx-padding: 5 10;" +
                "-fx-font-size: 12px;" +
                "-fx-text-fill: white;");
        clearButton.setOnMouseEntered(event -> {
            ScaleTransition scaleTransitionBtn = new ScaleTransition(Duration.millis(100), clearButton);
            scaleTransitionBtn.setToX(1.2);
            scaleTransitionBtn.setToY(1.2);
            scaleTransitionBtn.play();
        });
        clearButton.setOnMouseExited(event -> {
            ScaleTransition scaleTransitionBtn = new ScaleTransition(Duration.millis(100), clearButton);
            scaleTransitionBtn.setToX(1);
            scaleTransitionBtn.setToY(1);
            scaleTransitionBtn.play();
        });
        clearButton.setOnAction(event -> calculatorController.clearHistory());

        // 将清空按钮和历史记录文本区域添加到抽屉内容面板
        VBox drawerContent = new VBox(10); // 使用间距来调整按钮和文本区域的垂直布局
        drawerContent.setPadding(new Insets(10));
        drawerContent.setStyle("-fx-background-color: linear-gradient(to bottom, #ff9d6a, #FFC4CB, #FFB0BE, #FF97AC);");

        drawerContent.getChildren().addAll(clearButton, historyField);


        // 让历史记录文本区域自动滚动到底部内容
        historyField.textProperty().addListener((observable, oldValue, newValue) -> historyField.setScrollTop(Double.MAX_VALUE));

        return drawerContent;
    }

    private void toggleDrawer(Pane backgroundPane, Pane drawerContent) {
        if (drawerVisible) {
            closeDrawer(backgroundPane, drawerContent);
        } else {
            // 加载抽屉内容
            calculatorController.updateRecords();
            openDrawer(backgroundPane, drawerContent);
        }
        drawerVisible = !drawerVisible;
    }

    private void closeDrawer(Pane backgroundPane, Pane drawerContent) {
        root.setCenter(backgroundPane);
        backgroundPane.setVisible(true);
        // 创建平移动画
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), backgroundPane);
        translateTransition.setToX(0);

        // 创建淡入动画
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), backgroundPane);
        fadeTransition.setToValue(1);

        // 创建并行动画，同时播放平移和淡入动画
        ParallelTransition openTransition = new ParallelTransition(translateTransition, fadeTransition);

        // 创建顺序动画，先隐藏抽屉内容，再播放顺序动画
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(openTransition);
        sequentialTransition.setOnFinished(event -> drawerContent.setVisible(false));
        sequentialTransition.play();
    }

    private void openDrawer(Pane backgroundPane, Pane drawerContent) {
        // 创建平移动画
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), backgroundPane);
        translateTransition.setToX(-DRAWER_WIDTH);

        // 创建淡出动画
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), backgroundPane);
        fadeTransition.setToValue(0);

        // 创建并行动画，同时播放平移和淡出动画
        ParallelTransition closeTransition = new ParallelTransition(translateTransition, fadeTransition);

        // 创建顺序动画，先隐藏抽屉内容，再播放顺序动画
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(closeTransition);
        sequentialTransition.setOnFinished(event -> {
            backgroundPane.setVisible(false);
            drawerContent.setVisible(true);
            root.setCenter(drawerContent);
        });
        sequentialTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
