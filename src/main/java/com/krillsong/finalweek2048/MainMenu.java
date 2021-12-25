package com.krillsong.finalweek2048;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.input.view.MouseButtonView;
import com.almasb.fxgl.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.util.ArrayList;
import static javafx.scene.input.KeyCode.*;
import static com.almasb.fxgl.dsl.FXGL.*;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/15 0:49 
 @ Version: 1.0
 @ Description: 重写主界面菜单，暂未完成
__________________________*/

public class MainMenu extends FXGLMenu {

    private VBox scoresRoot = new VBox(10);
    private Node highScores;

    public MainMenu() {
        super(MenuType.MAIN_MENU);
        var bg = texture("bg.png", getAppWidth(), getAppHeight());
        bg.setTranslateY(0);
        bg.setTranslateX(0);
        getContentRoot().getChildren().add(bg);
        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);
        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);

        var gameStory = getUIFactoryService().newText("2021年12月21日，江西财经大学软件与物联网工程学院正式结课\n与此同时，软件学院的同学们即将面临 的是期末周！\n接踵而来的专业课考试让平时划水的小宗与小旭十分头疼\n他们决定在仅剩的时间中恶补，可" +
                "面对他们的却是女娲补天\n大难临头，他们究竟能否到达学完所有科目顺利通过期末考试呢...", Color.WHITE, FontType.GAME, 20);
        gameStory.setStroke(Color.WHITESMOKE);
        gameStory.setLineSpacing(5.0);
        gameStory.setStrokeWidth(1.0);
        gameStory.setTranslateX(getAppWidth() / 4.0);
        gameStory.setTranslateY(280);


        if (!FXGL.isMobile()) {
            title.setEffect(new Bloom(0.6));
        }
        centerTextBind(title, getAppWidth() / 2.0, 200);

        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 22.0);
        centerTextBind(version, getAppWidth() / 2.0, 220);

        getContentRoot().getChildren().addAll(title, version, gameStory);

        var menuBox = new VBox(
                3,
                new MenuButton("开始游戏", () -> fireNewGame()),
                new MenuButton("玩法介绍", this::instructions),
                new MenuButton("退出游戏", () -> fireExit())
        );
        menuBox.setSpacing(20.0); // 设置按钮之间的距离
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setPadding(new Insets(10, 10, 10, 10));
        menuBox.setTranslateX(getAppWidth() / 2.0 - 80);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);
        getContentRoot().getChildren().addAll(menuBox);
    }


    private void instructions() {
        GridPane pane = new GridPane();
        if (!FXGL.isMobile()) {
            pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.BLUE));
        }
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(25);
        pane.setVgap(10);
        pane.addRow(0, getUIFactoryService().newText("合成2048获取全部知识度过期末周!"));
        pane.addRow(1, getUIFactoryService().newText("玩家一"), new HBox(3, new KeyView(A), new KeyView(D), new KeyView(S)));
        pane.addRow(2, getUIFactoryService().newText("玩家二"), new HBox(3, new KeyView(LEFT), new KeyView(RIGHT), new KeyView(DOWN)));

        getDialogService().showBox("玩法介绍", pane, getUIFactoryService().newButton("确认"));
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITE, 36.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());

            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.GREENYELLOW)
                            .otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());
            setPickOnBounds(true);
            getChildren().add(text);
        }
    }
}
