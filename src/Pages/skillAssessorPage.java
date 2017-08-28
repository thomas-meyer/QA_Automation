package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import AutomationFramework.Contact;
import AutomationFramework.SystemCommands;

public class skillAssessorPage extends Page {

	public By contacts=By.linkText("Contacts");
	public By go=By.xpath("//*[@title=\"Go!\"]");
	public By revName=By.xpath("//*[@title=\"Reviewer Name\"]");
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve / Reject");
	public By approve=By.xpath("//*[@title=\"Approve\"]");
	public By newBut=By.id("createNewButton");
	public By sideBar=By.id("pinIndicator");
	
	
	public skillAssessorPage(WebDriver driverBeingUsed, String URL){
		Page.driver=driverBeingUsed;
		this.login(URL);
	}
	
	public void approveApp(Contact reviewer) {
		SystemCommands.pause(5);
		this.buttonClick(this.contacts);
		this.buttonClick(this.go);
		this.buttonClick(this.revName);
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		String letter=lookUp.charAt(0)+"";
		this.buttonClick(By.linkText(letter));
		SystemCommands.pause();
		boolean foundName=false;
		do {
			SystemCommands.pause();
			if(this.buttonClick(By.linkText(lookUp))) {
				foundName=true;
			}else {
				if(!this.buttonClick(By.partialLinkText("Next"))) {
					if(!"A".equals(letter)) {
						this.buttonClick(By.linkText("A"));
					}else {
						this.buttonClick(By.linkText("B"));
					}
					this.buttonClick(By.linkText(letter));
				}
			}
		}while(!foundName);
		this.closeBar();
		this.buttonClick(this.app);
		if(this.buttonClick(this.approveReject)) {
			this.buttonClick(this.approve);
		}else{
			//pre-approved?
		}
		
	}

	@Override
	public void login(Object loginInfo) {
		if(loginInfo instanceof String) {
			Page.driver.navigate().to((String) loginInfo);
			this.buttonClick(By.xpath("//*[@title=\"Login\"]"));
		}
		
	}
	//Ensures that the "convenient" side-bar
	//is minimized.
	public void closeBar() {
		try {
			this.buttonClick(newBut);
			this.buttonClick(this.sideBar);
			SystemCommands.pause();
		}catch(ElementNotVisibleException e) {
			//If it isn't visible, we are good to go!
		}
	}
	
}
