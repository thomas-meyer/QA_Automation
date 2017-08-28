package Pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnsupportedCommandException;
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
			System.out.print("FAILED TO FIND: ("+element.toString()+")");
			return false;
		}catch(ElementNotVisibleException hidEl) {
			System.out.print("HIDDEN ELEMENT: ("+element.toString()+") ");
			return false;
		}
	}
	
	public void buttonClick(By button,String titleChange) {
		if(this.elementExists(button)) {
			driver.findElement(button).click();
			this.expectedTitle=titleChange;
		}else {
			System.out.println(" \"clickable button\"");
		}
	}
	
	public boolean buttonClick(By button) {
		if(this.elementExists(button)) {
			driver.findElement(button).click();
			return true;
		}else {
			System.out.println(" \"clickable button\"");
			return false;
		}
	}
	
	public boolean enterField(By field, String input) {
		if(this.elementExists(field)) {
			try {
				driver.findElement(field).sendKeys(input);
				return true;
			}catch(UnsupportedCommandException e) {
				System.out.println("FAILED TO ENTER information into the field: ("+field.toString()+")");
				return false;
			}
		}else {
			System.out.println(" \"field\"");
			return false;
		}
	}
	
	public boolean selectList(By selectMenu,String selectOption) {
		if(this.elementExists(selectMenu)) {
			try {
				Select menu=new Select(driver.findElement(By.id("Profile")));
				menu.selectByVisibleText("Reviewer");
				return true;
			}catch(NoSuchElementException e) {
				System.out.println("FAILED TO SELECT the value \""+selectOption+"\" from the picklist ("+selectMenu.toString()+")");
				return false;
			}
		}else {
			System.out.println(" \"picklist\"");
			return false;
		}
	}
	
	public boolean selectList(By selectMenu,int selectOption) {
		if(this.elementExists(selectMenu)) {
			try{
				Select menu=new Select(driver.findElement(selectMenu));
				menu.selectByIndex(selectOption);
				return true;
			}catch(NoSuchElementException e) {
				System.out.println("FAILED TO SELECT the "+Integer.toString(selectOption)+"value from the picklist ("+selectMenu.toString()+")");
				return false;
			}
		}else {
			System.out.println(" \"picklist\"");
			return false;
		}
	}
	
	public void accept() {
		driver.switchTo().alert().accept();
	}
	
	
}
