public class Dancer {

    private String name;
    private int number;

    public Dancer(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public int getNumber() {
        return this.number;
    }

    @Override
    public String toString() {
        return this.name + " #" + this.number;
    }
}