package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.SystemCommands;

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
	
	//From Contacts Tab
	public By newContact=By.xpath("//*[@title=\"New Contact\"]");
	public By newBut=By.xpath("//*[@title=\"New\"]");
	public String newTitle="Contact Edit: New Contact ~ Applicant";
	
	//From New Contact Page
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
		login(contractorURL);
	}
	
	public void createReviewer(Contact newReviewer, int pauseTime){
		createContact(newReviewer, pauseTime);
		select(profile, "Reviewer");
		SystemCommands.pause(pauseTime);
		click(save);
		SystemCommands.pause(pauseTime);
	}
	
	public void createTeamLeader(Contact teamLead, int pauseTime){
		createContact(teamLead, pauseTime);
		select(profile, 2);
		SystemCommands.pause(pauseTime);
		click(save);
		SystemCommands.pause(pauseTime);
	}

	private void createContact(Contact newContact, int pauseTime) {
		click(contacts);
		SystemCommands.pause(pauseTime);
		click(newBut);
		SystemCommands.pause(pauseTime);
		type(firstNameField,newContact.getFirstName());
		type(lastNameField,newContact.getLastName());
		type(emailField,newContact.getEmail());
		type(orgNameField,"F2 Solutions LLC");
		SystemCommands.pause(pauseTime);
		click(save);
		SystemCommands.pause(pauseTime);
		click(portal);
		SystemCommands.pause(pauseTime);
		click(enableParnterUse);
		SystemCommands.pause(pauseTime);
	}
	
	@Override
	//loginInfo is String URL of contractor
	public void login(Object loginInfo) {
		if(loginInfo instanceof String) {
			Page.driver.navigate().to((String) loginInfo);
			if(!Page.driver.getTitle().equals("Contact: Andrew Manning ~ Salesforce - Enterprise Edition")) {
				SystemCommands.pause(2);
				Page.driver.navigate().to((String) loginInfo);
				if(!Page.driver.getTitle().equals("Contact: Andrew Manning ~ Salesforce - Enterprise Edition")) {
					System.out.println("UNEXPECTED WEBPAGE: link for \"Contractor Login Page\" might be broken");
				}
			}
			click(portal);
			click(userLog);
		}else {
			System.out.println("ERROR: contractor URL is not entered as a String-This error should never be reached");
		}
	}


}
