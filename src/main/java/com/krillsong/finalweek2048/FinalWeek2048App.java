package com.krillsong.finalweek2048;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.krillsong.finalweek2048.components.PlayerComponent;
import javafx.css.converter.FontConverter;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/14 15:07 
 @ Version: 1.0
 @ Description: 游戏入口
__________________________*/
public class FinalWeek2048App extends GameApplication {
    private AStarGrid grid;
    private Entity firstPlayer;
    private Entity secondPlayer;
    private PlayerComponent firstPlayerComponent;
    private PlayerComponent secondPlayerComponent;
    // 游戏初始化设置
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1200);
        gameSettings.setHeight(800);
        gameSettings.setTitle("Final Week 2048");
        gameSettings.setVersion("江财版");

        // 启用主界面菜单
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu();
            }
            @Override
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }
        });
    }

    public AStarGrid getGrid() {
        return grid;
    }
    /**
    * @param: []
    * @return: void
    * @description: 初始化游戏
    * @date:2021/12/15
    **/
    @Override
    protected void initGame() {
        // 加入实体工厂
        getGameWorld().addEntityFactory(new FinalWeekFactory());
        spawn("background"); // 载入背景
        // 载入关卡
        var level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(80, 80, '0'));
        getGameWorld().setLevel(level);
        grid = AStarGrid.fromWorld(getGameWorld(), 40, 40, 80, 80, type -> {
            if (type.equals(FinalWeekType.BOOKBLOCK))
                return CellState.NOT_WALKABLE;
            else return CellState.WALKABLE;
        });
        for(int i = 0; i < 10; i++) {
            spawn("platform", i * 120, 160);
        }
        // 载入玩家一和玩家二
        firstPlayer = spawn("firstPlayer");
        firstPlayerComponent = firstPlayer.getComponent(PlayerComponent.class);
        secondPlayer = spawn("secondPlayer");
        secondPlayerComponent = secondPlayer.getComponent(PlayerComponent.class);

        // 监听玩家的分数
        getWorldProperties().<Integer>addListener("firstPlayerScore", (old, newScore) -> {
            if (newScore >= 2048) {
                showGameOver("玩家一");
                if(level.getHeight() < 2) inc("level", +1);
            }
        });

        getWorldProperties().<Integer>addListener("secondPlayerScore", (old, newScore) -> {
            if (newScore >= 2048) {
                showGameOver("玩家二");
                if(level.getHeight() < 2) inc("level", +1);
            }
        });
        getGameWorld().addEntity(entityBuilder().buildScreenBounds(100));
    }

    /**
    * @param: []
    * @return: void
    * @description:设置角色控制键
    * @date:2021/12/15
    **/
    @Override
    protected void initInput() {
        Input input = getInput();
            input.addAction(new UserAction("玩家一释放方块") {
                @Override
                protected void onActionEnd() {
                    int num = getWorldProperties().getInt("fBlock");
                    num = (int) (Math.log(num) / Math.log(2)) - 1;
                    firstPlayerComponent.placeBlock((char)(num + 'a') + "", 1);
                    getWorldProperties().setValue("fBlock", (int) Math.pow(2, random(1, 6)));
                }
            }, KeyCode.S);
            onKeyDown(KeyCode.D, "玩家一向右移动", () -> firstPlayerComponent.moveRight());
            onKeyDown(KeyCode.A, "玩家一向左移动", () -> firstPlayerComponent.moveLeft());
            onKeyDown(KeyCode.LEFT, "玩家二向左移动", () -> secondPlayerComponent.moveLeft());
            onKeyDown(KeyCode.RIGHT, "玩家二向右移动", () -> secondPlayerComponent.moveRight());
            input.addAction(new UserAction("玩家二释放方块") {
            @Override
            protected void onActionEnd() {
                int num = getWorldProperties().getInt("sBlock");
                num = (int) (Math.log(num) / Math.log(2));
                secondPlayerComponent.placeBlock((char)(num + 'a') + "", 2);
                getWorldProperties().setValue("sBlock", (int) Math.pow(2, random(1, 6)));
            }
        }, KeyCode.DOWN);
    }
    /**
    * @param: []
    * @return: void
    * @description:游戏界面UI初始化
    * @date:2021/12/15
    **/
    @Override
    protected void initUI() {
        // 玩家一和玩家二的分数
        Text textFirst = new Text(), nextBlockFirst = new Text("下一个: ");
        textFirst.setTranslateX(50);
        textFirst.setTranslateY(50);
        nextBlockFirst.setTranslateX(50);
        nextBlockFirst.setTranslateY(80);
        Text textSecond = new Text(), nextBlockSecond = new Text("下一个: ");
        textSecond.setTranslateX(getAppWidth() - 200);
        textSecond.setTranslateY(50);
        nextBlockSecond.setTranslateX(getAppWidth() - 200);
        nextBlockSecond.setTranslateY(80);

        textFirst.setStroke(Color.WHITESMOKE);
        textSecond.setStroke(Color.WHITESMOKE);
        nextBlockFirst.setStroke(Color.WHITESMOKE);
        nextBlockSecond.setStroke(Color.WHITESMOKE);

        textFirst.textProperty().bind(getWorldProperties().intProperty("firstPlayerScore").asString("玩家一得分: [%d]"));
        textSecond.textProperty().bind(getWorldProperties().intProperty("secondPlayerScore").asString("玩家二得分: [%d]"));
        nextBlockFirst.textProperty().bind(getWorldProperties().intProperty("fBlock").asString("下一个方块: [%d]"));
        nextBlockSecond.textProperty().bind(getWorldProperties().intProperty("sBlock").asString("下一个方块: [%d]"));
        // 将与实体相关联的视图添加到场景图中
        getGameScene().addUINodes(textFirst, textSecond, nextBlockFirst, nextBlockSecond);
    }

    /**
    * @param: []
    * @return: void
    * @description:设置游戏的物理世界
    * @date:2021/12/15
    **/
    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        // 当两个相同的方块碰撞在一起后消失，并产生一个加倍的方块
        physicsWorld.addCollisionHandler(new CollisionHandler(FinalWeekType.BOOKBLOCK, FinalWeekType.BOOKBLOCK) {
            @Override
            protected void onCollision(Entity playerBlock, Entity block) {
                int num1 = playerBlock.getComponent(HealthIntComponent.class).getMaxValue();
                int num2 = block.getComponent(HealthIntComponent.class).getMaxValue();
                String curBlock = "";
                if(playerBlock.isType(block.getType())) {
                    if(playerBlock.hasComponent(IDComponent.class)) {
                        curBlock = playerBlock.getComponent(IDComponent.class).getName();
                    }
                    if(num1 == num2) {
                        Point2D explosionSpawnPoint = playerBlock.getCenter().subtract(64, 64);
                        spawn("explosion", explosionSpawnPoint);
                        runOnce(()->play("combine.wav"), Duration.seconds(0.5));
                        double x = block.getCenter().getX() - 40, y = block.getCenter().getY() - 40;
                        playerBlock.removeFromWorld();
                        block.removeFromWorld();
                        int score = (int)(Math.log(num1) / Math.log(2));
                        char ch = (char) (score + 'a');
                        if(ch >= 'k') {
                            if(curBlock == "firstPlayerScore") showGameOver("玩家一");
                                else showGameOver("玩家二");
                        }
                        String str = "" + ch;
                        spawn(str, x, y);
                        if(curBlock != "") inc(curBlock, +num1);
                    }
                }
            }
        });
        physicsWorld.addCollisionHandler(new CollisionHandler(FinalWeekType.BOOKBLOCK, FinalWeekType.PLATFORM) {
            @Override
            protected void onCollision(Entity block, Entity platform) {
                if(block.getComponent(IDComponent.class).getName() == "firstPlayerScore") showGameOver("玩家二");
                    else showGameOver("玩家一");
            }
        });
    }
    /**
    * @param: [vars]
    * @return: void
    * @description:载入游戏全局变量
    * @date:2021/12/15
    **/
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("firstPlayerScore", 0);
        vars.put("secondPlayerScore", 0);
        vars.put("fBlock", 2);
        vars.put("sBlock", 2);
        vars.put("level", 0);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalSoundVolume(0.5);
        getSettings().setGlobalMusicVolume(0.15);
        loopBGM("Scott Joplin.mp3"); // 载入背景音乐
    }

    private void showGameOver(String winner) {
        getDialogService().showMessageBox(winner + "胜利!\n感谢参与测试！", getGameController()::gotoGameMenu);

    }
    public static void main(String[] args) {
        launch(args);
    }
}
