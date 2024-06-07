package ru.netology.banklogin.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.banklogin.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginElement = $("[data-test-id=login] input");
    private final SelenideElement passwordElement = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id='action-login']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        loginElement.setValue(authInfo.getLogin());
        passwordElement.setValue(authInfo.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo authInfo) {
        loginElement.setValue(authInfo.getLogin());
        passwordElement.setValue(authInfo.getPassword());
        loginButton.click();
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(Condition.exactText(expectedText)).shouldBe(Condition.visible);
    }

    public void clearFields() {
        loginElement.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        passwordElement.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }
}

