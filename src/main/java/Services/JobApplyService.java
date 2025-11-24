package Services;

import java.util.List;
import com.qa.jobhunt.model.JobDetails;

public class JobApplyService {

    private JobApplyFlow jobApplyFlow;

    public JobApplyService(JobApplyFlow jobApplyFlow) {
        this.jobApplyFlow = jobApplyFlow;
    }

    public void searchAndApplyJobs(List<String> keywords) {
        List<JobDetails> appliedJobs = jobApplyFlow.processJobsAndApplyIfMatched(keywords);

        for (JobDetails job : appliedJobs) {
            System.out.println("📌 Applied Job: " + job.getJobTitle() + " | " + job.getJobUrl());
        }

        System.out.println("🎯 Finished processing job applications. Total applied: " + appliedJobs.size());
    }
}
