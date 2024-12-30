import org.json.JSONObject;

public class JsonTest {
    public static void main(String[] args) {
        String jsonString = "{ \"name\": \"John\", \"age\": 30 }";
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println("Name: " + jsonObject.getString("name"));
        System.out.println("Age: " + jsonObject.getInt("age"));
    }
}