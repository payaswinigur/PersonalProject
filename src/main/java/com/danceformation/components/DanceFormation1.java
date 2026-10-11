package com.danceformation.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete kernel implementation of DanceFormation.
 *
 * <p>
 * <b>Overview:</b><br>
 * DanceFormation1 is a concrete implementation of the DanceFormation interface
 * that manages dance formations using a list of maps. Each formation slide
 * maps a dancer name to the single Position that dancer occupies on the stage.
 * A dancer absent from the map is not on stage in that slide. This class
 * handles all operations for creating, modifying, and navigating between
 * formation slides.
 * </p>
 *
 * <p>
 * <b>Data Structure:</b><br>
 * <ul>
 * <li><code>formations</code>: A List of Map&lt;String, Position&gt;, each
 * representing one slide</li>
 * <li><code>current</code>: The index of the currently active formation</li>
 * <li><code>width</code>: The number of columns on the stage</li>
 * <li><code>height</code>: The number of rows on the stage</li>
 * <li><code>roster</code>: The list of available dancer names</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Conventions:</b><br>
 * <ul>
 * <li>formations is a List&lt;Map&lt;String, Position&gt;&gt;, one map per
 * formation slide</li>
 * <li>current is the index of the active formation (0-based)</li>
 * <li>Each map sends a dancer name to that dancer's single Position</li>
 * <li>A name absent from the map means that dancer is not on stage</li>
 * <li>width represents the x-dimension (columns), height represents the
 * y-dimension (rows)</li>
 * <li>All formations share the same width x height stage</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Representation Invariant:</b><br>
 * <ul>
 * <li>formations is not null and contains at least one element</li>
 * <li>0 &lt;= current &lt; formations.size()</li>
 * <li>width &gt; 0 and height &gt; 0</li>
 * <li>roster is not null (may be empty)</li>
 * <li>Each element of formations is a non-null map</li>
 * <li>Every Position in every map is within the width x height stage</li>
 * <li>Within one map, no two dancers share the same Position</li>
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
 * <li>a dancer is at position (x, y) iff formations.get(current) maps that
 * dancer's name to Position(x, y)</li>
 * <li>position (x, y) is empty iff no name in the map is sent to
 * Position(x, y)</li>
 * <li>because the map is keyed by name, a dancer occupies at most one
 * position per slide</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Grid Coordinates:</b><br>
 * The formation grid uses (x, y) coordinates where:
 * <ul>
 * <li>x is the column index (0 to width-1)</li>
 * <li>y is the row index (0 to height-1)</li>
 * <li>Lookup: scan formations.get(current) for Position(x, y)</li>
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
     * Each slide maps a dancer name to the one position that dancer holds, and
     * dancers can be added to or removed from the stage. The formation
     * maintains a roster of dancers that can be used in the formations.
     */

    private List<String> roster;
    private List<Map<String, Position>> formations;
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

        // Record the grid dimensions so every later formation can be sized
        this.width = width;
        this.height = height;

        // Initialize the roster as an empty list of dancer names
        this.roster = new ArrayList<>();

        // Initialize the list to store all formation slides
        this.formations = new ArrayList<>();

        // Create the first (initial) empty formation, with nobody on stage
        this.formations.add(new LinkedHashMap<>());

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
     * Adds a dancer to the current formation at the given position. The
     * placement is refused when another dancer already holds that position.
     * Because a slide is keyed by dancer, placing a dancer who is already on
     * stage moves them rather than duplicating them.
     *
     * @param name
     *            dancer name
     * @param x
     *            column index
     * @param y
     *            row index
     * @updates this
     * @requires 0 <= x < formationWidth() and 0 <= y < formationHeight()
     * @ensures if position (x, y) was empty then name is at (x, y) and holds
     *          no other position, otherwise this is unchanged
     */
    @Override
    public void addDancer(String name, int x, int y) {
        this.checkOnStage(x, y);

        // Refuse the placement when someone else already holds the spot
        if (this.dancerAt(x, y) == null) {
            this.formations.get(this.current).put(name, new Position(x, y));
        }
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
        this.checkOnStage(x, y);

        // The map is keyed by dancer, so find the occupant before removing
        String occupant = this.dancerAt(x, y);
        if (occupant != null) {
            this.formations.get(this.current).remove(occupant);
        }
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
        this.checkOnStage(x, y);

        // A slide maps dancer -> position, so finding who is at a position
        // means searching the values
        Position target = new Position(x, y);
        for (Map.Entry<String, Position> entry : this.formations
                .get(this.current).entrySet()) {
            if (entry.getValue().equals(target)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Reports whether the given position lies on the stage.
     *
     * @param x
     *            column index
     * @param y
     *            row index
     * @throws IndexOutOfBoundsException
     *             if (x, y) is off the stage
     */
    private void checkOnStage(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            throw new IndexOutOfBoundsException("position (" + x + ", " + y
                    + ") is outside the " + this.width + "x" + this.height
                    + " stage");
        }
    }

    /**
     * Adds a new formation to the list.
     *
     * @updates this
     * @ensures formations = #formations + [copy of current formation] and
     *          current = #current + 1
     */
    @Override
    public void addFormation() {

        // Copy the current slide forward so each formation starts from the
        // previous pose, then make the new slide current
        this.formations
                .add(new LinkedHashMap<>(this.formations.get(this.current)));
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

        // The representation invariant requires at least one formation,
        // so the final slide must never be removed
        if (this.formations.size() > 1) {
            this.formations.remove(this.current);

            if (this.current > 0) {
                this.current--;
            }
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