package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;

public class contractorPage extends Page{

	//Navigating Contractor Page
	public By home=By.linkText("Home");
	public String homeTitle="Applicant";
	
	public By organizations=By.linkText("Organizations");
	public String organizationsTitle="Organizations: Home ~ Applicant";
	
	public By contacts=By.linkText("Contacts");
	public String contactsTitle="Contacts: Home ~ Applicant";
	
	public By reviewerProfiles=By.linkText("Reviewer Profiles");
	public String reviewerProfilesTitle="Reviewer Profiles: Home ~ Applicant";
	
	public By applicationReviewTeams=By.linkText("Application Review Teams");
	public String applicationReviewTeamsTitle="Application Review Teams: Home ~ Applicant";
	
	public By newContact=By.xpath("//*[@title=\"New Contact\"]");
	public By newBut=By.xpath("//*[@title=\"New\"]");
	public String newTitle="Contact Edit: New Contact ~ Applicant";
	
	public By go=By.xpath("//*[@title=\"Go!\"]");
	public String contactsListTitle="Contacts ~ Applicant";
	//
	//filling out reviewer Profile
	public By firstNameField=By.id("name_firstcon2");
	public By lastNameField=By.id("name_lastcon2");
	public By emailField=By.id("con15");
	public By orgNameField=By.id("con4");
	//
	//Misc Buttons
	public By save=By.xpath("//*[@title=\"Save\"]");
	public By enableParnterUse=By.partialLinkText("Enable Partner");
	public By profile=By.id("Profile");
	public By portal=By.id("workWithPortalLabel");
	public By userLog=By.partialLinkText("Log in to Comm");
	
	public contractorPage(WebDriver driverBeingUsed, String contractorURL){
		Page.driver=driverBeingUsed;
		//ensure fresh login
		this.expectedTitle="Applicant";
		this.login(contractorURL);
	}
	
	public void createReviewer(Contact newContact){
		this.buttonClick(this.contacts);
		this.buttonClick(this.newBut);
		this.enterField(this.firstNameField,newContact.getFirstName());
		this.enterField(this.lastNameField,newContact.getLastName());
		this.enterField(this.emailField,newContact.getEmail());
		this.enterField(this.orgNameField,"F2 Solutions LLC");
		this.buttonClick(this.save);
		this.buttonClick(this.portal);
		this.buttonClick(this.enableParnterUse);
		this.selectList(this.profile, "Reviewer");		
		this.buttonClick(this.save);
	}

	@Override
	//loginInfo is String URL of contractor
	public void login(Object loginInfo) {
		if(loginInfo instanceof String) {
			Page.driver.navigate().to((String) loginInfo);
			this.buttonClick(this.portal);
			this.buttonClick(this.userLog);
		}
		
	}

}
