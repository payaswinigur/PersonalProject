package Components;

import java.util.List;

/**
 * Kernel interface for managing dance formations with multiple slides.
 *
 * <p>
 * <b>Overview:</b><br>
 * DanceFormationKernel defines the core contract for managing a collection of
 * dance formation slides. Each formation is a 2D grid where dancers can be
 * positioned. This interface provides fundamental operations for
 * adding/removing dancers, managing multiple formation slides, and maintaining
 * a roster of available dancers.
 * </p>
 *
 * <p>
 * <b>Functionality:</b><br>
 * This kernel interface supports:
 * <ul>
 * <li>Setting and managing a roster of dancers</li>
 * <li>Adding and removing individual dancers from the current formation</li>
 * <li>Creating, removing, and navigating between formation slides</li>
 * <li>Querying dancer positions and formation dimensions</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Conventions:</b><br>
 * <ul>
 * <li>formations: A collection of 2D arrays, each representing a single
 * slide</li>
 * <li>current: The index of the currently active formation slide</li>
 * <li>width x height: The fixed dimensions of all formation grids</li>
 * <li>Null entries: Represent empty positions in the formation grid</li>
 * <li>Roster: A list of dancer names that can be placed in formations</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Representation Invariant:</b><br>
 * <ul>
 * <li>The formation collection is not null and contains at least one
 * formation</li>
 * <li>0 &lt;= current &lt; numberOfFormations()</li>
 * <li>All formations have identical dimensions (width x height)</li>
 * <li>width &gt; 0 and height &gt; 0</li>
 * <li>The roster is not null (may be empty)</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Correspondence:</b><br>
 * The abstract data type represents: (formations, current, roster, width,
 * height)
 * <ul>
 * <li>Current formation = formations[current]</li>
 * <li>Each cell (x, y) in a formation corresponds to a dancer position</li>
 * <li>Null values indicate empty positions; non-null values are dancer
 * names</li>
 * </ul>
 * </p>
 *
 * @author Payaswini Gurung
 * @version 1.0
 * @since 1.0
 *
 * @see DanceFormation
 * @see DanceFormationSecondary
 * @see DanceFormation1
 */

public interface DanceFormationKernel {

    /**
     * Sets the roster of dancers that may appear in formations.
     *
     * @param roster
     *            list of dancers
     * @updates this
     * @requires roster /= null
     * @ensures roster of this = roster
     */
    void setRoster(List<String> roster);

    /**
     * Adds a dancer to the current formation at a given position.
     *
     * @param name
     *            name of dancer
     * @param x
     *            the x coordinate of the dancer's position
     * @param y
     *            the y coordiante of the dancer's position position in the
     *            formation
     * @updates this
     * @requires name is in roster and position is valid
     * @ensures dancer name occupies position in the current formation
     */
    void addDancer(String name, int x, int y);

    /**
     * Removes the dancer at a given position in the current formation. name of
     * dancer
     * 
     * @param x
     *            the x coordinate of the dancer's position.
     * @param y
     *            the y coordiante of the dancer's position position in the
     *            formation.
     * @updates this
     * @requires position is valid
     * @ensures position in the current formation is empty
     */
    void removeDancer(int x, int y);

    /**
     * Returns the name of the dancer at a given position in the current
     * formation.
     *
     * @param x
     *            x-coordinate of position
     * @param y
     *            y-coordinate of position
     * @return name of dancer at position, or null if position is empty
     * @ensures dancerAt = name of dancer at position, or null if position is
     *          empty
     *
     */
    String dancerAt(int x, int y);

    /**
     * Creates a new formation slide. The new formation copies the dancer
     * positions of the current formation.
     *
     * @updates this
     * @ensures number of formations = #number of formations + 1 and new
     *          formation = copy of previous formation
     */
    void addFormation();

    /**
     * Removes the current formation slide.
     *
     * @updates this
     * @ensures numebr of formations = #number of formations - 1 and current
     *          formation = previous formation
     */

    void removeFormation();

    /**
     * Sets the current formation slide to the given index.
     * 
     * @param index
     */
    void setCurrentFormation(int index);

    /**
     * Returns the number of formation slides.
     *
     * @return number of formation slides
     * @ensures return = number of formations stored
     */
    int numberOfFormations();

    /**
     * Returns the index of the current formation slide.
     *
     * @return index of current formation slide.
     */
    int currentFormation();

    /**
     * Returns the width of the formation grid.
     *
     * @return width of formation grid
     * @ensures return = width of formation grid
     */

    int formationWidth();

    /**
     * Returns the height of the formation grid.
     * 
     * @return height of formation grid
     * @ensures return = height of formation grid
     */
    int formationHeight();
}