package Pages;

import org.openqa.selenium.By;

import AutomationFramework.Contact;

public class OLCPage extends Page{
	public By revList=By.partialLinkText("(50+)");//sloppy
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve/Reject");
	public By approveApp=By.xpath("//*[@title=\"Approve\"]");
	public By edit=By.xpath("//*[@title=\"Edit\"]");
	public By clearedNoRestric=By.id("00Nt0000000SWhY");
	public By resume=By.id("00Nt0000000SgVk");
	public By save=By.xpath("//*[@title=\"Save\"]");
	


	public void OLCapprove(Contact reviewer) {
		salesforcePage spage=new salesforcePage(driver);
		this.buttonClick(this.revList);
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		spage.findContact(lookUp,"more");
		this.buttonClick(this.app);
		if(this.buttonClick(this.approveReject)) {
			this.buttonClick(this.approveApp);
		}
		this.buttonClick(this.edit);
		this.selectList(this.clearedNoRestric, 0);
		this.buttonClick(this.resume);
		this.buttonClick(this.save);
	}
}