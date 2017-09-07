package Pages;

import java.io.IOException;
import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;

public class teamLeaderPage extends Page{

	
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
	//Form clickables
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
	//Misc Buttons
	public By newBut=By.id("createNewButton");
	public By sideBar=By.id("pinIndicator");
	
	//Team Leader Buttons
	public By appAssign=By.linkText("Application Assignment");
	public By pickTeam=By.id("j_id0:j_id2:j_id28:j_id29:j_id30:team");
	public By go=By.name("j_id0:j_id2:j_id28:j_id29:j_id30:go");
	public By scoreSelect=By.id("fcf");
	public By teamLevAssignSelect=By.id("j_id0:j_id2:j_id28:j_id29:j_id30:team");
	public By saveChan=By.id("j_id0:j_id2:j_id28:j_id29:j_id32:save");;

	
	
	public teamLeaderPage(WebDriver driverBeingUsed, String sandboxURL, Contact teamLeader){
		Page.driver=driverBeingUsed;
		Object[] loginInfo= {sandboxURL,teamLeader};
		//refresh
		login(loginInfo);
	}
	
	@Override
	public void login(Object[] loginInfo) {
		if(loginInfo[0] instanceof String) {
			String sandBoxURL=(String) loginInfo[0];
			//Salesforce Admin Login
			if(!webPage(sandBoxURL,"Login "+'|'+" Salesforce")) {
					System.out.println("LOGIN ERROR: remember to logout, before logging back in");
				}else {
				try {
					String[] creditials=SystemCommands.getLoginCred();
					type(By.id("username"), creditials[0]);
					type(By.id("password"), creditials[1]);
				}catch(IOException f) {
					System.out.println("MISSING FILE: couldn't find \"loginInfo.txt\"");
				}
				click(By.id("Login"));
			}
		}
		if(loginInfo[1] instanceof Contact) {
			Contact searchFor=(Contact) loginInfo[1];
			SystemCommands.pause(2);
			click(By.xpath("//*[@title=\"Contacts Tab\"]"));
			SystemCommands.pause(2);
			click(By.xpath("//*[@title=\"Go!\"]"));
			SystemCommands.pause(2);
			String lookUp=searchFor.getLastName()+", "+searchFor.getFirstName();
			String letter=lookUp.charAt(0)+"";
			click(By.linkText(letter));
			SystemCommands.pause(2);
			boolean foundName=false;int infCount=0;
			//Potential Infinite Loop
			PrintStream original = System.out;
			System.setOut(new NullPrintStream());
			do {
				SystemCommands.pause(1);
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
					}
				}
				infCount++;
			}while(!foundName & infCount<100);
			System.setOut(original);
			SystemCommands.pause(2);
			if(infCount!=100) {
				click(portal);
				click(userLog);
			}else {
				System.out.println("LOOPING ERROR: Couldn't find \""+lookUp+"\"");
			}
		}
	}
		
	@Override
	public void logout() {
		click(By.id("userNavLabel"));
		click(By.xpath("//*[@title=\"Logout\"]"));
	}
	
	//Team Lead Actions
	public void createLeadProfile(Contact teamLead, int pauseTime) {
		click(reviewerProfiles);
		SystemCommands.pause(pauseTime);
		click(newRevPro);
		SystemCommands.pause(pauseTime);
		click(continueBut);
		SystemCommands.pause(pauseTime);
		type(name,teamLead.getLastName()+" "+teamLead.getFirstName());
		select(formReviewProg, 2);
		SystemCommands.pause();
		click(add);
		select(fiscalYear, 1);
		select(terms, 1);
		SystemCommands.pause(pauseTime);
		Boolean processed=false;int infCount=0;
		while(!processed & infCount!=300) {
			SystemCommands.pause(1);
			click(save);
			if(!getTitle().equals("Reviewer Profile Edit: New Reviewer Profile ~ Applicant")) {
				processed=true;
			}
			infCount++;
		}
		if(infCount==300) {
			System.out.println("LOOPING ERROR: System couldn't find Reviewer's name.");
		}else {
			SystemCommands.pause(pauseTime);
			if(teamLead.getCOIcond()) {
				click(addLedCOI);
				addCOI(teamLead, pauseTime,teamLead.getCOIs());
			}else {
				noCOI(teamLead, pauseTime);
			}
			SystemCommands.pause(pauseTime);
		}
	}
	
	public void grantAccess(int teamNum, int pauseTime) {
		openBar();
		SystemCommands.pause(pauseTime);
		click(appAssign);
		SystemCommands.pause(pauseTime);
		select(teamLevAssignSelect,1);
		SystemCommands.pause(pauseTime);
		click(go);
		for(int i=0;i<teamNum;i++) {
			SystemCommands.pause(pauseTime);
			for(int j=1;j<5;j++) {
				type(By.name("j_id0:j_id2:j_id28:j_id36:j_id41:"+j+":j_id43"),"10/10/2017");
				SystemCommands.pause(pauseTime);
				select(By.name("j_id0:j_id2:j_id28:j_id36:j_id41:"+j+":j_id45:0:j_id47"),"Granted");
				SystemCommands.pause(pauseTime);
				select(By.name("j_id0:j_id2:j_id28:j_id36:j_id41:"+j+":j_id45:1:j_id47"),"Granted");
				SystemCommands.pause(pauseTime);
				select(By.name("j_id0:j_id2:j_id28:j_id36:j_id41:"+j+":j_id45:2:j_id47"),"Granted");
				SystemCommands.pause(pauseTime);
			}
			click(saveChan);
			SystemCommands.pause(pauseTime);
			select(teamLevAssignSelect,i+1);
			SystemCommands.pause(pauseTime);
			click(go);
			
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
		select(correctCOI, 2);
		select(readCOI, 1);
		SystemCommands.pause(pauseTime);
		click(saveCOI);
		SystemCommands.pause(pauseTime);
		click(exitCOI);
		SystemCommands.pause(pauseTime);
		click(submit);
		SystemCommands.pause(pauseTime);
		try {
			accept();
		}catch(NoAlertPresentException e) {
			System.out.println("After Submission, No Accept Pop-Up found.");
		}
	}
	
	public By getAssignedDate(int num) {
		return By.id("j_id0:j_id2:j_id28:j_id36:j_id41:"+num+":j_id43");
	}
	
	public void assignScoreCards() {
		
	}
	
	public void reviseDueDate(int row, int column, String date) {
		type(By.id("j_id0:j_id2:j_id28:j_id36:j_id41:"+row+":j_id45:"+column+":j_id52"),date);
	}
	
	//everyone actions
	public void addCOI(Contact reviewer,int pauseTime, int[][] COInums) {
		SystemCommands.pause(pauseTime);
		select(correctCOI, 1);
		select(readCOI, 1);
		//Fills out COI here
		for(int i=0;i<COInums[0].length;i++) {
			String[] COI=getCOIVal(COInums[0][i]);
			click(By.name(COI[0]));
			type(By.name(COI[1]), "Reason");
			//2: Partial Ban, 3: Total Ban, 4: No Conflict
			select(By.name(COI[2]), 2);
		}	
		SystemCommands.pause(pauseTime);
		click(saveCOI);
		SystemCommands.pause(pauseTime);
		click(exitCOI);
		SystemCommands.pause(pauseTime);
		click(submit);
		SystemCommands.pause(pauseTime);
		try {
			accept();
		}catch(NoAlertPresentException e) {
			System.out.println("After Submission, No Accept Pop-Up found.");
		}
	}
	
	public void closeBar() {
		try {
			click(newBut);
			click(sideBar);
			SystemCommands.pause();
		}catch(ElementNotVisibleException e) {
			//If it isn't visible, you are good to go!
		}
	}
	
	public void openBar() {
		try {
			click(newBut);
			//If it's already visible, you are good to go!
		}catch(ElementNotVisibleException e) {
			click(sideBar);
			SystemCommands.pause();
		}
	}

	
	
}
