# FinalWeek2048
FXGLGame
![](https://img2020.cnblogs.com/blog/2532168/202112/2532168-20211225211701716-1856489173.jpg)

> 一个 uml 课程的大作业，项目要求设计并开发一款 2048 与某种游戏类型相结合的创新游戏。可以选择只建模或者既建模又实现，既然要做当然是选择实现啦（虽然没有接触过游戏...期末周的莽冲hhh，小组内我负责代码实现，用的是基于JavaFX的游戏开发框架FXGL

### 游戏介绍
##### 游戏背景
因为这次大作业赶上期末复习周，我们游戏的名字就叫**FinalWeek2048**，融入了我们学校期末周的背景。以2048游戏元素为主，加入**双人竞技**，**碰撞**，**放置**的创新元素。
2048中从2-2048共11种方块，我们用C, C++, java, Python，数据库，数据结构，计算机组成原理，计算机网络，Web开发，HTML，UML共11中课程方块替代，分值上和2048中相对应。
![游戏元素](https://img2020.cnblogs.com/blog/2532168/202201/2532168-20220105231000086-1467978063.png)
##### 游戏玩法
玩家一和玩家二通过按键控制角色，随机释放不同的方块，所放置的方块具有重力属性，放置后自由下坠，触碰到其他不同的方块或游戏边界则停留，若与其他相同的方块相撞则合成一个更加高级的方块，并加上等级相应的分数。
游戏结束条件：堆积的方块触到玩家所站的平台或一方胜利
胜利条件：a. 率先得到2048分；b. 率先合成UML方块；c. 一方触顶，另一方胜利

### [游戏开发框架FXGL](https://github.com/AlmasB/FXGL)
以下是FXGL在Github上对自己的介绍和定位（直接谷歌生翻的
![image](https://img2020.cnblogs.com/blog/2532168/202201/2532168-20220105232843246-1116050885.png)

我选择使用FXGL，首先是考虑如果在这么短的时间去重新学习目前比较热门的游戏框架难度更大，然后之前已经学习了JavaFX正好FXGL是基于它的，学习成本会小很多，还能锻炼一下。
事实证明，我想多了，阻力比我想象的大，FXGL 在国内外都比较冷门，在国内资料几乎没有，官方给的 wiki 不能满足我的需求，再配上官方的游戏 demo，勉强做了下来。（感觉如果之前用过Unity或者Cocos这样的框架会好学很多，因为官方也提到了 FXGL 是参照热门的一些游戏框架开发的）
然后就是我过菜的英文水平！基本的就是应付官方文档和 wiki，非常关键在于在社区找 bug 的解决方案的时候...翻译不通
还有一点，FXGL的API文档版本很旧，目前没有发布最新的但框架好像已经更到了17...

### 成果展示
![image](https://img2020.cnblogs.com/blog/2532168/202112/2532168-20211225211701716-1856489173.jpg)

##### 1. 主菜单界面
主菜单是继承了MainMenu自己重新写了个，蓝色是糊掉了学校信息，还加上了与期末周格格不入的欢快背景音乐
还有个游戏结束时的子菜单，直接用了内置的SimpleGameMenu。
![image](https://img2020.cnblogs.com/blog/2532168/202201/2532168-20220106001928789-561546656.png)

##### 2. 游戏主界面
虽然有素材，但没有给两个玩家加上移动的动态动作，当时时间赶没有分开写。背景是我们学校的一个塔，放这里还挺合适的。方块碰撞消除会有音效和粒子碰撞效果~
![image](https://img2020.cnblogs.com/blog/2532168/202201/2532168-20220106001719950-85229636.png)

### 实现细节
以下是在实现过程中我觉得难度较大的部分，可以先跳过看源码了解整体的框架
1. **在工厂中对方块实体的设置**
要使得实体间产生碰撞或者具有重力，则需要把它们加入物理世界并且设置参数。
关于相同的方块碰撞加分，就是赋予了方块实体HealthIntComponent部件（一般在游戏中是血条），让它具有一个整型参数属性，应该还有更好的处理方法，然后为了方便布置关卡，我是在工厂写出了这11中方块实体从a-k。
```java
    @Spawns("a")
    public Entity newA(SpawnData data) {
		// 这里就是设置方块的物理属性，设置密度，能在物理世界自由下坠
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
```
2. **方块之间和方块与平台的碰撞处理**
不同方块直接堆叠，相同方块碰撞后移除游戏世界产生特效实体以及新的方块实体
处理碰撞的计分归属，是在随机参数的方块加了IDComponent来识别是玩家一还是玩家二放置的。
```java
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
	// 处理方块与玩家所站的平台底部的碰撞
        physicsWorld.addCollisionHandler(new CollisionHandler(FinalWeekType.BOOKBLOCK, FinalWeekType.PLATFORM) {
            @Override
            protected void onCollision(Entity block, Entity platform) {
                if(block.getComponent(IDComponent.class).getName() == "firstPlayerScore") showGameOver("玩家二");
                    else showGameOver("玩家一");
            }
        });
    }
```
