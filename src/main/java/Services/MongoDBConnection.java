package Services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBConnection {
    private static MongoClient mongoClientInstance;
    private static MongoDatabase databaseInstance;
    private static final String CONNECTION_STRING = "mongodb://localhost:27017/";
    private static final String DATABASE_NAME = "jobAutoApply";

    private MongoDBConnection() {}

    public static synchronized MongoClient getMongoClient() {
        if (mongoClientInstance == null) {
            CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
            CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

            ConnectionString connString = new ConnectionString(CONNECTION_STRING);
            MongoClientSettings clientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .codecRegistry(codecRegistry)
                    .build();

            mongoClientInstance = MongoClients.create(clientSettings);
            System.out.println("✅ Connected to MongoDB.");
        }
        return mongoClientInstance;
    }

    public static synchronized MongoDatabase getDatabase() {
        if (databaseInstance == null) {
            databaseInstance = getMongoClient().getDatabase(DATABASE_NAME);
            System.out.println("✅ Accessed database: " + DATABASE_NAME);
        }
        return databaseInstance;
    }

    public static synchronized void closeConnection() {
        if (mongoClientInstance != null) {
            mongoClientInstance.close();
            mongoClientInstance = null;
            databaseInstance = null;
            System.out.println("✅ MongoDB connection closed.");
        }
    }
}
