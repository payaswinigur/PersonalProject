import java.util.ArrayList;
import java.util.List;

/**
 * Kernel implementation of DanceFormation.
 *
 * <p>
 * 
 * @CONVENTION:
 *              <ul>
 *              <li>formations is a list of 2D arrays representing each
 *              slide</li>
 *              <li>current is the index of the active formation</li>
 *              <li>each String[][] represents a grid of dancer names</li>
 *              <li>null entries represent empty positions</li>
 *              </ul>
 *
 *              <p>
 *              REPRESENTATION INVARIANT:
 *              <ul>
 *              <li>formations is not null and contains at least one
 *              element</li>
 *              <li>0 <= current < formations.size()</li>
 *              <li>all formations have dimensions width x height</li>
 *              </ul>
 *
 *              <p>
 *              CORRESPONDENCE:
 *              <ul>
 *              <li>this = (formations, current, roster, width, height)</li>
 *              <li>current formation = formations[current]</li>
 *              <li>each cell in formations[current] corresponds to a dancer
 *              position</li>
 *              </ul>
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
        this.width = width;
        this.height = height;

        this.roster = new ArrayList<>();
        this.formations = new ArrayList<>();
        this.formations.add(new String[width][height]);
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
     * @requires current formation is not the only formation
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