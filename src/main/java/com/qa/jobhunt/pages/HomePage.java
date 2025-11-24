package com.qa.jobhunt.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

	private WebDriver driver;

	public HomePage(WebDriver driver) {
		this.driver = driver;
	}

	// By Locators:
	By SearchBar = By.xpath("//div[@class='nI-gNb-sb__main']");
	By SearchKeyword = By.xpath("//input[contains(@placeholder,'keyword')]");
	By Experience = By.xpath("//input[@id = 'experienceDD']");
	By selectLocation = By.xpath("//input[contains(@placeholder,'location')]");
	By keywordDropdownSuggestions = By.xpath("//div[contains(@class,'drop-layer')]//ul[@class='layer-wrap']/li");
	By expDropdownSuggestions = By.xpath("//ul[@class='dropdown ']/li[normalize-space()]");
	By locationDropdownSuggestions = By.xpath("//div[contains(@class,'drop-layer')]//ul[@class='layer-wrap']/li");
    By Search = By.xpath("//span[@class='ni-gnb-icn ni-gnb-icn-search']");

	// Page actions:

	public void doSearchBarClick() {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(SearchBar));
		driver.findElement(SearchBar).click();
	}

	public JobSearchPage dropDownHandlingToSearch(String keyword, String exp, String location) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    // ====== Enter Keyword ======
	    driver.findElement(SearchKeyword).sendKeys(keyword);

	    // Wait for keyword suggestions dropdown to be visible
	    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(keywordDropdownSuggestions));

	    // Get all keyword suggestion elements
	    List<WebElement> expSuggestions = driver.findElements(keywordDropdownSuggestions);

	    // Loop through suggestions and click the matching one
	    for (WebElement suggestion : expSuggestions) {
	        String text = suggestion.getText();
	        if (text.toLowerCase().contains(keyword.toLowerCase())) {
	            System.out.println("Clicking on keyword suggestion: " + text);
	            suggestion.click();
	            break;
	        }
	    }

	    // ====== Enter Experience ======
	    driver.findElement(Experience).click();

	    // Wait for experience suggestions dropdown to be visible
	    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(expDropdownSuggestions));

	    // Get all experience suggestion elements
	    List<WebElement> keywordSuggestions = driver.findElements(expDropdownSuggestions);

	    // Loop through experience suggestions and click the matching one
	    for (WebElement suggestion : keywordSuggestions) {
	        String text = suggestion.getText();
	        if (text.toLowerCase().contains(exp.toLowerCase())) {
	            System.out.println("Clicking on experience suggestion: " + text);
	            suggestion.click();
	            break;
	        }
	    }

	    // ====== Enter Location ======
	    driver.findElement(selectLocation).sendKeys(location);

	    // Wait for location suggestions dropdown to be visible
	    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locationDropdownSuggestions));

	    // Get all location suggestion elements
	    List<WebElement> locationSuggestions = driver.findElements(locationDropdownSuggestions);

	    // Loop through location suggestions and click the matching one
	    for (WebElement suggestion : locationSuggestions) {
	        String text = suggestion.getText();
	        if (text.toLowerCase().contains(location.toLowerCase())) {
	            System.out.println("Clicking on location suggestion: " + text);
	            suggestion.click();
	            break;
	        }
	    }
	    
	    driver.findElement(Search).click();

	    // Return the page title after selections
	    return new JobSearchPage(driver);
	}

}
