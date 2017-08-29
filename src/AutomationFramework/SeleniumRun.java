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
		try {
	        System.setOut(new PrintStream(new File("QA_Results_"+dateFormat.format(date)+".txt")));
			dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
			System.out.println("QA Automation run at: "+dateFormat.format(date));
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
		
		
		Random rand=new Random();
		Contact reviewer= new Contact(rand.nextInt(100000));
		contactToPool(driver,reviewer);
		Contact newLead= new Contact(rand.nextInt(100000));
		createTeamLead(driver,newLead);
		
		
		////***THE CODE YOU WISH TO RUN SHOULD BE FINISHED***
		driver.close();
	}
	
	//This function is designed to take a potential reviewer/contact
	//and automate the process from creation to successfully moving them
	//to the qualified candidate pool
	public static void contactToPool(WebDriver driver, Contact reviewer) throws InterruptedException {
		System.out.println("BEGIN: Contact to Qualified Pool Process");
		//Important links.  If any of these are broken, the process will r
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		loginPage lPage;
		int pauseTime=1;
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
	
	public static void createTeamLead(WebDriver driver, Contact teamLead, int pauseTime) {
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";//Andrew Manning
		contractorPage cPage;
		System.out.println("BEGIN: Creating new TeamLead");
		cPage = new contractorPage(driver,contractorURL);
		cPage.createTeamLeader(teamLead,pauseTime);
		System.out.println("END: Creating new TeamLead");
	}
	
	public static void createTeamLeadProfile(WebDriver driver, Contact teamLead, int pauseTime) {
		System.out.println("BEGIN: Filling out Reviewer Profile");
		reviewerPage rPage;
		rPage=new reviewerPage(driver, teamLead);
		rPage.createLeadProfile(teamLead,pauseTime);
		System.out.println("END: Filling out Reviewer Profile");
	}
	
	public static void createReviewer(WebDriver driver, Contact reviewer, int pauseTime) {
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";//Andrew Manning
		contractorPage cPage;
		System.out.println("BEGIN: Creating new Reviewer");
		cPage = new contractorPage(driver,contractorURL);
		cPage.createReviewer(reviewer,pauseTime);
		System.out.println("END: Creating new Reviewer");
	}
	
	public static void skillApproval(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println("BEGIN: Approving Reviewer's Skills");
		String skillAssessorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1";//Ayana Sufian
		skillAssessorPage sPage;
		sPage=new skillAssessorPage(driver, skillAssessorURL);
		sPage.approveApp(reviewer,pauseTime);
		System.out.println("END: Approving Reviewer's Skills");
	}
	
	public static void createReviewProfile(WebDriver driver, Contact reviewer, int pauseTime) {
		System.out.println("BEGIN: Filling out Reviewer Profile");
		reviewerPage rPage;
		rPage=new reviewerPage(driver, reviewer);
		rPage.createProfile(reviewer,pauseTime);
		System.out.println("END: Filling out Reviewer Profile");
	}
	
	public static void fillOutCOI(WebDriver driver, Contact contact, int pauseTime) {
		System.out.println("BEGIN: Filling out COI");
		reviewerPage rPage;
		rPage=new reviewerPage(driver, contact);
		rPage.reviewerAddCOI(contact,pauseTime);
		System.out.println("END: Filling out COI");
	}
	
	public static void olcApprove(WebDriver driver, Contact contact, int pauseTime) {
		String olcStaffURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1";//Ashanti Kimbrough
		String F2orgConURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/003?rlid=RelatedContactList&id=00135000003Dfsv";
		OLCPage olcPage;
		System.out.println("BEGIN: Approving Reviewer COI");
		olcPage=new OLCPage(driver, olcStaffURL);
		olcPage.approveApp(contact,F2orgConURL,pauseTime);
		System.out.println("END: Approving Reviewer COI");
	}
	
	
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
}
