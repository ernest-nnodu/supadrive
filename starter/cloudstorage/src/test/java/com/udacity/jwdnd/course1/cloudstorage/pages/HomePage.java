package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {

    @FindBy(id = "logout-btn")
    private WebElement logoutButton;

    @FindBy(className = "note-row")
    private List<WebElement> noteRows;

    @FindBy(className = "note-edit-btn")
    private List<WebElement> noteEditButton;

    @FindBy(className = "note-delete-btn")
    private List<WebElement> noteDeleteButton;

    @FindBy(className = "note-title")
    private List<WebElement> noteTitle;

    @FindBy(className = "note-description")
    private List<WebElement> noteDescription;

    @FindBy(id = "note-title")
    private WebElement noteTitleValueField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionValueField;

    @FindBy(id = "save-note-btn")
    private WebElement saveNoteButton;

    @FindBy(className = "credential-row")
    private List<WebElement> credentialRows;

    @FindBy(className = "credential-edit-btn")
    private List<WebElement> credentialEditButton;

    @FindBy(className = "credential-delete-btn")
    private List<WebElement> credentialDeleteButton;

    @FindBy(className = "credential-url")
    private List<WebElement> credentialUrl;

    @FindBy(className = "credential-username")
    private List<WebElement> credentialUsername;

    @FindBy(className = "credential-password")
    private List<WebElement> credentialPassword;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlValueField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameValueField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordValueField;

    @FindBy(id = "save-credential-btn")
    private WebElement saveCredentialButton;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void saveNote(String title, String description) {

        enterField(noteTitleValueField, title);
        enterField(noteDescriptionValueField, description);
        saveNoteButton.click();
    }

    public void logout() {
        logoutButton.click();
    }

    public int getNoteRowCount() {
        return noteRows.size();
    }

    public String getNoteTitle(int index) {
        if (noteRows.isEmpty()) {
            return "";
        }
        return noteTitle.get(index).getText();
    }

    public String getNoteDescription(int index) {
        if (noteRows.isEmpty()) {
            return "";
        }
        return noteDescription.get(index).getText();
    }

    public WebElement getNoteEditButton(int index) {
        if (noteRows.isEmpty()) {
            return null;
        }

        return noteEditButton.get(index);
    }

    public WebElement getNoteDeleteButton(int index) {
        if (noteRows.isEmpty()) {
            return null;
        }

        return noteDeleteButton.get(index);
    }

    private void enterField(WebElement webElement, String value) {
        webElement.clear();
        webElement.sendKeys(value);
    }
}
