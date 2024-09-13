
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author suhan
 */
public class NewClass {

    public static void main(String[] args) {
        String secretKey = "contohkuncirahasia"; // Ganti dengan kunci rahasia Anda
        
        // Waktu kadaluarsa token (misalnya, 1 jam dari sekarang)
        long expirationTime = System.currentTimeMillis() + 3600000; // 1 jam dalam milidetik
        
        // Membuat token JWT
        String token = Jwts.builder()
            .setSubject("contoh@pengguna.com") // Subjek token
            .setExpiration(new Date(expirationTime)) // Waktu kadaluarsa token
            .signWith(SignatureAlgorithm.HS256, secretKey) // Tanda tangan token dengan HMAC menggunakan SHA-256 dan kunci rahasia
            .compact();
        
        // Tampilkan token
        System.out.println("Token JWT: " + token);
    }

}
