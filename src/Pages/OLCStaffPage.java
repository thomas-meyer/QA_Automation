package Pages;

import java.io.IOException;
import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import AutomationFramework.Contact;
import AutomationFramework.NullPrintStream;
import AutomationFramework.SystemCommands;

public class OLCStaffPage extends Page{
	public By app=By.partialLinkText("REV-");
	public By approveReject=By.linkText("Approve / Reject");
	public By approveApp=By.xpath("//*[@title=\"Approve\"]");
	public By edit=By.xpath("//*[@title=\"Edit\"]");
	public By clearedStat=By.id("00Nt0000000SWhY");
	public By resume=By.id("00Nt0000000SgVk");
	public By save=By.xpath("//*[@title=\"Save\"]");
	public By reviewed=By.id("00Nt0000000SWhL");
	public By addCon=By.name("add_conflict");
	public By exitCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id38");
	public By saveCOI=By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id37");
	
	public OLCStaffPage(WebDriver driverBeingUsed, String sandBoxURL, String OLCstaffURL){
		Page.driver=driverBeingUsed;
		String[] loginInfo= {sandBoxURL,OLCstaffURL};
		login(loginInfo);
	}


	public void approveApp(Contact reviewer,String contactListURL, int pauseTime) {
		//Navigate to the correct page
		driver.navigate().to(contactListURL);
		SystemCommands.pause(pauseTime);
		if(!Page.driver.getTitle().equals("Contacts: F2 Solutions LLC ~ Salesforce - Enterprise Edition")) {
			SystemCommands.pause(pauseTime+2);
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
		click(By.linkText(letter));
		SystemCommands.pause(pauseTime);
		boolean foundName=false;int infCount=0;
		PrintStream original = System.out;
		System.setOut(new NullPrintStream());
		do {
			SystemCommands.pause();
			if(click(By.linkText(lookUp))) {
				foundName=true;
			}else {
				if(!click(By.partialLinkText("more"))) {
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
		if(infCount==100) {
			System.out.println("LOOPING ERROR: Couldn't find \""+lookUp+"\"");
		}
		SystemCommands.pause(pauseTime);
		click(app);
		SystemCommands.pause(pauseTime);
		if(click(approveReject)) {
			SystemCommands.pause(pauseTime);
			click(approveApp);
				SystemCommands.pause(pauseTime);
		}
		click(addCon);
		SystemCommands.pause(pauseTime);
		if(reviewer.getCOIcond()) {
			int[][] COIs=reviewer.getCOIs();
			for(int i=0;i<COIs[0].length;i++) {
				SystemCommands.pause(pauseTime);
				verifyCOI(COIs[0][i],COIs[1][i],pauseTime);
			}
		}
		SystemCommands.pause(pauseTime);
		click(saveCOI);
		SystemCommands.pause(pauseTime);
		click(exitCOI);
		SystemCommands.pause(pauseTime);
		click(edit);
		SystemCommands.pause(pauseTime);
		if(reviewer.getCOIcond()) {
			int[] COIstatus=reviewer.getCOIs()[1];
			int determ=0;
			for(int i=0;i<COIstatus.length;i++) {
				determ+=COIstatus[i];
			}
			SystemCommands.pause(pauseTime);
			if(determ==0) {
				select(clearedStat, 1);
			}else {
				select(clearedStat, 2);
			}
			SystemCommands.pause(pauseTime);
		}
		else {
			select(clearedStat, 1);
			SystemCommands.pause(pauseTime);
		}
		click(resume);
		SystemCommands.pause(pauseTime);
		if (elementExists(reviewed)) {
			if(!driver.findElement(reviewed).isSelected()){
				click(reviewed);
			}
		}
		SystemCommands.pause(pauseTime);
		click(save);
		SystemCommands.pause(pauseTime);	
	}

	//COIstatus
	//0: no ban, 1: partial ban, 2: total ban
	private void verifyCOI(int input, int COIstatus, int pauseTime) {
		String[] vals=getCOIVal(input);
		select(By.name(vals[3]), 2);
		SystemCommands.pause(pauseTime);
		if(COIstatus==1) {
			//Partial Ban
			select(By.name(vals[4]), 2);
		}else if(COIstatus==2) {
			//Total Ban
			select(By.name(vals[4]), 3);
		}else {
			//No Conflict
			select(By.name(vals[4]), 4);
		}
		SystemCommands.pause(pauseTime);
		type(By.name(vals[5]), "Looked At");
		SystemCommands.pause(pauseTime);
		
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
	public void login(Object[] loginInfo) {
		if(loginInfo instanceof String[]) {
			String sandBoxURL=(String) loginInfo[0];
			String OLCstaffURL=(String) loginInfo[1];
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
			webPage(OLCstaffURL,"User: Ashanti Kimbrough ~ Salesforce - Enterprise Edition");
			click(By.xpath("//*[@title=\"Login\"]"));
		}else {
			System.out.println("ERROR: Program is corrupted");
		}
	}


	@Override
	public void logout() {
		click(By.id("userNavLabel"));
		click(By.xpath("//*[@title=\"Logout\"]"));
	}
}
