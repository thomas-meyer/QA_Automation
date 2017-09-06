package AutomationFramework;

import org.openqa.selenium.WebDriver;

import Pages.NMTCStaffPage;
import Pages.OLCStaffPage;
import Pages.contractorPage;
import Pages.reviewerPage;
import Pages.teamLeaderPage;

public class NMTC {
	//Primary Methods Section
	
	//This function is designed to take a potential reviewer/contact
	//and automate the process from creation to successfully moving them
	//to the qualified candidate pool
	public static void contactToPool(WebDriver driver, Contact reviewer) throws InterruptedException {
		System.out.println("BEGIN: Contact to Qualified Pool Process");	int pauseTime=0;
		createReviewer(driver,reviewer,pauseTime);
		createReviewProfile(driver, reviewer, pauseTime);
		skillApproval(driver,reviewer,pauseTime);
		fillOutCOI(driver,reviewer,pauseTime);
		olcApprove(driver,reviewer,pauseTime);
		System.out.println("END: Contact to Qualified Pool Process");
	}
	
	public static void createTeamLead(WebDriver driver, Contact reviewer) throws InterruptedException{
		System.out.println("BEGIN: Clearing Team Lead");int pauseTime=1;
		createTeamLead(driver,reviewer,pauseTime);
		createTeamLeadProfile(driver, reviewer, pauseTime);
		olcApprove(driver,reviewer,pauseTime);
		System.out.println("END: Clearing Team Lead");
	}
	////
	
	public static void createTeam(WebDriver driver, Contact mainLead, int pauseTime) {
		System.out.println("BEGIN: Generating Teams");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		String skillAssessorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1";//Ayana Sufian
		NMTCStaffPage sPage;
		sPage=new NMTCStaffPage(driver, sandBoxURL, skillAssessorURL);
		sPage.makeTeams(5,mainLead, pauseTime);
		sPage.assignDueDates(15,pauseTime);
		System.out.println("End: Generating Teams");
	}
	
	public static void grantAndCOmpleteScore(WebDriver driver, Contact mainLead, int pauseTime) {
		System.out.println("BEGIN: Scorecard Granting and Completing");
		System.out.println("BEGIN: Generating Teams");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		teamLeaderPage tPage;
		tPage=new teamLeaderPage(driver,sandBoxURL,mainLead);
		tPage.grantAcess(pauseTime);
		System.out.println("End: Scorecard Granting and Completing");
	}
	
	//Sub Methods Section
	public static void createTeamLead(WebDriver driver, Contact teamLead, int pauseTime) {
		System.out.println("BEGIN: Creating new TeamLead");
		//Important link.  If this is broken, part of program won't work properly
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";
		contractorPage cPage;
		cPage = new contractorPage(driver,sandBoxURL,contractorURL);
		cPage.createTeamLeader(teamLead,pauseTime);
		System.out.println("END: Creating new TeamLead");
	}
	
	//Contractor creates the profile of a team Lead
	public static void createTeamLeadProfile(WebDriver driver, Contact teamLead, int pauseTime) {
		System.out.println("BEGIN: Filling out Team Leader Profile");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		teamLeaderPage tPage;
		tPage=new teamLeaderPage(driver,sandBoxURL,teamLead);
		tPage.createLeadProfile(teamLead,pauseTime);
		System.out.println("END: Filling out Team Leader Profile");
	}
	
	//Contractor create the profile of a reviewer
	public static void createReviewer(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println("BEGIN: Creating new Reviewer");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";
		contractorPage cPage;
		cPage = new contractorPage(driver,sandBoxURL,contractorURL);
		cPage.createReviewer(reviewer,pauseTime);
		System.out.println("END: Creating new Reviewer");
	}
	
	//A skill assessor staff aproves an application
	public static void skillApproval(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println("BEGIN: Approving Reviewer's Skills");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		String skillAssessorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1";//Ayana Sufian
		NMTCStaffPage sPage;
		sPage=new NMTCStaffPage(driver, sandBoxURL, skillAssessorURL);
		sPage.approveApp(reviewer,pauseTime);
		System.out.println("END: Approving Reviewer's Skills");
	}
	
	//A reviewer creates there skills assessment ready profile
	public static void createReviewProfile(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println("BEGIN: Filling out Reviewer Profile");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		reviewerPage rPage;
		rPage=new reviewerPage(driver,sandBoxURL,reviewer);
		rPage.createRevProfile(reviewer,pauseTime);
		System.out.println("END: Filling out Reviewer Profile");
	}
	
	//A reviewer fills out their COI
	public static void fillOutCOI(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println("BEGIN: Filling out COI");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		reviewerPage rPage;
		rPage=new reviewerPage(driver,sandBoxURL,reviewer);
		rPage.reviewerAddCOI(reviewer,pauseTime);
		System.out.println("END: Filling out COI");
	}
	
	//OLC Staff approves an application's COIs
	public static void olcApprove(WebDriver driver, Contact contact, int pauseTime) {
		System.out.println("BEGIN: Approving Contacts COIs");
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		String olcStaffURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1";//Ashanti Kimbrough
		String F2orgConURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/003?rlid=RelatedContactList&id=00135000003Dfsv";
		OLCStaffPage olcPage;
		olcPage=new OLCStaffPage(driver,sandBoxURL,olcStaffURL);
		olcPage.approveApp(contact,F2orgConURL,pauseTime);
		System.out.println("END: Approving Contacts COIs");
	}
	////
	
	
	
}
