//This package should contain Selenium add-ons
package executeClass;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import AutomationFramework.Contact;
import AutomationFramework.NMTC;


public class execute{
	
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
		///
		
		//
		//String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/?ec=302&startURL=%2Fhome%2Fhome.jsp";
		//new loginPage(driver, sandBoxURL);
		
		Contact teamLead= new Contact("James","Decker");
		teamLead.setCOI(false);
		teamLead.setExp(true);
		NMTC.createTeam(driver,teamLead,1);
		/*
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
		*/
		
		////
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
