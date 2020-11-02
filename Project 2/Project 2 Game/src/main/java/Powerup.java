/**
 * @author Julius Miguel Bernaudo 994332
 */
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;


public class Powerup extends Sprite{
    private Vector2 velocity;
    private static final double SPEED = 3;
    private static final double RADIUS = 5;
    /**
     * the destination is used as a directional influence for the powerup and is also used to generate
     * the powerup once the powerup is within 5 pixels of its destination
     */
    public Rectangle destination;

    /**
     * Powerup provides a fireball once destroyed by the ball
     * @param point is used to generate its initial position
     * @param direction is used to provide the object a direction to move towards
     */
    public Powerup(Point point, Vector2 direction) {
        super(point, "res/powerup.png");
        velocity = direction.sub(point.asVector()).normalised().mul(SPEED);
        destination = new Rectangle(direction.x, direction.y, RADIUS, RADIUS);
    }

    /**
     * update the powerup's velocity and draw its image
     */
    @Override
    public void update() {
        super.move(velocity);
        super.draw();
    }
}
