package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "inputUsername")
    private WebElement usernameValueField;

    @FindBy(id = "inputPassword")
    private WebElement passwordValueField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(id = "logout-msg")
    private WebElement logoutMessageText;

    @FindBy(id = "error-msg")
    private WebElement errorMessageText;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void loginUser(String username, String password) {
        enterField(usernameValueField, username);
        enterField(passwordValueField, password);
        loginButton.click();
    }

    public String getLogoutMessage() {
        return logoutMessageText.getText();
    }

    public String getErrorMessage() {
        return errorMessageText.getText();
    }

    private void enterField(WebElement webElement, String value) {
        webElement.clear();
        webElement.sendKeys(value);
    }
}
