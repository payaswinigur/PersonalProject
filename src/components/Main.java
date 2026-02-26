import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for the Choreography Formations App
 */
public class ChoreographyTool {

    private Map<String, String> formations;
    private List<Dancer> dancers;

    public ChoreographyTool() {
        this.formations = new HashMap<>();
        this.dancers = new ArrayList<>();
    }

    public void newDancer(String name, int number) {
        this.dancers.add(new Dancer(name, number));
    }

    public void addFormation(String name, String description) {
        this.formations.put(name, description);
    }

    public void perform() {
        System.out.println("Performing formations:");
        for (String key : this.formations.keySet()) {
            System.out.println(key + " -> " + this.formations.get(key));
        }
    }

    public static void main(String[] args) {
        ChoreographyTool tool = new ChoreographyTool();

        tool.newDancer("Ava", 1);
        tool.newDancer("Leo", 2);

        tool.addFormation("Line", "Straight line formation.");
        tool.addFormation("Circle", "Circle formation.");

        tool.perform();
    }
}