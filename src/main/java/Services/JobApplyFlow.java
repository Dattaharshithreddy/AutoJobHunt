package Services;

import java.time.Duration;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.qa.jobhunt.model.JobDetails;
import com.qa.jobhunt.pages.JobDetailsPage;
import com.qa.jobhunt.pages.JobSearchPage;

public class JobApplyFlow {
    WebDriver driver;
    WebDriverWait wait;
    JobDataService jobDataService;

    private static final Set<String> EXCLUDED_COMPANIES = new HashSet<>(Arrays.asList(
        "cigniti", "coforge", "capgemini"
    ));

    public JobApplyFlow(WebDriver driver, Map<String, String> answers, JobDataService jobDataService) {
        this.driver = driver;
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.jobDataService = jobDataService;
    }

    public List<JobDetails> processJobsAndApplyIfMatched(List<String> keywords) {
        JobSearchPage searchPage = new JobSearchPage(driver);
        List<JobDetails> appliedJobs = new ArrayList<>();
        String originalWindow = driver.getWindowHandle();

        driver.switchTo().window(originalWindow);
        waitForJobCardsToBeReady(searchPage);

        while (true) {
            waitForJobCardsToBeReady(searchPage);
            List<WebElement> jobCards = searchPage.getJobCards();

            if (jobCards.isEmpty()) {
                System.out.println("⚠️ No job cards found on this page.");
                break;
            }

            for (int i = 0; i < jobCards.size(); i++) {
                try {
                    waitForJobCardsToBeReady(searchPage);
                    jobCards = searchPage.getJobCards();
                    WebElement jobCard = jobCards.get(i);
                    wait.until(ExpectedConditions.elementToBeClickable(jobCard));
                    jobCard.click();

                    switchToNewTab(originalWindow);

                    JobDetailsPage jobDetailsPage = new JobDetailsPage(driver);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(jobDetailsPage.getJobTitleLocator()));
                    jobDetailsPage.scrollToSkillsSection();

                    String jobTitle = jobDetailsPage.getJobTitle();
                    List<String> keySkills = jobDetailsPage.getKeySkills();
                    String companyName = jobDetailsPage.getCompanyName();
                    String jobUrl = driver.getCurrentUrl();

                    System.out.println("Job: " + jobTitle + " | " + companyName + " | " + jobUrl);

                    String companyLower = companyName.toLowerCase().trim();
                    if (EXCLUDED_COMPANIES.contains(companyLower)) {
                        System.out.println("⚠️ Skipping job from excluded company: " + companyName);
                        driver.close();
                        driver.switchTo().window(originalWindow);
                        continue;
                    }

                    boolean matched = isSkillMatch(keySkills, keywords);
                    if (matched) {
                        WebElement applyButton = driver.findElement(jobDetailsPage.getApplyButtonLocator());
                        String btnText = applyButton.getText().trim();

                        if (!btnText.equalsIgnoreCase("Apply")) {
                            System.out.println("⚠️ Skipping due to apply button text: " + btnText);
                            driver.close();
                            driver.switchTo().window(originalWindow);
                            continue;
                        }

                        System.out.println("✅ Matched and Apply button found — Applying...");
                        applyButton.click();
                        Thread.sleep(2500);

                        handleChatbotPopup();

                        JobDetails job = new JobDetails(jobTitle, companyName, keySkills, jobUrl);
                        job.setApplied(true);
                        appliedJobs.add(job);

                        jobDataService.saveAppliedJob(job);
                        System.out.println("✔️ Applied to job: " + jobTitle);

                        driver.close();
                        driver.switchTo().window(originalWindow);
                    } else {
                        System.out.println("❌ No skill match found.");
                        driver.close();
                        driver.switchTo().window(originalWindow);
                    }

                } catch (NoSuchSessionException | NoSuchWindowException e) {
                    System.out.println("⚠️ Session or window invalidated: " + e.getMessage());
                    break;  // Stop processing if session/window lost
                } catch (Exception e) {
                    System.out.println("⚠️ Error processing job at index " + i + ": " + e.getMessage());
                    closeOtherTabs(originalWindow);
                    try {
                        driver.switchTo().window(originalWindow);
                    } catch (Exception ignored) {}
                }
            }

            if (searchPage.isNextButtonVisible()) {
                System.out.println("➡️ Moving to next page...");
                searchPage.clickNextButton();
                waitForJobCardsToBeReady(searchPage);
            } else {
                System.out.println("🏁 No more pages found. Ending job search.");
                break;
            }
        }

        System.out.println("🎯 Job hunt completed. Total applied: " + appliedJobs.size());
        return appliedJobs;
    }

    private boolean isSkillMatch(List<String> keySkills, List<String> keywords) {
        for (String skill : keySkills) {
            String normSkill = normalize(skill);
            List<String> skillTokens = Arrays.asList(normSkill.split(" "));

            for (String keyword : keywords) {
                String normKeyword = normalize(keyword);
                List<String> keywordTokens = Arrays.asList(normKeyword.split(" "));

                if (normSkill.contains(normKeyword) || normKeyword.contains(normSkill)) {
                    return true;
                }

                for (String token : skillTokens) {
                    if (keywordTokens.contains(token)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String normalize(String s) {
        return s == null ? "" : s.toLowerCase().replaceAll("[^a-z0-9 ]", " ").replaceAll("\\s+", " ").trim();
    }

    private void waitForJobCardsToBeReady(JobSearchPage searchPage) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loader, .styles_loader__spinner, [data-testid='loader']")));
            wait.until(ExpectedConditions.presenceOfElementLocated(searchPage.getJobCardsLocator()));

            List<WebElement> cards = driver.findElements(searchPage.getJobCardsLocator());
            if (!cards.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(cards.get(0)));
            }
        } catch (Exception e) {
            System.out.println("⚠️ Waiting for job cards failed or no cards found: " + e.getMessage());
        }
    }

    private void switchToNewTab(String originalWindowHandle) {
        try {
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            Set<String> allWindows = driver.getWindowHandles();
            for (String windowHandle : allWindows) {
                if (!originalWindowHandle.equals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    return;
                }
            }
        } catch (TimeoutException ex) {
            System.out.println("Timeout waiting for new tab.");
        } catch (NoSuchSessionException | NoSuchWindowException e) {
            System.out.println("Window or session invalid when switching tabs: " + e.getMessage());
        }
    }

    private void closeOtherTabs(String originalWindow) {
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                if (!window.equals(originalWindow)) {
                    try {
                        driver.switchTo().window(window);
                        driver.close();
                    } catch (NoSuchSessionException | NoSuchWindowException e) {
                        System.out.println("Session/window already closed or invalid: " + e.getMessage());
                    }
                }
            }
            try {
                driver.switchTo().window(originalWindow);
            } catch (NoSuchSessionException | NoSuchWindowException e) {
                System.out.println("Cannot switch to original window, session may be closed: " + e.getMessage());
            }
        } catch (NoSuchSessionException e) {
            System.out.println("Session is invalid, skipping window closing: " + e.getMessage());
        }
    }


    private void handleChatbotPopup() {
        try {
            Thread.sleep(2000);  // Wait for popup

            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            for (WebElement iframe : iframes) {
                try {
                    driver.switchTo().frame(iframe);

                    List<WebElement> questions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(".botMsg div span")));

                    if (!questions.isEmpty()) {
                        for (WebElement questionElem : questions) {
                            String question = questionElem.getText().toLowerCase().trim();
                            System.out.println("📝 Bot asks: " + question);

                            String answer = getAnswerFor(question);
                            if (answer != null) {
                            	WebElement chatInput = wait.until(ExpectedConditions.elementToBeClickable(
                            			   By.cssSelector("div.textArea[contenteditable='true'][data-placeholder]")
                            			));
                            			JavascriptExecutor js = (JavascriptExecutor) driver;

                            			// Clear and set the answer text
                            			js.executeScript("arguments[0].innerText = arguments[1];", chatInput, answer);

                            			// Dispatch 'input' event for UI to register change
                            			js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", chatInput);

                            			Thread.sleep(200); // Optionally allow a short UI update wait

                            			// Click input to ensure focus (sometimes helps)
                            			chatInput.click();
                            			chatInput.sendKeys(Keys.ENTER);

                            			// Click the Save/Send button (use type='button' OR text if needed)
                            			WebElement sendBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            			   By.xpath("//button[contains(.,'Save')]"))
                            			);
                            			sendBtn.click();


                                System.out.println("✅ Answered: " + answer);
                                Thread.sleep(800);
                            } else {
                                System.out.println("⚠️ No matching answer for: " + question);
                            }
                        }
                        driver.switchTo().defaultContent();
                        return;
                    }
                    driver.switchTo().defaultContent();
                } catch (Exception e) {
                    driver.switchTo().defaultContent();
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Chatbot popup not found or error: " + e.getMessage());
        }
    }

    private boolean isElementVisibleViaJS(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var elem=arguments[0];" +
                        "var style = window.getComputedStyle(elem);" +
                        "return (style && style.display !== 'none' && style.visibility !== 'hidden' && style.opacity !== '0');";
        return (Boolean) js.executeScript(script, element);
    }

    private String getAnswerFor(String question) {
        if (question.contains("selenium") && question.contains("experience")) {
            return "3.10 years";
        } else if (question.contains("total") && question.contains("experience")) {
            return "4.4 years";
        } else if (question.contains("current") && (question.contains("ctc") || question.contains("package"))) {
            return "7.67 LPA";
        } else if (question.contains("expected") && (question.contains("ctc") || question.contains("package"))) {
            return "14 LPA";
        } else if (question.contains("notice") && question.contains("period")) {
            return "90 days";
        } else if (question.contains("date of birth") || question.contains("dob")) {
            return "05-10-1998";
        }
        return null;
    }
}
