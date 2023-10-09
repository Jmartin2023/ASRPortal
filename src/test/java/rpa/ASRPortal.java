package rpa;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import objects.ExcelOperations;
import objects.SeleniumUtils;
import objects.Utility;
import utilities.ExcelReader;




public class ASRPortal {
	Logger logger = LogManager.getLogger(ASRPortal.class);

	String projDirPath, status, memberID, firstName ,lastName, DOB ,serviceDate ,claimNo, checkDate, checkNum,date,ecwStatus, employeeName, patientFullName, charges,currency;
	String[] cptArray;
	SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");
	// output format: yyyy-MM-dd
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	//System.out.println(formatter.format(parser.parse(data.get("DOB"))));
	public static ExcelReader excel; public static String sheetName = "ASR health";
	int rowNum = 1;

	WebDriver driver;
	
	//JavascriptExecutor js;
	SeleniumUtils sel;
	Utility utility;
	
	ExcelOperations excelFile;
	ASR_Objects asrObj;
	static String excelFileName;

@BeforeTest
public void preRec() throws InterruptedException, SAXException, IOException, ParserConfigurationException {
	
	sel = new SeleniumUtils(projDirPath);

	driver = sel.getDriver();

	//js = (JavascriptExecutor) driver;
	asrObj= new ASR_Objects(driver);
	utility = new Utility();
	
	String[] params = new String[]{"url", "username", "password", "excelName"};
	HashMap<String, String> configs = utility.getConfig("config.xml", params);

	String url = configs.get("url"), 
			username = configs.get("username"), 
			password = configs.get("password");

	excelFileName = configs.get("excelName");
	System.out.println(excelFileName);

	driver.get(url);
	logger.info("Open url: " + url);
	asrObj.waitFunc(asrObj.usernameField);
	
	asrObj.usernameField.clear();
	asrObj.usernameField.sendKeys(username);
	logger.info("Enter username: " + username);

	asrObj.passwordField.clear();
	asrObj.passwordField.sendKeys(password);
	logger.info("Enter password");

	asrObj.loginBtn.click();
	logger.info("Clicked on login");
	
	
}
@Test(dataProvider= "getData",priority=2) 
public void ASRCase(Hashtable<String,String> data) throws InterruptedException, ParseException, IOException {
	rowNum++;
	status = data.get("Final Status");
	
	if(status.isEmpty() || status.isBlank()) {
	SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");
	// output format: yyyy-MM-dd
	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
	 DOB = data.get("Date of Birth");
	System.out.println(formatter.format(parser.parse(DOB)) +" is date");
	 date=	formatter.format(parser.parse(DOB));
	 firstName = data.get("First Name ").toUpperCase();
	 lastName = data.get("Last Name").toUpperCase();
	 patientFullName = data.get("PATIENT");
	 logger.info("Last name is "+lastName);
	 logger.info("First name is "+firstName);
	 
	 charges = data.get("CHARGES");
		double balanceDouble = Double.parseDouble(charges); 
		 NumberFormat currencyformatter=NumberFormat.getCurrencyInstance(Locale.US);  
		  currency=currencyformatter.format(balanceDouble);
		System.out.println(currency);
	 
	asrObj.waitFunc(asrObj.inquiryTab);
	asrObj.inquiryTab.click();
	logger.info("Hovered over 'Inquiry'");
	
	asrObj.waitFunc(asrObj.CheckClaimStatusTab);
//	Thread.sleep(3000);
	asrObj.CheckClaimStatusTab.click();
	logger.info("Clicked on 'Check Claim Status Tab'");

	 
	 claimNo = data.get("Claims#");
	 
	// DOB = data.get("Date of Birth");
//	 logger.info("DOB is "+DOB);
	 serviceDate = data.get("SERVICE DATE");
	 memberID= data.get("Member ID");
	 serviceDate= formatter.format(parser.parse(serviceDate));
	

	
	
	

	
		asrObj.waitFunc(asrObj.memberIDField);
		Thread.sleep(2000);
		asrObj.memberIDField.clear();
	/*	int claimNumber = 0;
		try {
			Double claimNum = Double.parseDouble(claimNo);
			 claimNumber = claimNum.intValue();
			 Integer.toString(claimNumber);
		}catch(Exception e) {
			
		}*/
		//System.out.println(Integer.toString(claimNumber));
	//	adjObj.claimField.sendKeys(Integer.toString(claimNumber));
	//	logger.info("Entered Claim No. in Claims Field" +Integer.toString(claimNumber));
	
		asrObj.memberIDField.sendKeys(memberID);
		logger.info("Member ID entered: "+ memberID);
		
		asrObj.searchBtn.click();
		logger.info("Clicked on Search Button");
		
		try {
			asrObj.waitFunc(asrObj.memberIDInvalid);
			logger.info("Member ID is invalid");
			excel.setCellData(sheetName, "Final Status", rowNum, "Invalid Member ID");
			throw new SkipException("Skipping this exception,Invalid Member ID");
		}catch(Exception e) {
			
		}
		
		try {
			asrObj.waitFunc(asrObj.moreBtn);
			asrObj.moreBtn.click();
			logger.info("Clicked on More Button");
		}catch(Exception e) {
			excel.setCellData(sheetName, "Final Status", rowNum, "Terminated");
			throw new SkipException("Skipping this exception, Terminated");
		}
		asrObj.waitFunc(asrObj.employeeDetails);
		employeeName= asrObj.employeeName.getText();
		if(employeeName.equals(firstName+" "+lastName)) {
			asrObj.employeeClaim.click();
			logger.info("Clicked on Employee claim link with name and DOB as: "+ lastName+ " "+firstName+" "+date);
		}
		else {
		try {
			asrObj.noDependents.isDisplayed();
			excel.setCellData(sheetName, "Final Status", rowNum, "No dependents are covered for this employee");
			throw new SkipException("Skipping this exception");
		}catch(Exception e) {
			
		}
		try {		
			asrObj.claimLink(lastName, firstName, date).click();
		logger.info("Clicked on claim link with name and DOB as: "+ lastName+" "+firstName+" "+date);
		}catch(Exception e) {
			try {
			asrObj.claimLink(firstName, lastName, date).click();
			logger.info("Clicked on claim link with name and DOB as: "+ firstName+" "+lastName+" "+date);
			}catch(Exception e1) {
				excel.setCellData(sheetName, "Final Status", rowNum, "Name or DOB mismatch");
				throw new SkipException("Skipping this exception");
			}
		}
		}
		try {
		asrObj.waitFuncList(asrObj.viewLink(serviceDate, currency));
		}catch(Exception e) {
			
		}
		if(asrObj.viewLink(serviceDate,currency).size()>1) {
			excel.setCellData(sheetName, "Final Status", rowNum, "multiple records with same charge and DOS");
			throw new SkipException("Skipping this exception, multiple records with same charge and DOS");
		}
		try {
		asrObj.viewLink(serviceDate,currency).get(0).click();
		logger.info("Clicked on View");
		}catch(Exception e) {
			excel.setCellData(sheetName, "Final Status", rowNum, "DOS or Charge amount mismatch");
			throw new SkipException("Skipping this exception, DOS or Charge amount mismatch");
		}
		asrObj.waitFunc(asrObj.checkDate);
		checkDate = asrObj.checkDate.getText();
		checkNum = asrObj.checkNumber.getText();
		
		logger.info("Check Date is: "+ checkDate);
		logger.info("Check Number is: "+ checkNum);
		
		asrObj.reportsTab.click();
		logger.info("Hovered over Reports");
		asrObj.waitFunc(asrObj.reportsDashboard);
		asrObj.reportsDashboard.click();
		logger.info("Clicked on Reports Dashboard");
		
		asrObj.waitFunc(asrObj.reportDashboardTitle);
		
		try {
			asrObj.forwardArrowProvider.isDisplayed();
			asrObj.forwardArrowProvider.click();
			logger.info("Forward Arrow Clicked");
		}catch(Exception e) {
			
		}
		asrObj.waitFunc(asrObj.providerCheckEOB);
		asrObj.providerCheckEOB.click();
		
		asrObj.waitFunc(asrObj.checkDateInput);
		asrObj.checkDateInput.sendKeys(checkDate);
		
		asrObj.waitFunc(asrObj.checkNumberInput);
		asrObj.checkNumberInput.sendKeys(checkNum);
		
		asrObj.SearchBtn.click();
		logger.info("Clicked on Search Check");
		Thread.sleep(5000);
		System.out.println(System.getProperty("user.dir"));
		
		excel.setCellData(sheetName, "Final Status", rowNum, "Pass");
		logger.info("status set");
//			excel.setCellData(sheetName, "Final Status", rowNum, "fail");
//			Assert.fail(e.getMessage().toString());
//			
	
		
		excel.setCellData(sheetName, "Check Number", rowNum, checkNum);
		excel.setCellData(sheetName, "Check Date", rowNum, checkDate);
	
				
				//System.out.println("Another OK4 Clicked");
				//throw new SkipException("Skipping this exception");
				//Assert.fail("Balance and Adjustment do not match");
				
			
			
		
		
	}
	}
@Test(priority=3, dependsOnMethods="ASRCase") 
public void ecwLogin(){
	rowNum = 1;
	driver.get("https://azuarq3ezwcrczrn8xapp.ecwcloud.com/mobiledoc/jsp/webemr/login/newLogin.jsp#/mobiledoc/jsp/webemr/webpm/claimLookup.jsp");
	logger.info("Open url: https://azuarq3ezwcrczrn8xapp.ecwcloud.com/mobiledoc/jsp/webemr/login/newLogin.jsp#/mobiledoc/jsp/webemr/webpm/claimLookup.jsp");
	asrObj.waitFunc(asrObj.usernameFieldECW);
	
	asrObj.usernameFieldECW.clear();
	asrObj.usernameFieldECW.sendKeys("jimmartin");
	logger.info("Enter username: jimmartin");

	asrObj.nextBtnECW.click();
	logger.info("Click next button");

	sel.pauseClick(asrObj.loginBtnECW,10);
	
	asrObj.passwordFieldECW.clear();
	asrObj.passwordFieldECW.sendKeys("!Ndian@193");
	logger.info("Enter password");

	asrObj.loginBtnECW.click();
	logger.info("Clicked on login");
	
	sel.pauseClick(asrObj.patientLookupBtnECW,150);
	


	asrObj.expandMenubtnECW.click();
	logger.info("Click expand menu button");

	asrObj.billingTabECW.click();
	logger.info("Click Billing Tab");

	asrObj.claimsMenuECW.click();
	logger.info("Click Claims");
	
}

@Test(dataProvider= "getData",priority=4, dependsOnMethods={"ecwLogin","ASRCase"}) 
public void ASRtoECWCase(Hashtable<String,String> data) throws InterruptedException, ParseException, IOException {
	rowNum++;
	status = data.get("Final Status");
	ecwStatus = data.get("ECW Status");

	if(status.equals("Pass")&& (ecwStatus.isBlank() || ecwStatus.isEmpty()) ) {
		
		sel.pauseClick(asrObj.claimLookupInputECW, 30);
		claimNo = data.get("CLAIMS#").replace(".0", "");
		asrObj.claimLookupInputECW.clear();
		asrObj.claimLookupInputECW.sendKeys(claimNo);
		logger.info("Claim no entered as :"+ claimNo);
		asrObj.claimLookupBtnECW.click();
		logger.info("Clicked on look up button");
		
		sel.pauseClick(asrObj.saveClaimBtnECW, 10);
		sel.pauseClick(asrObj.followUpArrowECW, 10);
		
		try {
			if(!asrObj.claimNotesECW.isDisplayed()) {
				asrObj.followUpArrowECW.click();
				logger.info("Clicked on follow up arrows");
			}
			
		}catch(Exception e) {
			
		}
		
	
		sel.pauseClick(asrObj.claimNotesECW, 15);
		
		asrObj.claimNotesECW.sendKeys("Claim is processed. EOB is downloaded.");
		logger.info("Claim is processed. EOB is downloaded. Entered");
		
		asrObj.saveClaimBtnECW.click();
		logger.info("Save button clicked");
		excel.setCellData(sheetName, "ECW Status", rowNum, "Pass");
}
}

@Test(dataProvider= "getData", priority=5) 
public void Test2(Hashtable<String,String> data) {
	System.out.println("Hi");
}


@AfterMethod()
public void afterMethod(ITestResult result) throws IOException {

	if(!result.isSuccess()) {
		// Test Failed
		String error = result.getThrowable().getLocalizedMessage();
		logger.info(error);
		//result.getThrowable().printStackTrace();
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File ss = ts.getScreenshotAs(OutputType.FILE);
			String ssPath = "./Screenshots/" + result.getName() + " - " + rowNum + ".png";
			FileUtils.copyFile(ss, new File(ssPath));
		} catch (Exception e) {
			System.out.println("Error taking screenshot");
		}

	}
	else {
		logger.info("Test completed successfully");
	}}
@DataProvider
public static Object[][] getData(){
	
	
	if(excel == null){
		
		
		excel = new ExcelReader(System.getProperty("user.dir")+"\\"+excelFileName);
		
		
	}
	
	
	int rows = excel.getRowCount(sheetName);
	int cols = excel.getColumnCount(sheetName);
	
	Object[][] data = new Object[rows-1][1];
	
	Hashtable<String,String> table = null;
	
	for(int rowNum=2; rowNum<=rows; rowNum++){
		
		table = new Hashtable<String,String>();
		
		for(int colNum=0; colNum<cols; colNum++){
			
	//	data[rowNum-2][colNum]=	excel.getCellData(sheetName, colNum, rowNum);
	
		table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));	
		data[rowNum-2][0]=table;	
			
		}
}
	
	return data;
	
}}