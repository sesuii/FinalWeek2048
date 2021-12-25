package com.krillsong.finalweek2048.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.util.Duration;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/14 15:10
 @ Version: 1.0
 @ description: 玩家组件类
__________________________*/
public class PlayerComponent extends Component {
    private CellMoveComponent cell;
    private AStarMoveComponent astar;
    private int shoot = 0;
     // 玩家按方格移动
    public void moveRight() {
        astar.moveToRightCell();
    }
    public void moveLeft() {
        astar.moveToLeftCell();
    }
    // 放置方块
    public void placeBlock(String blockType, int player) {
        int cnt = 0;
        if (shoot == 1) {
            return;
        }
        shoot++;
        Entity block = spawn(blockType, new SpawnData(cell.getCellX() * 80, cell.getCellY() * 80 + 125));
        if(player == 1) block.addComponent(new IDComponent("firstPlayerScore", cnt++));
        else block.addComponent(new IDComponent("secondPlayerScore", cnt++));
        getGameWorld().addEntity(block);
        getGameTimer().runOnceAfter(() -> {
            shoot--;
        }, Duration.seconds(0.5));

    }
}
