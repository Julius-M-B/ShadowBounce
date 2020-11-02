/**
 * Sample solution for SWEN20003 Object Oriented Software Development
 * Project 1, Semester 2, 2019
 *
 * @author Eleanor McMurtry
 *
 * Rest of the code
 * @author Julius Miguel Bernaudo 994332
 */

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ShadowBounce extends AbstractGame {
    private Bucket bucket;
    private Ball[] balls = new Ball[3];
    private GreenPeg greenPeg;
    private Powerup powerup = null;
    private static final Point BALL_POSITION = new Point(512, 32);
    private static final Point BUCKET_POSITION = new Point(512, 744);
    private int pegCounter = 0;
    /**
     * The Arraylist will hold what type of peg (grey, blue, green, red) while the pegpos will store the positions
     * of each peg as an object of peg.
     */
    public ArrayList<String> pegType = new ArrayList<>();
    public ArrayList<Peg> pegPos = new ArrayList<>();
    private static final int TYPE = 0;
    private static final int X_POS = 1;
    private static final int Y_POS = 2;
    private static final String GREEN = "green";
    private static final String BLUE = "blue";
    private static final String GREY = "grey";
    private static final String RED = "red";
    private static final double ONE_FIFTH = 0.2;
    private static final int SPAWN_CHANCE = 1;
    private static final int ZERO = 0;
    private static final int LAST_BOARD = 5;
    private static final double RADIUS = 70;
    private Rectangle explosionRadius;
    /**
     * totalRedPegs is calculated using the ONE_FIFTH of the total pegs counted
     */
    public int totalRedPegs = 0;
    private static int board = 0;
    private int totalShots = 20;
    private int prevPos;
    private static Point prevPoint;
    private static final int OUT_OF_TEN = 10;
    private static final Vector2 DIAG_RIGHT = new Vector2(1, -1);
    private static final Vector2 DIAG_LEFT = new Vector2(-1, -1);
    private int hitOnce = 0;
    private int currentRedPegs;


    /**
     * The game ShadowBounce in which a ball will strike a peg and the aim is to destroy all red pegs and advance to
     * the next board until all boards are complete.
     * The constructor which calls the loadboard method so that the board is loaded.
     */
    public ShadowBounce() {
        loadboard();
    }

    /**
     * loadboard will load one board at a time storing all the pegs and displaying them on the screen, whilst
     * also resetting all necessary values when the next board is loaded.
     */
    public void loadboard() {
        totalShots = 20;
        totalRedPegs = 0;
        pegCounter = 0;
        pegType.clear();
        pegPos.clear();
        Arrays.fill(balls, null);
        // using a try and catch to read the csv file
        try {
            BufferedReader file = new BufferedReader(new FileReader("res/"+board+".csv"));
            String row;
            // reading the line and splitting it up into each component
            while ((row = file.readLine()) != null) {
                String[] storage = row.split(",");
                String[] type = storage[TYPE].split("_");

                // If the type contains 3 segments it means its a horizontal or vertical peg and will store it.
                if (type.length == 3){
                    if (type[0].equals(BLUE)){
                        pegType.add(type[2]+"-"+type[1]);
                    } else {
                        pegType.add(type[0]+"-"+type[2]+"-"+type[1]);
                    }
                } else {
                    if (type[0].equals(BLUE)){
                        pegType.add(type[1]);
                    } else {
                        pegType.add(type[0]+"-"+type[1]);
                    }
                }
                // Creates the centres and stores them as a Peg object
                Point p = new Point(Double.parseDouble(storage[X_POS]), Double.parseDouble(storage[Y_POS]));
                pegPos.add(new Peg(p, pegType.get(pegCounter)));
                pegCounter++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Generate the red pegs and store them
        totalRedPegs = (int)(pegCounter * ONE_FIFTH);
        for (int i = 0; i < totalRedPegs; i++){
            Red redPeg = new Red();
            int index = redPeg.spawn(pegPos, pegType, pegCounter);
            pegPos.set(index, new Peg(pegPos.get(index).position, pegType.get(index)));
        }

        // generate the first green peg and store it
        greenPeg = new GreenPeg();
        int index = greenPeg.spawn(pegPos, pegType, pegCounter);
        pegPos.set(index, new Peg(pegPos.get(index).position, pegType.get(index)));
        prevPos = index;

        bucket = new Bucket(BUCKET_POSITION, "res/bucket.png");
    }

    /**
     * the spawnGenerator will generate a number out of 10 so that the powerup has a 1/10 chance
     * @return the number generated to be compared
     */
    public int spawnGenerator() {
        Random random = new Random();
        int r = random.nextInt(OUT_OF_TEN);
        return r;
    }

    /**
     * Checks if the ball fell out of the screen and sets it to null
     * @param ball is the Ball object
     * @return the new Ball incase it was set to null
     */
    public Ball checkOnScreen(Ball ball) {
        // Delete the ball when it leaves the screen
        if (ball.outOfScreen()) {
            ball = null;
            // hitOnce is used so that the ball only connects with the bucket once.
            hitOnce = 0;
        }
        return ball;
    }

    /**
     * generates a random direction for the powerup
     * @return the direction the powerup will move as a Vector2
     */
    public Vector2 randomDirection() {
        Random random = new Random();
        double x = random.nextDouble()*Window.getWidth();
        double y = random.nextDouble()*Window.getHeight();
        Vector2 direction = new Vector2(x, y);
        return direction;
    }

    /**
     * generates the first initial spawn for the powerup
     * @return the point the powerup spawns at
     */
    public Point randomInitialPos() {
        Random random = new Random();
        double x = random.nextDouble()*Window.getWidth();
        double y = random.nextDouble()*Window.getHeight();
        Point p = new Point(x, y);
        return p;
    }

    @Override
    protected void update(Input input) {
        currentRedPegs = 0;
        bucket.update();
        // Check all non-deleted pegs for intersection with the ball
        for (int i = 0; i < pegCounter; ++i) {
            String[] tmp = pegType.get(i).split("-");
            // Check all balls if on screen for intersection with any object in the game
            for (int j = 0; j < balls.length; j++) {
                if (balls[j] != null) {
                    // Generates a fireball if the ball hits the powerup
                    if (powerup != null && balls[j].intersects(powerup)) {
                        powerup = null;
                        balls[j] = new FireBall(balls[j].getRect().centre(), balls[j].getVelocity().normalised(),
                                "res/Fireball.png");
                    }
                    // if the ball hits the bucket +1 to the total shots and only collides once.
                    if (bucket != null && balls[j].intersects(bucket) && hitOnce == 0) {
                        hitOnce++;
                        totalShots++;
                    }
                    // Checks for collisions with pegs
                    if (pegPos.get(i) != null) {
                        if (balls[j] != null && balls[j].intersects(pegPos.get(i)) && tmp[ZERO].equals(GREY)) {
                            balls[j].bounce(pegPos, i);
                        }
                        if (balls[j] != null && balls[j].intersects(pegPos.get(i)) && !(tmp[ZERO].equals(GREY))) {
                            // If it was red minus from the totalRedPegs
                            if (tmp[ZERO].equals(RED)) {
                                totalRedPegs--;
                            }
                            // If it was green generate 2 more balls of the same type that collided with the peg
                            if (tmp[ZERO].equals(GREEN)) {
                                if (balls[j] instanceof FireBall) {
                                    balls[1] = new FireBall(pegPos.get(i).position, DIAG_RIGHT,
                                            "res/Fireball.png");
                                    balls[2] = new FireBall(pegPos.get(i).position, DIAG_LEFT,
                                            "res/Fireball.png");
                                } else {
                                    balls[1] = new Ball(pegPos.get(i).position, DIAG_RIGHT, "res/ball.png");
                                    balls[2] = new Ball(pegPos.get(i).position, DIAG_LEFT, "res/ball.png");
                                }
                            }
                            // bounce the ball
                            balls[j].bounce(pegPos, i);
                            // If the ball is a fireball destroy within a 70 pixel radius upon collision
                            if (balls[j] instanceof FireBall) {
                                for (int k = 0; k < pegCounter; k++) {
                                    if (pegPos.get(k) != null && pegPos.get(i) != null) {
                                        String[] temp = pegType.get(k).split("-");
                                        explosionRadius = new Rectangle(pegPos.get(i).position.x,
                                                pegPos.get(i).position.y, RADIUS, RADIUS);
                                        pegPos.get(i).setRect(explosionRadius);
                                        if (pegPos.get(i).intersects(pegPos.get(k)) && !(temp[ZERO].equals(GREY))) {
                                            if (tmp[ZERO].equals(RED)) {
                                                totalRedPegs--;
                                            }
                                            if (tmp[ZERO].equals(GREEN)) {
                                                if (balls[j] instanceof FireBall) {
                                                    balls[1] = new FireBall(pegPos.get(i).position, DIAG_RIGHT,
                                                            "res/Fireball.png");
                                                    balls[2] = new FireBall(pegPos.get(i).position, DIAG_LEFT,
                                                            "res/Fireball.png");
                                                }
                                            }
                                            pegPos.set(k, null);
                                        }
                                    }
                                }
                            }
                            // sets the peg to null upon collision
                            pegPos.set(i, null);
                        }
                    }
                }
            }
            if (pegPos.get(i) != null) {
                pegPos.get(i).update();
            }
        }

        // counts the current red pegs
        for (int i = 0; i < pegCounter; i++) {
            if (pegPos.get(i) != null) {
                String[] tmp = pegType.get(i).split("-");
                if (tmp[ZERO].equals(RED)) {
                    currentRedPegs++;
                }
            }
        }


        // If all red pegs are gone, load the next board
        if (currentRedPegs == ZERO) {
            board++;
            if (board == LAST_BOARD) {
                System.out.println("Congratulations you have completed ShadowBounce!");
                Window.close();
            } else {
                loadboard();
            }
        }


        // If we don't have a ball and the mouse button was clicked, create one
        if (input.wasPressed(MouseButtons.LEFT) && balls[0] == null && balls[1] == null && balls[2] == null) {
            if (totalShots == ZERO) {
                System.out.println("Zero shots remaining, game over.");
                Window.close();
                return;
            }
            balls[0] = new Ball(BALL_POSITION, input.directionToMouse(BALL_POSITION), "res/ball.png");
            totalShots--;
            System.out.println(totalShots+" shots remaining");

        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        // If powerup is still on screen then update its position and generate a new destination
        if (powerup != null) {
            powerup.update();
            if (powerup.getRect().intersects(powerup.destination)) {
                powerup = new Powerup(prevPoint, randomDirection());
                prevPoint = powerup.destination.centre();
            }
        }

        // updates the position of the ball
        for (int i = 0; i < balls.length; i++) {
            if (balls[i] != null){
                balls[i].update();
                // Checks if the ball on screen and deletes it when it leaves the screen
                balls[i] = checkOnScreen(balls[i]);
                // This is a turn, once all three potential balls are off screen
                if (balls[0] == null && balls[1] == null && balls[2] == null) {
                    // if green peg was not destroyed it will convert it back to blue
                    if (pegPos.get((prevPos)) != null) {
                        greenPeg.returnToBlue(pegType, prevPos);
                        pegPos.set(prevPos, new Peg(pegPos.get(prevPos).position, pegType.get(prevPos)));
                    }
                    // spawns a new green peg
                    int index = greenPeg.spawn(pegPos, pegType, pegCounter);
                    pegPos.set(index, new Peg(pegPos.get(index).position, pegType.get(index)));
                    prevPos = index;
                    // Checks after a turn to see if the powerup will spawn next turn
                    if (powerup == null) {
                        if (spawnGenerator() == SPAWN_CHANCE) {
                            powerup = new Powerup(randomInitialPos(), randomDirection());
                            prevPoint = powerup.destination.centre();
                        }
                    } else {
                        if (spawnGenerator() == SPAWN_CHANCE) {
                            powerup = new Powerup(randomInitialPos(), randomDirection());
                            prevPoint = powerup.destination.centre();
                        } else {
                            powerup = null;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new ShadowBounce().run();
    }
}
