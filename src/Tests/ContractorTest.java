package Tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import AutomationFramework.Contact;
import AutomationFramework.SystemCommands;
import Pages.contractorPage;
import Pages.loginPage;

import java.util.Random;

public class ContractorTest {
  
	public WebDriver driver;
	public Contact testGuy;
	public contractorPage cPage;
	
	
	@BeforeTest
	public void setUp() {
		this.driver=SystemCommands.creatDriver();
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com";
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";
		new loginPage(this.driver, sandBoxURL);
		cPage=new contractorPage(driver,contractorURL);
		Random rand=new Random();
		String testGuyFirstName="Test";
		String testGuyLastName="Guy"+rand.nextInt(10000000);
		testGuy=new Contact(testGuyFirstName,testGuyLastName);
	}
	
	
	@Test (priority=0)
	public void workingHomTabs() {
		cPage.buttonClick(cPage.home,"Applicant");
		cPage.verifyTitle();
	}
	
	@Test (priority=0)
	public void workingOrgTab() {
		cPage.buttonClick(cPage.organizations,cPage.organizationsTitle);
		cPage.verifyTitle();
	}
		
	@Test (priority=0)
	public void workingContactsTab() {
		cPage.buttonClick(cPage.contacts,cPage.contactsTitle);
		cPage.verifyTitle();
	}
	
	@Test (priority=0)
	public void workingRevProTab() {
		cPage.buttonClick(cPage.reviewerProfiles,cPage.reviewerProfilesTitle);
		cPage.verifyTitle();
	}
	
	@Test (priority=0)
	public void workingAppRevTeamTab() {
		cPage.buttonClick(cPage.applicationReviewTeams,cPage.applicationReviewTeamsTitle);
		cPage.verifyTitle();
	}
	
	@Test (priority=1, dependsOnMethods= {"workingContactsTab"})
	public void newContactButton() {
		cPage.buttonClick(cPage.contacts);
		cPage.buttonClick(cPage.newBut,cPage.newTitle);
		cPage.verifyTitle();
		cPage.buttonClick(cPage.contacts);
		cPage.buttonClick(cPage.go,cPage.contactsListTitle);
		cPage.buttonClick(cPage.newContact,cPage.newTitle);
		cPage.verifyTitle();
	}
	

	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void firstNameField() {
		Assert.assertTrue(cPage.enterField(cPage.firstNameField, this.testGuy.getFirstName()));
	}
	
	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void lastNameField() {
		Assert.assertTrue(cPage.enterField(cPage.lastNameField, this.testGuy.getLastName()));
	}
	
	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void emailField() {
		Assert.assertTrue(cPage.enterField(cPage.emailField, this.testGuy.getEmail()));
	}
	
	@Test (priority=2,dependsOnMethods= {"newContactButton"})
	public void orgField() {
		Assert.assertTrue(cPage.enterField(cPage.orgNameField, "F2 Solutions LLC"));
	}
	
	@Test (priority=3,dependsOnMethods= {"newContactButton"})
	public void saveContact() {
		cPage.buttonClick(cPage.save,"Contact: "+this.testGuy.getFirstName()+" "+this.testGuy.getLastName()+" ~ Applicant");
		cPage.verifyTitle();
	}
	
	@Test (priority=4,dependsOnMethods= {"newContactButton","saveContact"})
	public void enablePartnerUse() {
		cPage.buttonClick(cPage.portal);
		cPage.buttonClick(cPage.enableParnterUse);
		cPage.selectList(cPage.profile, "Reviewer");
		cPage.buttonClick(cPage.save,"Contact: "+this.testGuy.getFirstName()+" "+this.testGuy.getLastName()+" ~ Applicant");
		cPage.verifyTitle();
	}
	
	@Test (priority=6)
	public void allTogether() {
		cPage.buttonClick(cPage.home,"Applicant");
		cPage.verifyTitle();
		Contact testAll=new Contact(new Random().nextInt(1000000));
		cPage.createReviewer(testAll,0);
		Assert.assertEquals(cPage.getTitle(), "Contact: "+testAll.getFirstName()+" "+testAll.getLastName()+" ~ Applicant");
		
	}
	
}
