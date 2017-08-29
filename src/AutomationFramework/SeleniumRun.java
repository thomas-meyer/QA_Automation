//This package should contain Selenium add-ons
package AutomationFramework;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		
		
		
		Contact newReviewer= new Contact("Test","Messy6");
		contactToPool(driver,newReviewer);
		
		
		
		
		////***THE CODE YOU WISH TO RUN SHOULD BE FINISHED***
		driver.close();
	}
	
	//This function is designed to take a potential reviewer/contact
	//and automate the process from creation to successfully moving them
	//to the qualified candidate pool
	public static void contactToPool(WebDriver driver, Contact reviewer) throws InterruptedException {
		//important links.  If any of these are broken, the process will crash
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com";
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";//Andrew Manning
		String skillAssessorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1";//Ayana Sufian
		String olcStaffURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1";//Ashanti Kimbrough
		String F2orgConURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/003?rlid=RelatedContactList&id=00135000003Dfsv";
		//Users that will be used during the approval process
		loginPage lPage;contractorPage cPage;reviewerPage rPage;skillAssessorPage sPage;OLCPage olcPage;
		//Pause time, default is 0.  Change to make display easier to follow
		int pauseTime=1;
		//Logs into the sandbox
		lPage=new loginPage(driver, sandBoxURL);
		if(!driver.getTitle().equals("Salesforce - Enterprise Edition")) {
			System.out.println("Unable to Log into Salesforce."+'\n'
					+"Check \"loginInto.txt\" and verify that the login creditials are correct");
		}
		System.out.println("BEGIN: Creating new Reviewer");
		cPage = new contractorPage(driver,contractorURL);
		cPage.createReviewer(reviewer,pauseTime);
		System.out.println("END: Creating new Reviewer");
		lPage.changeUser();
		System.out.println("BEGIN: Filling out Reviewer Profile");
		rPage=new reviewerPage(driver, reviewer);
		rPage.createProfile(reviewer,pauseTime);
		System.out.println("END: Filling out Reviewer Profile");
		lPage.changeUser();
		System.out.println("BEGIN: Approving Reviewer's Skills");
		sPage=new skillAssessorPage(driver, skillAssessorURL);
		sPage.approveApp(reviewer,pauseTime);
		System.out.println("END: Approving Reviewer's Skills");
		lPage.changeUser();
		System.out.println("BEGIN: Filling out Reviewer COI");
		rPage=new reviewerPage(driver, reviewer);
		rPage.addCOI(reviewer,pauseTime);
		System.out.println("END: Filling out Reviewer COI");
		lPage.changeUser();
		System.out.println("BEGIN: Approving Reviewer COI");
		olcPage=new OLCPage(driver, olcStaffURL);
		olcPage.approveApp(reviewer,F2orgConURL,pauseTime);
		System.out.println("END: Approving Reviewer COI");
		lPage.logout();		
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
