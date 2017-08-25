package Tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import AutomationFramework.Contact;
import AutomationFramework.SystemCommands;
import Pages.contractorPage;
import Pages.salesforcePage;
import java.util.Random;

public class ContractorTest {
  
	public WebDriver driver;
	public Contact testGuy;
	
	
	@BeforeTest
	public void setUp() {
		this.driver=SystemCommands.creatDriver();
		salesforcePage page=new salesforcePage(driver);
		page.Login();
		page.loginAsContractor("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S");
		Random rand=new Random();
		String testGuyFirstName="Test";
		String testGuyLastName="Guy"+rand.nextInt(10000000);
		testGuy=new Contact(testGuyFirstName,testGuyLastName);
	}
	
	
	@Test (priority=0)
	public void workingHomTabs() {
		contractorPage testThis=new contractorPage(this.driver);
		testThis.buttonClick(testThis.home,"Applicant");
		testThis.verifyTitle();
	}
	
	@Test (priority=0)
	public void workingOrgTab() {
		contractorPage page= new contractorPage(this.driver);
		page.buttonClick(page.organizations,page.organizationsTitle);
		page.verifyTitle();
	}
		
	@Test (priority=0)
	public void workingContactsTab() {
		contractorPage page=new contractorPage(this.driver);
		page.buttonClick(page.contacts,page.contactsTitle);
		page.verifyTitle();
	}
	
	@Test (priority=0)
	public void workingRevProTab() {
		contractorPage page=new contractorPage(this.driver);
		page.buttonClick(page.reviewerProfiles,page.reviewerProfilesTitle);
		page.verifyTitle();
	}
	
	@Test (priority=0)
	public void workingAppRevTeamTab() {
		contractorPage page=new contractorPage(this.driver);
		page.buttonClick(page.applicationReviewTeams,page.applicationReviewTeamsTitle);
		page.verifyTitle();
	}
	
	@Test (priority=1, dependsOnMethods= {"workingContactsTab"})
	public void newContactButton() {
		contractorPage page=new contractorPage(this.driver);
		page.buttonClick(page.contacts);
		page.buttonClick(page.newBut,page.newTitle);
		page.verifyTitle();
		page.buttonClick(page.contacts);
		page.buttonClick(page.go,page.contactsListTitle);
		page.buttonClick(page.newContact,page.newTitle);
		page.verifyTitle();
	}
	

	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void firstNameField() {
		contractorPage page=new contractorPage(this.driver);
		Assert.assertTrue(page.enterField(page.firstNameField, this.testGuy.getFirstName()));
	}
	
	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void lastNameField() {
		contractorPage page=new contractorPage(this.driver);
		Assert.assertTrue(page.enterField(page.lastNameField, this.testGuy.getLastName()));
	}
	
	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void emailField() {
		contractorPage page=new contractorPage(this.driver);
		Assert.assertTrue(page.enterField(page.emailField, this.testGuy.getEmail()));
	}
	
	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void orgField() {
		contractorPage page=new contractorPage(this.driver);
		Assert.assertTrue(page.enterField(page.orgNameField, "F2 Solutions LLC"));
	}
	
	@Test (priority=3,dependsOnMethods= {"newContactButton"})
	public void saveContact() {
		contractorPage page=new contractorPage(this.driver);
		page.buttonClick(page.save,"Contact: "+this.testGuy.getFirstName()+" "+this.testGuy.getLastName()+" ~ Applicant");
		page.verifyTitle();
	}
	
	@Test (priority=4,dependsOnMethods= {"newContactButton","saveContact"})
	public void enablePartnerUse() {
		contractorPage page=new contractorPage(this.driver);
		page.buttonClick(page.portal);
		page.buttonClick(page.enableParnterUse);
		page.verifyTitle();
		page.selectList(page.profile, "Reviewer");
		page.buttonClick(page.save,"Contact: "+this.testGuy.getFirstName()+" "+this.testGuy.getLastName()+" ~ Applicant");
		page.verifyTitle();
	}
	
	@Test (priority=6)
	public void allTogether() {
		contractorPage page=new contractorPage(this.driver);
		page.buttonClick(page.home,"Applicant");
		page.verifyTitle();
		Contact testAll=new Contact(new Random().nextInt(1000000));
		page.contractorPageExe(testAll);
		Assert.assertEquals(page.getTitle(), "Contact: "+testAll.getFirstName()+" "+testAll.getLastName()+" ~ Applicant");
		
	}
	
}
