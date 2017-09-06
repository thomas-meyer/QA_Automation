package Pages;

import java.io.IOException;

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
	
	public contractorPage(WebDriver driverBeingUsed, String sandBoxURL, String contractorURL){
		Page.driver=driverBeingUsed;
		//ensure fresh login
		String[] loginInfo= {sandBoxURL,contractorURL};
		login(loginInfo);
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
	protected void login(Object[] loginInfo) {
		if(loginInfo instanceof String[]) {
			String sandBoxURL=(String) loginInfo[0];
			String contractorURL=(String) loginInfo[1];
			//Salesforce Admin Login
			if(!webPage(sandBoxURL,"Login "+'|'+" Salesforce")) {
					System.out.println("LOGIN ERROR: remember to logout, before logging back in");
				}else {
				try {
					String[] creditials=SystemCommands.getLoginCred();
					type(By.id("username"), creditials[0]);
					type(By.id("password"), creditials[1]);
				}catch(IOException f) {
					System.out.println("MISSING FILE: couldn't find \"loginInfo.txt\"");
				}
				click(By.id("Login"));
			}
			//Contractor Login
			webPage(contractorURL,"Contact: Andrew Manning ~ Salesforce - Enterprise Edition");
			click(portal);
			click(userLog);
		}else {
			System.out.println("ERROR: Program is corrupted");
		}
	}

	@Override
	public void logout() {
		click(By.id("userNavLabel"));
		click(By.xpath("//*[@title=\"Logout\"]"));
	}


}
