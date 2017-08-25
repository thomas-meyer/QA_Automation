package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.SystemCommands;

public class reviewerPage extends Page{

	
	public By home=By.linkText("Home");
	public By appLauncher=By.linkText("App Launcher");
	public String homeTitle="Applicant";
	
	public By reviewerProfiles=By.linkText("Reviewer Profiles");
	public String reviewerProfilesTitle="Reviewer Profiles: Home ~ Applicant";
	
	public By scorecards=By.linkText("Scorecards");
	public String scorecardsTitle="Scorecards: Home ~ Applicant";
	
	public By newRevPro=By.xpath("//*[@title=\"Create New Reviewer Profile\"]");
	public By continueBut=By.xpath("//*[@title=\"Continue\"]");
	public By save=By.xpath("//*[@title=\"Save\"]");
	public By submit=By.xpath("//*[@title=\"Submit for Approval\"]");
	public By recordType=By.id("p3");
	public By formReviewProg=By.xpath("//*[@title=\"Review Program - Available\"]");
	public By add=By.xpath("//*[@title=\"Add\"]");
	public By fiscalYear=By.name("00Nt0000000SWhh");
	public By prevExp=By.name("00N35000000aO61");
	public By name=By.id("CF00Nt0000000SWKG");
	public By terms=By.name("00Nt0000000SgVo");
	public By app=By.partialLinkText("REV-");//This is sloppy
	public By addCOI=By.xpath("//*[@title=\"New Applicant List – Identify Conflicts\"]");
	public By correctCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id42");
	public By readCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id41");
	public By exitCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id38");
	public By saveCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id37");
	
	public reviewerPage(WebDriver driverBeingUsed){
		Page.driver=driverBeingUsed;
		this.expectedTitle="Applicant";
	}
	
	public void reviewerProfile(Contact reviewer) {
		this.buttonClick(this.reviewerProfiles);
		this.buttonClick(this.newRevPro);
		this.selectList(this.recordType, 2);
		this.buttonClick(this.continueBut);
		this.selectList(this.formReviewProg, 0);
		this.buttonClick(this.add);
		this.selectList(this.fiscalYear, 2);
		if(reviewer.getExp()) {
			this.selectList(this.prevExp,1);
		}else {
			this.selectList(this.prevExp,2);
		}
		this.enterField(this.name,reviewer.getLastName()+" "+reviewer.getFirstName());
		this.selectList(this.terms, 1);
		Boolean processed=false;
		while(!processed) {
			SystemCommands.pause();
			this.buttonClick(this.save);
			if(!this.checkTitle("Reviewer Profile Edit: New Reviewer Profile ~ Applicant")) {
				processed=true;
			}
		}
	}
	public void addCOI(Contact reviewer) {
		this.buttonClick(this.reviewerProfiles);
		this.closeBar();
		this.buttonClick(this.app);
		this.buttonClick(this.addCOI);
		this.selectList(this.correctCOI, 2);
		this.selectList(this.readCOI, 1);
		this.buttonClick(this.saveCOI);
		this.buttonClick(this.exitCOI);
		this.buttonClick(this.submit);
		this.accept();
		
	}
}
