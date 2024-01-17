package dao;

import com.mongodb.client.MongoCollection;
import dto.TransactionDto;
import dto.TransactionType;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionDao extends BaseDao<TransactionDto> {

  private static TransactionDao instance;

  public TransactionDao(MongoCollection<Document> collection) {
    super(collection);
  }

  public static TransactionDao getInstance() {
    if (instance != null) {
      return instance;
    }
    instance = new TransactionDao(MongoConnection.getCollection("TransactionDao"));
    return instance;
  }

  public static TransactionDao getInstance(MongoCollection<Document> collection) {
    instance = new TransactionDao(collection);
    return instance;
  }

  public void put(TransactionDto transactionDto) {
    Document document = transactionDto.toDocument();
    collection.insertOne(document);
  }

  public TransactionDto get(String uniqueId) {
    Document filter = new Document("uniqueId", uniqueId);
    Document document = collection.find(filter).first();
    if (document != null) {
      return TransactionDto.fromDocument(document);
    } else {
      return null;
    }
  }

  public List<TransactionDto> getAll() {
    List<Document> documents = collection.find().into(new ArrayList<>());
    return documents.stream()
            .map(TransactionDto::fromDocument)
            .collect(Collectors.toList());
  }

  public void insert(Document document) {
    collection.insertOne(document);
  }

  public List<TransactionDto> query(Document filter) {
    List<Document> documentList = collection.find(filter).into(new ArrayList<>());
    return documentList.stream()
            .map(TransactionDto::fromDocument)
            .collect(Collectors.toList());
  }

  public List<TransactionDto> getBuyTransactions(){
    Document filter = new Document("transactionType", TransactionType.Buy.toString());
    List<Document> documents = collection.find(filter).into(new ArrayList<>());
    return documents.stream()
            .map(TransactionDto::fromDocument)
            .collect(Collectors.toList());
  }

  public Double getSpendingSummaryForUser(String userId){
    Document filter = new Document("userId", userId)
            .append("transactionType", TransactionType.Buy.toString());
    List<Document> buyTransactions = collection.find(filter).into(new ArrayList<>());
    return buyTransactions.stream()
            .mapToDouble(document -> document.getDouble("amount"))
            .sum();
  }

}
