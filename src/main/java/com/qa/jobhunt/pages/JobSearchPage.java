package com.qa.jobhunt.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JobSearchPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public JobSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // increased to 20 for stability
        this.js = (JavascriptExecutor) driver;
    }

    // Locators
    private By freshnessFilter = By.xpath("//button[@id= 'filter-freshness']/i[@class='ni-icon-arrow-down']");
    private By freshnessFilterSuggestions = By.xpath("//div[@data-filter-id='freshness']//child::ul/child::li//span");
    private By sortByFilter = By.xpath("//button[@id='filter-sort']/i[@class='ni-icon-arrow-down']");
    private By sortBySuggestions = By.xpath("//ul[@data-filter-id='sort']//li/a/span");
    private By jobCardLocator = By.xpath("//div[contains(@class ,'styles_jlc__main')]/div[@data-job-id]");
    private By nextButtonLocator = By.xpath("//a[text()='Next']");

    // Generic method to select filters
    public String selectJobFilters(String freshnessOption, String sortByOption) {

        // Sort By
        wait.until(ExpectedConditions.elementToBeClickable(sortByFilter)).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sortBySuggestions));

        List<WebElement> sortOptions = driver.findElements(sortBySuggestions);
        boolean isSortSelected = false;
        for (WebElement option : sortOptions) {
            if (option.getText().contains(sortByOption)) {
                System.out.println("✅ Clicking on Sort By option: " + option.getText());
                option.click();
                isSortSelected = true;
                break;
            }
        }
        if (!isSortSelected) System.out.println("❌ No matching Sort By option found for: " + sortByOption);

        // Freshness
        wait.until(ExpectedConditions.elementToBeClickable(freshnessFilter));
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(freshnessFilter));
        driver.findElement(freshnessFilter).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(freshnessFilterSuggestions));

        List<WebElement> freshnessOptions = driver.findElements(freshnessFilterSuggestions);
        boolean isFreshnessSelected = false;
        for (WebElement option : freshnessOptions) {
            if (option.getText().contains(freshnessOption)) {
                System.out.println("✅ Clicking on Freshness option: " + option.getText());
                option.click();
                isFreshnessSelected = true;
                break;
            }
        }
        if (!isFreshnessSelected) System.out.println("❌ No matching Freshness option found for: " + freshnessOption);

        // 👈👇 ADD this wait to ensure job cards reloaded after filters applied
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(jobCardLocator));

        return driver.getTitle();
    }

    public By getJobCardsLocator() {
    	
        return jobCardLocator;
    }

    public List<WebElement> getJobCards() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(jobCardLocator));
        return driver.findElements(jobCardLocator);
    }

    public boolean isNextButtonVisible() {
        try {
            WebElement nextBtn = driver.findElement(By.xpath("//a[contains(@class, 'fright') and text()='Next']"));
            return nextBtn.isDisplayed() && nextBtn.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickNextButton() {
        try {
            WebElement nextBtn = driver.findElement(By.xpath("//a[contains(@class, 'fright') and text()='Next']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);
            Thread.sleep(300);
            wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            nextBtn.click();
            System.out.println("➡️ Clicked Next page");
        } catch (Exception e) {
            System.out.println("❌ Could not click Next button: " + e.getMessage());
        }
    }
	public void waitForJobCardsToLoad() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(jobCardLocator));
	
		
	}
}
