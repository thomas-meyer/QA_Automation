package Tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import AutomationFramework.SeleniumRun;
import Pages.contractorPage;

public class ContractorTest {
  
	public WebDriver driver;
	
	@BeforeTest
	public void setUp() {
		String chromeDriveLoc="chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",chromeDriveLoc);
		this.driver = new ChromeDriver();
		SeleniumRun.Login(driver);
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S");
		SeleniumRun.pause();
		driver.findElement(By.id("workWithPortalLabel")).click();
		SeleniumRun.pause();
		driver.findElement(By.partialLinkText("Log in to Comm")).click();
		SeleniumRun.pause();
	}
	
	@Test
	public void workingHomTabs() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.homeTabClick();
		Assert.assertEquals(testThis.getTitle(), "Applicant");
	}
	
	@Test
	public void workingOrgTab() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.orgTabClick();
		Assert.assertEquals(testThis.getTitle(), "Organizations: Home ~ Applicant");
	}
		
	@Test
	public void workingContactsTab() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.contactsTabClick();
		Assert.assertEquals(testThis.getTitle(), "Contacts: Home ~ Applicant");
	}
	
	@Test
	public void workingRevProTab() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.revProTabClick();
		Assert.assertEquals(testThis.getTitle(), "Reviewer Profiles: Home ~ Applicant");
	}
	
	@Test
	public void listOfRevList() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.revProTabClick();
		testThis.goButtonClick();
		Assert.assertEquals(testThis.getTitle(), "Reviewer Profiles ~ Applicant");
	}
	
	@Test
	public void newRevButton() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.revProTabClick();
		testThis.goButtonClick();
		testThis.createNewRevClick();
		Assert.assertEquals(testThis.getTitle(), "New Reviewer Profile: Select Reviewer Profile Record Type ~ Applicant");
		testThis.revProTabClick();
		testThis.createNewRevClick();
		Assert.assertEquals(testThis.getTitle(), "New Reviewer Profile: Select Reviewer Profile Record Type ~ Applicant");
	}
	
	@Test
	public void workingAppRevTeamTab() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.appRevTeamTabClick();
		Assert.assertEquals(testThis.getTitle(), "Application Review Teams: Home ~ Applicant");
	}
}
