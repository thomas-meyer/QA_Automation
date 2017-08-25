package Pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.SystemCommands;

public class salesforcePage extends Page{

	static String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com";
	public By usernameField=By.id("username");
	public By passwordField=By.id("password");
	public By login=By.id("Login");
	public By login2=By.xpath("//*[@title=\"Login\"]");
	public By portal=By.id("workWithPortalLabel");
	public By userLog=By.partialLinkText("Log in to Comm");
	public By logout=By.xpath("//*[@title=\"Logout\"]");
	public By user=By.id("userNavLabel");
	public By contacts=By.xpath("//*[@title=\"Contacts Tab\"]");
	public By go=By.xpath("//*[@title=\"Go!\"]");
	
	public salesforcePage(WebDriver driverBeingUsed){
		Page.driver=driverBeingUsed;
		Page.driver.get(sandBoxURL);
		this.expectedTitle="Login "+'|'+" Salesforce";
	}
	
	//Logins into our sandbox environment
	public void Login() {
		try {
			String[] creditials=SystemCommands.getLoginCred();
			this.enterField(this.usernameField, creditials[0]);
			this.enterField(this.passwordField, creditials[1]);
		}catch(IOException f) {
			//file with information not found
		}
		this.buttonClick(this.login);
	}
	
	public void loginAsContractor(String userURL) {
		Page.driver.navigate().to(userURL);
		this.buttonClick(this.portal);
		this.buttonClick(this.userLog);
	}
	
	public void loginAsReviewer(Contact reviewer) {
		Page.driver.navigate().to(sandBoxURL);
		this.buttonClick(this.contacts);
		this.buttonClick(this.go);
		SystemCommands.pause();
		findContact(reviewer.getLastName()+", "+reviewer.getFirstName(),"Next");
		this.buttonClick(this.portal);
		this.buttonClick(this.userLog);
	}
	
	public void loginAsUser(String userURL) {
		Page.driver.navigate().to(userURL);
		this.buttonClick(this.login2);
	}
	
	public void findContact(String lookUp, String iterator) {
		String letter=lookUp.charAt(0)+"";
		this.buttonClick(By.linkText(letter));
		SystemCommands.pause();
		boolean foundName=false;
		do {
			SystemCommands.pause();
			if(this.buttonClick(By.linkText(lookUp))) {
				foundName=true;
			}else {
				if(!this.buttonClick(By.partialLinkText(iterator))) {
					if(!"A".equals(letter)) {
						this.buttonClick(By.linkText("A"));
					}else {
						this.buttonClick(By.linkText("B"));
					}
					this.buttonClick(By.linkText(letter));
				}
			}
		}while(!foundName);
	}
	

	

	
	public void refreshUser() {
		Logout();
		SystemCommands.pause(1);
		driver.navigate().to(sandBoxURL);
		SystemCommands.pause(1);
		Login();
	}
}
