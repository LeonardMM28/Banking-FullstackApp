package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import dto.UserDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao extends BaseDao<UserDto> {

  private static UserDao instance;

  private UserDao(MongoCollection<Document> collection) {
    super(collection);
  }

  public static UserDao getInstance() {
    if (instance != null) {
      return instance;
    }
    instance = new UserDao(MongoConnection.getCollection("UserDao"));
    return instance;
  }

  public static UserDao getInstance(MongoCollection<Document> collection) {
    instance = new UserDao(collection);
    return instance;
  }

  public void insert(Document document) {
    // Insert the document into the collection
    collection.insertOne(document);
  }

  public UpdateResult update(Document filter, Document update) {
    return collection.updateOne(filter, new Document("$set", update));
  }

  public void updateUserBalance(UserDto user) {
    // Define the filter to find the user by userName
    Document filter = new Document("userName", user.getUserName());

    // Define the update operation to set the balance field
    Document update = new Document("$set", new Document("balance", user.getBalance()));

    // Perform the update operation
    UpdateResult result = collection.updateOne(filter, update);
  }

  // New method to update cryptocurrency amounts
  public void updateUserBTCAmount(UserDto user) {
    // Define the filter to find the user by userName
    Document filter = new Document("userName", user.getUserName());

    // Define the update operation to set the balance field
    Document update = new Document("$set", new Document("BTC", user.getBTC()));

    // Perform the update operation
    UpdateResult result = collection.updateOne(filter, update);
  }

  public void updateUserDOGEAmount(UserDto user) {
    Document filter = new Document("userName", user.getUserName());

    Document update = new Document("$set", new Document("DOGE", user.getDOGE()));

    UpdateResult result = collection.updateOne(filter, update);
  }

  public void updateUserETHAmount(UserDto user) {
    Document filter = new Document("userName", user.getUserName());

    Document update = new Document("$set", new Document("ETH", user.getETH()));

    UpdateResult result = collection.updateOne(filter, update);
  }

  public void updateUserLTCAmount(UserDto user) {
    Document filter = new Document("userName", user.getUserName());

    Document update = new Document("$set", new Document("LTC", user.getLTC()));

    UpdateResult result = collection.updateOne(filter, update);
  }

  public void updateUserXLMAmount(UserDto user) {
    Document filter = new Document("userName", user.getUserName());

    Document update = new Document("$set", new Document("XLM", user.getXLM()));

    UpdateResult result = collection.updateOne(filter, update);
  }


  public List<UserDto> query(Document filter) {
    // Todo
    List<UserDto> userDtos = new ArrayList<>();
    List<Document> documents = collection.find(filter).into(new ArrayList<>());

    for (Document document : documents) {
      UserDto userDto = UserDto.fromDocument(document);
      userDtos.add(userDto);
    }
    return userDtos;
  }
}
