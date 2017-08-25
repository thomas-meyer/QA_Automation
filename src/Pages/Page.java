package Pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import AutomationFramework.SystemCommands;

public abstract class Page {
	static WebDriver driver;
	String expectedTitle;
	public By newBut=By.id("createNewButton");
	public By sideBar=By.id("pinIndicator");
	public By logout=By.xpath("//*[@title=\"Logout\"]");
	public By user=By.id("userNavLabel");
	
	public String getTitle(){
	        return Page.driver.getTitle();
	}
	
	public void verifyTitle() {
		Assert.assertEquals(this.expectedTitle,this.getTitle());
	}
	
	public boolean checkTitle(String expectedTitle) {
		return this.getTitle().equals(expectedTitle);
	}
	
	public boolean elementExists(By element) {
		try {
			Page.driver.findElement(element);
			return true;
		}catch(NoSuchElementException noEl) {
			return false;
		}catch(ElementNotVisibleException hidEl) {
			return false;
		}
	}
	
	public void buttonClick(By button,String titleChange) {
		if(this.elementExists(button)) {
			driver.findElement(button).click();
			this.expectedTitle=titleChange;
		}else {
		}
	}
	
	public boolean buttonClick(By button) {
		if(this.elementExists(button)) {
			driver.findElement(button).click();
			return true;
		}else {
			return false;
		}
	}
	
	public boolean enterField(By field, String input) {
		if(this.elementExists(field)) {
			driver.findElement(field).sendKeys(input);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean selectList(By selectMenu,String selectOption) {
		if(this.elementExists(selectMenu)) {
			Select menu=new Select(driver.findElement(By.id("Profile")));
			menu.selectByVisibleText("Reviewer");
			return true;
		}else {
			//maybe you tried to select something you couldn't
			return false;
		}
	}
	
	public boolean selectList(By selectMenu,int selectOption) {
		if(this.elementExists(selectMenu)) {
			Select menu=new Select(driver.findElement(selectMenu));
			try{
				menu.selectByIndex(selectOption);
				return true;
			}catch(NoSuchElementException e) {
				return false;
			}
		}else {
			//maybe you tried to select something you couldn't
			return false;
		}
	}
	
	public void Logout() {
		//Page.driver.navigate().to(sandBoxURL);
		this.buttonClick(this.user);
		this.buttonClick(this.logout);
	}
	
	public void accept() {
		driver.switchTo().alert().accept();
	}
	
	//Ensures that the "convenient" side-bar
	//is minimized.
	public void closeBar() {
		try {
			this.buttonClick(newBut);
			this.buttonClick(this.sideBar);
			SystemCommands.pause();
		}catch(ElementNotVisibleException e) {
			//If it isn't visible, we are good to go!
		}
	}
	
}
