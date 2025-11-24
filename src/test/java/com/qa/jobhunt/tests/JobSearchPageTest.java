package com.qa.jobhunt.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.jobhunt.base.BaseTest;
import com.qa.jobhunt.model.JobDetails;

import Services.JobApplyFlow;
import Services.JobDataService;

public class JobSearchPageTest extends BaseTest {

    JobDataService jobDataService;
    Map<String, String> answers;

    @BeforeClass
    public void jobSearchPageSetup() {
        homepage = loginpage.doLogin("dattaharshithreddynagireddy@gmail.com", "Harshithdhr@1968");
        homepage.doSearchBarClick();
        jobsearchpage = homepage.dropDownHandlingToSearch("automation test engineer", "4", "chennai");

        // ✅ Initialize Mongo service
        jobDataService = new JobDataService();

        // ✅ Prepare chatbot question-answers
        answers = new HashMap<>();
        answers.put("selenium experience", "4 years");
        answers.put("total experience", "5 years");
        answers.put("current ctc", "7.67 LPA");
        answers.put("expected ctc", "13 LPA");
        answers.put("notice period", "60 days");
        answers.put("date of birth", "05-10-1998");
        // Add more flexible keys if you anticipate other questions
    }

    @Test(priority = 1)
    public void freshnessFilterSelectTest() {
        jobsearchpage.selectJobFilters("7", "Relevance");
    }

    @Test(priority = 2)
    public void applyToJobsWithMatchingSkillsTest() {
        // ✅ List of keywords to match jobs against
        List<String> keywords = Arrays.asList("Selenium", "Java", "TestNG", "RestAssured");

        // ✅ Create JobApplyFlow with driver, answers and DB service
        JobApplyFlow jobApplyFlow = new JobApplyFlow(driver, answers, jobDataService);

        // Process jobs and collect applied jobs
        List<JobDetails> appliedJobs = jobApplyFlow.processJobsAndApplyIfMatched(keywords);

        // Assert appliedJobs list not null
        Assert.assertNotNull(appliedJobs, "Applied jobs list should not be null");

        // ✅ Log applied jobs
        for (JobDetails job : appliedJobs) {
            System.out.println("✅ Applied Job: " + job.getJobTitle() + " at " + job.getCompanyName() + " | URL: " + job.getJobUrl());
        }
    }
}
