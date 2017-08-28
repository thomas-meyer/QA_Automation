package Pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public abstract class Page {
	static WebDriver driver;
	String expectedTitle;


	
	public abstract void login(Object loginInfo);

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
	
	public void accept() {
		driver.switchTo().alert().accept();
	}
	
	
}
