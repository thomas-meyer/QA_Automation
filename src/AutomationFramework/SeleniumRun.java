//This package should contain Selenium add-ons
package AutomationFramework;



import org.openqa.selenium.chrome.ChromeDriver; //Currently the program is set to run on a chrome browser

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

//This is the name of your class
public class SeleniumRun{
	
	public static void main(String[] args) throws InterruptedException {
		//The driver should be in the project 
		//folder, else hard code your driver location
		String chromeDriveLoc="chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",chromeDriveLoc);WebDriver driver = new ChromeDriver();
		//We are now ready to rock'n'roll
		//See Contact Class for documentation
		Contact newReviewer= new Contact("Mrs","Robotic3");
		contactToPool(driver,newReviewer);
		//driver.close();
	}
	
	//This function is designed to take a potential reviewer/contact
	//and automate the process from creation to successfully moving them
	//to the qualified candidate pool
	public static void contactToPool(WebDriver driver, Contact reviewer) throws InterruptedException {
		//logs into salesforce environment
		Login(driver);
		//Andrew Mannings (Contractor), logs 
		//in and creates reviewer profile
		ContractEnter(driver,reviewer);
		//The user logs in and creates
		//their profile
		UserProfileSetup(driver, reviewer);
		//A necessary log out, log back in
		//to ensure no bugs are encountered
		refreshUser(driver);
		//Ayana Sufian (Skill Assessor), logs 
		//in and approves reviewer's skills
		approveApp(driver, reviewer);
		//A necessary log out, log back in
		//to ensure no bugs are encountered
		refreshUser(driver);
		//The user logs in and fills out
		//their COI forms
		AddCOI(driver, reviewer);
		//A necessary log out, log back in
		//to ensure no bugs are encountered
		refreshUser(driver);
		//Ashanti Kimbrough (OLC Staff), logs 
		//in and approves COI information
		OLCApprove(driver, reviewer);
		//Logs out after the process is finished
		Logout(driver);
	}
	
	//Ensures that the "convenient" side-bar
	//is minimized.
	public static void closeBar(WebDriver driver) {
		try {
			//Checks if one of the side-bar's buttons are visible
			driver.findElement(By.id("createNewButton")).click();
			//If it is visible, no exception is found and
			//the side-bar is minimized
			driver.findElement(By.id("pinIndicator")).click();
			pause();
		}catch(ElementNotVisibleException e) {
			//If it isn't visible, we are good to go!
		}
	}
	
	//Pauses the automation momentarily
	public static void pause() {
		//Pausing has two purposes.  Longer pauses allow
		//for a spectator to comprehend the process.  Short
		//pauses gives the web pages time to properly load
		try {
			//This is a hard-coded number and can be changed
			//on user preference
			//NOTE: errors have been encountered below 100
			Thread.sleep(100);
		} catch (InterruptedException e) {
			//The Interruption Exception should never be thrown
		}
	}
	
	//Searches through salesforce database to find a particular person
	//param: lookup= the expected format which the contact being search for wil be displayed
	//param: iterator= the type on the "more"/"next" button used to iterate through the list
	public static void findContact(WebDriver driver, String lookUp, String iterator) {
		//Simplifies the search by alphabetizing and then jumping to the letter
		//of the alphebet that we expect our contacted to be listed at
		String letter=lookUp.charAt(0)+"";
		driver.findElement(By.linkText(letter)).click();
		pause();
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
				pause();
				//set boolean so that look terminates
				foundName=true;
			}catch(NoSuchElementException e2) {
				//if we don't find it, click our iterator button
				try{
					//...assuming the button is valid
					driver.findElement(By.partialLinkText(iterator)).click();
					pause();
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
		
	//Logins into our sandbox environment
	public static void Login(WebDriver driver) {
		//This is the link to our sandbox environment
		driver.get("https://cdfi1--cdfiqa01.cs33.my.salesforce.com");
		pause();
		try {
			//fills in the username/password and logs in.
			WebElement usernameLogin= driver.findElement(By.id("username"));
			WebElement passwordLogin= driver.findElement(By.id("password"));			
			try {
				String[] creditials=getLoginCred();
				usernameLogin.sendKeys(creditials[0]);
				passwordLogin.sendKeys(creditials[1]);
			}catch(IOException f) {
				//file with information not found
			}
			//="thomas.meyer@creativesyscon.com";
			//="Tbrorm#17!";

			pause();
			//AUTOMATION SWITCH
			//usernameLogin.sendKeys("shawkat.sufian@creativesyscon.com.cdfiqa01");
			//passwordLogin.sendKeys("$Lifeisgreat2017");
			driver.findElement(By.id("Login")).click();
			pause();
		}catch(NoSuchElementException e){
			//Occasionally the page will load/reload too fast.  In these cases
			//the pause feature is invoked and the process is attempted again.
			pause();pause();
			Login(driver);
		}
		if(driver.getTitle().equals("Unable to Process Request")) {
			//Login failed.  Program will loop forever until it gets in
			Login(driver);
		}
	}
	
	//The contact's profile is entered into the system.  Allowing the
	//contact himself to log in after the function has completed
	//param: reviewer= the relevant contact we are adding to the system
	public static void ContractEnter(WebDriver driver, Contact reviewer) {
		//Link to Andrew Manning's (contractor) salesforce pages
		pause();
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00335000003Pu9S");
		pause();
		//As a admin of the system, the program logs in as A. Manning
		driver.findElement(By.id("workWithPortalLabel")).click();
		pause();
		driver.findElement(By.partialLinkText("Log in to Comm")).click();
		pause();
		//The program begins creating a new Contact
		driver.findElement(By.xpath("//*[@title=\"Contacts Tab\"]")).click();
		pause();
		driver.findElement(By.xpath("//*[@title=\"New\"]")).click();
		pause();
		//Using the contacts information, the fields are filled out accordingly
		WebElement lastNameBut= driver.findElement(By.id("name_lastcon2"));
		WebElement firstNameBut= driver.findElement(By.id("name_firstcon2"));
		WebElement emailBut= driver.findElement(By.id("con15"));
		lastNameBut.sendKeys(reviewer.getLastName());
		firstNameBut.sendKeys(reviewer.getFirstName());
		emailBut.sendKeys(reviewer.getEmail());
		WebElement orgNa= driver.findElement(By.id("con4"));
		orgNa.sendKeys("F2 Solutions LLC");	
		pause();
		//Once entered, the new contact is saved into the system
		driver.findElement(By.xpath("//*[@title=\"Save\"]")).click();
		pause();
		driver.findElement(By.id("workWithPortalLabel")).click();
		pause();
		//The ability for us to log in as the new contact, is enabled
		driver.findElement(By.partialLinkText("Enable Partner")).click();
		pause();
		//I don't know what the purpose of this set is, but I was told
		//that it is absolutely nessessary.  AKA more data entry
		Select define=new Select(driver.findElement(By.id("Profile")));
		closeBar(driver);
		pause();
		define.selectByVisibleText("Reviewer");
		pause();
		//Information is saved, completing the creation of this new contact
		driver.findElement(By.xpath("//*[@title=\"Save\"]")).click();
		pause();
	}
	
	//Logs in as our Contact
	//used in conjunction with filling out contact forms
	//param: reviewer= the relevant contact who is filling out forms
	public static void UserLogin(WebDriver driver,Contact reviewer) {
		//navigates to our sandbox environment
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/");
		pause();
		//navigates to the list that contains our contact
		driver.findElement(By.xpath("//*[@title=\"Contacts Tab\"]")).click();
		pause();
		driver.findElement(By.xpath("//*[@title=\"Go!\"]")).click();
		pause();
		//This is how we expect our contacts 
		//information to be displayed in the list
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		//Finds out contacts
		findContact(driver,lookUp,"Next");
		//Logs into the system as our contact
		driver.findElement(By.id("workWithPortalLabel")).click();
		driver.findElement(By.partialLinkText("Log in to Comm")).click();
	}
	
	//Creates the Contacts profile
	//param: reviewer= the relevant contact who is filling out forms
	public static void UserProfileSetup(WebDriver driver, Contact reviewer) throws InterruptedException {
		//Logs into the sstem as the contact
		UserLogin(driver,reviewer);
		//Navigates to and begins creating profile
		driver.findElement(By.xpath("//*[@title=\"Reviewer Profiles Tab\"]")).click();
		pause();
		driver.findElement(By.xpath("//*[@title=\"Create New Reviewer Profile\"]")).click();
		pause();
		//Selects themselves as Reviewer
		WebElement newSelect;Select useThis;
		newSelect= driver.findElement(By.id("p3"));
		useThis=new Select(newSelect);
		useThis.selectByIndex(2);
		pause();
		//Continues to bigger form
		driver.findElement(By.xpath("//*[@title=\"Continue\"]")).click();
		pause();
		//Fill out the bigger form
		newSelect= driver.findElement(By.xpath("//*[@title=\"Review Program - Available\"]"));
		useThis=new Select(newSelect);
		useThis.selectByIndex(0);
		pause();
		driver.findElement(By.xpath("//*[@title=\"Add\"]")).click();
		//Selects Fiscal Year
		newSelect=driver.findElement(By.name("00Nt0000000SWhh"));
		useThis=new Select(newSelect);
		//1=2016, 2=2017
		useThis.selectByIndex(2);
		pause();
		//Selects previous experience
		newSelect=driver.findElement(By.name("00N35000000aO61"));
		useThis=new Select(newSelect);
		if(reviewer.getExp()) {
			//Has Experience
			useThis.selectByIndex(1);
		}else {
			//Doesn't have experience
			useThis.selectByIndex(2);
		}
		pause();
		//Enters the reviewers name
		driver.findElement(By.id("CF00Nt0000000SWKG")).sendKeys(reviewer.getLastName()+" "+reviewer.getFirstName());
		//Verifies that you've agreed and read the terms 
		//that in reality you didn't read...cause you're a robot
		pause();
		newSelect=driver.findElement(By.name("00Nt0000000SgVo"));
		useThis=new Select(newSelect);
		useThis.selectByIndex(1);
		pause();
		//variable used to register when the profile has been processed
		//NOTE: sometimes it takes time for the contact's information 
		//to have been added to the system.  If it begins looping, be patient
		Boolean processed=false;
		while(!processed) {
			pause();
			driver.findElement(By.xpath("//*[@title=\"Save\"]")).click();
			//Once the application has been processed, the page's title will change
			if(!driver.getTitle().equals("Reviewer Profile Edit: New Reviewer Profile ~ Applicant")) {
				//Proceed!
				processed=true;
			}
		}
		//IF YOU WANTED TO ATTACH A RESUME, HERE IS WHERE THE CODE WOULD GO
		//The application has now been filled out and we submit
		driver.findElement(By.xpath("//*[@title=\"Submit for Approval\"]")).click();
		pause();
		//Ok pop-up, we are sure.  Are robots ever unsure?
		driver.switchTo().alert().accept();
	}
	
	//The contact's application is entered into the system.
	//now we just need to blindly approve it
	//param: reviewer= the relevant contact whose skills we are approving
	public static void approveApp(WebDriver driver, Contact reviewer) {
		//Link to Ayana Sufian's (skill assessor) salesforce pages
		pause();
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00535000000UVtYAAW?noredirect=1&isUserEntityOverride=1");
		pause();
		//Logs in as the skill assessor
		driver.findElement(By.xpath("//*[@title=\"Login\"]")).click();
		pause();
		//Goes to lookup our candiate/reviewer
		driver.findElement(By.xpath("//*[@title=\"Reviewer Profiles Tab\"]")).click();
		pause();
		driver.findElement(By.xpath("//*[@title=\"Go!\"]")).click();
		pause();
		//ensures that the reviewers are recognized by name, not by application
		//Necessary for out "findContact" helper function
		driver.findElement(By.xpath("//*[@title=\"Reviewer Name\"]")).click();
		pause();
		//This is how we expect our contact to be listed
		String lookUp=reviewer.getFirstName()+" "+reviewer.getLastName();
		//Finds the contact
		findContact(driver,lookUp,"Next");
		pause();
		//ensures side-bar is closed, so that 
		//we don't accidentally grab to wrong application
		closeBar(driver);
		pause();
		//grabs the application
		driver.findElement(By.partialLinkText("REV-")).click();
		try {
			//approves it
			driver.findElement(By.linkText("Approve / Reject")).click();
			pause();
			driver.findElement(By.xpath("//*[@title=\"Approve\"]")).click();
			pause();
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
		pause();
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
		pause();
		//No COI's, GREAT, submit for approval
		driver.findElement(By.xpath("//*[@title=\"Submit for Approval\"]")).click();
		pause();
		driver.switchTo().alert().accept();
		//Note that this is hard coded to not have COI's
		//A helper function would bee needed to manually add COI's
		
	}

	//The contact's COI's have been entered.
	//now we just need to blindly approve it
	//param: reviewer= the relevant contact whose COI we are approving
	public static void OLCApprove(WebDriver driver, Contact reviewer) {
		//Link to Ashanti Kimbrough's (OLC Staff) salesforce pages
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/005t0000000cWV1AAM?noredirect=1&isUserEntityOverride=1");
		pause();
		//Login as her
		driver.findElement(By.xpath("//*[@title=\"Login\"]")).click();
		pause();
		//There probably is a better way to do this navigation...but it works
		//navigates to the F2 Org page
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/00135000003Dfsv");
		pause();
		//This link will take us to F2 Org Contacts
		//Hard Code disaster here.  A better link is needed
		driver.findElement(By.partialLinkText("(50+)")).click();
		pause();
		//This is how we expect the contact's information to be displayed
		String lookUp=reviewer.getLastName()+", "+reviewer.getFirstName();
		//We find the contact
		findContact(driver,lookUp,"more");
		//ensuring the side-bar is closed
		closeBar(driver);
		//click on the app
		driver.findElement(By.partialLinkText("REV-")).click();
		pause();
		try{
			//blindly approve it
			driver.findElement(By.linkText("Approve / Reject")).click();
			pause();
			//If I wanted to add comments here, I could...
			driver.findElement(By.xpath("//*[@title=\"Approve\"]")).click();
			pause();
		}catch(NoSuchElementException e) {
			//it's already been approved
			//this is a sketchy place to be
			//in debugging, this often meant
			//we grabbed the wrong application
		}
	}
	
	//Logs out of Salesforce
	//useful for ensuring that we aren't logged in as a contact
	public static void Logout(WebDriver driver) {
		//navigates to a page with log out option
		driver.navigate().to("https://cdfi1--cdfiqa01.cs33.my.salesforce.com/001/o");
		//clicks the logout button
		driver.findElement(By.id("userNavLabel")).click();
		driver.findElement(By.xpath("//*[@title=\"Logout\"]")).click();
		
	}
	
	//logs out and back in immediately
	public static void refreshUser(WebDriver driver) {
		//This is self explanatory, if you need 
		//comments to understand this code...
		//either I can't name functions or you need
		//to learn english
		Logout(driver);
		pause();
		Login(driver);
	}
	
	//accesses a text file with login information
	//this is to avoid hard coding usernames/passwords
	public static String[] getLoginCred() throws IOException{
		FileReader fileName;
		//the file the information is stored in
		fileName = new FileReader("loginInfo.txt");
		BufferedReader br = new BufferedReader(fileName);
		String line = br.readLine();
		br.close();
		//the information should be split by a comma
		return line.split(",");


	}

}
