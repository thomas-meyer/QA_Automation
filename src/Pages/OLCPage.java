package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.SystemCommands;

public class OLCPage extends Page{
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve / Reject");
	public By approveApp=By.xpath("//*[@title=\"Approve\"]");
	public By edit=By.xpath("//*[@title=\"Edit\"]");
	public By clearedNoRestric=By.id("00Nt0000000SWhY");
	public By resume=By.id("00Nt0000000SgVk");
	public By save=By.xpath("//*[@title=\"Save\"]");
	
	public OLCPage(WebDriver driverBeingUsed, String URL) {
		Page.driver=driverBeingUsed;
		this.login(URL);
	}


	public void approveApp(Contact reviewer,String contactListURL, int pauseTime) {
		driver.navigate().to(contactListURL);
		SystemCommands.pause(pauseTime);
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		String letter=lookUp.charAt(0)+"";
		this.buttonClick(By.linkText(letter));
		SystemCommands.pause();
		boolean foundName=false;
		//Potential Infinite Loop
		do {
			SystemCommands.pause();
			if(this.buttonClick(By.linkText(lookUp))) {
				foundName=true;
			}else {
				if(!this.buttonClick(By.partialLinkText("more"))) {
					if(!"A".equals(letter)) {
						this.buttonClick(By.linkText("A"));
					}else {
						this.buttonClick(By.linkText("B"));
					}
					this.buttonClick(By.linkText(letter));
				}
			}
		}while(!foundName);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.app);
		SystemCommands.pause(pauseTime);
		if(this.buttonClick(this.approveReject)) {
			SystemCommands.pause(pauseTime);
			this.buttonClick(this.approveApp);
			SystemCommands.pause(pauseTime);
		}
		this.buttonClick(this.edit);
		SystemCommands.pause(pauseTime);
		this.selectList(this.clearedNoRestric, 1);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.resume);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.save);
		SystemCommands.pause(pauseTime);
	}



	@Override
	public void login(Object loginInfo) {
		if(loginInfo instanceof String) {
			Page.driver.navigate().to((String) loginInfo);
			this.buttonClick(By.xpath("//*[@title=\"Login\"]"));
		}	
	}
}