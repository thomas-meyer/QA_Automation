//This package should contain Selenium add-ons
package AutomationFramework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver; //Currently the program is set to run on a chrome browser
import Pages.OLCPage;
import Pages.contractorPage;
import Pages.loginPage;
import Pages.reviewerPage;
import Pages.skillAssessorPage;


public class SeleniumRun{
	
	//This is what will actally run when you run the code
	public static void main(String[] args) throws InterruptedException {
		//The driver should be in the project 
		//folder, else hard code your driver location
		String chromeDriveLoc="chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",chromeDriveLoc);
		WebDriver driver = new ChromeDriver();
		//We are now ready to rock'n'roll
		//See Contact Class for documentation
		Contact newReviewer= new Contact("Should","Complete7");
		contactToPool(driver,newReviewer);
		//driver.close();
	}
	
	//This function is designed to take a potential reviewer/contact
	//and automate the process from creation to successfully moving them
	//to the qualified candidate pool
	public static void contactToPool(WebDriver driver, Contact reviewer) throws InterruptedException {
		String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com";
		String contractorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S";//Andrew Manning
		String skillAssessorURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1";//Ayana Sufian
		String olcStaffURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1";//Ashanti Kimbrough
		String F2orgConURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/003?rlid=RelatedContactList&id=00135000003Dfsv";
		loginPage lPage;contractorPage cPage;reviewerPage rPage;skillAssessorPage sPage;OLCPage olcPage;int pauseTime=0;
		//Logs into the sandbox
		lPage=new loginPage(driver, sandBoxURL);
		//Everything below here is the process
		cPage = new contractorPage(driver,contractorURL);
		cPage.createReviewer(reviewer,pauseTime);
		lPage.changeUser();
		rPage=new reviewerPage(driver, reviewer);
		rPage.createProfile(reviewer,pauseTime);
		lPage.changeUser();
		sPage=new skillAssessorPage(driver, skillAssessorURL);
		sPage.approveApp(reviewer,pauseTime);
		lPage.changeUser();
		rPage=new reviewerPage(driver, reviewer);
		rPage.addCOI(reviewer,pauseTime);
		lPage.changeUser();
		olcPage=new OLCPage(driver, olcStaffURL);
		olcPage.approveApp(reviewer,F2orgConURL,pauseTime);
		lPage.logout();
	}
	
}
