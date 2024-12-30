import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Main3 {
    public static void main(String[] args) {
        try (FileReader reader = new FileReader("input1.json")) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);
            
            int n = root.getJSONObject("keys").getInt("n");
            int k = root.getJSONObject("keys").getInt("k");
            
            ArrayList<int[]> points = new ArrayList<>();
            for (String key : root.keySet()) {
                if (!key.equals("keys")) {
                    int x = Integer.parseInt(key);
                    int base = root.getJSONObject(key).getInt("base");
                    String value = root.getJSONObject(key).getString("value");
                    int y = Integer.parseInt(value, base);
                    points.add(new int[] {x, y});
                }
            }

            // Select the first k points for Lagrange interpolation
            BigDecimal constantTerm = calculateLagrangeInterpolation(points.subList(0, k));
            System.out.println("Constant term (c): " + constantTerm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BigDecimal calculateLagrangeInterpolation(List<int[]> points) {
        BigDecimal c = BigDecimal.ZERO;
        int k = points.size();

        for (int i = 0; i < k; i++) {
            int xi = points.get(i)[0];
            BigDecimal yi = BigDecimal.valueOf(points.get(i)[1]);
            BigDecimal li = BigDecimal.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = points.get(j)[0];
                    li = li.multiply(BigDecimal.valueOf(-xj))
                           .divide(BigDecimal.valueOf(xi - xj), 10, RoundingMode.HALF_UP);  // Use RoundingMode.HALF_UP
                }
            }
            c = c.add(yi.multiply(li));
        }
        return c;
    }
}