package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id = "success")
    private WebElement successMessageText;

    @FindBy(id = "exist")
    private WebElement existMessageText;

    @FindBy(id = "error")
    private WebElement errorMessageText;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getSuccessMessage() {
        return successMessageText.getText();
    }

    public String getExistMessageText() {
        return existMessageText.getText();
    }

    public String getErrorMessageText() {
        return errorMessageText.getText();
    }
}
