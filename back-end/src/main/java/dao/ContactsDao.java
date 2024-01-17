package dao;

import dto.ContactsDto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactsDao extends BaseDao<ContactsDto>{

    private static ContactsDao instance;
    private ContactsDao(MongoCollection<Document> collection) {
        super(collection);
    }

    public static ContactsDao getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new ContactsDao(MongoConnection.getCollection("ContactsDao"));
        return instance;
    }

    public static ContactsDao getInstance(MongoCollection<Document> collection) {
        instance = new ContactsDao(collection);
        return instance;
    }
    public void insert(Document document) {
        // Insert the document into the collection
        collection.insertOne(document);
    }
    public ContactsDto get(String uniqueId) {
        Document filter = new Document("uniqueId", uniqueId);
        Document document = collection.find(filter).first();
        if (document != null) {
            return ContactsDto.fromDocument(document);
        } else {
            return null;
        }
    }

    public List<ContactsDto> getAll() {
        List<Document> documents = collection.find().into(new ArrayList<>());
        return documents.stream()
                .map(ContactsDto::fromDocument)
                .collect(Collectors.toList());
    }
    public UpdateResult update(Document filter, Document update) {
        return collection.updateOne(filter, new Document("$set", update));
    }

    public void updateFirstName(ContactsDto contacts) {
        // Define the filter to find the contacts by account Id
        Document filter = new Document("accountId", contacts.getAccountId());

        // Define the update operation to set the firstName field
        Document update = new Document("$set", new Document("firstName", contacts.getFirstName()));

        // Perform the update operation
        UpdateResult result = collection.updateOne(filter, update);
    }

    public void updateLastName(ContactsDto contacts) {
        // Define the filter to find the contacts by account Id
        Document filter = new Document("accountId", contacts.getAccountId());

        // Define the update operation to set the lastName field
        Document update = new Document("$set", new Document("lastName", contacts.getLastName()));

        // Perform the update operation
        UpdateResult result = collection.updateOne(filter, update);
    }

    public void updatePhone(ContactsDto contacts) {
        // Define the filter to find the contacts by account Id
        Document filter = new Document("accountId", contacts.getAccountId());

        // Define the update operation to set the phone field
        Document update = new Document("$set", new Document("phone", contacts.getPhone()));

        // Perform the update operation
        UpdateResult result = collection.updateOne(filter, update);
    }

    @Override
    public List<ContactsDto> query(Document filter) {
        List<Document> documentList = collection.find(filter).into(new ArrayList<>());
        return documentList.stream()
                .map(ContactsDto::fromDocument)
                .collect(Collectors.toList());
    }
}
