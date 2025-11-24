package com.qa.jobhunt.model;

import java.util.List;

public class JobDetails {
    private String jobTitle;
    private String companyName;
    private String jobUrl;
    private boolean applied;
    private List<String> jobKeySkills;

    public JobDetails(String jobTitle, String companyName, List<String> jobKeySkills, String jobUrl) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobKeySkills = jobKeySkills;
        this.jobUrl = jobUrl;
        this.applied = false;
    }

    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public List<String> getJobKeySkills() { return jobKeySkills; }
    public String getJobUrl() { return jobUrl; }
    public boolean isApplied() { return applied; }

    public void setApplied(boolean applied) { this.applied = applied; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setJobKeySkills(List<String> jobSkills) { this.jobKeySkills = jobSkills; }
    public void setJobUrl(String currentUrl) { this.jobUrl = currentUrl; }
}
