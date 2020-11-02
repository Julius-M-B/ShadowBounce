/**
 * @author Julius Miguel Bernaudo 994332
 */

import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

public class Bucket extends Sprite{
    private static final double SPEED = 4;
    private Vector2 velocity;
    private static final Vector2 DIRECTION_LEFT = new Vector2(-1,0);
    private static final double BUCKET_HEIGHT = 48;
    private static final double BUCKET_WIDTH = 72;
    private static final Rectangle BUCKET_RECT = new Rectangle(512, 712, BUCKET_WIDTH, BUCKET_HEIGHT);

    /**
     * The bucket will provide an extra shot upon colliding with the ball
     * @param point is used to generate its initial position
     * @param imageSrc is used to generate its sprite image
     */
    public Bucket(Point point, String imageSrc) {
        super(point, imageSrc);
        velocity = DIRECTION_LEFT.mul(SPEED);
        setRect(BUCKET_RECT);
    }

    /**
     * Update the buckets movement and check if it will collide with the sides
     */
    @Override
    public void update() {
        super.move(velocity);

        if (super.getRect().left() < 0 || super.getRect().right() > Window.getWidth()) {
            velocity = new Vector2(-velocity.x, velocity.y);
        }

        super.draw();

    }
}
