package com.krillsong.finalweek2048.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.spawn;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/14 15:10 
 @ Version: 1.0
__________________________*/
public class PlayerComponent extends Component {
    private CellMoveComponent cell;
    private AStarMoveComponent astar;
    private int shoot = 0;
    public void moveRight() {
        astar.moveToRightCell();
    }

    public void moveLeft() {
        astar.moveToLeftCell();
    }
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
