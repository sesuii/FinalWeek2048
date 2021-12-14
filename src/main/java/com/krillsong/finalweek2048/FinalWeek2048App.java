package com.krillsong.finalweek2048;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.krillsong.finalweek2048.components.PlayerComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/14 15:07 
 @ Version: 1.0
__________________________*/
public class FinalWeek2048App extends GameApplication {
    private AStarGrid grid;
    private Entity firstPlayer;
    private Entity secondPlayer;
    private PlayerComponent playerComponent;
    private PlayerComponent secondPlayerComponent;
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1200);
        gameSettings.setHeight(800);
        gameSettings.setTitle("Demo 2048");
        gameSettings.setVersion("1.0");
    }

    public AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new FinalWeekFactory());
        spawn("background");
        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(80, 80, '0'));
        getGameWorld().setLevel(level);
        grid = AStarGrid.fromWorld(getGameWorld(), 40, 40, 80, 80, type -> {
            if (type.equals(FinalWeekType.BOOKBLOCK))
                return CellState.NOT_WALKABLE;
            else return CellState.WALKABLE;
        });

        firstPlayer = spawn("firstPlayer");
        playerComponent = firstPlayer.getComponent(PlayerComponent.class);
        secondPlayer = spawn("secondPlayer");
        secondPlayerComponent = secondPlayer.getComponent(PlayerComponent.class);

    }

    @Override
    protected void initInput() {
        Input input = getInput();
            FXGL.onKeyDown(KeyCode.D, () -> firstPlayer.getComponent(PlayerComponent.class).moveRight());
            FXGL.onKeyDown(KeyCode.A, () -> firstPlayer.getComponent(PlayerComponent.class).moveLeft());
            FXGL.onKeyDown(KeyCode.S, () -> {
            });
            FXGL.onKeyDown(KeyCode.LEFT, () -> secondPlayer.getComponent(PlayerComponent.class).moveLeft());
            FXGL.onKeyDown(KeyCode.RIGHT, () -> secondPlayer.getComponent(PlayerComponent.class).moveRight());
            FXGL.onKeyDown(KeyCode.DOWN, () -> {
            });
    }
    @Override
    protected void initUI() {
        Text textFirst = new Text();
        textFirst.setTranslateX(50);
        textFirst.setTranslateY(50);

        Text textSecond = new Text();
        textSecond.setTranslateX(getAppWidth() - 200);
        textSecond.setTranslateY(50);

        textFirst.textProperty().bind(FXGL.getWorldProperties().intProperty("firstPlayerScore").asString("First Player Score: %d"));
        textSecond.textProperty().bind(FXGL.getWorldProperties().intProperty("secondPlayerScore").asString("Second Player Score: %d"));
        // 将与实体相关联的视图添加到场景图中
        FXGL.getGameScene().addUINodes(textFirst, textSecond);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("firstPlayerScore", 0);
        vars.put("secondPlayerScore", 0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
