package ru.netology.banklogin.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper(){
    }

    private static final Faker FAKER = new Faker(new Locale("en"));

    public static AuthInfo getAuthInfoWithTestData() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getAuthInfoWithLoginInBaseAndRandomPassword() {
        return new AuthInfo("vasya", generateRandomPassword());
    }

    public static AuthInfo getOtherAuthInfoWithTestData() {
        return new AuthInfo("petya", "123qwerty");
    }

    public static String generateRandomLogin(){
        return FAKER.name().username();
    }

    public static String generateRandomPassword(){
        return FAKER.internet().password();
    }

    public static AuthInfo generateRandomUser(){
        return new AuthInfo(generateRandomLogin(), generateRandomPassword());
    }

    public static VerificationCode getFakeVerificationCode(){
        return new VerificationCode(FAKER.numerify("######"));
    }

    @Value
    public static class AuthInfo{
        private String login;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationCode {
        String code;
    }
}
