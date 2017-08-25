//This package should contain Selenium add-ons
package AutomationFramework;



import org.openqa.selenium.chrome.ChromeDriver; //Currently the program is set to run on a chrome browser
import org.openqa.selenium.remote.Augmenter;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Pages.contractorPage;
import Pages.reviewerPage;
import Pages.salesforcePage;
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
		Contact newReviewer= new Contact("Timito","Tamato");
		contactToPool(driver,newReviewer);
		//driver.close();
	}
	
	//This function is designed to take a potential reviewer/contact
	//and automate the process from creation to successfully moving them
	//to the qualified candidate pool
	public static void contactToPool(WebDriver driver, Contact reviewer) throws InterruptedException {
		salesforcePage page=new salesforcePage(driver);
		contractorPage cPage = new contractorPage(driver);
		reviewerPage rPage=new reviewerPage(driver);
		skillAssessorPage sPage=new skillAssessorPage(driver);
		page.Login();
		//page.loginAsContractor("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S");
		//cPage.contractorPageExe(reviewer);
		//page.loginAsReviewer(reviewer);
		//rPage.reviewerProfile(reviewer);
		//page.refreshUser();
		page.loginAsUser("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1");
		sPage.approveApp(reviewer);
		page.refreshUser();
		page.loginAsReviewer(reviewer);
		rPage.addCOI(reviewer);
		page.refreshUser();
		page.loginAsUser("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1");
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00135000003Dfsv");//F2 Oog
		//Ashanti Kimbrough (OLC Staff), logs 
		//in and approves COI information
		//OLCApprove(driver, reviewer);
		//Logs out after the process is finished
		page.Logout();
	}
	
}
