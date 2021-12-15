package com.krillsong.finalweek2048;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
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
        gameSettings.setTitle("Demo 2048");
        gameSettings.setVersion("1.0");
        gameSettings.setProfilingEnabled(false);
        // 启用主界面菜单，这里之后会重写菜单
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setManualResizeEnabled(true);
        gameSettings.setPreserveResizeRatio(true);
        gameSettings.setIntroEnabled(false);
//        gameSettings.setDefaultLanguage(Language.CHINESE);
        gameSettings.setApplicationMode(ApplicationMode.RELEASE);
//        gameSettings.setSceneFactory(new SceneFactory() {
//            @Override
//            public FXGLMenu newMainMenu() {
//                return new FinalWeekMenu();
//            }
//            @Override
//            public FXGLMenu newGameMenu() {
//                return new SimpleGameMenu();
//            }
//        });
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
        // 载入关卡，目前只有一关，之后还会加
        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(80, 80, '0'));
        getGameWorld().setLevel(level);
        grid = AStarGrid.fromWorld(getGameWorld(), 40, 40, 80, 80, type -> {
            if (type.equals(FinalWeekType.BOOKBLOCK))
                return CellState.NOT_WALKABLE;
            else return CellState.WALKABLE;
        });
        // 载入玩家一和玩家二
        firstPlayer = spawn("firstPlayer");
        firstPlayerComponent = firstPlayer.getComponent(PlayerComponent.class);
        secondPlayer = spawn("secondPlayer");
        secondPlayerComponent = secondPlayer.getComponent(PlayerComponent.class);

        // 监听玩家的分数
        getWorldProperties().<Integer>addListener("firstPlayerScore", (old, newScore) -> {
            if (newScore == 11) {
                showGameOver("Player 1");
            }
        });

        getWorldProperties().<Integer>addListener("secondPlayerScore", (old, newScore) -> {
            if (newScore == 11) {
                showGameOver("Player 2");
            }
        });
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
            FXGL.onKeyDown(KeyCode.D, "玩家一向右移动", () -> firstPlayerComponent.moveRight());
            FXGL.onKeyDown(KeyCode.A, "玩家一向左移动", () -> firstPlayerComponent.moveLeft());
            FXGL.onKey(KeyCode.S,"玩家一释放方块", () -> firstPlayerComponent.placeBlock());
            FXGL.onKeyDown(KeyCode.LEFT, "玩家二向左移动", () -> secondPlayerComponent.moveLeft());
            FXGL.onKeyDown(KeyCode.RIGHT, "玩家二向右移动", () -> secondPlayerComponent.moveRight());
            FXGL.onKey(KeyCode.DOWN, "玩家二释放方块", () -> secondPlayerComponent.placeBlock());
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
//        Text goodLuck = getUIFactoryService().newText("双人对战！尽最大的努力拿到2048", Color.AQUA, 38);
//        addUINode(goodLuck);
//        centerText(goodLuck);
//        animationBuilder()
//                .duration(Duration.seconds(2))
//                .autoReverse(true)
//                .repeat(2)
//                .fadeIn(goodLuck)
//                .build();
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
                int num1 = playerBlock.getComponent(IDComponent.class).getId();
                int num2 = block.getComponent(IDComponent.class).getId();
                if(num1 == num2) {
                    playerBlock.removeFromWorld();
                    block.removeFromWorld();
                    char ch = (char) ((int)(Math.log(num1) / Math.log(2)) + 'a');
                    System.out.println(num1 + " " + ch);
                    if(ch >= 'k') ch = 'k'; // 游戏胜利处理
                    String str = "" + ch;
                    spawn(str, 0, 0);
                    inc("secondPlayerScore", +1);
                }

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
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalSoundVolume(0.25);
        getSettings().setGlobalMusicVolume(0.5);
        loopBGM("Scott Joplin.mp3"); // 载入背景音乐
    }
    private void showGameOver(String winner) {
        getDialogService().showMessageBox(winner + " won!\nThanks for playing", getGameController()::gotoGameMenu);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
