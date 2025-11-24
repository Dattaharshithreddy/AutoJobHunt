package com.qa.jobhunt.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	private WebDriver driver;
	
	public LoginPage( WebDriver driver) {
		this.driver =driver;
	}

	
	
	
	//Bylocators
	
     private 	By login= By.id("login_Layer");
	private By emailID = By.xpath("//*[@id=\"root\"]/div[4]/div[2]/div/div/div[2]/div/form/div[2]/input");
	private By password = By.xpath("//*[@id=\"root\"]/div[4]/div[2]/div/div/div[2]/div/form/div[3]/input");
	private By loginBtn = By.cssSelector(".btn-primary.loginButton");
	
	
	//page actions
	
	public HomePage doLogin(String username, String pswd) {
		
		driver.findElement(login).click();
		 // Explicit wait: wait until emailID input field is visible (max 10 seconds)
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.visibilityOfElementLocated(emailID));
	    
		System.out.println("Your app credentials are"+ username +  ":" + pswd);
		driver.findElement(emailID).sendKeys(username);
		driver.findElement(password).sendKeys(pswd);
		driver.findElement(loginBtn).click();
		return new HomePage(driver);
	}
	
}
