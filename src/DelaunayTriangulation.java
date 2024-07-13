import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class DelaunayTriangulation extends JFrame{
    private JTextField pointsCountField;
    private JTextField filePathField;
    private JButton generateButton;
    private JButton triangulateButton;
    private JButton loadFileButton;
    private DrawPanel drawPanel;
    private List<Point> points;
    private List<Triangle> triangles;

    public DelaunayTriangulation(){
        points = new ArrayList<>();
        triangles = new ArrayList<>();

        setTitle("Delaunay Triangulation");
        setSize(800, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        pointsCountField = new JTextField(5);
        filePathField = new JTextField(20);
        generateButton = new JButton("Создать точки");
        triangulateButton = new JButton("Триангуляция Делоне");
        loadFileButton = new JButton("Загрузить из файла");

        controlPanel.add(new JLabel("Количество точек:"));
        controlPanel.add(pointsCountField);
        controlPanel.add(generateButton);
        controlPanel.add(triangulateButton);
        controlPanel.add(new JLabel("Путь к файлу:"));
        controlPanel.add(filePathField);
        controlPanel.add(loadFileButton);

        add(controlPanel, BorderLayout.NORTH);

        drawPanel = new DrawPanel(points, triangles);
        add(drawPanel, BorderLayout.CENTER);

        generateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                generatePoints();
            }
        });

        triangulateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                triangulatePoints();
            }
        });

        loadFileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                loadPointsFromFile();
            }
        });

        setVisible(true);
    }

    private void generatePoints(){
        points.clear();
        String countText = pointsCountField.getText();
        int count;
        try {
            count = Integer.parseInt(countText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректное число", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            points.add(new Point(rand.nextDouble() * drawPanel.getWidth(), rand.nextDouble() * drawPanel.getHeight()));
        }
        triangles.clear();
        drawPanel.repaint();
    }

    private void triangulatePoints(){
        if (points.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Сначала создайте точки", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        triangles = triangulate(points);
        drawPanel.setTriangles(triangles);
        drawPanel.repaint();
    }

    private void loadPointsFromFile(){
        points.clear();
        String filePath = filePathField.getText();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] coords = line.split("\\s+");
                if (coords.length == 2) {
                    double x = Double.parseDouble(coords[0]);
                    double y = Double.parseDouble(coords[1]);
                    points.add(new Point(x, y));
                }
            }
        } catch (IOException | NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Ошибка при чтении файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        triangles.clear();
        drawPanel.repaint();
    }

    public static List<Triangle> triangulate(List<Point> points){
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        for (Point p : points) {
            if (p.x < minX) minX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.x > maxX) maxX = p.x;
            if (p.y > maxY) maxY = p.y;
        }

        double dx = maxX - minX;
        double dy = maxY - minY;
        double deltaMax = Math.max(dx, dy);
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;

        Point p1 = new Point(midX - 20 * deltaMax, midY - deltaMax);
        Point p2 = new Point(midX, midY + 20 * deltaMax);
        Point p3 = new Point(midX + 20 * deltaMax, midY - deltaMax);

        List<Triangle> triangles = new ArrayList<>();
        triangles.add(new Triangle(p1, p2, p3));

        for (Point p : points){
            List<Triangle> badTriangles = new ArrayList<>();
            List<Edge> polygon = new ArrayList<>();

            for (Triangle t : triangles){
                if (t.circumCircleContains(p)){
                    badTriangles.add(t);
                }
            }

            for (Triangle t : badTriangles){
                for (Edge e : Arrays.asList(new Edge(t.p1, t.p2), new Edge(t.p2, t.p3), new Edge(t.p3, t.p1))){
                    boolean shared = false;
                    for (Triangle ot : badTriangles){
                        if (ot == t) continue;
                        if (ot.containsVertex(e.p1) && ot.containsVertex(e.p2)){
                            shared = true;
                            break;
                        }
                    }
                    if (!shared) polygon.add(e);
                }
            }

            for (Triangle t : badTriangles){
                triangles.remove(t);
            }

            for (Edge e : polygon){
                triangles.add(new Triangle(e.p1, e.p2, p));
            }
        }

        triangles.removeIf(t -> t.containsVertex(p1) || t.containsVertex(p2) || t.containsVertex(p3));
        return triangles;
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(DelaunayTriangulation::new);
    }
}