/**
 * Sample solution for SWEN20003 Object Oriented Software Development
 * Project 1, Semester 2, 2019
 *
 * @author Eleanor McMurtry
 *
 * Rest of the code
 * @author Julius Miguel Bernaudo 994332
 */

import bagel.util.Point;

/**
 * The pegs are the objects in the game that the ball destroys upon impact.
 * This peg class is the base for the pegs and their behaviour
 */
public class Peg extends Sprite {
    public Point position;
    public String shape;

    /**
     * Upon initial creation the Peg will have a determined type and position
     * @param point the position the ball spawns in
     * @param type the type of peg that is created
     */
    public Peg(Point point, String type) {
        super(point, "res/"+type+".png");
        this.position = point;
        this.shape = type;
    }

    /**
     * an update cycle to continuously draw the image of the peg
     */
    @Override
    public void update() {
        super.draw();
    }
}
