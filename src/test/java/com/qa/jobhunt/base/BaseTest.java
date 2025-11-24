package com.qa.jobhunt.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.qa.jobhunt.factory.DriverFactory;
import com.qa.jobhunt.pages.HomePage;
import com.qa.jobhunt.pages.JobDetailsPage;
import com.qa.jobhunt.pages.JobSearchPage;
import com.qa.jobhunt.pages.LoginPage;

public class BaseTest {

    protected WebDriver driver;
    protected DriverFactory df;
    protected LoginPage loginpage;
    protected HomePage homepage;
    protected JobSearchPage jobsearchpage;

    protected JobDetailsPage jobdetailspage;


    @BeforeClass
    public void setup() {
        df = new DriverFactory();
        driver = df.initDriver("chrome");
        loginpage = new LoginPage(driver);
    
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}