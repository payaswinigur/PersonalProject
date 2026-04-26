package components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Components.DanceFormation;
import Components.DanceFormation1;

/**
 * JUnit test class for DanceFormationSecondary convenience methods.
 *
 * <p>
 * This test class verifies the functionality of all secondary/convenience
 * methods defined in the DanceFormation interface and implemented in
 * DanceFormationSecondary. Tests use DanceFormation1 as the concrete
 * implementation.
 * </p>
 *
 * <p>
 * Secondary methods tested:
 * <ul>
 * <li>nextForm() - move to next formation</li>
 * <li>previousForm() - move to previous formation</li>
 * <li>totalFormations() - get formation count</li>
 * <li>positionEmpty(int, int) - check if position is empty</li>
 * <li>dancerInFormation(String) - check if dancer exists</li>
 * <li>dancersInFormation() - get all dancers in formation</li>
 * </ul>
 * </p>
 *
 * @author Test Suite
 * @version 1.0
 */
public class DanceFormationSecondaryTest {

    /** Test formation object using DanceFormation1 implementation */
    private DanceFormation formation;

    /** Sample roster of dancers for testing */
    private List<String> roster;

    /**
     * Sets up test fixtures before each test method. Creates a new
     * DanceFormation1 with dimensions 4x4 and a sample roster.
     */
    @Before
    public void setUp() {
        this.formation = new DanceFormation1(4, 4);
        this.roster = new ArrayList<>();
        this.roster.add("Alice");
        this.roster.add("Bob");
        this.roster.add("Charlie");
        this.roster.add("Diana");
        this.formation.setRoster(this.roster);
    }

    // ==================== nextForm Tests ====================

    /**
     * Test: nextForm advances to next formation.
     */
    @Test
    public void testNextForm() {
        this.formation.addFormation();
        this.formation.setCurrentFormation(0);

        this.formation.nextForm();

        assertEquals(1, this.formation.currentFormation());
    }

    /**
     * Test: nextForm with multiple formations.
     */
    @Test
    public void testNextFormMultiple() {
        this.formation.addFormation();
        this.formation.addFormation();
        this.formation.setCurrentFormation(0);

        this.formation.nextForm();
        assertEquals(1, this.formation.currentFormation());

        this.formation.nextForm();
        assertEquals(2, this.formation.currentFormation());
    }

    /**
     * Test: nextForm increments correctly over sequence.
     */
    @Test
    public void testNextFormSequence() {
        for (int i = 0; i < 5; i++) {
            this.formation.addFormation();
        }

        this.formation.setCurrentFormation(0);

        for (int i = 0; i < 5; i++) {
            assertEquals(i, this.formation.currentFormation());
            if (i < 5) {
                this.formation.nextForm();
            }
        }
    }

    // ==================== previousForm Tests ====================

    /**
     * Test: previousForm goes to previous formation.
     */
    @Test
    public void testPreviousForm() {
        this.formation.addFormation();
        this.formation.setCurrentFormation(1);

        this.formation.previousForm();

        assertEquals(0, this.formation.currentFormation());
    }

    /**
     * Test: previousForm with multiple formations.
     */
    @Test
    public void testPreviousFormMultiple() {
        this.formation.addFormation();
        this.formation.addFormation();
        this.formation.setCurrentFormation(2);

        this.formation.previousForm();
        assertEquals(1, this.formation.currentFormation());

        this.formation.previousForm();
        assertEquals(0, this.formation.currentFormation());
    }

    /**
     * Test: previousForm decrements correctly over sequence.
     */
    @Test
    public void testPreviousFormSequence() {
        for (int i = 0; i < 5; i++) {
            this.formation.addFormation();
        }

        this.formation.setCurrentFormation(5);

        for (int i = 5; i > 0; i--) {
            assertEquals(i, this.formation.currentFormation());
            this.formation.previousForm();
        }

        assertEquals(0, this.formation.currentFormation());
    }

    /**
     * Test: nextForm and previousForm are inverse operations.
     */
    @Test
    public void testNextPreviousInverse() {
        this.formation.addFormation();
        this.formation.addFormation();
        this.formation.setCurrentFormation(1);

        int beforeCurrent = this.formation.currentFormation();

        this.formation.nextForm();
        this.formation.previousForm();

        assertEquals(beforeCurrent, this.formation.currentFormation());
    }

    // ==================== totalFormations Tests ====================

    /**
     * Test: totalFormations returns correct count.
     */
    @Test
    public void testTotalFormations() {
        assertEquals(1, this.formation.totalFormations());

        this.formation.addFormation();
        assertEquals(2, this.formation.totalFormations());

        this.formation.addFormation();
        assertEquals(3, this.formation.totalFormations());
    }

    /**
     * Test: totalFormations equals numberOfFormations.
     */
    @Test
    public void testTotalFormationsEqualsNumberOfFormations() {
        for (int i = 0; i < 5; i++) {
            this.formation.addFormation();
            assertEquals(this.formation.numberOfFormations(),
                    this.formation.totalFormations());
        }
    }

    /**
     * Test: totalFormations after removal.
     */
    @Test
    public void testTotalFormationsAfterRemoval() {
        this.formation.addFormation();
        this.formation.addFormation();
        assertEquals(3, this.formation.totalFormations());

        this.formation.removeFormation();
        assertEquals(2, this.formation.totalFormations());
    }

    // ==================== positionEmpty Tests ====================

    /**
     * Test: positionEmpty returns true for empty position.
     */
    @Test
    public void testPositionEmptyTrue() {
        assertTrue(this.formation.positionEmpty(0, 0));
        assertTrue(this.formation.positionEmpty(2, 2));
        assertTrue(this.formation.positionEmpty(3, 3));
    }

    /**
     * Test: positionEmpty returns false when dancer present.
     */
    @Test
    public void testPositionEmptyFalse() {
        this.formation.addDancer("Alice", 1, 1);

        assertFalse(this.formation.positionEmpty(1, 1));
    }

    /**
     * Test: positionEmpty with multiple dancers.
     */
    @Test
    public void testPositionEmptyMultipleDancers() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addDancer("Charlie", 2, 2);

        assertFalse(this.formation.positionEmpty(0, 0));
        assertFalse(this.formation.positionEmpty(1, 1));
        assertFalse(this.formation.positionEmpty(2, 2));

        assertTrue(this.formation.positionEmpty(0, 1));
        assertTrue(this.formation.positionEmpty(1, 0));
        assertTrue(this.formation.positionEmpty(3, 3));
    }

    /**
     * Test: positionEmpty after dancer removal.
     */
    @Test
    public void testPositionEmptyAfterRemoval() {
        this.formation.addDancer("Alice", 2, 2);
        assertFalse(this.formation.positionEmpty(2, 2));

        this.formation.removeDancer(2, 2);
        assertTrue(this.formation.positionEmpty(2, 2));
    }

    /**
     * Test: positionEmpty at all positions.
     */
    @Test
    public void testPositionEmptyAllPositions() {
        // Check all empty at start
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                assertTrue(this.formation.positionEmpty(x, y));
            }
        }
    }

    // ==================== dancerInFormation Tests ====================

    /**
     * Test: dancerInFormation returns true when dancer exists.
     */
    @Test
    public void testDancerInFormationTrue() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);

        assertTrue(this.formation.dancerInFormation("Alice"));
        assertTrue(this.formation.dancerInFormation("Bob"));
    }

    /**
     * Test: dancerInFormation returns false when dancer not present.
     */
    @Test
    public void testDancerInFormationFalse() {
        this.formation.addDancer("Alice", 0, 0);

        assertFalse(this.formation.dancerInFormation("Bob"));
        assertFalse(this.formation.dancerInFormation("Charlie"));
    }

    /**
     * Test: dancerInFormation with empty formation.
     */
    @Test
    public void testDancerInFormationEmpty() {
        assertFalse(this.formation.dancerInFormation("Alice"));
        assertFalse(this.formation.dancerInFormation("Bob"));
    }

    /**
     * Test: dancerInFormation after dancer removal.
     */
    @Test
    public void testDancerInFormationAfterRemoval() {
        this.formation.addDancer("Alice", 0, 0);
        assertTrue(this.formation.dancerInFormation("Alice"));

        this.formation.removeDancer(0, 0);
        assertFalse(this.formation.dancerInFormation("Alice"));
    }

    /**
     * Test: dancerInFormation with multiple dancers of interest.
     */
    @Test
    public void testDancerInFormationMultiple() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addDancer("Charlie", 2, 2);
        this.formation.addDancer("Diana", 3, 3);

        assertTrue(this.formation.dancerInFormation("Alice"));
        assertTrue(this.formation.dancerInFormation("Bob"));
        assertTrue(this.formation.dancerInFormation("Charlie"));
        assertTrue(this.formation.dancerInFormation("Diana"));
    }

    /**
     * Test: dancerInFormation is case-sensitive.
     */
    @Test
    public void testDancerInFormationCaseSensitive() {
        this.formation.addDancer("Alice", 0, 0);

        assertTrue(this.formation.dancerInFormation("Alice"));
        assertFalse(this.formation.dancerInFormation("alice"));
        assertFalse(this.formation.dancerInFormation("ALICE"));
    }

    /**
     * Test: dancerInFormation across multiple formations.
     */
    @Test
    public void testDancerInFormationMultipleFormations() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addFormation();
        this.formation.addDancer("Bob", 1, 1);

        this.formation.setCurrentFormation(0);
        assertTrue(this.formation.dancerInFormation("Alice"));
        assertFalse(this.formation.dancerInFormation("Bob"));

        this.formation.setCurrentFormation(1);
        assertFalse(this.formation.dancerInFormation("Alice"));
        assertTrue(this.formation.dancerInFormation("Bob"));
    }

    // ==================== dancersInFormation Tests ====================

    /**
     * Test: dancersInFormation returns empty list for empty formation.
     */
    @Test
    public void testDancersInFormationEmpty() {
        List<String> dancers = this.formation.dancersInFormation();

        assertEquals(0, dancers.size());
        assertTrue(dancers.isEmpty());
    }

    /**
     * Test: dancersInFormation returns single dancer.
     */
    @Test
    public void testDancersInFormationSingle() {
        this.formation.addDancer("Alice", 0, 0);

        List<String> dancers = this.formation.dancersInFormation();

        assertEquals(1, dancers.size());
        assertTrue(dancers.contains("Alice"));
    }

    /**
     * Test: dancersInFormation returns all dancers.
     */
    @Test
    public void testDancersInFormationMultiple() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addDancer("Charlie", 2, 2);

        List<String> dancers = this.formation.dancersInFormation();

        assertEquals(3, dancers.size());
        assertTrue(dancers.contains("Alice"));
        assertTrue(dancers.contains("Bob"));
        assertTrue(dancers.contains("Charlie"));
    }

    /**
     * Test: dancersInFormation excludes null entries.
     */
    @Test
    public void testDancersInFormationExcludesNull() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);

        List<String> dancers = this.formation.dancersInFormation();

        assertEquals(2, dancers.size());
        assertFalse(dancers.contains(null));
    }

    /**
     * Test: dancersInFormation after dancer removal.
     */
    @Test
    public void testDancersInFormationAfterRemoval() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addDancer("Charlie", 2, 2);

        this.formation.removeDancer(1, 1);

        List<String> dancers = this.formation.dancersInFormation();

        assertEquals(2, dancers.size());
        assertTrue(dancers.contains("Alice"));
        assertFalse(dancers.contains("Bob"));
        assertTrue(dancers.contains("Charlie"));
    }

    /**
     * Test: dancersInFormation with full formation.
     */
    @Test
    public void testDancersInFormationFull() {
        List<String> allDancers = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            allDancers.add("Dancer" + i);
        }
        this.formation.setRoster(allDancers);

        // Fill 4x4 formation
        int index = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                this.formation.addDancer("Dancer" + index, x, y);
                index++;
            }
        }

        List<String> dancers = this.formation.dancersInFormation();

        assertEquals(16, dancers.size());
    }

    /**
     * Test: dancersInFormation in different formations.
     */
    @Test
    public void testDancersInFormationDifferentFormations() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addFormation();
        this.formation.addDancer("Charlie", 2, 2);
        this.formation.addDancer("Diana", 3, 3);

        this.formation.setCurrentFormation(0);
        List<String> dancers0 = this.formation.dancersInFormation();
        assertEquals(2, dancers0.size());
        assertTrue(dancers0.contains("Alice"));
        assertTrue(dancers0.contains("Bob"));

        this.formation.setCurrentFormation(1);
        List<String> dancers1 = this.formation.dancersInFormation();
        assertEquals(4, dancers1.size());
        assertTrue(dancers1.contains("Alice"));
        assertTrue(dancers1.contains("Bob"));
        assertTrue(dancers1.contains("Charlie"));
        assertTrue(dancers1.contains("Diana"));
    }

    /**
     * Test: dancersInFormation does not contain duplicates.
     */
    @Test
    public void testDancersInFormationNoDuplicates() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addDancer("Charlie", 2, 2);

        List<String> dancers = this.formation.dancersInFormation();

        // Count occurrences of Alice
        int aliceCount = 0;
        for (String dancer : dancers) {
            if (dancer.equals("Alice")) {
                aliceCount++;
            }
        }

        assertEquals(1, aliceCount);
    }

    // ==================== Integration Tests ====================

    /**
     * Test: Complex navigation and queries.
     */
    @Test
    public void testComplexNavigation() {
        // Set up formations
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addFormation();
        this.formation.addDancer("Charlie", 2, 2);
        this.formation.addFormation();
        this.formation.addDancer("Diana", 3, 3);

        // Current should be formation 2
        assertEquals(2, this.formation.currentFormation());
        assertTrue(this.formation.dancerInFormation("Diana"));

        // Navigate backward
        this.formation.previousForm();
        assertEquals(1, this.formation.currentFormation());
        assertTrue(this.formation.dancerInFormation("Charlie"));

        // Navigate backward again
        this.formation.previousForm();
        assertEquals(0, this.formation.currentFormation());
        assertTrue(this.formation.dancerInFormation("Alice"));
        assertTrue(this.formation.dancerInFormation("Bob"));
    }

    /**
     * Test: Position queries and dancer queries consistency.
     */
    @Test
    public void testQueriesConsistency() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);

        // If position is not empty, dancer should be in formation
        assertFalse(this.formation.positionEmpty(0, 0));
        assertTrue(this.formation.dancerInFormation("Alice"));

        assertFalse(this.formation.positionEmpty(1, 1));
        assertTrue(this.formation.dancerInFormation("Bob"));

        // dancersInFormation should contain exact dancers at non-empty positions
        List<String> dancers = this.formation.dancersInFormation();
        assertEquals(2, dancers.size());
    }

    /**
     * Test: Large formation with many operations.
     */
    @Test
    public void testLargeFormationOperations() {
        // Add many formations
        for (int i = 0; i < 10; i++) {
            this.formation.addFormation();
        }

        assertEquals(11, this.formation.totalFormations());
        assertEquals(10, this.formation.currentFormation());

        // Navigate to different formation
        this.formation.setCurrentFormation(5);
        assertEquals(5, this.formation.currentFormation());

        // Add dancers and verify
        this.formation.addDancer("TestDancer", 0, 0);
        assertTrue(this.formation.dancerInFormation("TestDancer"));
        assertFalse(this.formation.positionEmpty(0, 0));
    }

    /**
     * Test: State preservation across operations.
     */
    @Test
    public void testStatePreservation() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);

        // Perform read-only operations
        List<String> dancers1 = this.formation.dancersInFormation();
        boolean empty = this.formation.positionEmpty(2, 2);
        boolean hasAlice = this.formation.dancerInFormation("Alice");

        // Perform same operations again
        List<String> dancers2 = this.formation.dancersInFormation();
        boolean empty2 = this.formation.positionEmpty(2, 2);
        boolean hasAlice2 = this.formation.dancerInFormation("Alice");

        // Results should be identical
        assertEquals(dancers1.size(), dancers2.size());
        assertEquals(empty, empty2);
        assertEquals(hasAlice, hasAlice2);
    }
}