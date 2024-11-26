package us.jonathans.entity.sprites;

import us.jonathans.entity.geom.Align;
import us.jonathans.entity.geom.CircleBoundingBox;
import us.jonathans.entity.geom.Obj2;

import java.awt.*;

public class Stone extends CircleBoundingBox implements Drawable {
    private final Obj2 shadow;
    private final Color color;

    public Stone(int x, int y, int width, int height, Align align, Color color) {
        super(x, y, width, height, align);

        this.color = color;
        shadow = this.copy();
        shadow.grow(3, 3);
    }

    @Override
    public void draw(Graphics g) {
        shadow.fillOval(g, Color.BLACK);
        this.fillOval(g, color);
    }
}