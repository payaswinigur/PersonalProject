package com.danceformation.components;

/**
 * An immutable (x, y) spot on the stage grid.
 *
 * <p>
 * Declared as a record so that equality is by value rather than by identity.
 * That matters because DanceFormation1 stores a Position as a map value and
 * finds the dancer at a spot by comparing positions, which only works if two
 * separately constructed Position objects with the same coordinates are equal.
 * </p>
 *
 * <p>
 * <b>Conventions:</b>
 * <ul>
 * <li>x is the column index, 0 to formationWidth() - 1</li>
 * <li>y is the row index, 0 to formationHeight() - 1</li>
 * </ul>
 * </p>
 *
 * @param x
 *            the column index
 * @param y
 *            the row index
 * @author Payaswini Gurung
 * @version 1.0
 */
public record Position(int x, int y) {
}
