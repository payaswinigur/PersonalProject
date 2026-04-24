package Components;

import java.util.ArrayList;
import java.util.List;

/**
 * Secondary abstract implementation of DanceFormation interface.
 *
 * <p>
 * <b>Overview:</b><br>
 * DanceFormationSecondary is an abstract class that provides default
 * implementations for all convenience methods defined in the DanceFormation
 * interface. It serves as an intermediate layer between the interface contract
 * and concrete implementations, reducing code duplication by implementing the
 * secondary (convenience) methods.
 * </p>
 *
 * <p>
 * <b>Design Pattern:</b><br>
 * This class follows the Template Method pattern:
 * <ul>
 * <li>It implements all DanceFormation secondary methods</li>
 * <li>These methods delegate to the kernel methods (abstract from
 * DanceFormationKernel)</li>
 * <li>Concrete subclasses must implement only the kernel methods</li>
 * <li>All secondary methods are immediately available to concrete
 * subclasses</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Responsibility:</b><br>
 * This class implements the following secondary methods from DanceFormation:
 * <ul>
 * <li>{@link #nextForm()} - advances to the next formation slide</li>
 * <li>{@link #previousForm()} - goes to the previous formation slide</li>
 * <li>{@link #totalFormations()} - returns the total number of formations</li>
 * <li>{@link #positionEmpty(int, int)} - checks if a position is empty</li>
 * <li>{@link #dancerInFormation(String)} - searches for a dancer by name</li>
 * <li>{@link #dancersInFormation()} - returns all dancers in current
 * formation</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Subclass Requirements:</b><br>
 * Concrete subclasses must implement all kernel methods from
 * DanceFormationKernel:
 * <ul>
 * <li>setRoster(List&lt;String&gt;)</li>
 * <li>addDancer(String, int, int)</li>
 * <li>removeDancer(int, int)</li>
 * <li>dancerAt(int, int)</li>
 * <li>addFormation()</li>
 * <li>removeFormation()</li>
 * <li>setCurrentFormation(int)</li>
 * <li>numberOfFormations()</li>
 * <li>currentFormation()</li>
 * <li>formationWidth()</li>
 * <li>formationHeight()</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Conventions:</b><br>
 * Inherits all conventions from DanceFormationKernel
 * </p>
 *
 * <p>
 * <b>Representation Invariant:</b><br>
 * Inherits all invariants from DanceFormationKernel
 * </p>
 *
 * @author Payaswini Gurung
 * @version 1.0
 * @since 1.0
 *
 * @see DanceFormation
 * @see DanceFormationKernel
 * @see DanceFormation1
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
            if (dancer.equals(name)) {
                return true;
            }
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