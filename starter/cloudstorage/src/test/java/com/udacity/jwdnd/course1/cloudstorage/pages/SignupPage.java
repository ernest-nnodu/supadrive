package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    private WebElement firstNameValueField;

    @FindBy(id = "inputLastName")
    private WebElement lastNameValueField;

    @FindBy(id = "inputUsername")
    private WebElement usernameValueField;

    @FindBy(id = "inputPassword")
    private WebElement passwordValueField;

    @FindBy(id = "buttonSignUp")
    private WebElement signupButton;

    @FindBy(id = "success-msg")
    private WebElement successMessageText;

    @FindBy(id = "error-msg")
    private WebElement errorMessageText;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void signupUser(String firstName, String lastName, String username, String password) {
        enterField(firstNameValueField, firstName);
        enterField(lastNameValueField, lastName);
        enterField(usernameValueField, username);
        enterField(passwordValueField, password);
        signupButton.click();
    }

    private void enterField(WebElement webElement, String value) {
        webElement.clear();
        webElement.sendKeys(value);
    }

    public String getSuccessMessage() {
        return successMessageText.getText();
    }

    public String getErrorMessage() {
        return errorMessageText.getText();
    }
}
