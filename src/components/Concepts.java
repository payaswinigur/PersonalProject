package components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

//TODO -add the osu components
/**
 * Concepts class for the Choreography Formations App. This class contains the
 * concepts and methods for the app, but they are not yet broken down into MVC
 * based methods.
 *
 * CAN IGNORE NOT FOR COMPONENT CHECKPOINT PURPOSES
 */

/*USING MVC */

/**
 * Dancer Object Class
 */

class Dancers {
    private String name;
    private int number;
    private Queue<String> formationOrder = new Queue<String>();
    private Map<String, String> formations = new Map<String, String>();

    //need a visual reprersetation of dancer too

    public Dancers(String name, int number) {
        this.name = name;
        this.number = number;
        this.formationOrder = new LinkedList<>();
        this.formations = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public int getNumber() {
        return this.number;
    }

    public Queue<String> getFormationOrder() {
        return this.formationOrder;
    }

    public Map<String, String> getFormations() {
        return this.formations;
    }
}

class Formations {
    /**
     * -number of dancers in formation -grouped coordinates/formations of each
     * dancer -formation name -visual representation of formation
     */
}

/**
 * Abstract Class for the methods for the Choreography Formations App METHODS
 * NOT BROKEN DOWN INTO MVC BASED METHODS YET
 */

abstract class absChoreographyTool {
    /**
     * Creates a new dancer with the given name and number, and adds them to the
     * list of dancers.
     *
     * @param name
     * @param number
     * @return
     */
    public abstract void newDancer(String name, int number);

    /**
     * Removes the dancer with the given name and number from the list of
     * dancers.
     *
     * @param name
     * @param number
     * @return
     */
    public abstract void removeDancer(String name);

    /**
     * Adds a new formation with the given name and description to the list of
     * formations.
     *
     * @param formationName
     * @param formationDescription
     */
    public abstract void addFormation(String formationName,
            String formationDescription);

    /**
     * Removes the formation with the given name from the list of formations.
     *
     * @param formationName
     */
    public abstract void removeFormation(String formationName);

    /**
     * Changes the current formation to the formation with the given name.
     *
     * @param formationName
     */
    public abstract void changeFormation(String formationName);

    /**
     * Copies the formation with the given name.
     *
     * @param formation
     */
    public abstract void copyFormation(String formation);

    /**
     * Clears all formations from the list of formations.
     */
    public abstract void clearFormations();

    /**
     * Returns the name of the choreography tool.
     *
     * @return
     */
    public abstract String getName();

    /**
     * Returns a list of all dancers currently in the choreography tool.
     *
     * @return List of dancers
     */
    public abstract List<Dancers> getDancers();

    /**
     * Returns a list of all formations currently in the choreography tool.
     *
     * @updates updates the view and plays the formations
     */
    public abstract void perform();

    /**
     * Stops the performance and resets the choreography tool to its initial
     * state.
     *
     * @updates the view and stops the performance
     */
    public abstract void stop();

    /**
     * Pauses the performance and allows the user to resume it later.
     *
     * @updates the view and pauses the performance
     */
    public abstract void pause();

    /**
     * Resumes the performance from where it was paused.
     *
     * @updates the view and resumes the performance
     */
    public abstract void resume();
}

// /**
//  * Concrete implementation of the ChoreographyTool abstract class.
//  */
// public class ChoreographyTool {

//     private Map<String, String> formations;
//     private List<Dancers> dancers;

//     public ChoreographyTool() {
//         this.formations = new HashMap<>();
//         this.dancers = new ArrayList<>();
//     }

//     public void newDancer(String name, int number) {
//         this.dancers.add(new Dancers(name, number));
//     }

//     public void addFormation(String name, String description) {
//         this.formations.put(name, description);
//     }

//     public static void main(String[] args) {
//         ChoreographyTool tool = new ChoreographyTool();
//         tool.newDancer("Ava", 1);
//         tool.addFormation("Line", "Straight line formation.");
//         System.out.println("Proof of concept works.");
//     }
// }