/**
 * Sample solution for SWEN20003 Object Oriented Software Development
 * Project 1, Semester 2, 2019
 *
 * @author Eleanor McMurtry
 *
 * Rest of the code
 * @author Julius Miguel Bernaudo 994332
 */

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * An abstract class that stands a base for each object in the game
 * providing it an image and a rectangle (a hitbox in a sense) to each object
 */
public abstract class Sprite {
    private Image image;
    private Rectangle rect;

    /**
     * generates the image at the point using the imagesrc
     * @param point the coordinates the image will be drawn at
     * @param imageSrc the type of image being drawn
     */
    public Sprite(Point point, String imageSrc) {
        image = new Image(imageSrc);
        rect = image.getBoundingBoxAt(point);
    }

    /**
     * gets the rectangle of the sprite object
     * @return the rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * sets the rectangle for the object
     * @param rect the rectangle it is being changed to
     */
    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    /**
     * checks if the two sprites intersect each other by checking their rectangles
     * @param other the other sprite that it being collided with
     * @return true or false if the two objects collided with their rectangles
     */
    public boolean intersects(Sprite other) {
        return rect.intersects(other.rect);
    }

    /**
     * moves the object in the direction provided
     * @param dx the amount it is moved by
     */
    public void move(Vector2 dx) {
        rect.moveTo(rect.topLeft().asVector().add(dx).asPoint());
    }

    /**
     * draws the object at the centre of its rect
     */
    public void draw() {
        image.draw(rect.centre().x, rect.centre().y);
    }

    /**
     * abstract update cycle so that each object has an update cycle unique to itself
     */
    public abstract void update();
}
