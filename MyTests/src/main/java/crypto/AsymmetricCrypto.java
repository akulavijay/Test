package crypto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class AsymmetricCrypto {
	public static PublicKey getPubKey(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}
	public static PrivateKey getPrivKey(String filename)   throws Exception {
		    byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
		    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    return kf.generatePrivate(spec);
		  }

	public static void main(String[] args) {
		try {
			//Convert x509 to PEM  openssl x509 -outform der -in certificatename.pem -out certificatename.der
			PublicKey publicKey = getPubKey("C:\\Vijay\\Apps\\portecle-1.11\\MyCerts\\opnssl_Pub.der");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			String plainText = "Hare Krishna";
			System.out.println(plainText);
			cipher.update(plainText.getBytes());
			byte[] cipherText = cipher.doFinal();			
			System.out.println(cipherText);
			String b64Encoded = Base64.getEncoder().encodeToString(cipherText);
			System.out.println(b64Encoded);
			PrivateKey privKey = getPrivKey("C:\\Vijay\\Apps\\portecle-1.11\\MyCerts\\opnssl_Pvt.der");
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] decipheredText = cipher.doFinal(Base64.getDecoder().decode(b64Encoded.getBytes()));
			System.out.println(new String(decipheredText));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
