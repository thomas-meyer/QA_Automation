package Pages;

import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;

public class OLCPage extends Page{
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve / Reject");
	public By approveApp=By.xpath("//*[@title=\"Approve\"]");
	public By edit=By.xpath("//*[@title=\"Edit\"]");
	public By clearedNoRestric=By.id("00Nt0000000SWhY");
	public By resume=By.id("00Nt0000000SgVk");
	public By save=By.xpath("//*[@title=\"Save\"]");
	public By reviewed=By.id("00Nt0000000SWhL");
	public By addCon=By.name("add_conflict");
	public By exitCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id38");
	public By saveCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id37");
	
	public OLCPage(WebDriver driverBeingUsed, String URL) {
		Page.driver=driverBeingUsed;
		this.login(URL);
	}


	public void approveApp(Contact reviewer,String contactListURL, int pauseTime) {
		//Navigate to the correct page
		driver.navigate().to(contactListURL);
		SystemCommands.pause(pauseTime);
		if(!Page.driver.getTitle().equals("Contacts: F2 Solutions LLC ~ Salesforce - Enterprise Edition")) {
			SystemCommands.pause(2);
			//If the link fails the first time, try again
			driver.navigate().to(contactListURL);
			if(!Page.driver.getTitle().equals("Contacts: F2 Solutions LLC ~ Salesforce - Enterprise Edition")) {
				System.out.println("UNEXPECTED WEBPAGE: link for \"Skills Assessor Login Page\" might be broken");
			}
		}
		//contact whose app will be approved
		SystemCommands.pause(pauseTime);
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		String letter=lookUp.charAt(0)+"";
		this.buttonClick(By.linkText(letter));
		SystemCommands.pause();
		boolean foundName=false;int infCount=0;
		PrintStream original = System.out;
		System.setOut(new NullPrintStream());
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
			infCount++;
		}while(!foundName & infCount<100);
		System.setOut(original);
		if(infCount==100) {
			System.out.println("LOOPING ERROR: Couldn't find \""+lookUp+"\"");
		}
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.app);
		SystemCommands.pause(pauseTime);
		if(this.buttonClick(this.approveReject)) {
			SystemCommands.pause(pauseTime);
			this.buttonClick(this.approveApp);
				SystemCommands.pause(pauseTime);
		}
		this.buttonClick(this.addCon);
		if(reviewer.getCOIcond()) {
			int[] COIs=reviewer.getCOIs();
			for(int i=0;i<COIs.length;i++) {
				verifyCOI(COIs[i]);
			}
		}
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.saveCOI);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.exitCOI);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.edit);
		SystemCommands.pause(pauseTime);
		if(reviewer.getCOIcond()) {
			this.selectList(this.clearedNoRestric, 2);
		}
		else {
			this.selectList(this.clearedNoRestric, 1);
		}
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.resume);
		SystemCommands.pause(pauseTime);
		if (elementExists(this.reviewed)) {
			if(!driver.findElement(this.reviewed).isSelected()){
				this.buttonClick(this.reviewed);
			}
		}
		this.buttonClick(this.save);
		SystemCommands.pause(pauseTime);	
	}

	private void verifyCOI(int input) {
		String[] vals=getCOIVal(input);
		this.selectList(By.name(vals[3]), 2);
		//2: partial, 3: total, 4:no conflict
		this.selectList(By.name(vals[4]), 2);
		this.enterField(By.name(vals[5]), "Looked At");
		
	}


	public String[] getCOIVal(int rowNum) {
		String[] result={"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id45"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id51"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id53"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id64"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id67"
			,"pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:pbtApplicationUpdate:"+rowNum+":j_id70"};
		return result;
	}

	@Override
	public void login(Object loginInfo) {
		if(loginInfo instanceof String) {
			Page.driver.navigate().to((String) loginInfo);
			if(!Page.driver.getTitle().equals("User: Ashanti Kimbrough ~ Salesforce - Enterprise Edition")) {
				System.out.println("UNEXPECTED WEBPAGE: link for \"OLC Login Page\" might be broken");
			}
			this.buttonClick(By.xpath("//*[@title=\"Login\"]"));
		}	
	}
}