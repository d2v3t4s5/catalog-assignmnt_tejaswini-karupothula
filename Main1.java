import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.util.HashMap;

public class Main1 {
    public static void main(String[] args) {
        // Load and parse JSON file
        try (FileReader reader = new FileReader("input1.json")) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);
            
            // Read the "keys" object
            JSONObject keys = root.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");
    
            System.out.println("Number of roots (n): " + n);
            System.out.println("Minimum roots required (k): " + k);
    
            // Map to store decoded (x, y) pairs
            HashMap<Integer, Integer> points = new HashMap<>();
    
            // Decode each root in the JSON file
            for (String key : root.keySet()) {
                if (key.equals("keys")) continue; // Skip the "keys" object
                
                int x = Integer.parseInt(key); // x is the key of the object
                
                JSONObject point = root.getJSONObject(key);
                int base = point.getInt("base");
                String value = point.getString("value");
    
                // Decode y value from the specified base to base 10
                int y = Integer.parseInt(value, base);
                points.put(x, y);
    
                System.out.println("Point (" + x + ", " + y + ") decoded from base " + base);
            }
            
            // At this point, 'points' contains all decoded (x, y) pairs
            System.out.println("Decoded points: " + points);
            
            // Calculate and print the constant term using Lagrange Interpolation
            int constantTerm = lagrangeInterpolation(points);
            System.out.println("Constant term (c) of the polynomial: " + constantTerm);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // Updated Lagrange Interpolation Method using BigInteger
    // Add this method inside the Main class
public static int lagrangeInterpolation(HashMap<Integer, Integer> points) {
    int constantTerm = 0;
    
    // Iterate over each xi in the points HashMap
    for (Integer xi : points.keySet()) {
        int yi = points.get(xi);  // Get corresponding y value for xi
        
        // Calculate the Lagrange basis polynomial L_i(0)
        double Li = 1.0;
        for (Integer xj : points.keySet()) {
            if (!xj.equals(xi)) {  // Skip if xj is the same as xi
                Li *= (0 - xj) / (double)(xi - xj);  // Calculate L_i(0)
            }
        }
        
        // Update the constant term by adding yi * Li
        constantTerm += (int)(Li * yi);
    }
    
    return constantTerm;
}
}