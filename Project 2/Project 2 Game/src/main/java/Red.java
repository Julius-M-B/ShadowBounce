/**
 * @author Julius Miguel Bernaudo 994332
 */
import java.util.ArrayList;
import java.util.Random;

/**
 * The red pegs are the prime objective of the game, once all red pegs are destroyed the
 * next board is loaded. Each peg is the same as a blue in which it can be destroyed
 */
public class Red implements Generated{
    private static final String RED_VERT = "red-vertical-peg";
    private static final String RED_HORI = "red-horizontal-peg";
    private static final String RED_PEG = "red-peg";
    private static final int VERTICAL = 12;
    private static final int HORIZONTAL = 14;
    private static ArrayList<Integer> redList = new ArrayList<>();
    private static int counter = 0;
    private static final int ZERO = 0;
    private static final String GREY = "grey";

    /**
     * spawns in the red pegs at the random chosen blue pegs
     * @param pegPos contains the positions of all the pegs
     * @param pegType contains which type is located at each peg
     * @param totalPegs the total number of pegs involved to provide a random index
     * @return the index at which the red peg is spawned at
     */
    @Override
    public int spawn(ArrayList<Peg> pegPos, ArrayList<String> pegType, int totalPegs) {
        // generating a random index
        Random random = new Random();
        int r = random.nextInt(totalPegs);
        String[] tmp = pegType.get(r).split("-");
        // making sure it is not a grey peg
        while (tmp[ZERO].equals(GREY)){
            r = random.nextInt(totalPegs);
            tmp = pegType.get(r).split("-");
        }
        // a counter is used so that a red peg does not overlap with another red peg
        if (counter != ZERO){
            int i = 0;
            // iterates through the list of red pegs and changes if they overlap with a grey or red
            while (i < redList.size()) {
                if (redList.get(i) == r) {
                    r = random.nextInt(totalPegs);
                    tmp = pegType.get(r).split("-");
                    while (tmp[ZERO].equals(GREY)){
                        r = random.nextInt(totalPegs);
                        tmp = pegType.get(r).split("-");
                    }
                    i = 0;
                } else {
                    i++;
                }
            }
        }

        if (pegType.get(r).length() == VERTICAL){
            pegType.set(r, RED_VERT);
        } else if (pegType.get(r).length() == HORIZONTAL){
            pegType.set(r, RED_HORI);
        } else {
            pegType.set(r, RED_PEG);
        }

        redList.add(r);
        counter++;
        return r;
    }
}
