package Pages;

import java.io.IOException;
import java.io.PrintStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;
public class loginPage extends Page{
	public String sandboxURL;
	
	public By usernameField=By.id("username");
	public By passwordField=By.id("password");
	public By login=By.id("Login");
	public By logout=By.xpath("//*[@title=\"Logout\"]");
	public By user=By.id("userNavLabel");
	
	public loginPage(WebDriver driverBeingUsed, String URL) {
		Page.driver=driverBeingUsed;
		this.sandboxURL=URL;
		this.login(this.sandboxURL);
	}
	
	public void changeUser() {		
		PrintStream original = System.out;
		System.setOut(new NullPrintStream());	SystemCommands.pause(1);
		this.logout();
			SystemCommands.pause(1);
		this.login(this.sandboxURL);
			SystemCommands.pause(2);
		//Potential Infinite Loop
		System.setOut(original);
		if(!Page.driver.getTitle().equals("Salesforce - Enterprise Edition")) {
			System.out.println("RELOGIN FAILED: trying again");
			this.login(this.sandboxURL);
			if(!Page.driver.getTitle().equals("Salesforce - Enterprise Edition")) {
				System.out.println("RELOGIN FAILED: trying again");
				this.login(this.sandboxURL);
				if(!Page.driver.getTitle().equals("Salesforce - Enterprise Edition")) {
					System.out.println("RELOGIN FAILED: trying again");
					this.login(this.sandboxURL);
				}
			}
		}
		
	}
	
	public void logout() {
		this.buttonClick(this.user);
		this.buttonClick(this.logout);
	}
	
	@Override
	public void login(Object loginInfo) {
		if(loginInfo instanceof String) {
			Page.driver.navigate().to((String) loginInfo);
			if(!Page.driver.getTitle().equals("Login "+'|'+" Salesforce")) {
				System.out.println("UNEXPECTED WEBPAGE: link for \"Sandbox Login Page\" might be broken");
			}
			//Page.driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com");
				SystemCommands.pause(1);
			try {
				String[] creditials=SystemCommands.getLoginCred();
				this.enterField(this.usernameField, creditials[0]);
				this.enterField(this.passwordField, creditials[1]);
			}catch(IOException f) {
				//file with information not found
			}
			this.buttonClick(this.login);
		}else {
			System.out.println("ERROR: Sandbox URL is not entered as a String-This error should never be reached");
		}
	}

}
