import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Answer {
    public static void main(String[] args) {
        try {
            // Read and process input1.json
            System.out.println("Processing input1.json...");
            BigDecimal constantTerm1 = processFile("input1.json");
            System.out.println("Constant term from input1.json: " + constantTerm1);

            // Read and process input2.json
            System.out.println("Processing input2.json...");
            BigDecimal constantTerm2 = processFile("input2.json");
            System.out.println("Constant term from input2.json: " + constantTerm2);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to process a given JSON file and calculate the constant term using Lagrange interpolation
    private static BigDecimal processFile(String fileName) throws Exception {
        try (FileReader reader = new FileReader(fileName)) {
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

                    // Use BigInteger for large numbers with specified base
                    BigInteger y = new BigInteger(value, base);
                    points.add(new int[] {x, y.intValue()});
                }
            }

            // Select the first k points for Lagrange interpolation
            return calculateLagrangeInterpolation(points.subList(0, k));
        }
    }

    // Lagrange interpolation method
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