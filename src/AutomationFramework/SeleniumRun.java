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
		Contact newReviewer= new Contact("Todd","Harper");
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
		page.Login();
		page.LoginAsUser("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S");
		cPage.contractorPageExe(reviewer);
		page.LoginAsUser(reviewer);
		rPage.reviewerProfile(reviewer);
		page.refreshUser();
		//Ayana Sufian (Skill Assessor), logs 
		//in and approves reviewer's skills
		approveApp(driver, reviewer);
		//A necessary log out, log back in
		//to ensure no bugs are encountered
		page.refreshUser();
		//The user logs in and fills out
		//their COI forms
		AddCOI(driver, reviewer);
		//A necessary log out, log back in
		//to ensure no bugs are encountered
		page.refreshUser();
		//Ashanti Kimbrough (OLC Staff), logs 
		//in and approves COI information
		OLCApprove(driver, reviewer);
		//Logs out after the process is finished
		page.Logout();
	}
	

	

	
	//Searches through salesforce database to find a particular person
	//param: lookup= the expected format which the contact being search for wil be displayed
	//param: iterator= the type on the "more"/"next" button used to iterate through the list
	public static void findContact(WebDriver driver, String lookUp, String iterator) {
		//Simplifies the search by alphabetizing and then jumping to the letter
		//of the alphebet that we expect our contacted to be listed at
		String letter=lookUp.charAt(0)+"";
		driver.findElement(By.linkText(letter)).click();
		SystemCommands.pause();
		//var: foundName=is used to iterate through
		//the list until we've found our Contact
		boolean foundName=false;
		//NOTE: a potential problem arises if our page 
		//view is set to more than 10 per page.  ADD CODE HERE
		do {
		//The do-while loop, runs until we've found our 
		//contact potentially add a termination condition 
		//to avoid potentially infinite loops
			try {
				//if we find our contact, click it
				driver.findElement(By.linkText(lookUp)).click();
				SystemCommands.pause();
				//set boolean so that look terminates
				foundName=true;
			}catch(NoSuchElementException e2) {
				//if we don't find it, click our iterator button
				try{
					//...assuming the button is valid
					driver.findElement(By.partialLinkText(iterator)).click();
					SystemCommands.pause();
				}catch(NoSuchElementException e3) {
					//if the iterator button isn't valid, we were too fast
					//the quick if/else statement, refreshes out search
					if(!"A".equals(letter)) {
						driver.findElement(By.linkText("A")).click();
					}else {
						driver.findElement(By.linkText("B")).click();
					}
					driver.findElement(By.linkText(letter)).click();
				}
			}	
		}while(!foundName);
		//Yay! we found our contact
	}
	
	//The contact's application is entered into the system.
	//now we just need to blindly approve it
	//param: reviewer= the relevant contact whose skills we are approving
	public static void approveApp(WebDriver driver, Contact reviewer) {
		//Link to Ayana Sufian's (skill assessor) salesforce pages
		SystemCommands.pause(5);
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1");
		SystemCommands.pause();
		//Logs in as the skill assessor
		driver.findElement(By.xpath("//*[@title=\"Login\"]")).click();
		SystemCommands.pause();
		//Goes to lookup our candiate/reviewer
		driver.findElement(By.xpath("//*[@title=\"Reviewer Profiles Tab\"]")).click();
		SystemCommands.pause();
		driver.findElement(By.xpath("//*[@title=\"Go!\"]")).click();
		SystemCommands.pause();
		//ensures that the reviewers are recognized by name, not by application
		//Necessary for out "findContact" helper function
		driver.findElement(By.xpath("//*[@title=\"Reviewer Name\"]")).click();
		SystemCommands.pause();
		//This is how we expect our contact to be listed
		String lookUp=reviewer.getFirstName()+" "+reviewer.getLastName();
		//Finds the contact
		findContact(driver,lookUp,"Next");
		SystemCommands.pause();
		//ensures side-bar is closed, so that 
		//we don't accidentally grab to wrong application
		closeBar(driver);
		SystemCommands.pause();
		//grabs the application
		driver.findElement(By.partialLinkText("REV-")).click();
		try {
			//approves it
			driver.findElement(By.linkText("Approve / Reject")).click();
			SystemCommands.pause();
			driver.findElement(By.xpath("//*[@title=\"Approve\"]")).click();
			SystemCommands.pause();
		}catch(NoSuchElementException e) {
			//it's already been approved
			//this is a sketchy place to be
			//in debugging, this often meant
			//we grabbed the wrong application
		}
	}

	//Creates the Contacts COI information
	//param: reviewer= the relevant contact who is filling out COI forms
	public static void AddCOI(WebDriver driver, Contact reviewer) {
		//logs in as relevant Contact
		UserLogin(driver,reviewer);
		//traverses to his applications
		driver.findElement(By.xpath("//*[@title=\"Reviewer Profiles Tab\"]")).click();
		SystemCommands.pause();
		//ensures the side-bar is closed (even though 
		//I think it's impossible to have a side-bar 
		//open at this point)...I've been wrong before
		closeBar(driver);
		//clicks on the application
		driver.findElement(By.partialLinkText("REV-")).click();
		//begins to fill COI
		driver.findElement(By.xpath("//*[@title=\"New Applicant List – Identify Conflicts\"]")).click();
		//Fills out the forms.  I don't feel like detailing 
		//what each select does in the comments of the code
		WebElement newSelect;Select useThis;
		newSelect= driver.findElement(By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id42"));
		useThis=new Select(newSelect);
		useThis.selectByIndex(2);
		newSelect= driver.findElement(By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id40:j_id41"));
		useThis=new Select(newSelect);
		useThis.selectByIndex(1);
		driver.findElement(By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id37")).click();
		driver.findElement(By.name("pgApplicationUpdate:frmApplicationUpdate:pbApplicationUpdate:j_id36:j_id38")).click();
		SystemCommands.pause();
		//No COI's, GREAT, submit for approval
		driver.findElement(By.xpath("//*[@title=\"Submit for Approval\"]")).click();
		SystemCommands.pause();
		driver.switchTo().alert().accept();
		//Note that this is hard coded to not have COI's
		//A helper function would bee needed to manually add COI's
		
	}

	//The contact's COI's have been entered.
	//now we just need to blindly approve it
	//param: reviewer= the relevant contact whose COI we are approving
	public static void OLCApprove(WebDriver driver, Contact reviewer) {
		//Link to Ashanti Kimbrough's (OLC Staff) salesforce pages
		SystemCommands.pause(5);
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1");
		SystemCommands.pause();
		//Login as her
		driver.findElement(By.xpath("//*[@title=\"Login\"]")).click();
		SystemCommands.pause();
		//There probably is a better way to do this navigation...but it works
		//navigates to the F2 Org page
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00135000003Dfsv");
		SystemCommands.pause();
		//This link will take us to F2 Org Contacts
		//Hard Code disaster here.  A better link is needed
		driver.findElement(By.partialLinkText("(50+)")).click();
		SystemCommands.pause();
		//This is how we expect the contact's information to be displayed
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		//We find the contact
		findContact(driver,lookUp,"more");
		//ensuring the side-bar is closed
		closeBar(driver);
		//click on the app
		driver.findElement(By.partialLinkText("REV-")).click();
		SystemCommands.pause();
		try{
			//blindly approve it
			driver.findElement(By.linkText("Approve / Reject")).click();
			SystemCommands.pause();
			//If I wanted to add comments here, I could...
			driver.findElement(By.xpath("//*[@title=\"Approve\"]")).click();
			SystemCommands.pause();
		}catch(NoSuchElementException e) {
			//it's already been approved
			//this is a sketchy place to be
			//in debugging, this often meant
			//we grabbed the wrong application
		}
		//Now we just need to clear the contact
		//Enters the final phase
		driver.findElement(By.xpath("//*[@title=\"Edit\"]")).click();
		//Selects that the Contact is cleared with no restrictions
		WebElement newSelect;Select useThis;
		newSelect= driver.findElement(By.id("00Nt0000000SWhY"));
		useThis=new Select(newSelect);
		useThis.selectByIndex(1);
		//Clicks that we reviewed the resume that wasn't included
		driver.findElement(By.id("00Nt0000000SgVk")).click();
		//Saving...we are finished now
		driver.findElement(By.xpath("//*[@title=\"Save\"]")).click();
	}

	
}
