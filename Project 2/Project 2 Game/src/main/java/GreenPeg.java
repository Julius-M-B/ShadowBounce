/**
 * @author Julius Miguel Bernaudo 994332
 */
import java.util.ArrayList;
import java.util.Random;

/**
 * Greenpeg will provide 2 extra balls upon collision
 */
public class GreenPeg implements Generated{
    private static final int ZERO = 0;
    private static final String GREY = "grey";
    private static final String RED = "red";
    private static final String GREEN_VERT = "green-vertical-peg";
    private static final String GREEN_HORI = "green-horizontal-peg";
    private static final String GREEN_PEG = "green-peg";
    private static final String BLUE_PEG = "peg";
    private static final String BLUE_VERT = "vertical-peg";
    private static final String BLUE_HORI = "horizontal-peg";
    private static final int VERTICAL = 12;
    private static final int HORIZONTAL = 14;

    /**
     * returns the previous location of the greenpeg back to blue
     * @param pegType is the storage for each peg's type
     * @param prevPos the previous location of the green peg
     */
    public void returnToBlue(ArrayList<String> pegType, int prevPos) {
        if (pegType.get(prevPos).equals(GREEN_HORI)) {
            pegType.set(prevPos, BLUE_HORI);
        } else if (pegType.get(prevPos).equals(GREEN_VERT)){
            pegType.set(prevPos, BLUE_VERT);
        } else {
            pegType.set(prevPos, BLUE_PEG);
        }
    }

    /**
     * will spawn the green peg on any given blue peg on the board
     * @param pegPos is the storage for each pegs position
     * @param pegType is the storage for each peg's type
     * @param totalPegs the total number of pegs present on board
     * @return the index of the peg chosen
     */
    @Override
    public int spawn(ArrayList<Peg> pegPos, ArrayList<String> pegType, int totalPegs) {
        Random random = new Random();
        int r = random.nextInt(totalPegs);
        String[] tmp = pegType.get(r).split("-");

        while (tmp[ZERO].equals(GREY) || tmp[ZERO].equals(RED) || pegPos.get(r) == null) {
            r = random.nextInt(totalPegs);
            tmp = pegType.get(r).split("-");
        }

        if (pegType.get(r).length() == VERTICAL){
            pegType.set(r, GREEN_VERT);
        } else if (pegType.get(r).length() == HORIZONTAL){
            pegType.set(r, GREEN_HORI);
        } else {
            pegType.set(r, GREEN_PEG);
        }
        return r;
    }
}
