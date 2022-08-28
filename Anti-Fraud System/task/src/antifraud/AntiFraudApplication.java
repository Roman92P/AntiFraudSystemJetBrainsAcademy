package antifraud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@SpringBootApplication
public class AntiFraudApplication {
    public static void main(String[] args) {

        SpringApplication.run(AntiFraudApplication.class, args);

    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
//    @Bean
//    public PasswordEncoder getEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public String testBean() {
//        PasswordEncoder encoder = getEncoder();
//        Base64.Encoder encoder1 = Base64.getEncoder();
//        String s = encoder1.encodeToString("admin:123456".getBytes(StandardCharsets.UTF_8));
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decode = decoder.decode("SV9hZG1pcmU6eW91cl9za2lsbHM=".getBytes(StandardCharsets.UTF_8));
//        for (byte b : decode) {
//            System.out.print(Character.toChars(b));
//        }
//        return " ";
//    }
//    @Bean
//    public String passwordEncoder() {
////        String password = "hackme"; // should be kept in a secure place and not be shared
////        String salt = "8560b4f4b3"; // should be hex-encoded with even number of chars
////        System.out.println(NoOpPasswordEncoder.getInstance().encode("qwerty"));
//        String input = "information to encrypt";
//        TextEncryptor textEncryptor = Encryptors.text("password", "3a1f");
//        System.out.println(textEncryptor.encrypt(input));
//        return textEncryptor.encrypt(input);
//    }
}