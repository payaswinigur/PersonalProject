package Components;

import java.util.List;

/**
 * Secondary interface providing convenience methods for dance formation
 * management.
 *
 * <p>
 * <b>Overview:</b><br>
 * DanceFormation extends DanceFormationKernel with additional convenience
 * methods that provide higher-level operations for working with dance
 * formations. While the kernel interface defines core operations, this
 * interface adds query and navigation methods that build upon those core
 * operations.
 * </p>
 *
 * <p>
 * <b>Purpose:</b><br>
 * This interface is designed to provide clients with:
 * <ul>
 * <li>Easy navigation between formation slides (next/previous)</li>
 * <li>Query methods for checking dancer presence and position status</li>
 * <li>Bulk retrieval of dancers in the current formation</li>
 * <li>All kernel functionality inherited from DanceFormationKernel</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Usage Pattern:</b><br>
 * Implement this interface when you need both kernel operations (from
 * DanceFormationKernel) and convenience operations (from DanceFormation). Most
 * client code should reference variables of this type rather than
 * DanceFormationKernel.
 * </p>
 *
 * <p>
 * <b>Conventions:</b><br>
 * Inherits all conventions from DanceFormationKernel:
 * <ul>
 * <li>formations: A list of 2D arrays representing slides</li>
 * <li>current: The index of the active formation</li>
 * <li>Each String[][] grid has fixed dimensions (width x height)</li>
 * <li>Null entries represent empty dancer positions</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Representation Invariant:</b><br>
 * Inherits all invariants from DanceFormationKernel
 * </p>
 *
 * <p>
 * <b>Correspondence:</b><br>
 * Inherits correspondence from DanceFormationKernel
 * </p>
 *
 * @author Payaswini Gurung
 * @version 1.0
 * @since 1.0
 *
 * @see DanceFormationKernel
 * @see DanceFormationSecondary
 * @see DanceFormation1
 */

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