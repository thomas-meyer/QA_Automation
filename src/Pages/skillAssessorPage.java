package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import AutomationFramework.Contact;

public class skillAssessorPage extends Page {

	public salesforcePage page;
	public By contacts=By.linkText("Contacts");
	public By go=By.xpath("//*[@title=\"Go!\"]");
	public By revName=By.xpath("//*[@title=\"Reviewer Name\"]");
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve / Reject");
	public By approve=By.xpath("//*[@title=\"Approve\"]");
	
	
	public skillAssessorPage(WebDriver driverBeingUsed){
		Page.driver=driverBeingUsed;
		page=new salesforcePage(Page.driver);
	}
	
	public void approveApp(Contact reviewer) {
		this.buttonClick(this.contacts);
		this.buttonClick(this.go);
		this.buttonClick(this.revName);
		page.findContact(reviewer.getLastName()+", "+reviewer.getFirstName(), "Next");
		page.closeBar();
		this.buttonClick(this.app);
		if(this.buttonClick(this.approveReject)) {
			this.buttonClick(this.approve);
		}else{
			//pre-approved?
		}	
	}
}
