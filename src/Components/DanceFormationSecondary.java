import java.util.ArrayList;
import java.util.List;

/**
 * Secondary implementation of DanceFormation interface. Provides default
 * implementations for some methods, while leaving others abstract for concrete
 * subclasses to implement.
 *
 * @author Payaswini Gurung
 */
public abstract class DanceFormationSecondary implements DanceFormation {

    /**
     * Moves to the next formation slide.
     *
     * @updates this
     * @requires current formation is not the last formation
     * @ensures current formation = #current formation + 1
     */
    @Override
    public void nextForm() {
        this.setCurrentFormation(this.currentFormation() + 1);
    }

    /**
     * Moves to the previous formation slide.
     *
     * @updates this
     * @requires current formation is not the first formation
     * @ensures current formation = #current formation - 1
     */
    @Override
    public void previousForm() {
        this.setCurrentFormation(this.currentFormation() - 1);
    }

    /**
     * Returns the total number of formations.
     *
     * @return number of formation slides
     * @ensures totalFormations = total formations stored
     */
    @Override
    public int totalFormations() {
        return this.numberOfFormations();
    }

    /**
     * Checks if a position is empty in the current formation.
     *
     * @param x
     *            row index
     * @param y
     *            column index
     * @return true if position has no dancer
     * @ensures positionEmpty = true iff no dancer occupies position
     */
    @Override
    public boolean positionEmpty(int x, int y) {
        return this.dancerAt(x, y) == null;
    }

    /**
     * Checks if a dancer appears in the current formation. Default
     * implementation uses dancersInFormation().
     *
     * @param name
     *            dancer name
     * @return true if dancer appears in formation
     * @ensures dancerInFormation = true iff name appears in formation
     */
    @Override
    public boolean dancerInFormation(String name) {
        for (String dancer : this.dancersInFormation()) {
            if (dancer.equals(name))
                return true;
        }
        return false;
    }

    /**
     * Returns all dancers in the current formation.
     *
     * @return list of dancers currently in the formation
     * @ensures return contains exactly the dancers occupying positions in the
     *          current formation
     */
    @Override
    public List<String> dancersInFormation() {
        List<String> dancers = new ArrayList<>();

        int width = this.formationWidth();
        int height = this.formationHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String dancer = this.dancerAt(x, y);
                if (dancer != null) {
                    dancers.add(dancer);
                }
            }
        }
        return dancers;
    }
}