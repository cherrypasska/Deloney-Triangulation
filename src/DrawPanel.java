import javax.swing.*;
import java.awt.*;
import java.util.List;

class DrawPanel extends JPanel{
    private List<Point> points;
    private List<Triangle> triangles;

    public DrawPanel(List<Point> points, List<Triangle> triangles){
        this.points = points;
        this.triangles = triangles;
    }

    public void setTriangles(List<Triangle> triangles){
        this.triangles = triangles;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLUE);
        for (Triangle t : triangles){
            g2.drawLine((int) t.p1.x, (int) t.p1.y, (int) t.p2.x, (int) t.p2.y);
            g2.drawLine((int) t.p2.x, (int) t.p2.y, (int) t.p3.x, (int) t.p3.y);
            g2.drawLine((int) t.p3.x, (int) t.p3.y, (int) t.p1.x, (int) t.p1.y);
        }

        g2.setColor(Color.RED);
        for (Point p : points){
            g2.fillOval((int) p.x - 2, (int) p.y - 2, 4, 4);
        }
    }
}