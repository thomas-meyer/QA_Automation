package AutomationFramework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Augmenter;

public class SystemCommands {

	static String sandBoxURL="https://cdfi1--cdfiqa01.cs33.my.salesforce.com";
	
	public static WebDriver creatDriver() {
		String chromeDriveLoc="chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",chromeDriveLoc);
		return new ChromeDriver();
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
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//The Interruption Exception should never be thrown
		}
	}
	
	//Allows you to pause for a inputed amount of time
	public static void pause(double num) {
		if(num!=0) {
			try {
				//This is a hard-coded number and can be changed
				//on user preference
				//NOTE: errors have been encountered below 100
				Thread.sleep((long) (num*1000));
			} catch (InterruptedException e) {
				//The Interruption Exception should never be thrown
			}
		}
	}
	
	public String captureScreen(WebDriver driver) {
		SystemCommands.pause(4);
	    String path;
	    try {
	        WebDriver augmentedDriver = new Augmenter().augment(driver);
	        File source = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
	        path = "./"+source.getName();//path name goes here
	        FileUtils.copyFile(source, new File(path)); 
	    }
	    catch(IOException e) {
	        path = "Failed to capture screenshot: " + e.getMessage();
	    }
	    SystemCommands.pause();
	    return path;
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
