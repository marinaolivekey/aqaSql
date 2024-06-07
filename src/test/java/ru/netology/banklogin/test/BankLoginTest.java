package ru.netology.banklogin.test;

import org.junit.jupiter.api.*;
import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;
import ru.netology.banklogin.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.banklogin.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage;

    @AfterEach
    void tearDown(){
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll(){
        cleanDatabase();
    }

    @BeforeEach
    void setup(){
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("should successfully login to Dashboard with exist login and password from SUT test data")
    void shouldSuccessfulLogin(){
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage  = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("should get error notification if user is not exist in db")
    void shouldGetErrorNotificationIfUserNotInBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");

    }

    @Test
    @DisplayName("should get error notification if login with exist in base and active user but random verification code")
    void shouldGetErrorNotificationIfLoginWithExistUserButRandomVerificationCode(){
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage  = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.getFakeVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }

}
