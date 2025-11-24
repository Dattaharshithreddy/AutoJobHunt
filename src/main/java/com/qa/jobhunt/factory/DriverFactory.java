package com.qa.jobhunt.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.qa.jobhunt.exception.FrameworkException;

public class DriverFactory {
	
	WebDriver driver;
	
	public WebDriver initDriver(String browserName) {
		System.out.println("Browser name is :=>" +browserName);
		
		switch (browserName.trim().toLowerCase()) {

		case "chrome":

			driver = new ChromeDriver();
			break;

		case "edge":

			driver = new EdgeDriver();
			break;

		case "firefox":

			driver = new FirefoxDriver();
			break;

		case "safari":

			driver = new SafariDriver();
			break;

		default:

			System.out.println("Please enter the valid browserName" + browserName);
			throw new FrameworkException("====invalid browser name======");

	}
		ChromeOptions options = new ChromeOptions();
		options.setAcceptInsecureCerts(true);

        WebDriver driver = new ChromeDriver(options);
       
		driver.get("https://www.naukri.com/");
		driver.manage().window().maximize(); 
	
		return driver;
	
  
	}
	
}
