package com.krillsong.finalweek2048;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.krillsong.finalweek2048.components.PlayerComponent;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/14 15:14 
 @ Version: 1.0
__________________________*/
public class FinalWeekFactory implements EntityFactory {

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .at(0,0)
                .view(texture("backgroundColorGrass.png", getAppWidth(), getAppHeight()))
                .with(new IrremovableComponent())
                .zIndex(-100)
                .build();
    }

    @Spawns("firstPlayer")
    public Entity newFirstPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(40, 40), new Point2D(200, 120))
                .type(FinalWeekType.FIRSTPLAYER)
                .viewWithBBox(texture("Female/Poses/female_idle.png", 80, 80))
                .with(new CellMoveComponent(80, 80, 100))
                .with(new AStarMoveComponent(FXGL.<FinalWeek2048App>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .zIndex(10)
                .build();
    }

    @Spawns("secondPlayer")
    public Entity newSecondPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(40, 40), new Point2D(getAppWidth() - 200, 120))
                .viewWithBBox(texture("Player/Poses/player_idle.png", 80, 80))
                .type(FinalWeekType.SECONDPLAYER)
                .with(new CellMoveComponent(80, 80, 100))
                .with(new AStarMoveComponent(FXGL.<FinalWeek2048App>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .zIndex(10)
                .build();
    }

    @Spawns("bookblock")
    public Entity newBookblock(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        FixtureDef fd = new FixtureDef();
        fd.setDensity(0.03f);
        physics.setFixtureDef(fd);
        var num = (int)Math.pow(2, FXGLMath.random(1, 10));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", num))
                .viewWithBBox(texture("bookblock_" + num + ".png", 80, 80))
                .with(new CollidableComponent(true))
                .with(physics)
                .buildAndAttach();
    }

    //----------------------布置地图--------------------------

    @Spawns("a")
    public Entity newA(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 2))
                .viewWithBBox(texture("bookblock_2.png", 80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }
    @Spawns("b")
    public Entity newB(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 4))
                .viewWithBBox(texture("bookblock_4.png", 80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }
    @Spawns("c")
    public Entity newC(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 8))
                .viewWithBBox(texture("bookblock_8.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }
    @Spawns("d")
    public Entity newD(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 16))
                .viewWithBBox(texture("bookblock_16.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("e")
    public Entity newE(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 32))
                .viewWithBBox(texture("bookblock_32.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("f")
    public Entity newF(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 64))
                .viewWithBBox(texture("bookblock_64.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("g")
    public Entity newG(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 128))
                .viewWithBBox(texture("bookblock_128.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("h")
    public Entity newH(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 256))
                .viewWithBBox(texture("bookblock_256.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("i")
    public Entity newI(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 512))
                .viewWithBBox(texture("bookblock_512.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("j")
    public Entity newJ(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 1024))
                .viewWithBBox(texture("bookblock_1024.png", 80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("k")
    public Entity newK(SpawnData data) {
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new IDComponent("block", 2048))
                .viewWithBBox(texture("bookblock_2048.png",  80, 80))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }
}
