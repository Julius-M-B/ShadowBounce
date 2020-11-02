/**
 * @author Julius Miguel Bernaudo 994332
 */
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;

public class FireBall extends Ball {
    private Vector2 velocity;
    private static final double GRAVITY = 0.15;
    private static final double SPEED = 10;

    /**
     * Fireball is similar to a ball except it destroys other pegs in a 70 pixel radius
     * @param point is used to generate its initial position
     * @param direction is used to provide a direction the ball moves towards
     * @param imageSrc the image source which the object will appear as
     */
    public FireBall(Point point, Vector2 direction, String imageSrc) {
        super(point, direction, imageSrc);
        velocity = getVelocity();
    }

    /**
     * changes of the velocity of the fireball upon collision
     * @param pegPos contains all the positions of the pegs
     * @param pegIndex contains the index at which peg
     */
    @Override
    public void bounce(ArrayList<Peg> pegPos, int pegIndex) {
        super.bounce(pegPos, pegIndex);
        velocity = getVelocity();
    }

    /**
     * updates the balls velocity and bounces it off the walls if it collides with it
     */
    @Override
    public void update() {
        velocity = velocity.add(Vector2.down.mul(GRAVITY));
        super.move(velocity);

        if (super.getRect().left() < 0 || super.getRect().right() > Window.getWidth()) {
            velocity = new Vector2(-velocity.x, velocity.y);
        }

        super.draw();
    }
}
