package com.krillsong.finalweek2048;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.ManaDoubleComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.ComponentHelper;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.components.IntegerComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.krillsong.finalweek2048.components.PlayerComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/14 15:14 
 @ Version: 1.0
 @ Description: 实体加工工厂
__________________________*/
public class FinalWeekFactory implements EntityFactory {
    @Spawns("explosion")
    public Entity newExplosion(SpawnData data) {
        var emitter = ParticleEmitters.newExplosionEmitter(350);
        emitter.setMaxEmissions(1);
        emitter.setSize(2, 10);
        emitter.setStartColor(Color.WHITE);
        emitter.setEndColor(Color.BLUE);
        emitter.setSpawnPointFunction(i -> new Point2D(64, 64));
        return entityBuilder(data)
                .with(new ExpireCleanComponent(Duration.seconds(0.66)))
                .with(new ParticleComponent(emitter))
                .build();
    }

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(FinalWeekType.PLATFORM)
                .viewWithBBox(texture("bat.png", 120, 40))
                .collidable()
                .with(new PhysicsComponent())
                .zIndex(-10)
                .build();
    }
    // 加载背景
    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .at(0,0)
                .view(texture("town.jpg", getAppWidth(), getAppHeight()))
                .with(new IrremovableComponent())
                .zIndex(-100)
                .build();
    }
    // 设置玩家属性
    @Spawns("firstPlayer")
    public Entity newFirstPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(40, 40), new Point2D(200, 120))
                .type(FinalWeekType.FIRSTPLAYER)
                .viewWithBBox(texture("Female/Poses/female_idle.png", 80, 80))
                .with(new CellMoveComponent(80, 80, 100)) // 可碰撞
                .with(new AStarMoveComponent(FXGL.<FinalWeek2048App>getAppCast().getGrid())) // 可以在方格移动
                .with(new PlayerComponent()) // 玩家部件，可移动
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

    //----------------------布置地图--------------------------

    @Spawns("a")
    public Entity newA(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));

        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_2.png", 80, 80))
                .with(new HealthIntComponent(2))
                .with(physics)
                .collidable()
                .build();
    }
    @Spawns("b")
    public Entity newB(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));

        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_4.png", 80, 80))
                .with(new HealthIntComponent(4))
                .with(physics)
                .collidable()
                .build();
    }
    @Spawns("c")
    public Entity newC(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));

        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_8.png",  80, 80))
                .with(new HealthIntComponent(8))
                .with(physics)
                .collidable()
                .build();
    }
    @Spawns("d")
    public Entity newD(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_16.png",  80, 80))
                .with(new HealthIntComponent(16))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("e")
    public Entity newE(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_32.png",  80, 80))
                .with(new HealthIntComponent(32))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("f")
    public Entity newF(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_64.png",  80, 80))
                .with(new HealthIntComponent(64))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("g")
    public Entity newG(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_128.png",  80, 80))
                .with(new HealthIntComponent(128))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("h")
    public Entity newH(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_256.png",  80, 80))
                .with(new HealthIntComponent(256))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("i")
    public Entity newI(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_512.png",  80, 80))
                .with(new HealthIntComponent(512))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("j")
    public Entity newJ(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .with(new HealthIntComponent(2))
                .viewWithBBox(texture("bookblock_1024.png", 80, 80))
                .with(new HealthIntComponent(1024))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("k")
    public Entity newK(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.03f));
        return entityBuilder(data)
                .type(FinalWeekType.BOOKBLOCK)
                .viewWithBBox(texture("bookblock_2048.png",  80, 80))
                .with(new HealthIntComponent(2048))
                .with(physics)
                .collidable()
                .build();
    }
}
