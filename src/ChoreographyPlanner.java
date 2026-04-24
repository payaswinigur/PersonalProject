import java.util.ArrayList;
import java.util.List;

import Components.DanceFormation1;

/**
 * ChoreographyPlanner: A practical use case for DanceFormation.
 *
 * <p>
 * This class demonstrates how a choreographer can use the DanceFormation
 * component to plan and organize a dance routine with multiple formations. A
 * choreographer can add dancers, create formations for different sections of
 * the routine (verse, chorus, bridge), and preview the formations before
 * teaching the dancers.
 * </p>
 *
 * <p>
 * Usage Scenario:<br>
 * A dance teacher needs to choreograph a routine for 8 dancers. The routine has
 * multiple sections (intro, verse, chorus, bridge, finale), each with different
 * formations. This class helps organize and plan those formations.
 * </p>
 *
 * @author Dance Program
 * @version 1.0
 */
public class ChoreographyPlanner {

    /** The formation grid for the routine */
    private DanceFormation1 formation;

    /** Descriptions of each formation section */
    private List<String> sectionNames;

    /**
     * Creates a new ChoreographyPlanner for a dance routine.
     *
     * @param width
     *            the width of the dance floor (in positions)
     * @param height
     *            the height of the dance floor (in positions)
     */
    public ChoreographyPlanner(int width, int height) {
        this.formation = new DanceFormation1(width, height);
        this.sectionNames = new ArrayList<>();
    }

    /**
     * Sets the roster of dancers in the routine.
     *
     * @param dancers
     *            list of dancer names
     */
    public void setDancers(List<String> dancers) {
        this.formation.setRoster(dancers);
    }

    /**
     * Adds a new formation section to the routine.
     *
     * @param sectionName
     *            the name of this section (e.g., "Verse", "Chorus")
     */
    public void addFormationSection(String sectionName) {
        this.sectionNames.add(sectionName);
        this.formation.addFormation();
    }

    /**
     * Places a dancer at a specific position in the current formation.
     *
     * @param dancerName
     *            the name of the dancer
     * @param x
     *            the x position on the stage
     * @param y
     *            the y position on the stage
     */
    public void placeDancer(String dancerName, int x, int y) {
        this.formation.addDancer(dancerName, x, y);
    }

    /**
     * Removes a dancer from the current formation.
     *
     * @param x
     *            the x position
     * @param y
     *            the y position
     */
    public void removeDancer(int x, int y) {
        this.formation.removeDancer(x, y);
    }

    /**
     * Moves to the next section of the routine.
     */
    public void nextSection() {
        this.formation.nextForm();
    }

    /**
     * Moves to the previous section of the routine.
     */
    public void previousSection() {
        this.formation.previousForm();
    }

    /**
     * Gets the name of the current section.
     *
     * @return the name of the current formation section
     */
    public String getCurrentSectionName() {
        int currentIndex = this.formation.currentFormation();
        if (currentIndex < this.sectionNames.size()) {
            return this.sectionNames.get(currentIndex);
        }
        return "Formation " + currentIndex;
    }

    /**
     * Displays the current formation in a readable format.
     */
    public void displayCurrentFormation() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Section: " + this.getCurrentSectionName());
        System.out
                .println("Formation " + (this.formation.currentFormation() + 1)
                        + " of " + this.formation.numberOfFormations());
        System.out.println("=".repeat(40));

        int width = this.formation.formationWidth();
        int height = this.formation.formationHeight();

        // Print column headers
        System.out.print("   ");
        for (int x = 0; x < width; x++) {
            System.out.printf("%3d", x);
        }
        System.out.println();

        // Print grid
        for (int y = 0; y < height; y++) {
            System.out.printf("%2d:", y);
            for (int x = 0; x < width; x++) {
                String dancer = this.formation.dancerAt(x, y);
                if (dancer != null) {
                    System.out.printf("%3s",
                            dancer.substring(0, 1).toUpperCase());
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }

        // Print dancers in formation
        List<String> dancers = this.formation.dancersInFormation();
        System.out.println("\nDancers in formation: " + dancers);
    }

    /**
     * Main method demonstrating how to use ChoreographyPlanner.
     *
     * @param args
     *            command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Dance Routine Choreography Planner ===\n");

        // Create a 4x4 stage
        ChoreographyPlanner planner = new ChoreographyPlanner(4, 4);

        // Set up the dancers
        List<String> dancers = new ArrayList<>();
        dancers.add("Alice");
        dancers.add("Bob");
        dancers.add("Charlie");
        dancers.add("Diana");
        dancers.add("Eve");
        dancers.add("Frank");
        dancers.add("Grace");
        dancers.add("Henry");
        planner.setDancers(dancers);

        // ========== INTRO SECTION ==========
        planner.addFormationSection("INTRO");
        planner.placeDancer("Alice", 0, 0);
        planner.placeDancer("Bob", 3, 0);
        planner.placeDancer("Charlie", 0, 3);
        planner.placeDancer("Diana", 3, 3);
        planner.displayCurrentFormation();

        // ========== VERSE SECTION ==========
        planner.addFormationSection("VERSE");
        planner.placeDancer("Eve", 1, 1);
        planner.placeDancer("Frank", 2, 1);
        planner.placeDancer("Grace", 1, 2);
        planner.placeDancer("Henry", 2, 2);
        planner.displayCurrentFormation();

        // ========== CHORUS SECTION ==========
        planner.addFormationSection("CHORUS");
        planner.placeDancer("Alice", 0, 1);
        planner.placeDancer("Bob", 1, 0);
        planner.placeDancer("Charlie", 2, 0);
        planner.placeDancer("Diana", 3, 1);
        planner.placeDancer("Eve", 3, 2);
        planner.placeDancer("Frank", 2, 3);
        planner.placeDancer("Grace", 1, 3);
        planner.placeDancer("Henry", 0, 2);
        planner.displayCurrentFormation();

        // ========== BRIDGE SECTION ==========
        planner.addFormationSection("BRIDGE");
        planner.placeDancer("Alice", 1, 1);
        planner.placeDancer("Bob", 2, 1);
        planner.placeDancer("Charlie", 1, 2);
        planner.placeDancer("Diana", 2, 2);
        planner.displayCurrentFormation();

        // ========== FINALE SECTION ==========
        planner.addFormationSection("FINALE");
        planner.placeDancer("Alice", 0, 0);
        planner.placeDancer("Bob", 1, 0);
        planner.placeDancer("Charlie", 2, 0);
        planner.placeDancer("Diana", 3, 0);
        planner.placeDancer("Eve", 0, 1);
        planner.placeDancer("Frank", 3, 1);
        planner.placeDancer("Grace", 0, 3);
        planner.placeDancer("Henry", 3, 3);
        planner.displayCurrentFormation();

        // ========== PREVIEW THE ROUTINE ==========
        System.out.println("\n" + "=".repeat(40));
        System.out.println("ROUTINE PREVIEW - Navigating Through Sections");
        System.out.println("=".repeat(40));

        planner.previousSection();
        System.out.println("\nMoved to: " + planner.getCurrentSectionName());

        planner.previousSection();
        System.out.println("Moved to: " + planner.getCurrentSectionName());

        planner.nextSection();
        planner.nextSection();
        System.out.println("Moved to: " + planner.getCurrentSectionName());

        // ========== SUMMARY ==========
        System.out.println("\n" + "=".repeat(40));
        System.out.println("ROUTINE SUMMARY");
        System.out.println("=".repeat(40));
        System.out.println(
                "Total Sections: " + planner.formation.numberOfFormations());
        System.out.println("Stage Size: " + planner.formation.formationWidth()
                + " x " + planner.formation.formationHeight());
        System.out.println("Total Dancers: " + dancers.size());
        System.out.println("\nRoutine is ready to teach the dancers!");
    }
}
