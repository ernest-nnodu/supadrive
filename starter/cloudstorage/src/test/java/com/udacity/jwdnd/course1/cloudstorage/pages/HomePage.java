package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {

    @FindBy(id = "logout-btn")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteNavigationTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialNavigationTab;

    @FindBy(id = "new-note-btn")
    private WebElement addNoteButton;

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

    private void enterField(WebElement webElement, String value) {
        webElement.clear();
        webElement.sendKeys(value);
    }
}
