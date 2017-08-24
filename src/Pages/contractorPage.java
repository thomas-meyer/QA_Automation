package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class contractorPage extends Page{

	By home=By.linkText("Home");
	By organizations=By.linkText("Organizations");
	By contacts=By.linkText("Contacts");
	By reviewer_profiles=By.linkText("Reviewer Profiles");
	By application_review_teams=By.linkText("Application Review Teams");
	
	public contractorPage(WebDriver driverBeingUsed){
		this.driver=driverBeingUsed;
		this.expectedTitle="Applicant";
	}
	public boolean homeTabClick() {
		if(this.elementExists(home)) {
			driver.findElement(home).click();
			return true;
		}else {
			return false;
		}
	}
	public boolean orgTabClick() {
		if(this.elementExists(organizations)) {
			driver.findElement(organizations).click();
			return true;
		}else {
			return false;
		}
	}
	public boolean contactsTabClick() {
		if(this.elementExists(contacts)) {
			driver.findElement(contacts).click();
			return true;
		}else {
			return false;
		}
	}
	public boolean revProTabClick() {
		if(this.elementExists(reviewer_profiles)) {
			driver.findElement(reviewer_profiles).click();
			return true;
		}else {
			return false;
		}
	}
	public boolean appRevTeamTabClick() {
		if(this.elementExists(application_review_teams)) {
			driver.findElement(application_review_teams).click();
			return true;
		}else {
			return false;
		}
	}
	public boolean createNewRevClick() {
		if(this.elementExists(By.xpath("//*[@title=\"Create New Reviewer Profile\"]"))) {
			driver.findElement(By.xpath("//*[@title=\"Create New Reviewer Profile\"]")).click();
			return true;
		}else if(this.elementExists(By.xpath("//*[@title=\"New Reviewer Profile\"]"))){
			driver.findElement(By.xpath("//*[@title=\"New Reviewer Profile\"]")).click();
			return true;
		}else {
			return false;
		}
	}
	public boolean goButtonClick() {
		By button=By.xpath("//*[@title=\"Go!\"]");
		if(this.elementExists(button)) {
			driver.findElement(button).click();
			return true;
		}else {
			return false;
		}
	}
}
