//This package should contain Selenium add-ons
package AutomationFramework;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver; //Currently the program is set to run on a chrome browser

import Pages.OLCPage;
import Pages.contractorPage;
import Pages.loginPage;
import Pages.reviewerPage;
import Pages.skillAssessorPage;

//This is the name of your class
public class SeleniumRun{
	
	public static void main(String[] args) throws InterruptedException {
		//The driver should be in the project 
		//folder, else hard code your driver location
		String chromeDriveLoc="chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",chromeDriveLoc);
		WebDriver driver = new ChromeDriver();
		//We are now ready to rock'n'roll
		//See Contact Class for documentation
		Contact newReviewer= new Contact("New","Frame6");
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
		String F2orgURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00135000003Dfsv";
		loginPage lPage;
		contractorPage cPage;
		reviewerPage rPage;
		skillAssessorPage sPage;
		OLCPage olcPage;
		lPage=new loginPage(driver, sandBoxURL);
		//cPage = new contractorPage(driver,contractorURL);
		//cPage.createReviewer(reviewer);
		//lPage.changeUser();
		//rPage=new reviewerPage(driver, reviewer);
		//rPage.createProfile(reviewer);
		//lPage.changeUser();
		sPage=new skillAssessorPage(driver, skillAssessorURL);
		sPage.approveApp(reviewer);
		lPage.changeUser();
		rPage=new reviewerPage(driver, reviewer);
		rPage.addCOI(reviewer);
		lPage.changeUser();
		olcPage=new OLCPage(driver, olcStaffURL);
		driver.navigate().to(F2orgURL);
		olcPage.approveApp(reviewer);
		//...
		lPage.logout();
	}
	
}
