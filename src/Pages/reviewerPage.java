package Pages;

import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;

public class reviewerPage extends Page{

	
	public By home=By.linkText("Home");
	public By appLauncher=By.linkText("App Launcher");
	public String homeTitle="Applicant";
	
	public By reviewerProfiles=By.linkText("Reviewer Profiles");
	public String reviewerProfilesTitle="Reviewer Profiles: Home ~ Applicant";
	
	public By scorecards=By.linkText("Scorecards");
	public String scorecardsTitle="Scorecards: Home ~ Applicant";
	
	public By portal=By.id("workWithPortalLabel");
	public By userLog=By.partialLinkText("Log in to Comm");
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
	public By addRevCOI=By.name("new_applicant_list_identify_conflicts");
	public By addLedCOI=By.name("pending_application");
	public By correctCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id42");
	public By readCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id41");
	public By exitCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id38");
	public By saveCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id37");
	public By newBut=By.id("createNewButton");
	public By sideBar=By.id("pinIndicator");
	
	public reviewerPage(WebDriver driverBeingUsed, Contact reviewer){
		Page.driver=driverBeingUsed;
		//refresh
		this.login(reviewer);
	}
	
	@Override
	public void login(Object loginInfo) {
		if(loginInfo instanceof Contact) {
			SystemCommands.pause(2);
			this.buttonClick(By.xpath("//*[@title=\"Contacts Tab\"]"));
			SystemCommands.pause();
			this.buttonClick(By.xpath("//*[@title=\"Go!\"]"));
			SystemCommands.pause();
			String lookUp=((Contact) loginInfo).getLastName()+", "+((Contact) loginInfo).getFirstName();
			String letter=lookUp.charAt(0)+"";
			this.buttonClick(By.linkText(letter));
			SystemCommands.pause();
			boolean foundName=false;int infCount=0;
			//Potential Infinite Loop
			PrintStream original = System.out;
			System.setOut(new NullPrintStream());
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
				infCount++;
			}while(!foundName & infCount<100);
			System.setOut(original);
			if(infCount!=100) {
				this.buttonClick(this.portal);
				this.buttonClick(this.userLog);
			}else {
				System.out.println("LOOPING ERROR: Couldn't find \""+lookUp+"\"");
			}
		}
	}
	
	public void createRevProfile(Contact reviewer, int pauseTime) {
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.reviewerProfiles);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.newRevPro);
		SystemCommands.pause(pauseTime);
		this.selectList(this.recordType, 2);
		this.buttonClick(this.continueBut);
		SystemCommands.pause(pauseTime);
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
		SystemCommands.pause(pauseTime);
		Boolean processed=false;int infCount=0;
		while(!processed & infCount!=300) {
			SystemCommands.pause(1);
			this.buttonClick(this.save);
			if(!this.getTitle().equals("Reviewer Profile Edit: New Reviewer Profile ~ Applicant")) {
				processed=true;
			}
			infCount++;
		}
		if(infCount!=300) {
			SystemCommands.pause(pauseTime);
			this.buttonClick(this.submit);
			SystemCommands.pause(pauseTime);
			this.accept();
			SystemCommands.pause(pauseTime);
		}else {
			System.out.println("LOOPING ERROR: System couldn't find Reviewer's name.");
		}
	}
	
	public void reviewerAddCOI(Contact reviewer,int pauseTime) {
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.reviewerProfiles);
		SystemCommands.pause(pauseTime);
		this.closeBar();
		this.buttonClick(this.app);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.addRevCOI);
		if(reviewer.getCOIcond()) {
			int[] COIs= {1};
			addCOI(reviewer, pauseTime,COIs);
		}else {
			noCOI(reviewer, pauseTime);
		}
	}
	
	public void createLeadProfile(Contact teamLead, int pauseTime) {
		this.buttonClick(this.reviewerProfiles);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.newRevPro);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.continueBut);
		SystemCommands.pause(pauseTime);
		this.enterField(this.name,teamLead.getLastName()+" "+teamLead.getFirstName());
		this.selectList(this.formReviewProg, 2);
		SystemCommands.pause();
		this.buttonClick(this.add);
		this.selectList(this.fiscalYear, 1);
		this.selectList(this.terms, 1);
		SystemCommands.pause(pauseTime);
		Boolean processed=false;int infCount=0;
		while(!processed & infCount!=300) {
			SystemCommands.pause(1);
			this.buttonClick(this.save);
			if(!this.getTitle().equals("Reviewer Profile Edit: New Reviewer Profile ~ Applicant")) {
				processed=true;
			}
			infCount++;
		}
		if(infCount==300) {
			System.out.println("LOOPING ERROR: System couldn't find Reviewer's name.");
		}else {
			SystemCommands.pause(pauseTime);
			if(teamLead.getCOIcond()) {
				this.buttonClick(this.addLedCOI);
				addCOI(teamLead, pauseTime,teamLead.getCOIs());
			}else {
				noCOI(teamLead, pauseTime);
			}
			SystemCommands.pause(pauseTime);
		}
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
	
	public void noCOI(Contact reviewer,int pauseTime) {
		SystemCommands.pause(pauseTime);
		this.selectList(this.correctCOI, 2);
		this.selectList(this.readCOI, 1);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.saveCOI);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.exitCOI);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.submit);
		SystemCommands.pause(pauseTime);
		try {
			this.accept();
		}catch(NoAlertPresentException e) {
			System.out.println("After Submission, No Accept Pop-Up found.");
		}
	}
	
	public void addCOI(Contact reviewer,int pauseTime, int[] COInums) {
		SystemCommands.pause(pauseTime);
		this.selectList(this.correctCOI, 1);
		this.selectList(this.readCOI, 1);
		//Fills out COI here
		for(int i=0;i<COInums.length;i++) {
			String[] COI=getCOIVal(COInums[0]);
			this.buttonClick(By.name(COI[0]));
			this.enterField(By.name(COI[1]), "Reason");
			this.selectList(By.name(COI[2]), 2);
		}	
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.saveCOI);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.exitCOI);
		SystemCommands.pause(pauseTime);
		this.buttonClick(this.submit);
		SystemCommands.pause(pauseTime);
		try {
			this.accept();
		}catch(NoAlertPresentException e) {
			System.out.println("After Submission, No Accept Pop-Up found.");
		}
	}

	
	
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
