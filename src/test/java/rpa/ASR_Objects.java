package rpa;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ASR_Objects {

	WebDriver driver;
//	private WebDriverWait wait10, wait20;
	private WebDriverWait wait;
	public ASR_Objects(WebDriver driver) {
		super();
		this.driver = driver;
		wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
	
	
	@FindBy(id = "username")
	public WebElement usernameField;
	
	@FindBy(id = "password")
	public WebElement passwordField;
	
	@FindBy(xpath = "//button[@type='submit']")
	public WebElement loginBtn;
	
	@FindBy(xpath = "//span[text()='Inquiry']")
	public WebElement inquiryTab;
	
	@FindBy(xpath = "//span[text()='Check Claim Status']")
	public WebElement CheckClaimStatusTab;
	
	@FindBy(id = "ctl00_PageContent_txtMemberID")
	public WebElement memberIDField;
	
	@FindBy(id = "ctl00_PageContent_btnSearchAll")
	public WebElement searchBtn;

	
	@FindBy(xpath = "//table[@id='ctl00_PageContent_gvEmployees']/tbody/tr/td[text()='Current']/following-sibling::td/span/a[contains(text(),'more')]")
	public WebElement moreBtn;
	
	@FindBy(xpath = "//span[contains(text(),'No dependents are covered for this employee')]")
	public WebElement noDependents;
	
	
	@FindBy(xpath = "//span[text()='Employee Details']")
	public WebElement employeeDetails;
	
	@FindBy(id = "ctl00_PageContent_lblEeName")
	public WebElement employeeName;
	
	@FindBy(id = "ctl00_PageContent_lnkEeClaims")
	public WebElement employeeClaim;
	
	
	public WebElement claimLink(String lastname, String firstname, String DOB) {
		return driver.findElement(By.xpath("//table[@id='ctl00_PageContent_gvFamilyMembers']/tbody/tr/td[contains(text(),'"+lastname +" "+firstname+"')]/following-sibling::td[contains(text(),'"+DOB+"')]/following-sibling::td/descendant::a[contains(text(),'claims')]"));
	}
	
	
//	@FindBy(xpath = "//table[@id='ctl00_PageContent_gvFamilyMembers']/tbody/tr/td[contains(text(),'MARSHA PASSINO')]/following-sibling::td[contains(text(),'05-09-1975')]/following-sibling::td/descendant::a[contains(text(),'claims')]")
	//public WebElement claimLink;
	
	
	public List<WebElement> viewLink(String DateofService,String charge) {
		return driver.findElements(By.xpath("//table[@id='ctl00_PageContent_gvProcessed']/descendant::td[contains(text(),'"+DateofService+"')]/following-sibling::td[contains(text(),'"+charge+"')]/following-sibling::td/a[contains(text(),'view')]"));
	}
	@FindBy(xpath = "//table[@id='ctl00_PageContent_gvProcessed']/descendant::td[contains(text(),'02-20-2023')]/following-sibling::td[contains(text(),'02-20-2023')]/following-sibling::td/a[contains(text(),'view')]")
	public WebElement viewBtn;
	
	@FindBy(xpath = "//strong[text()='Check Number:']/following-sibling::span[contains(@id,'CheckNumber')]")
	public WebElement checkNumber;
	
	
	@FindBy(xpath = "//strong[text()='Check Date:']/following-sibling::span[contains(@id,'CheckDate')]")
	public WebElement checkDate;
	
	@FindBy(xpath = "//span[text()='Reports']")
	public WebElement reportsTab;
	
	@FindBy(xpath = "//span[text()='Report Dashboard']")
	public WebElement reportsDashboard;
	
	
	@FindBy(xpath = "//i[@class='ui-accordion-header-icon ui-icon fa fa-arrow-circle-right']")
	public WebElement forwardArrowProvider;
	
	@FindBy(xpath = "//i[@class='ui-accordion-header-icon ui-icon fa fa-arrow-circle-down']")
	public WebElement downArrowProvider;
	
	@FindBy(xpath = "//a[text()='Provider Check EOB']")
	public WebElement providerCheckEOB;
	
	
	@FindBy(xpath = "//span[text()='Report Dashboard' and @id='ctl00_lblPageTitle']")
	public WebElement reportDashboardTitle;
	
	@FindBy(id = "ctl00_PageContent_txtCheckNumber")
	public WebElement checkNumberInput;
	
	@FindBy(id = "ctl00_PageContent_dtpickerCheckDate_dateInput")
	public WebElement checkDateInput;
	
	@FindBy(id = "ctl00_PageContent_btnSubmitForm")
	public WebElement SearchBtn;
	/////////////////////////////////////
	
	@FindBy(id = "doctorID")
	public WebElement usernameFieldECW;
	
	@FindBy(id = "passwordField")
	public WebElement passwordFieldECW;
	

	@FindBy(id = "Login")
	public WebElement loginBtnECW;
	
	@FindBy(id = "nextStep")
	public WebElement nextBtnECW;
	
	@FindBy(xpath = "//a[text()='Action' and @lookupshortcut]")
	public WebElement patientLookupBtnECW;
	
	@FindBy(xpath = "//div[@class='favlist']/a[@class='navgator mainMenu']")
	public WebElement expandMenubtnECW;
	
	@FindBy(xpath = "//li[@title='Billing']/a")
	public WebElement billingTabECW;
	
	@FindBy(xpath = "//span[text()='Claims']/parent::a")
	public WebElement claimsMenuECW;
	
	@FindBy(id = "claimLookupIpt10")
	public WebElement claimLookupInputECW;
	
	@FindBy(id = "btnclaimlookup")
	public WebElement claimLookupBtnECW;
	
	@FindBy(xpath = "//button[@ng-click='saveAllData()']")
	public WebElement saveClaimBtnECW;
	
	
	@FindBy(xpath = "//div[@class='billing-right-toggle claimRightPanel-tog']")
	public WebElement followUpArrowECW;
	
	@FindBy(id = "claimRightPanelNotes")
	public WebElement claimNotesECW;
	
	@FindBy(id = "ctl00_PageContent_memberIdValidation")
	public WebElement memberIDInvalid;
	
	
	public void waitFunc(WebElement webEle) {
		wait.until(ExpectedConditions.elementToBeClickable(webEle));
	}
	
	public void waitFuncList(List<WebElement> webEle) {
		wait.until(ExpectedConditions.visibilityOfAllElements(webEle));
	}
	
	public void waitFuncInvisibility(WebElement webEle) {
		wait.until(ExpectedConditions.invisibilityOf(webEle));
	}
	
}
