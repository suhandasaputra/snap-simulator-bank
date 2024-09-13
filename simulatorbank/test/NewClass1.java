
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author suhan
 */



public class NewClass1 {
    public static void main(String[] args) {
        // Set kunci rahasia API Stripe Anda
        Stripe.apiKey = "sk_test_your_secret_key";

        try {
            // Buat parameter untuk pembuatan charge
            ChargeCreateParams params = ChargeCreateParams.builder()
              .setAmount(1000L) // Jumlah yang akan diisi (dalam sen)
              .setCurrency("usd") // Mata uang
              .setSource("tok_visa") // Token kartu pembayaran (dapat diperoleh melalui Stripe.js)
              .build();

            // Buat charge dengan parameter yang telah ditentukan
            Charge charge = Charge.create(params);

            // Lakukan sesuatu dengan charge (misalnya, verifikasi pembayaran)
            System.out.println("Charge ID: " + charge.getId());
            System.out.println("Status: " + charge.getStatus());
        } catch (StripeException e) {
            // Tangani kesalahan StripeException
            e.printStackTrace();
        }
    }
}
