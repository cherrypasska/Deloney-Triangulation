class Triangle{
    Point p1, p2, p3;

    Triangle(Point p1, Point p2, Point p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    boolean containsVertex(Point p){
        return p.equals(p1) || p.equals(p2) || p.equals(p3);
    }

    boolean circumCircleContains(Point p){
        double ab = (p1.x * p1.x) + (p1.y * p1.y);
        double cd = (p2.x * p2.x) + (p2.y * p2.y);
        double ef = (p3.x * p3.x) + (p3.y * p3.y);

        double circumX = (ab * (p3.y - p2.y) + cd * (p1.y - p3.y) + ef * (p2.y - p1.y)) /
                (p1.x * (p3.y - p2.y) + p2.x * (p1.y - p3.y) + p3.x * (p2.y - p1.y)) / 2;
        double circumY = (ab * (p3.x - p2.x) + cd * (p1.x - p3.x) + ef * (p2.x - p1.x)) /
                (p1.y * (p3.x - p2.x) + p2.y * (p1.x - p3.x) + p3.y * (p2.x - p1.x)) / 2;
        double circumRadius = Math.sqrt(((p1.x - circumX) * (p1.x - circumX)) + ((p1.y - circumY) * (p1.y - circumY)));

        double dist = Math.sqrt(((p.x - circumX) * (p.x - circumX)) + ((p.y - circumY) * (p.y - circumY)));
        return dist <= circumRadius;
    }
}
