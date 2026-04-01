import java.util.List;

public interface DanceFormation extends DanceFormationKernel {

    /**
     * Moves to the next formation slide.
     *
     * @updates this
     * @requires current formation is not the last formation
     * @ensures current formation = #current formation + 1
     */
    void nextForm();

    /**
     * Moves to the previous formation slide.
     *
     * @updates this
     * @requires current formation is not the first formation
     * @ensures current formation = #current formation - 1
     */
    void previousForm();

    /**
     * Returns the total number of formations.
     *
     * @return number of formation slides
     * @ensures numberOfForms = total formations stored
     */
    int totalFormations();

    /**
     * Checks if a position is empty in the current formation.
     *
     * @param position
     *            position in formation
     * @return true if position has no dancer
     * @ensures positionEmpty = true iff no dancer occupies position
     */
    boolean positionEmpty(int x, int y);

    /**
     * Checks if a dancer appears in the current formation.
     *
     * @param name
     *            dancer name
     * @return true if dancer appears in formation
     * @ensures dancerInFormation = true iff name appears in formation
     */
    boolean dancerInFormation(String name);

    /**
     * Returns all dancers in the current formation.
     *
     * @return list of dancers currently in the formation
     * @ensures return contains exactly the dancers occupying positions in the
     *          current formation
     */
    List<String> dancersInFormation();
}