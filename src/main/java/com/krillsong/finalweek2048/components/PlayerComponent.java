package com.krillsong.finalweek2048.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/14 15:10 
 @ Version: 1.0
__________________________*/
public class PlayerComponent extends Component {
    private CellMoveComponent cell;
    private AStarMoveComponent astar;

    public void moveRight() {
        astar.moveToRightCell();
    }

    public void moveLeft() {
        astar.moveToLeftCell();
    }
}
