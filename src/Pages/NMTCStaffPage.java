package Pages;

import java.io.IOException;
import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import AutomationFramework.Contact;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;

public class NMTCStaffPage extends Page {

	public By contacts=By.linkText("Contacts");
	public By go=By.xpath("//*[@title=\"Go!\"]");
	public By revName=By.xpath("//*[@title=\"Name\"]");
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve / Reject");
	public By approve=By.xpath("//*[@title=\"Approve\"]");
	public By creNewBut=By.id("createNewButton");
	public By sideBar=By.id("pinIndicator");
	public By login=By.xpath("//*[@title=\"Login\"]");
	
	//Tea Creation Buttons
	public By home=By.linkText("Home");
	public By teamCre=By.linkText("Access to Team Creation");
	public By annualReviewTempField=By.id("j_id0:j_id2:masterForm:j_id6:j_id7:j_id9");
	public By fiscalYear=By.id("j_id0:j_id2:masterForm:j_id6:j_id13:fiscalYear");
	public By autoAssign=By.name("j_id0:j_id2:masterForm:j_id3:j_id4");
	public By save=By.id("j_id0:j_id2:masterForm:j_id3:save");
	public By appDueDate=By.linkText("Application Due Date Assignment");
	public By appAnnualReviewTempField=By.id("j_id0:j_id2:j_id3:j_id5:j_id6:j_id8");
	public By saveRun=By.id("j_id0:j_id2:j_id3:j_id4:bottom:save");
	public By numTeamsCreate=By.id("j_id0:j_id2:masterForm:j_id6:j_id16:teamNo");

	public NMTCStaffPage(WebDriver driverBeingUsed, String sandBoxURL, String staffURL){
		Page.driver=driverBeingUsed;
		String[] loginInfo= {sandBoxURL,staffURL};
		login(loginInfo);
	}
	
	@Override
	public void login(Object[] loginInfo) {
		if(loginInfo instanceof String[]) {
			String sandBoxURL=(String) loginInfo[0];
			String skillAssessorURL=(String) loginInfo[1];
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
			//Contractor Login
			webPage(skillAssessorURL,"User: Ayana Sufian ~ Salesforce - Enterprise Edition");
			click(login);
		}else {
			System.out.println("ERROR: Program is corrupted");
		}		
	}
	
	@Override
	public void logout() {
		click(By.id("userNavLabel"));
		click(By.xpath("//*[@title=\"Logout\"]"));
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

	public void makeTeams(int numOfTeams, Contact mainLead, int pauseTime) {
		click(home);
		SystemCommands.pause(pauseTime);
		openBar();
		click(teamCre);
		SystemCommands.pause(pauseTime);
		type(numTeamsCreate,Integer.toString(numOfTeams));
		type(annualReviewTempField,"NMTC 2017");
		select(fiscalYear,"2017");
		SystemCommands.pause(pauseTime);
		click(autoAssign);
		SystemCommands.pause(pauseTime);
		for(int i=0;i<numOfTeams;i++) {
			select(getTeamLeadSelect(i),"James Decker");
			select(getAltTeamLeadSelect(i),4);
			SystemCommands.pause(pauseTime);
		}
		SystemCommands.pause(pauseTime);
		click(save);
		SystemCommands.pause(pauseTime);
		accept();
		SystemCommands.pause(pauseTime);
	}
	
	public void assignDueDates(int appNum, int pauseTime) {
		click(home);
		SystemCommands.pause(pauseTime);
		openBar();
		click(appDueDate);
		SystemCommands.pause(pauseTime);
		type(appAnnualReviewTempField,"NMTC 2017");
		SystemCommands.pause(pauseTime);
		for(int i=0;i<appNum;i++) {
			if(i%2==0) {
				selectDueDate("A", i/2, 10, 10, 2017);
				SystemCommands.pause(pauseTime);
			}else {
				selectDueDate("B", (i-1)/2, 10, 10, 2017);
				SystemCommands.pause(pauseTime);
			}
		}
		SystemCommands.pause(pauseTime);
		click(saveRun);
		SystemCommands.pause(pauseTime);
	}

	public By getTeamLeadSelect(int teamNum) {
		return By.name("j_id0:j_id2:masterForm:mainForm:j_id44:"+teamNum+":j_id57");
	}
	
	public By getAltTeamLeadSelect(int teamNum) {
		return By.name("j_id0:j_id2:masterForm:mainForm:j_id44:"+teamNum+":j_id60");
	}
	
	
	public void selectDueDate(String Team,int number,int day,int month,int year) {
		if(Team.equals("A")) {
			type(By.id("j_id0:j_id2:j_id3:mainForm:j_id35:"+number+":j_id37"),month+"/"+day+"/"+year);
			
		}else{
			type(By.id("j_id0:j_id2:j_id3:mainForm:j_id35:"+number+":j_id39"),month+"/"+day+"/"+year);
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
	
	public void openBar() {
		try {
			click(creNewBut);
			click(creNewBut);
			//If it's already visible, you are good to go!
		}catch(ElementNotVisibleException e) {
			click(sideBar);
			SystemCommands.pause();
		}
	}

	
}
