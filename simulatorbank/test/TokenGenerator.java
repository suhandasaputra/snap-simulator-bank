import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TokenGenerator {
    // Method untuk membuat token menggunakan HMAC
    public static String generateToken(String secretKey, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            byte[] bytes = sha256_HMAC.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method untuk memverifikasi token
    public static boolean verifyToken(String secretKey, String data, String token) {
        String generatedToken = generateToken(secretKey, data);
        return generatedToken != null && generatedToken.equals(token);
    }

    public static void main(String[] args) {
        String secretKey = "my_secret_key";
        String data = "my_data_to_be_tokenized";

        // Membuat token
        String token = generateToken(secretKey, data);
        System.out.println("Token: " + token);

        // Memverifikasi token
        boolean isTokenValid = verifyToken(secretKey, data, token);
        System.out.println("Is token valid? " + isTokenValid);
    }
}
