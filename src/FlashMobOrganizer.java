import java.util.ArrayList;
import java.util.List;

import Components.DanceFormation1;

/**
 * FlashMobOrganizer: A practical use case for DanceFormation.
 *
 * <p>
 * This class demonstrates how to organize a flash mob using the DanceFormation
 * component. A flash mob coordinator needs to organize dancers in specific
 * locations, track which dancers are positioned, and verify that all dancers
 * are properly placed before the performance begins.
 * </p>
 *
 *
 * <p>
 * Usage Scenario:<br>
 * A group wants to organize a flash mob in a public square. The square is
 * divided into a grid of positions. Dancers need to be positioned at specific
 * locations, and the coordinator needs to verify everyone is in place before
 * starting the music.
 * </p>
 *
 * @author Flash Mob Organization Committee
 * @version 1.0
 */
public class FlashMobOrganizer {

    /** The formation representing dancer positions in the square */
    private DanceFormation1 formation;

    /** List of all dancers participating */
    private List<String> allDancers;

    /** Map of dancer to their assigned position description */
    private List<String> dancerPositions;

    /**
     * Creates a new FlashMobOrganizer for a public space.
     *
     * @param gridWidth
     *            the width of the flash mob area (in grid positions)
     * @param gridHeight
     *            the height of the flash mob area (in grid positions)
     */
    public FlashMobOrganizer(int gridWidth, int gridHeight) {
        this.formation = new DanceFormation1(gridWidth, gridHeight);
        this.allDancers = new ArrayList<>();
        this.dancerPositions = new ArrayList<>();
    }

    /**
     * Registers a new dancer for the flash mob.
     *
     * @param dancerName
     *            the name of the dancer
     */
    public void registerDancer(String dancerName) {
        this.allDancers.add(dancerName);
        this.dancerPositions.add("Not positioned");
    }

    /**
     * Sets up all registered dancers in the formation roster.
     */
    public void setupRoster() {
        this.formation.setRoster(this.allDancers);
    }

    /**
     * Assigns a dancer to a specific grid position.
     *
     * @param dancerName
     *            the name of the dancer
     * @param x
     *            the x coordinate on the grid
     * @param y
     *            the y coordinate on the grid
     * @return true if assignment successful, false if dancer already positioned
     */
    public boolean assignDancer(String dancerName, int x, int y) {
        if (!this.formation.positionEmpty(x, y)) {
            System.out.println(
                    "Position (" + x + ", " + y + ") is already occupied!");
            return false;
        }

        this.formation.addDancer(dancerName, x, y);

        // Update position tracking
        int dancerIndex = this.allDancers.indexOf(dancerName);
        if (dancerIndex >= 0) {
            this.dancerPositions.set(dancerIndex, "(" + x + ", " + y + ")");
        }

        return true;
    }

    /**
     * Unassigns a dancer from their position.
     *
     * @param x
     *            the x coordinate of the dancer
     * @param y
     *            the y coordinate of the dancer
     */
    public void unassignDancer(int x, int y) {
        String dancer = this.formation.dancerAt(x, y);
        if (dancer != null) {
            this.formation.removeDancer(x, y);
            int dancerIndex = this.allDancers.indexOf(dancer);
            if (dancerIndex >= 0) {
                this.dancerPositions.set(dancerIndex, "Not positioned");
            }
        }
    }

    /**
     * Checks if a specific dancer is positioned in the formation.
     *
     * @param dancerName
     *            the name of the dancer
     * @return true if the dancer is positioned, false otherwise
     */
    public boolean isDancerPositioned(String dancerName) {
        return this.formation.dancerInFormation(dancerName);
    }

    /**
     * Gets the count of dancers who are positioned.
     *
     * @return the number of dancers in the formation
     */
    public int getPositionedDancerCount() {
        return this.formation.dancersInFormation().size();
    }

    /**
     * Checks if all dancers are positioned and ready to start.
     *
     * @return true if all registered dancers are in the formation
     */
    public boolean isReadyToStart() {
        return this.getPositionedDancerCount() == this.allDancers.size();
    }

    /**
     * Displays a report of all dancers and their positions.
     */
    public void displayDancerReport() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("FLASH MOB DANCER POSITION REPORT");
        System.out.println("=".repeat(50));

        for (int i = 0; i < this.allDancers.size(); i++) {
            String dancer = this.allDancers.get(i);
            String position = this.dancerPositions.get(i);
            String status = this.isDancerPositioned(dancer) ? "✓ Ready"
                    : "✗ Not positioned";

            System.out.printf("%-15s Position: %-15s %s%n", dancer, position,
                    status);
        }

        System.out.println("\n" + "-".repeat(50));
        System.out.printf("Positioned: %d / %d dancers%n",
                this.getPositionedDancerCount(), this.allDancers.size());

        if (this.isReadyToStart()) {
            System.out.println("Status: ✓ READY TO START! 🎉");
        } else {
            System.out.println("Status: ✗ WAITING FOR REMAINING DANCERS");
        }

        System.out.println("=".repeat(50));
    }

    /**
     * Displays the formation grid with dancer positions visualized.
     */
    public void displayFormationGrid() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("FLASH MOB FORMATION GRID");
        System.out.println("=".repeat(50));

        int width = this.formation.formationWidth();
        int height = this.formation.formationHeight();

        // Print column headers
        System.out.print("   ");
        for (int x = 0; x < width; x++) {
            System.out.printf("%4d", x);
        }
        System.out.println();

        // Print grid with dancers
        for (int y = 0; y < height; y++) {
            System.out.printf("%2d:", y);
            for (int x = 0; x < width; x++) {
                String dancer = this.formation.dancerAt(x, y);
                if (dancer != null) {
                    System.out.printf("%4s",
                            dancer.substring(0, 3).toUpperCase());
                } else {
                    System.out.print("   .");
                }
            }
            System.out.println();
        }

        System.out.println("=".repeat(50));
    }

    /**
     * Main method demonstrating how to use FlashMobOrganizer.
     *
     * @param args
     *            command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out
                .println("╔════════════════════════════════════════════════╗");
        System.out.println(
                "║   FLASH MOB COORDINATION SYSTEM v1.0             ║");
        System.out
                .println("╚════════════════════════════════════════════════╝");

        // ========== SETUP PHASE ==========
        System.out.println("\n[PHASE 1] Organizing Flash Mob Locations...\n");

        // Create organizer for a 6x5 public square
        FlashMobOrganizer organizer = new FlashMobOrganizer(6, 5);

        // Register dancers
        String[] dancerNames = { "Alex", "Bailey", "Casey", "Dakota", "Echo",
                "Finley", "Grace", "Harper", "Iris", "Jordan", "Kai", "Lane" };

        for (String name : dancerNames) {
            organizer.registerDancer(name);
        }

        organizer.setupRoster();
        System.out.println("✓ Registered " + dancerNames.length + " dancers");
        System.out.println("✓ Ready to assign positions\n");

        // ========== POSITIONING PHASE ==========
        System.out.println("[PHASE 2] Assigning Dancer Positions...\n");

        // Assign dancers in formation
        organizer.assignDancer("Alex", 0, 2);
        organizer.assignDancer("Bailey", 1, 1);
        organizer.assignDancer("Casey", 2, 0);
        organizer.assignDancer("Dakota", 3, 0);
        organizer.assignDancer("Echo", 4, 1);
        organizer.assignDancer("Finley", 5, 2);
        organizer.assignDancer("Grace", 1, 3);
        organizer.assignDancer("Harper", 2, 4);
        organizer.assignDancer("Iris", 3, 4);
        organizer.assignDancer("Jordan", 4, 3);

        System.out.println("✓ Positioned 10 dancers\n");

        organizer.displayDancerReport();
        organizer.displayFormationGrid();

        // ========== VERIFICATION PHASE ==========
        System.out.println("\n[PHASE 3] Verifying Formation...\n");

        System.out.println("Checking dancer positions:");
        System.out.println("  - Is Alex positioned? "
                + organizer.isDancerPositioned("Alex"));
        System.out.println("  - Is Kai positioned? "
                + organizer.isDancerPositioned("Kai"));
        System.out.println("  - Is Lane positioned? "
                + organizer.isDancerPositioned("Lane"));

        System.out.println("\nCurrent status:");
        System.out.println("  - Dancers positioned: "
                + organizer.getPositionedDancerCount() + " / 12");
        System.out.println("  - Ready to start? "
                + (organizer.isReadyToStart() ? "YES ✓" : "NO ✗"));

        // ========== FINAL POSITIONING ==========
        System.out.println("\n[PHASE 4] Final Dancer Arrivals...\n");

        organizer.assignDancer("Kai", 2, 2);
        System.out.println("✓ Kai arrived and positioned at (2, 2)");

        organizer.assignDancer("Lane", 4, 2);
        System.out.println("✓ Lane arrived and positioned at (4, 2)");

        System.out.println("\nFinal status check:");
        System.out.println("  - Dancers positioned: "
                + organizer.getPositionedDancerCount() + " / 12");
        System.out.println("  - Ready to start? "
                + (organizer.isReadyToStart() ? "YES ✓" : "NO ✗"));

        // ========== FINAL REPORTS ==========
        organizer.displayDancerReport();
        organizer.displayFormationGrid();

        // ========== GO TIME ==========
        if (organizer.isReadyToStart()) {
            System.out.println(
                    "\n╔════════════════════════════════════════════════╗");
            System.out.println(
                    "║  🎵 MUSIC STARTS... LET'S FLASH MOB! 🎵        ║");
            System.out.println(
                    "╚════════════════════════════════════════════════╝\n");
        }
    }
}
