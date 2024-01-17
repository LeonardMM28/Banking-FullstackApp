package dao;

import com.mongodb.client.MongoCollection;
import dto.AuthDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;
import com.mongodb.client.MongoCursor;


public class AuthDao extends BaseDao<AuthDto> {

  private static AuthDao instance;

  private AuthDao(MongoCollection<Document> collection){
    super(collection);
  }

  public static AuthDao getInstance(){
    if(instance != null){
      return instance;
    }
    instance = new AuthDao(MongoConnection.getCollection("AuthDao"));
    return instance;
  }

  public static AuthDao getInstance(MongoCollection<Document> collection){
    instance = new AuthDao(collection);
    return instance;
  }

  public void storeAuthToken(String userName, String authToken, Long expireTime) {
    // Create a Document to store the authentication token
    Document authDocument = new Document("userName", userName)
            .append("hash", authToken)
            .append("expireTime", expireTime); // Store the expiration time

    // Insert the document into the collection
    try {
      collection.insertOne(authDocument);
    } catch (Exception e) {
      // Handle exceptions (e.g., MongoDB connection issues)
      throw new RuntimeException("Failed to store authentication token", e);
    }
  }

  public void removeAuthToken(String authToken) {
    // Create a filter to find the authentication token
    Document filter = new Document("hash", authToken);

    // Remove the document from the collection
    try {
      collection.deleteOne(filter);
    } catch (Exception e) {
      // Handle exceptions (e.g., MongoDB connection issues)
      throw new RuntimeException("Failed to remove authentication token", e);
    }
  }



  @Override
  public List<AuthDto> query(Document filter) {
    List<AuthDto> authDtoList = new ArrayList<>();
    List<Document> documents = collection.find(filter).into(new ArrayList<>());
    for (Document document : documents) {
      AuthDto authDto = AuthDto.fromDocument(document);
      authDtoList.add(authDto);
    }
    return authDtoList;
  }

}