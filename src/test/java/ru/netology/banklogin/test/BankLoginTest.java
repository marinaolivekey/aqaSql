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

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("should successfully login to Dashboard with exist login and password from SUT test data")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
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
    void shouldGetErrorNotificationIfLoginWithExistUserButRandomVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.getFakeVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }

    @Test
    @DisplayName("should block user if active user entered password incorrectly 3 times")
    void shouldBlockSystemIfActiveUserEnteredPasswordIncorrectlyThreeTimes() {
        var authInfo = DataHelper.getAuthInfoWithLoginInBaseAndRandomPassword();
        var authInfoTest = DataHelper.getAuthInfoWithTestData();
        loginPage.invalidLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
        loginPage.clearFields();
        loginPage.invalidLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
        loginPage.clearFields();
        loginPage.invalidLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
        loginPage.clearFields();
        loginPage.invalidLogin(authInfoTest);
        loginPage.verifyErrorNotification("Ошибка! \nСлишком много попыток! Повторите через 2 минуты");
    }
}
