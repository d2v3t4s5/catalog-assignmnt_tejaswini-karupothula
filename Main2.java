import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.util.HashMap;
import java.math.BigInteger;

public class Main2 {
    public static void main(String[] args) {
        // Load and parse JSON file
        try (FileReader reader = new FileReader("input2.json")) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);
            
            // Read the "keys" object
            JSONObject keys = root.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            System.out.println("Number of roots (n): " + n);
            System.out.println("Minimum roots required (k): " + k);

            // Map to store decoded (x, y) pairs
            HashMap<Integer, BigInteger> points = new HashMap<>();

            // Decode each root in the JSON file
            for (String key : root.keySet()) {
                if (key.equals("keys")) continue; // Skip the "keys" object
                
                int x = Integer.parseInt(key); // x is the key of the object
                
                JSONObject point = root.getJSONObject(key);
                int base = point.getInt("base");
                String value = point.getString("value");

                // Decode y value from the specified base to base 10 using BigInteger
                BigInteger y = new BigInteger(value, base);
                points.put(x, y);

                System.out.println("Point (" + x + ", " + y + ") decoded from base " + base);
            }
            
            // At this point, 'points' contains all decoded (x, y) pairs
            System.out.println("Decoded points: " + points);

            BigInteger constantTerm = lagrangeInterpolation(points);
            System.out.println("Constant term (c) of the polynomial: " + constantTerm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Updated Lagrange Interpolation Method using BigInteger
    public static BigInteger lagrangeInterpolation(HashMap<Integer, BigInteger> points) {
        BigInteger constantTerm = BigInteger.ZERO;

        for (Integer xi : points.keySet()) {
            BigInteger yi = points.get(xi);
            BigInteger Li = BigInteger.ONE;

            for (Integer xj : points.keySet()) {
                if (!xj.equals(xi)) {
                    BigInteger numerator = BigInteger.ZERO.subtract(BigInteger.valueOf(xj));
                    BigInteger denominator = BigInteger.valueOf(xi - xj);
                    Li = Li.multiply(numerator).divide(denominator);
                }
            }
            constantTerm = constantTerm.add(Li.multiply(yi));
        }
        
        return constantTerm;
    }
}