package com.krillsong.finalweek2048;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.net.ContentHandler;
import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/15 0:49 
 @ Version: 1.0
__________________________*/
public class FinalWeekMenu extends FXGLMenu {

    public FinalWeekMenu() {
        super(MenuType.MAIN_MENU);
//        getContentRoot().getChildren().setAll(new Rectangle(getAppWidth(), getAppHeight()));
//
//        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 46.0);
//        title.setStroke(Color.WHITESMOKE);
//        title.setStrokeWidth(1.5);
//
//        if (!FXGL.isMobile()) {
//            title.setEffect(new Bloom(0.6));
//        }
//        centerTextBind(title, getAppWidth() / 2.0, 200);
//
//        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 22.0);
//        centerTextBind(version, getAppWidth() / 2.0, 220);
//
//        getContentRoot().getChildren().addAll(title, version);
//
//        var color = Color.DARKBLUE;
//
//        var menuBox = new VBox(
//                3,
//                new MenuButton("开始游戏", () -> fireNewGame()),
//                new MenuButton("设置", () -> fireContinue()),
//                new MenuButton("退出游戏", () -> fireExit())
//        );
//        menuBox.setAlignment(Pos.TOP_CENTER);
//        menuBox.setTranslateX(getAppWidth() / 2.0 - 125);
//        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);
//        getContentRoot().getChildren().addAll(menuBox);
    }
    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.BLUE, 36.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());
            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.BLUE)
                            .otherwise(Color.BLACK)
            );
            setOnMouseClicked(e -> action.run());
            setPickOnBounds(true);
            getChildren().add(text);
        }
    }
}
