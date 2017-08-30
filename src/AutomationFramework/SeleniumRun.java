//This package should contain Selenium add-ons
package AutomationFramework;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import Pages.OLCPage;
import Pages.contractorPage;
import Pages.loginPage;
import Pages.reviewerPage;
import Pages.skillAssessorPage;


public class SeleniumRun{
	
	public static void main(String[] args) throws InterruptedException {
		////Sets up the output file
		DateFormat dateFormat = new SimpleDateFormat("MM.dd_HH.mm");
		Date date = new Date();
		File debugg = null;
		try {
			debugg = new File("QA_Results_"+dateFormat.format(date)+".txt");
	        System.setOut(new PrintStream(debugg));
			dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
			System.out.println("QA Automation BEGIN run at: "+dateFormat.format(date));
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
		////Output file has now been set up
		
		////Sets up the browser for use.  
		//Uncomment the browser you wish to use an make sure
		//the proper .exe file is in the directory
		WebDriver driver=chromeSetup();//Setup for Chrome
		//WebDriver driver = new FirefoxDriver();//Setup for Firefox
		//WebDriver driver=IESetup(); //Setup for Internet Explorer
		////Browser has now been set up
		
		
		////***THE CODE YOU WISH TO RUN GOES HERE!***
		////
		
		Random rand=new Random();
		Contact reviewer= new Contact(rand.nextInt(100000));
		int[][] add;
		//Creating COIs for reviewer
		add= new int[2][1];
		add[0][0]=1;add[1][0]=2;
		//
		reviewer.setCOIs(add);
		contactToPool(driver,reviewer);
		Contact newLead= new Contact(rand.nextInt(100000));
		//Creating COIs for newLead
		add= new int[2][3];
		add[0][0]=1;add[1][0]=2;
		add[0][1]=3;add[1][1]=0;
		add[0][2]=4;add[1][2]=1;
		//
		newLead.setCOIs(add);
		createTeamLead(driver,newLead);
		
		////
		////***THE CODE YOU WISH TO RUN SHOULD BE FINISHED***
		driver.close();
		try {
			//Puts the end signature on the file
			date = new Date();
			dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
			System.out.println("QA Automation END run at: "+dateFormat.format(date));
	        debugg.setReadOnly();
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
	}
	
	//Primary Methods Section
	
	//This function is designed to take a potential reviewer/contact
	//and automate the process from creation to successfully moving them
	//to the qualified candidate pool
	public static void contactToPool(WebDriver driver, Contact reviewer) throws InterruptedException {
		System.out.println("BEGIN: Contact to Qualified Pool Process");
		//Important links.  If any of these are broken, the process will r
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		loginPage lPage;
		int pauseTime=0;
		lPage=new loginPage(driver, sandBoxURL);
		createReviewer(driver,reviewer,pauseTime);
		lPage.changeUser();
		createReviewProfile(driver, reviewer, pauseTime);
		lPage.changeUser();
		skillApproval(driver,reviewer,pauseTime);
		lPage.changeUser();
		fillOutCOI(driver,reviewer,pauseTime);
		lPage.changeUser();
		olcApprove(driver,reviewer,pauseTime);
		lPage.logout();	
		System.out.println("END: Contact to Qualified Pool Process");
	}
	
	public static void createTeamLead(WebDriver driver, Contact reviewer) throws InterruptedException{
		System.out.println("BEGIN: Clearing Team Lead");
		//Important links.  If any of these are broken, the process will r
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		loginPage lPage;
		int pauseTime=1;
		lPage=new loginPage(driver, sandBoxURL);
		createTeamLead(driver,reviewer,pauseTime);
		lPage.changeUser();
		createTeamLeadProfile(driver, reviewer, pauseTime);
		lPage.changeUser();
		olcApprove(driver,reviewer,pauseTime);
		lPage.logout();	
		System.out.println("END: Clearing Team Lead");
	}
	////
	
	//Sub Methods Section
	public static void createTeamLead(WebDriver driver, Contact teamLead, int pauseTime) {
		System.out.println('\t'+"BEGIN: Creating new TeamLead");
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";
		contractorPage cPage;
		cPage = new contractorPage(driver,contractorURL);
		cPage.createTeamLeader(teamLead,pauseTime);
		System.out.println('\t'+"END: Creating new TeamLead");
	}
	
	//Contractor creates the profile of a team Lead
	public static void createTeamLeadProfile(WebDriver driver, Contact teamLead, int pauseTime) {
		System.out.println('\t'+"BEGIN: Filling out Team Leader Profile");
		reviewerPage rPage;
		rPage=new reviewerPage(driver, teamLead);
		rPage.createLeadProfile(teamLead,pauseTime);
		System.out.println('\t'+"END: Filling out Team Leader Profile");
	}
	
	//Contractor create the profile of a reviewer
	public static void createReviewer(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println('\t'+"BEGIN: Creating new Reviewer");
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";
		contractorPage cPage;
		cPage = new contractorPage(driver,contractorURL);
		cPage.createReviewer(reviewer,pauseTime);
		System.out.println('\t'+"END: Creating new Reviewer");
	}
	
	//A skill assessor staff aproves an application
	public static void skillApproval(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println('\t'+"BEGIN: Approving Reviewer's Skills");
		String skillAssessorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1";//Ayana Sufian
		skillAssessorPage sPage;
		sPage=new skillAssessorPage(driver, skillAssessorURL);
		sPage.approveApp(reviewer,pauseTime);
		System.out.println('\t'+"END: Approving Reviewer's Skills");
	}
	
	//A reviewer creates there skills assessment ready profile
	public static void createReviewProfile(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println('\t'+"BEGIN: Filling out Reviewer Profile");
		reviewerPage rPage;
		rPage=new reviewerPage(driver, reviewer);
		rPage.createRevProfile(reviewer,pauseTime);
		System.out.println('\t'+"END: Filling out Reviewer Profile");
	}
	
	//A reviewer fills out their COI
	public static void fillOutCOI(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println('\t'+"BEGIN: Filling out COI");
		reviewerPage rPage;
		rPage=new reviewerPage(driver, reviewer);
		rPage.reviewerAddCOI(reviewer,pauseTime);
		System.out.println('\t'+"END: Filling out COI");
	}
	
	//OLC Staff approves an application's COIs
	public static void olcApprove(WebDriver driver, Contact contact, int pauseTime) {
		System.out.println('\t'+"BEGIN: Approving Reviewer COI");
		String olcStaffURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1";//Ashanti Kimbrough
		String F2orgConURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/003?rlid=RelatedContactList&id=00135000003Dfsv";
		OLCPage olcPage;
		olcPage=new OLCPage(driver, olcStaffURL);
		olcPage.approveApp(contact,F2orgConURL,pauseTime);
		System.out.println('\t'+"END: Approving Reviewer COI");
	}
	////
	
	
	//Browser Setup Section
	//Sets up a chrome browser
	public static WebDriver chromeSetup() {
		String chromeDriveLoc="chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",chromeDriveLoc);
		WebDriver driver = new ChromeDriver();
		return driver;
	}
	
	//Sets up an internet explorer browser
	public static WebDriver IESetup() {
		System.setProperty("webdriver.ie.driver","IEDriverServer.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability (InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		WebDriver driver = new InternetExplorerDriver(capabilities);
		return driver;

	}
	////
}
