package dto;

import org.bson.Document;

import java.time.Instant;

public class TransactionDto extends BaseDto {

  private String userId;
  private String toId;
  private Double amount;
  private TransactionType transactionType;
  private Long timestamp;
  private String cryptoType;  // Add this field for the cryptocurrency type
  private Double cryptoPrice; // Add this field for the cryptocurrency price

  public TransactionDto() {
    timestamp = Instant.now().toEpochMilli();
  }

  public TransactionDto(String uniqueId) {
    super(uniqueId);
    timestamp = Instant.now().toEpochMilli();
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getToId() {
    return toId;
  }

  public void setToId(String toId) {
    this.toId = toId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }
  public String getCryptoType() {
    return cryptoType;
  }

  public void setCryptoType(String cryptoType) {
    this.cryptoType = cryptoType;
  }

  public Double getCryptoPrice() {
    return cryptoPrice;
  }

  public void setCryptoPrice(Double cryptoPrice) {
    this.cryptoPrice = cryptoPrice;
  }

  public Document toDocument() {
    Document document = new Document();
    document.append("userId", userId);
    document.append("toId", toId);
    document.append("amount", amount);
    document.append("transactionType", transactionType.toString());
    document.append("timestamp", timestamp);
    document.append("cryptoType", cryptoType);   // Include cryptoType in the document
    document.append("cryptoPrice", cryptoPrice); // Include cryptoPrice in the document
    document.append("uniqueId", getUniqueId());
    return document;
  }

  public static TransactionDto fromDocument(Document document) {
    TransactionDto transactionDto = new TransactionDto(document.getString("uniqueId"));
    transactionDto.setUserId(document.getString("userId"));
    transactionDto.setToId(document.getString("toId"));
    transactionDto.setAmount(document.getDouble("amount"));
    transactionDto.setTransactionType(TransactionType.valueOf(document.getString("transactionType")));
    transactionDto.setTimestamp(document.getLong("timestamp"));
    transactionDto.setCryptoType(document.getString("cryptoType"));   // Retrieve cryptoType from the document
    transactionDto.setCryptoPrice(document.getDouble("cryptoPrice")); // Retrieve cryptoPrice from the document
    return transactionDto;
  }
}