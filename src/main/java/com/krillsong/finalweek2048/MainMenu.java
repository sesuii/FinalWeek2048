package com.krillsong.finalweek2048;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
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
//        var bg = texture("backgroundColorGrass.png", getAppWidth() + 450, getAppHeight() + 200);
//        bg.setTranslateY(-85);
//        bg.setTranslateX(-450);
        getContentRoot().getChildren().setAll(new Rectangle(getAppWidth(), getAppHeight()));

        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);
        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);

        if (!FXGL.isMobile()) {
            title.setEffect(new Bloom(0.6));
        }
        centerTextBind(title, getAppWidth() / 2.0, 200);

        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 22.0);
        centerTextBind(version, getAppWidth() / 2.0, 220);

        getContentRoot().getChildren().addAll(title, version);

        var color = Color.DARKBLUE;

        var blocks = new ArrayList<ColorBlock>();

        var blockStartX = getAppWidth() / 2.0 - 380;

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, color);
            block.setTranslateX(blockStartX + i*50);
            block.setTranslateY(90);

            blocks.add(block);
            getContentRoot().getChildren().add(block);
        }

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, color);
            block.setTranslateX(blockStartX + i*50);
            block.setTranslateY(220);

            blocks.add(block);
            getContentRoot().getChildren().add(block);
        }

        for (int i = 0; i < blocks.size(); i++) {
            var block = blocks.get(i);

            animationBuilder()
                    .delay(Duration.seconds(i * 0.05))
                    .duration(Duration.seconds(0.5))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .animate(block.fillProperty())
                    .from(color)
                    .to(color.brighter().brighter())
                    .buildAndPlay(this);
        }

        var menuBox = new VBox(
                3,
                new MenuButton("开始游戏", () -> fireNewGame()),
                new MenuButton("玩法介绍", this::instructions),
                new MenuButton("结束游戏", () -> fireExit())
        );
        menuBox.setSpacing(10.0); // 设置按钮之间的距离
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setPadding(new Insets(10, 10, 10, 10));
        menuBox.setTranslateX(getAppWidth() / 2.0 - 100);
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
                            .then(Color.BLUE)
                            .otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());
            setPickOnBounds(true);
            getChildren().add(text);
        }
    }
}
