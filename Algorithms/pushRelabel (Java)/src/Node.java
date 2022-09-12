public class Node {
    int height;
    double excess;
    int ID;

    public Node() {
        this.height = 0;
        this.excess = 0;
        this.ID = 0;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getExcess() {
        return excess;
    }

    public void setExcess(double excess) {
        this.excess = excess;
    }

    public String toString() {
        return "(ID: " + this.getID() + ", H: " + this.height + ", E: " + this.excess + ")";
    }

}
