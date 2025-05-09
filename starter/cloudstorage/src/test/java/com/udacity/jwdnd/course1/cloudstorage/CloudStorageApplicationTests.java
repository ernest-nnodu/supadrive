package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.util.Assert;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.io.File;
import java.time.Duration;
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {

		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {

		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@DisplayName("Unauthorized user cannot access homepage")
	public void homePage_unauthorizedUserAccess_accessDenied() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@DisplayName("Unauthorized user can access signup page")
	public void signupPage_unauthorizedUserAccess_accessGranted() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@DisplayName("Unauthorized user can access login page")
	public void loginPage_unauthorizedUserAccess_accessGranted() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@DisplayName("Signup a new user")
	public void signupPage_newUserDetailsProvided_userSignupSuccessful() {
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signupUser("peter", "pan", "peterpab", "pp");
		Assertions.assertTrue(signupPage.getSuccessMessage().contains("You successfully signed up!"));
	}

	@Test
	@DisplayName("Registered user can access home page")
	public void loginPage_registeredUserProvided_homepageAccessGranted() {
		login("peterpan", "pp");
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	@DisplayName("User redirected to login page when logged out")
	public void homePage_whenUserLogout_redirectToLoginPage() {
		login("peterpan", "pp");
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/*----------------------------------------------------START OF NOTE TEST-------------------------------------------*/
	@Test
	@DisplayName("Create a new note")
	public void homePage_createNewNote_createdNoteDisplayed() {

		String expectedTitle = "New Note";
		String expectedDescription = "This is a new note";
		int noteRowCount = getNoteRowCount();

		createNote(expectedTitle, expectedDescription);

		Note recentNote = getRecentNote();
		Assertions.assertAll(() -> Assertions.assertEquals(expectedTitle, recentNote.getNotetitle()),
				() -> Assertions.assertEquals(expectedDescription, recentNote.getNotedescription()),
				() -> Assertions.assertEquals(noteRowCount + 1, getNoteRowCount()));
	}

	@Test
	@DisplayName("Edit an existing note")
	public void homePage_editExistingNote_editedNoteUpdated() {
		String expectedTitle = "Update Note";
		String expectedDescription = "Updated note";

		editNote(expectedTitle, expectedDescription);

		Note recentNote = getRecentNote();
		Assertions.assertAll(() -> Assertions.assertEquals(expectedTitle, recentNote.getNotetitle()),
				() -> Assertions.assertEquals(expectedDescription, recentNote.getNotedescription()));
	}

	@Test
	@DisplayName("Delete an existing note")
	public void homePage_deleteExistingNote_noteNoLongerDisplayed() {
		int noteRowCount = getNoteRowCount();

		deleteNote();

		if (noteRowCount == 0) {
			Assertions.assertEquals(noteRowCount, getNoteRowCount());
		} else {
			Assertions.assertEquals(noteRowCount - 1, getNoteRowCount());
		}
	}

	private void navigateToNoteTab() {
		login("peterpan", "pp");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		driver.findElement(By.id("nav-notes-tab")).click();
	}

	private void createNote(String title, String description) {
		navigateToNoteTab();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement newNoteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-note-btn")));
		newNoteBtn.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));
		HomePage homePage = new HomePage(driver);
		homePage.saveNote(title, description);
	}

	private void editNote(String title, String description) {
		navigateToNoteTab();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		int noteRowCount = getNoteRowCount();

		if (noteRowCount == 0) {
			createNote("Temp Note", "Temporary note");
			wait.until(ExpectedConditions.urlContains("/notes"));
		}

		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		WebElement noteNavigation = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		noteNavigation.click();
		noteRowCount = getNoteRowCount();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-title")));
		WebElement editButton = homePage.getNoteEditButton(noteRowCount - 1);
		editButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));
		homePage.saveNote(title, description);
	}

	private void deleteNote() {
		navigateToNoteTab();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		int noteRowCount = getNoteRowCount();

		if (noteRowCount == 0) {
			createNote("Temp Note", "Temporary note");
			wait.until(ExpectedConditions.urlContains("/notes"));
		}

		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		WebElement noteNavigation = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		noteNavigation.click();
		noteRowCount = getNoteRowCount();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-title")));
		WebElement deleteButton = homePage.getNoteDeleteButton(noteRowCount - 1);
		deleteButton.click();
	}

	private Note getRecentNote() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.urlContains("/notes"));
		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		WebElement noteNavigation = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		noteNavigation.click();
		int rowCount = getNoteRowCount();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-title")));
		String noteTitle = homePage.getNoteTitle(rowCount - 1);
		String noteDescription = homePage.getNoteDescription(rowCount - 1);

		Note note = new Note();
		note.setNotetitle(noteTitle);
		note.setNotedescription(noteDescription);

		return note;
	}

	private int getNoteRowCount() {
		HomePage homePage = new HomePage(driver);
		return homePage.getNoteRowCount();
	}

	/*---------------------------START OF CREDENTIAL TEST-----------------------------------------------*/
	@Test
	@DisplayName("Create a new credential")
	public void homePage_createNewCredential_createdCredentialDisplayedAndPasswordEncrypted() {

		String expectedUrl = "www.example.com";
		String expectedUsername = "username";
		String expectedPassword = "password";
		int credentialRowCount = getCredentialRowCount();

		createCredential(expectedUrl, expectedUsername, expectedPassword);

		Credential recentCredential = getRecentCredential();
		Assertions.assertAll(() -> Assertions.assertEquals(expectedUrl, recentCredential.getUrl()),
				() -> Assertions.assertEquals(expectedUsername, recentCredential.getUsername()),
				() -> Assertions.assertEquals(24, recentCredential.getPassword().length()),
				() -> Assertions.assertEquals(credentialRowCount + 1, getCredentialRowCount()));
	}

	@Test
	@DisplayName("Edit an existing credential")
	public void homePage_editExistingCredential_editedCredentialUpdated() {
		String expectedUrl = "www.update.org";
		String expectedUsername = "update";
		String password = "pass";

		editCredential(expectedUrl, expectedUsername, password);

		Credential recentCredential = getRecentCredential();
		Assertions.assertAll(() -> Assertions.assertEquals(expectedUrl, recentCredential.getUrl()),
				() -> Assertions.assertEquals(expectedUsername, recentCredential.getUsername()));
	}

	@Test
	@DisplayName("Delete an existing credential")
	public void homePage_deleteExistingCredential_credentialNoLongerDisplayed() {
		int credentialRowCount = getCredentialRowCount();
		deleteCredential();

		if (credentialRowCount == 0) {
			Assertions.assertEquals(credentialRowCount, getCredentialRowCount());
		} else {
			Assertions.assertEquals(credentialRowCount - 1, getCredentialRowCount());
		}
	}

	private void createCredential(String url, String username, String password) {
		navigateToCredentialTab();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement newCredentialBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-credential-btn")));
		newCredentialBtn.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		HomePage homePage = new HomePage(driver);
		homePage.saveCredential(url, username, password);
	}

	private void editCredential(String url, String username, String password) {
		navigateToCredentialTab();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		int credentialRowCount = getCredentialRowCount();

		if (credentialRowCount == 0) {
			createCredential("www.temp.com", "temp", "pass");
			wait.until(ExpectedConditions.urlContains("/credentials"));
		}

		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		WebElement credentialNavigation = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialNavigation.click();
		credentialRowCount = getCredentialRowCount();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("credential-url")));
		WebElement editButton = homePage.getCredentialEditButton(credentialRowCount - 1);
		editButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		homePage.saveCredential(url, username, password);
	}

	private void deleteCredential() {
		navigateToCredentialTab();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		int credentialRowCount = getCredentialRowCount();

		if (credentialRowCount == 0) {
			createCredential("www.temp.com", "temp", "t123");
			wait.until(ExpectedConditions.urlContains("/credentials"));
		}

		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		WebElement credentialNavigation = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab")));
		credentialNavigation.click();
		credentialRowCount = getCredentialRowCount();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("credential-url")));
		WebElement deleteButton = homePage.getCredentialDeleteButton(credentialRowCount - 1);
		deleteButton.click();
	}

	private void navigateToCredentialTab() {
		login("peterpan", "pp");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		driver.findElement(By.id("nav-credentials-tab")).click();
	}

	private int getCredentialRowCount() {
		HomePage homePage = new HomePage(driver);
		return homePage.getCredentialRowCount();
	}

	private Credential getRecentCredential() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.urlContains("/credentials"));
		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		WebElement credentialNavigation = wait.until(ExpectedConditions.elementToBeClickable(
				By.id("nav-credentials-tab")));
		credentialNavigation.click();
		int rowCount = getCredentialRowCount();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("credential-url")));
		String credentialUrl = homePage.getCredentialUrl(rowCount - 1);
		String credentialUsername = homePage.getCredentialUsername(rowCount - 1);
		String credentialPassword = homePage.getCredentialPassword(rowCount - 1);

		Credential credential = new Credential();
		credential.setUrl(credentialUrl);
		credential.setUsername(credentialUsername);
		credential.setPassword(credentialPassword);

		return credential;
	}

	private Credential getPlainRecentCredential() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.urlContains("/credentials"));
		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		WebElement credentialNavigation = wait.until(ExpectedConditions.elementToBeClickable(
				By.id("nav-credentials-tab")));
		credentialNavigation.click();
		int credentialRowCount = getCredentialRowCount();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("credential-url")));
		WebElement editButton = homePage.getCredentialEditButton(credentialRowCount - 1);
		editButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

		String credentialUrl = homePage.getCredentialUrlValueField();
		String credentialUsername = homePage.getCredentialUsernameValueField();
		String credentialPassword = homePage.getCredentialPasswordValueField();

		Credential credential = new Credential();
		credential.setUrl(credentialUrl);
		credential.setUsername(credentialUsername);
		credential.setPassword(credentialPassword);

		return credential;
	}

	private void login(String username, String password) {
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser(username, password);
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

}
