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
	public By userInt=By.id("userNavLabel");
	
	public loginPage(WebDriver driverBeingUsed, String URL) {
		Page.driver=driverBeingUsed;
		sandboxURL=URL;
		login(sandboxURL);
	}
	
	public void changeUser() {		
		PrintStream original = System.out;
		System.setOut(new NullPrintStream());	
		SystemCommands.pause(1);
		logout();
		SystemCommands.pause(1);
		login(sandboxURL);
		SystemCommands.pause(2);
		//Potential Infinite Loop
		System.setOut(original);
		for(int i=0;i<3;i++) {
			if(!Page.driver.getTitle().equals("Salesforce - Enterprise Edition")) {
				System.out.println("RELOGIN FAILED: trying again");
				login(sandboxURL);
				SystemCommands.pause(1);
			}
		}
	}
	
	public void logout() {
		click(userInt);
		click(logout);
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
				type(usernameField, creditials[0]);
				type(passwordField, creditials[1]);
			}catch(IOException f) {
				//file with information not found
			}
			click(login);
		}else {
			System.out.println("ERROR: Sandbox URL is not entered as a String-This error should never be reached");
		}
	}

}
