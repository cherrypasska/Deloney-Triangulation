import java.util.Objects;

class Edge{
    Point p1, p2;

    Edge(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return (Objects.equals(p1, edge.p1) && Objects.equals(p2, edge.p2)) ||
                (Objects.equals(p1, edge.p2) && Objects.equals(p2, edge.p1));
    }
}

