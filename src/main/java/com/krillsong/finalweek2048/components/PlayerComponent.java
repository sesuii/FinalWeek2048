package com.krillsong.finalweek2048.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.spawn;

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
    public void placeBlock() {
        if (shoot == 1) {
            return;
        }
        shoot++;
        Entity bomb = spawn("bookblock", new SpawnData(cell.getCellX() * 80, cell.getCellY() * 80));
        getGameTimer().runOnceAfter(() -> {
            shoot--;
        }, Duration.seconds(0.5));
    }
}
