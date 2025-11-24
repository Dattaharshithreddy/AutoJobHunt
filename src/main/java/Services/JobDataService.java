package Services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.qa.jobhunt.model.JobDetails;

public class JobDataService {

    private final MongoDatabase database;

    public JobDataService() {
        this.database = MongoDBConnection.getDatabase();
    }

    /**
     * Check if a job with the given URL already exists in the appliedJobs collection.
     *
     * @param jobUrl the job URL to check
     * @return true if already applied, false otherwise
     */
    public boolean isJobAlreadyApplied(String jobUrl) {
        MongoCollection<Document> collection = database.getCollection("appliedJobs");
        Document query = new Document("jobUrl", jobUrl);
        boolean exists = collection.find(query).iterator().hasNext();
        if (exists) {
            System.out.println("📌 Job already applied (from DB): " + jobUrl);
        }
        return exists;
    }

    /**
     * Save the applied job's details into the appliedJobs collection.
     *
     * @param job the JobDetails object to save
     */
    public void saveAppliedJob(JobDetails job) {
        MongoCollection<Document> collection = database.getCollection("appliedJobs");
        Document doc = new Document("jobTitle", job.getJobTitle())
                .append("companyName", job.getCompanyName())
                .append("jobUrl", job.getJobUrl())
                .append("applied", job.isApplied())
                .append("jobKeySkills", job.getJobKeySkills())
                .append("appliedOn", new java.util.Date());
        collection.insertOne(doc);
        System.out.println("📦 Job saved to database: " + job.getJobTitle());
    }
}
