package components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Components.DanceFormation1;

/**
 * JUnit test class for DanceFormation1 kernel implementation.
 *
 * <p>
 * This test class verifies the functionality of all kernel methods in the
 * DanceFormation1 class. Tests include adding/removing dancers, managing
 * formations, and querying dancer positions.
 * </p>
 *
 * @author Test Suite
 * @version 1.0
 */
public class DanceFormation1Test {

    /** Test formation object used across all test methods */
    private DanceFormation1 formation;

    /** Sample roster of dancers for testing */
    private List<String> roster;

    /**
     * Sets up test fixtures before each test method. Creates a new
     * DanceFormation1 with dimensions 5x5 and a sample roster.
     */
    @Before
    public void setUp() {
        this.formation = new DanceFormation1(5, 5);
        this.roster = new ArrayList<>();
        this.roster.add("Alice");
        this.roster.add("Bob");
        this.roster.add("Charlie");
        this.roster.add("Diana");
        this.roster.add("Eve");
        this.formation.setRoster(this.roster);
    }

    // ==================== Constructor Tests ====================

    /**
     * Test: Constructor creates formation with correct dimensions.
     */
    @Test
    public void testConstructorDimensions() {
        assertEquals(5, this.formation.formationWidth());
        assertEquals(5, this.formation.formationHeight());
    }

    /**
     * Test: Constructor initializes one empty formation.
     */
    @Test
    public void testConstructorInitialFormation() {
        assertEquals(1, this.formation.numberOfFormations());
        assertEquals(0, this.formation.currentFormation());
    }

    /**
     * Test: Constructor creates formation with all empty positions.
     */
    @Test
    public void testConstructorAllPositionsEmpty() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                assertNull(this.formation.dancerAt(x, y));
            }
        }
    }

    // setRoster Tests

    /**
     * Test: setRoster correctly sets the roster.
     */
    @Test
    public void testSetRoster() {
        List<String> newRoster = Arrays.asList("Xavier", "Yara", "Zoe");
        this.formation.setRoster(newRoster);

        // Verify roster was set by adding dancers
        this.formation.addDancer("Xavier", 0, 0);
        assertEquals("Xavier", this.formation.dancerAt(0, 0));
    }

    /**
     * Test: setRoster creates defensive copy (external changes don't affect
     * internal roster).
     */
    @Test
    public void testSetRosterDefensiveCopy() {
        List<String> tempRoster = new ArrayList<>();
        tempRoster.add("Test1");
        tempRoster.add("Test2");

        this.formation.setRoster(tempRoster);

        // Modify external list
        tempRoster.add("Test3");

        // Internal roster should be unchanged
        this.formation.addDancer("Test1", 0, 0);
        assertEquals("Test1", this.formation.dancerAt(0, 0));
    }

    //addDancer Tests

    /**
     * Test: addDancer places dancer at specified position.
     */
    @Test
    public void testAddDancer() {
        this.formation.addDancer("Alice", 0, 0);
        assertEquals("Alice", this.formation.dancerAt(0, 0));
    }

    /**
     * Test: addDancer at multiple positions.
     */
    @Test
    public void testAddMultipleDancers() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 2, 2);
        this.formation.addDancer("Charlie", 4, 4);

        assertEquals("Alice", this.formation.dancerAt(0, 0));
        assertEquals("Bob", this.formation.dancerAt(2, 2));
        assertEquals("Charlie", this.formation.dancerAt(4, 4));
    }

    /**
     * Test: addDancer replaces existing dancer at position.
     */
    @Test
    public void testAddDancerReplacement() {
        this.formation.addDancer("Alice", 1, 1);
        assertEquals("Alice", this.formation.dancerAt(1, 1));

        this.formation.addDancer("Bob", 1, 1);
        assertEquals("Bob", this.formation.dancerAt(1, 1));
    }

    /**
     * Test: addDancer at boundary positions.
     */
    @Test
    public void testAddDancerBoundary() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 4, 4);
        this.formation.addDancer("Charlie", 0, 4);
        this.formation.addDancer("Diana", 4, 0);

        assertEquals("Alice", this.formation.dancerAt(0, 0));
        assertEquals("Bob", this.formation.dancerAt(4, 4));
        assertEquals("Charlie", this.formation.dancerAt(0, 4));
        assertEquals("Diana", this.formation.dancerAt(4, 0));
    }

    // ==================== removeDancer Tests ====================

    /**
     * Test: removeDancer clears a position.
     */
    @Test
    public void testRemoveDancer() {
        this.formation.addDancer("Alice", 2, 2);
        this.formation.removeDancer(2, 2);
        assertNull(this.formation.dancerAt(2, 2));
    }

    /**
     * Test: removeDancer on empty position has no effect.
     */
    @Test
    public void testRemoveDancerFromEmpty() {
        this.formation.removeDancer(2, 2);
        assertNull(this.formation.dancerAt(2, 2));
    }

    /**
     * Test: removeDancer does not affect other positions.
     */
    @Test
    public void testRemoveDancerSelectiveRemoval() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addDancer("Charlie", 2, 2);

        this.formation.removeDancer(1, 1);

        assertEquals("Alice", this.formation.dancerAt(0, 0));
        assertNull(this.formation.dancerAt(1, 1));
        assertEquals("Charlie", this.formation.dancerAt(2, 2));
    }

    // ==================== dancerAt Tests ====================

    /**
     * Test: dancerAt returns dancer name when present.
     */
    @Test
    public void testDancerAtPresent() {
        this.formation.addDancer("Alice", 3, 3);
        assertEquals("Alice", this.formation.dancerAt(3, 3));
    }

    /**
     * Test: dancerAt returns null when position is empty.
     */
    @Test
    public void testDancerAtEmpty() {
        assertNull(this.formation.dancerAt(2, 2));
    }

    /**
     * Test: dancerAt does not modify state.
     */
    @Test
    public void testDancerAtNonDestructive() {
        this.formation.addDancer("Alice", 1, 1);

        String dancer1 = this.formation.dancerAt(1, 1);
        String dancer2 = this.formation.dancerAt(1, 1);

        assertEquals(dancer1, dancer2);
        assertEquals("Alice", dancer1);
    }

    //addFormation Tests

    /**
     * Test: addFormation increases formation count.
     */
    @Test
    public void testAddFormationIncrementsCount() {
        assertEquals(1, this.formation.numberOfFormations());

        this.formation.addFormation();
        assertEquals(2, this.formation.numberOfFormations());

        this.formation.addFormation();
        assertEquals(3, this.formation.numberOfFormations());
    }

    /**
     * Test: addFormation copies current formation.
     */
    @Test
    public void testAddFormationCopiesPrevious() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);

        this.formation.addFormation();

        // New formation should be a copy
        assertEquals("Alice", this.formation.dancerAt(0, 0));
        assertEquals("Bob", this.formation.dancerAt(1, 1));
    }

    /**
     * Test: addFormation makes new formation current.
     */
    @Test
    public void testAddFormationMakesCurrent() {
        assertEquals(0, this.formation.currentFormation());

        this.formation.addFormation();
        assertEquals(1, this.formation.currentFormation());

        this.formation.addFormation();
        assertEquals(2, this.formation.currentFormation());
    }

    /**
     * Test: addFormation with modifications to new formation doesn't affect
     * previous.
     */
    @Test
    public void testAddFormationIndependence() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addFormation();

        // Modify new formation
        this.formation.addDancer("Bob", 0, 0);

        // Check previous formation
        this.formation.setCurrentFormation(0);
        assertEquals("Alice", this.formation.dancerAt(0, 0));

        // Check new formation
        this.formation.setCurrentFormation(1);
        assertEquals("Bob", this.formation.dancerAt(0, 0));
    }

    // ==================== removeFormation Tests ====================

    /**
     * Test: removeFormation decreases formation count.
     */
    @Test
    public void testRemoveFormationDecrementsCount() {
        this.formation.addFormation();
        this.formation.addFormation();
        assertEquals(3, this.formation.numberOfFormations());

        this.formation.removeFormation();
        assertEquals(2, this.formation.numberOfFormations());
    }

    /**
     * Test: removeFormation moves to previous formation.
     */
    @Test
    public void testRemoveFormationSetsCurrentToPrevious() {
        this.formation.addFormation();
        this.formation.addFormation();

        assertEquals(2, this.formation.currentFormation());
        this.formation.removeFormation();
        assertEquals(1, this.formation.currentFormation());
    }

    /**
     * Test: removeFormation at index 0 keeps current at 0.
     */
    @Test
    public void testRemoveFormationAtFirstIndex() {
        this.formation.addFormation();
        this.formation.setCurrentFormation(0);

        this.formation.removeFormation();
        assertEquals(0, this.formation.currentFormation());
    }

    /**
     * Test: Cannot remove last remaining formation.
     */
    @Test
    public void testCannotRemoveLastFormation() {
        assertEquals(1, this.formation.numberOfFormations());

        this.formation.removeFormation();

        // Should still have 1 formation
        assertEquals(1, this.formation.numberOfFormations());
    }

    /**
     * Test: removeFormation with multiple formations.
     */
    @Test
    public void testRemoveFormationMultiple() {
        this.formation.addFormation();
        this.formation.addFormation();
        this.formation.addFormation();

        assertEquals(4, this.formation.numberOfFormations());

        this.formation.removeFormation();
        this.formation.removeFormation();

        assertEquals(2, this.formation.numberOfFormations());
        assertEquals(1, this.formation.currentFormation());
    }

    // ==================== setCurrentFormation Tests ====================

    /**
     * Test: setCurrentFormation changes current formation.
     */
    @Test
    public void testSetCurrentFormation() {
        this.formation.addFormation();
        this.formation.addFormation();

        this.formation.setCurrentFormation(0);
        assertEquals(0, this.formation.currentFormation());

        this.formation.setCurrentFormation(2);
        assertEquals(2, this.formation.currentFormation());
    }

    /**
     * Test: setCurrentFormation with independent formations.
     */
    @Test
    public void testSetCurrentFormationIndependence() {
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addFormation();
        this.formation.addDancer("Bob", 0, 0);

        this.formation.setCurrentFormation(0);
        assertEquals("Alice", this.formation.dancerAt(0, 0));

        this.formation.setCurrentFormation(1);
        assertEquals("Bob", this.formation.dancerAt(0, 0));
    }

    // ==================== numberOfFormations Tests ====================

    /**
     * Test: numberOfFormations returns correct count.
     */
    @Test
    public void testNumberOfFormations() {
        assertEquals(1, this.formation.numberOfFormations());

        this.formation.addFormation();
        assertEquals(2, this.formation.numberOfFormations());

        this.formation.addFormation();
        assertEquals(3, this.formation.numberOfFormations());
    }

    // ==================== currentFormation Tests ====================

    /**
     * Test: currentFormation returns correct index.
     */
    @Test
    public void testCurrentFormation() {
        assertEquals(0, this.formation.currentFormation());

        this.formation.addFormation();
        assertEquals(1, this.formation.currentFormation());

        this.formation.setCurrentFormation(0);
        assertEquals(0, this.formation.currentFormation());
    }

    // ==================== Dimension Tests ====================

    /**
     * Test: formationWidth returns correct value.
     */
    @Test
    public void testFormationWidth() {
        DanceFormation1 f = new DanceFormation1(10, 8);
        assertEquals(10, f.formationWidth());
    }

    /**
     * Test: formationHeight returns correct value.
     */
    @Test
    public void testFormationHeight() {
        DanceFormation1 f = new DanceFormation1(10, 8);
        assertEquals(8, f.formationHeight());
    }

    /**
     * Test: Dimensions are consistent across formations.
     */
    @Test
    public void testDimensionConsistency() {
        this.formation.addFormation();
        this.formation.addFormation();

        this.formation.setCurrentFormation(0);
        int width0 = this.formation.formationWidth();
        int height0 = this.formation.formationHeight();

        this.formation.setCurrentFormation(1);
        int width1 = this.formation.formationWidth();
        int height1 = this.formation.formationHeight();

        this.formation.setCurrentFormation(2);
        int width2 = this.formation.formationWidth();
        int height2 = this.formation.formationHeight();

        assertEquals(width0, width1);
        assertEquals(width1, width2);
        assertEquals(height0, height1);
        assertEquals(height1, height2);
    }

    // ==================== Integration Tests ====================

    /**
     * Test: Complex scenario with multiple operations.
     */
    @Test
    public void testComplexScenario() {
        // Set up first formation
        this.formation.addDancer("Alice", 0, 0);
        this.formation.addDancer("Bob", 1, 1);
        this.formation.addDancer("Charlie", 2, 2);

        // Create second formation
        this.formation.addFormation();
        this.formation.addDancer("Diana", 3, 3);
        this.formation.addDancer("Eve", 4, 4);

        // Verify state
        assertEquals(2, this.formation.numberOfFormations());
        assertEquals(1, this.formation.currentFormation());
        assertEquals("Diana", this.formation.dancerAt(3, 3));

        // Navigate back
        this.formation.setCurrentFormation(0);
        assertEquals("Alice", this.formation.dancerAt(0, 0));
        assertEquals("Bob", this.formation.dancerAt(1, 1));

        // Remove dancer
        this.formation.removeDancer(1, 1);
        assertNull(this.formation.dancerAt(1, 1));
    }

    /**
     * Test: Fill entire formation with dancers.
     */
    @Test
    public void testFillEntireFormation() {
        List<String> largeRoster = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            largeRoster.add("Dancer" + i);
        }
        this.formation.setRoster(largeRoster);

        // Fill 5x5 formation
        int dancerIndex = 0;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                this.formation.addDancer("Dancer" + dancerIndex, x, y);
                dancerIndex++;
            }
        }

        // Verify all positions filled
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                assertEquals("Dancer" + (x * 5 + y),
                        this.formation.dancerAt(x, y));
            }
        }
    }
}