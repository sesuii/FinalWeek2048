package com.krillsong.finalweek2048;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/*________________________
 @ Author: _Krill
 @ Data: 2021/12/23 22:58 
 @ Version: 1.0
__________________________*/

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ColorBlock extends Rectangle {

    public ColorBlock(int size, Color color) {
        super(size, size, color);

        setArcWidth(8);
        setArcHeight(8);

        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(2.5);
        setStroke(Color.color(0.138, 0.138, 0.375, 0.66));

        if (!FXGL.isMobile()) {
            var shadow = new InnerShadow(25, Color.BLACK);
            shadow.setOffsetX(-3);
            shadow.setOffsetY(-3);

            setEffect(shadow);
        }
    }
}
