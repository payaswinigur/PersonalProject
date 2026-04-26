package Components;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete kernel implementation of DanceFormation.
 *
 * <p>
 * <b>Overview:</b><br>
 * DanceFormation1 is a concrete implementation of the DanceFormation interface
 * that manages dance formations using a list of 2D String arrays. Each
 * formation slide is represented as a 2D grid where each cell contains either a
 * dancer name (non-null String) or represents an empty position (null). This
 * class handles all operations for creating, modifying, and navigating between
 * formation slides.
 * </p>
 *
 * <p>
 * <b>Data Structure:</b><br>
 * <ul>
 * <li><code>formations</code>: A List of String[][] arrays, each representing
 * one slide</li>
 * <li><code>current</code>: The index of the currently active formation</li>
 * <li><code>width</code>: The number of columns in each formation grid</li>
 * <li><code>height</code>: The number of rows in each formation grid</li>
 * <li><code>roster</code>: The list of available dancer names</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Conventions:</b><br>
 * <ul>
 * <li>formations is a List&lt;String[][]&gt; representing each formation
 * slide</li>
 * <li>current is the index of the active formation (0-based)</li>
 * <li>Each String[][] represents a grid of dancer names and empty
 * positions</li>
 * <li>Null entries (null) represent empty positions in the grid</li>
 * <li>Non-null String values represent dancer names at specific positions</li>
 * <li>width represents the x-dimension (columns), height represents the
 * y-dimension (rows)</li>
 * <li>All formations have identical dimensions (width x height)</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Representation Invariant:</b><br>
 * <ul>
 * <li>formations is not null and contains at least one element</li>
 * <li>0 &lt;= current &lt; formations.size()</li>
 * <li>all formations have dimensions width x height</li>
 * <li>width &gt; 0 and height &gt; 0</li>
 * <li>roster is not null (may be empty)</li>
 * <li>Each element of formations is a non-null String[][] array</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Correspondence:</b><br>
 * The abstraction (interface contract) corresponds to the implementation as
 * follows:
 * <ul>
 * <li>this = (formations, current, roster, width, height)</li>
 * <li>current formation = formations.get(current)</li>
 * <li>each cell [x][y] in formations.get(current) corresponds to position (x,
 * y)</li>
 * <li>null values at [x][y] indicate empty positions</li>
 * <li>non-null String values at [x][y] are the names of dancers at that
 * position</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Grid Coordinates:</b><br>
 * The formation grid uses (x, y) coordinates where:
 * <ul>
 * <li>x is the column index (0 to width-1)</li>
 * <li>y is the row index (0 to height-1)</li>
 * <li>Access: formations.get(current)[x][y]</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Usage Example:</b><br>
 * <pre>
 * DanceFormation1 formation = new DanceFormation1(5, 5);
 * formation.setRoster(Arrays.asList("Alice", "Bob", "Charlie"));
 * formation.addDancer("Alice", 0, 0);
 * formation.addDancer("Bob", 2, 2);
 * formation.addFormation();  // Create a new slide
 * formation.addDancer("Charlie", 1, 1);
 * </pre>
 * </p>
 *
 * @author Payaswini Gurung
 * @version 1.0
 * @since 1.0
 *
 * @see DanceFormation
 * @see DanceFormationSecondary
 * @see DanceFormationKernel
 */

public class DanceFormation1 extends DanceFormationSecondary {
    /**
     * Abstract data type representing a dance formation with multiple slides.
     * Each slide is a grid of dancer positions, and dancers can be added or
     * removed from the grid. The formation maintains a roster of dancers that
     * can be used in the formations.
     */

    private List<String> roster;
    private List<String[][]> formations;
    private int current;
    private int width;
    private int height;

    /**
     * Creates a new DanceFormation with the given dimensions. Initially, there
     * is
     *
     * @param width
     * @param height
     */
    public DanceFormation1(int width, int height) {

        // Initialize the roster as an empty list of dancer names
        this.roster = new ArrayList<>();

        // Initialize the list to store all formation slides
        this.formations = new ArrayList<>();

        // Create the first (initial) empty formation with given dimensions
        this.formations.add(new String[width][height]);

        // Set the first formation as the current active formation
        this.current = 0;
    }

    /**
     * Returns the total number of formations.
     *
     * @return number of formation slides * @ensures totalFormations = total
     *         formations stored
     */

    @Override
    public void setRoster(List<String> roster) {
        this.roster = new ArrayList<>(roster);
    }

    /**
     * Adds a dancer to the current formation at the given position.
     *
     * @param name
     * @param x
     * @param y
     */
    @Override
    public void addDancer(String name, int x, int y) {
        this.formations.get(this.current)[x][y] = name;
    }

    /**
     * Removes the dancer at a given position in the current formation.
     *
     * @param position
     *            position in the formation
     * @updates this
     * @requires position is valid
     * @ensures position in the current formation is empty
     * @param x
     * @param y
     */
    @Override
    public void removeDancer(int x, int y) {
        this.formations.get(this.current)[x][y] = null;
    }

    /**
     * Returns the name of the dancer at a given position in the current
     * formation.
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public String dancerAt(int x, int y) {
        return this.formations.get(this.current)[x][y];
    }

    /**
     * Adds a new formation to the list.
     *
     * @updates this
     * @ensures formations = #formations + [new empty formation]
     */
    @Override
    public void addFormation() {
        String[][] curr = this.formations.get(this.current);
        String[][] copy = new String[this.width][this.height];

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                copy[i][j] = curr[i][j];
            }
        }

        this.formations.add(copy);
        this.current++;
    }

    /**
     * Removes the current formation from the list.
     *
     * @updates this
     * @requires current formation is not the only formation, has at least more
     *           than 1 formation
     * @ensures formations = #formations - [current formation]
     */
    @Override
    public void removeFormation() {
        this.formations.remove(this.current);

        if (this.current > 0) {
            this.current--;
        }
    }

    /**
     * Returns the total number of formations.
     *
     * @return
     */
    @Override
    public int numberOfFormations() {
        return this.formations.size();
    }

    /**
     * Returns the index of the current formation.
     *
     * @return
     */
    @Override
    public int currentFormation() {
        return this.current;
    }

    /**
     * Moves to the next formation slide.
     *
     * @param index
     */
    @Override
    public void setCurrentFormation(int index) {
        this.current = index;
    }

    /**
     * Returns the width of the formation grid.
     *
     * @return
     */
    @Override
    public int formationWidth() {
        return this.width;
    }

    /**
     * Returns the height of the formation grid.
     *
     * @return
     */
    @Override
    public int formationHeight() {
        return this.height;
    }
}