/**
 * Sample solution for SWEN20003 Object Oriented Software Development
 * Project 1, Semester 2, 2019
 *
 * @author Eleanor McMurtry
 *
 * Rest of the code
 * @author Julius Miguel Bernaudo 994332
 */

import bagel.Window;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;
import java.util.ArrayList;

/**
 * The ball is the main object within the ShadowBounce and is used to destroy the pegs and interact
 * with each object.
 */
public class Ball extends Sprite {
    private Vector2 velocity;
    private static final double GRAVITY = 0.15;
    private static final double SPEED = 10;

    /**
     * Contructor which generates an instance of the ball upon creation
     * @param point the position of the ball
     * @param direction the direction in which it moves (towards the mouse)
     * @param imageSrc the image depending if it is a ball or fireball
     */
    public Ball(Point point, Vector2 direction, String imageSrc) {
        super(point, imageSrc);
        velocity = direction.mul(SPEED);
    }


    /**
     * gets the velocity of the ball currently
     * @return the velocity
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * checks if the ball is out of the screen
     * @return true or false if the ball is outside the screen
     */
    public boolean outOfScreen() {
        return super.getRect().top() > Window.getHeight();
    }

    /**
     * checks which side of the peg the ball hit and will change the velocity
     * @param pegPos contains all the positions of the pegs
     * @param pegIndex contains the index at which peg
     */
    public void bounce(ArrayList<Peg> pegPos, int pegIndex) {
        Side collision = getRect().intersectedAt(getRect().centre(), pegPos.get(pegIndex).getRect().centre());
        // If hit on the top or bottom, the velocity is reverse vertically
        if (collision == Side.BOTTOM || collision == Side.TOP) {
            velocity = new Vector2(velocity.x, -velocity.y);
        }
        // If hit on the left or right, the velocity is reverse horizontally
        if (collision == Side.LEFT || collision == Side.RIGHT) {
            velocity = new Vector2(-velocity.x, velocity.y);
        }
    }

    /**
     * updates the ball, the velocity is changed each update and checks if the ball collides
     * with the sides of the screens and reverses its direction horizontally
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
