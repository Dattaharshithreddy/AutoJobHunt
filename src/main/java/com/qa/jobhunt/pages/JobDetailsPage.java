package com.qa.jobhunt.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JobDetailsPage {
	private WebDriver driver;
	private WebDriverWait wait;
    private JavascriptExecutor js;

	public JobDetailsPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
	}
	
	
	
	

	
	By jobTitle = By.xpath("//h1[contains(@class,'styles_jd-header-title')]");
	By keySkills = By.xpath("//div[contains(@class,'styles_key-skill')]/div[contains(@class,'styles_legend')]/following-sibling::div/child::a//i/following-sibling::span");
	By companyName = By.xpath("(//div[contains(@class,'styles_jd-header-comp-name')]/a[@title])");
	By jobUrl = By.xpath("(//div[contains(@class,'styles_jd-header-comp-name')]/a[@href])[1]");
	By keySkillsSection =By.xpath("//div[contains(@class,'styles_key-skill')]");
	By applyButtonLocator = By.xpath("(//button[@id='apply-button'])[2]");
	
	// ===== Page Actions =====

    public String getJobTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitle));
        return driver.findElement(jobTitle).getText();
        
        }
    public By getJobTitleLocator() {
        return jobTitle;
    }
    
    public void scrollToSkillsSection() {
        WebElement skillsElement = driver.findElement((keySkillsSection)); // Use correct locator for skills section
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", skillsElement);
    }

    public List<String> getKeySkills() {
        List<WebElement> skillElements = driver.findElements(keySkills);
        List<String> skills = new ArrayList<>();
        for (WebElement skillElement : skillElements) {
            skills.add(skillElement.getText());
        }
        return skills;
    }

    public String getCompanyName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(companyName));
        return driver.findElement(companyName).getText();
    }

    public String getJobUrl() {
        return driver.getCurrentUrl();
    }

    public void clickApply() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(applyButtonLocator));
        WebElement applyButton = driver.findElement(applyButtonLocator);
        js.executeScript("arguments[0].scrollIntoView(true);", applyButton);
        wait.until(ExpectedConditions.elementToBeClickable(applyButton));
        applyButton.click();
    }

    public void goBackToSearchPage() {
        driver.navigate().back();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class ,'styles_jlc__main')]/div[@data-job-id]")));
    }
    public By getApplyButtonLocator() {
        return applyButtonLocator;
    }
}


