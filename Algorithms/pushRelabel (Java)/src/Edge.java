public class Edge {
    double flow;
    double capacity;

    public double getCapacity() {
        return capacity;
    }

    public Edge(double capacity) {
        this.flow = 0;
        this.capacity = capacity;
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }

    @Override
    public String toString() {
        return this.flow + "/" + this.capacity;
    }
}
