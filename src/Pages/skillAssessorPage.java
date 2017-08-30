package Pages;

import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import AutomationFramework.Contact;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;

public class skillAssessorPage extends Page {

	public By contacts=By.linkText("Contacts");
	public By go=By.xpath("//*[@title=\"Go!\"]");
	public By revName=By.xpath("//*[@title=\"Name\"]");
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve / Reject");
	public By approve=By.xpath("//*[@title=\"Approve\"]");
	public By creNewBut=By.id("createNewButton");
	public By sideBar=By.id("pinIndicator");
	public By login=By.xpath("//*[@title=\"Login\"]");
	
	
	public skillAssessorPage(WebDriver driverBeingUsed, String URL){
		Page.driver=driverBeingUsed;
		login(URL);
	}
	
	public void approveApp(Contact reviewer, int pauseTime) {
		SystemCommands.pause(pauseTime+2);
		click(contacts);
		SystemCommands.pause(pauseTime);
		click(go);
		SystemCommands.pause(pauseTime);
		click(revName);
		SystemCommands.pause(pauseTime);
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		String letter=lookUp.charAt(0)+"";
		SystemCommands.pause(pauseTime);
		click(By.linkText(letter));
		SystemCommands.pause();
		boolean foundName=false;int infCount=0;
		//Potential Infinite Loop
		PrintStream original = System.out;
		System.setOut(new NullPrintStream());
		do {
				SystemCommands.pause();
			if(click(By.linkText(lookUp))) {
				foundName=true;
			}else {
				if(!click(By.partialLinkText("Next"))) {
					if(!"A".equals(letter)) {
						click(By.linkText("A"));
					}else {
						click(By.linkText("B"));
					}
					click(By.linkText(letter));
						SystemCommands.pause(pauseTime);
				}
			}
			infCount++;
		}while(!foundName & infCount<100);
		System.setOut(original);
		if(infCount!=100) {
			SystemCommands.pause(pauseTime);
			closeBar();
			SystemCommands.pause(pauseTime);
			click(app);
			SystemCommands.pause(pauseTime);
			if(click(approveReject)) {
				SystemCommands.pause(pauseTime);
				click(approve);
				SystemCommands.pause(pauseTime);
			}else{
				SystemCommands.pause(2);
			}
		}else {
			System.out.println("LOOPING ERROR: Couldn't find \""+lookUp+"\"");
		}
	}

	@Override
	public void login(Object loginInfo) {
		if(loginInfo instanceof String) {
			Page.driver.navigate().to((String) loginInfo);
			if(!Page.driver.getTitle().equals("User: Ayana Sufian ~ Salesforce - Enterprise Edition")) {
				SystemCommands.pause(2);
				Page.driver.navigate().to((String) loginInfo);
				if(!Page.driver.getTitle().equals("User: Ayana Sufian ~ Salesforce - Enterprise Edition")) {
					System.out.println("UNEXPECTED WEBPAGE: link for \"Skills Assessor Login Page\" might be broken");
				}
			}
			click(login);
		}
		
	}
	//Ensures that the "convenient" side-bar
	//is minimized.
	public void closeBar() {
		try {
			click(creNewBut);
			click(sideBar);
			SystemCommands.pause();
		}catch(ElementNotVisibleException e) {
			//If it isn't visible, we are good to go!
		}
	}
	
}
