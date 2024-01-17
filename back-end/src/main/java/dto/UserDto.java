package dto;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto extends BaseDto{

  private String userName;
  private String password;
  private Double balance = 0.0d;
  private Double BTC = 0.0d;
  private Double ETH = 0.0d;
  private Double LTC = 0.0d;
  private Double DOGE = 0.0d;
  private Double XLM = 0.0d;

  private List<TransactionDto> transactions; // Add a field to store transactions


  public UserDto() {
    super();
  }

  public UserDto(String uniqueId) {
    super(uniqueId);
  }

  public String getPassword() {
    return password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public Double getBTC() { return BTC; }

  public void setBTC(Double BTC) { this.BTC = BTC; }

  public Double getETH() { return ETH; }

  public void setETH(Double ETH) { this.ETH = ETH; }

  public Double getLTC() { return LTC; }

  public void setLTC(Double LTC) { this.LTC = LTC; }

  public Double getDOGE() { return DOGE; }

  public void setDOGE(Double DOGE) { this.DOGE = DOGE; }

  public Double getXLM() { return XLM; }

  public void setXLM(Double XLM) { this.XLM = XLM;}

  public List<TransactionDto> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<TransactionDto> transactions) {
    this.transactions = transactions;
  }

  public Document toDocument() {
    Document document = new Document();
    document.append("userName", userName);
    document.append("password", password);
    document.append("balance", balance);
    document.append("BTC", BTC);
    document.append("ETH", ETH);
    document.append("LTC", LTC);
    document.append("DOGE", DOGE);
    document.append("XLM", XLM);
//    document.append("uniqueId", getUniqueId());
    if (transactions != null) {
      List<Document> transactionDocuments = transactions.stream()
              .map(TransactionDto::toDocument)
              .collect(Collectors.toList());
      document.append("transactions", transactionDocuments);
    }
    return document;
  }

  public static UserDto fromDocument(Document match) {
    UserDto userDto = new UserDto(match.getString("uniqueId"));
    userDto.setUserName(match.getString("userName"));
    userDto.setPassword(match.getString("password"));
    userDto.setBalance(match.getDouble("balance"));
    userDto.setBTC(match.getDouble("BTC"));
    userDto.setETH(match.getDouble("ETH"));
    userDto.setLTC(match.getDouble("LTC"));
    userDto.setDOGE(match.getDouble("DOGE"));
    userDto.setXLM(match.getDouble("XLM"));
    // Deserialize transactions from the list of Document (if needed)
    if (match.containsKey("transactions")) {
      List<Document> transactionDocuments = (List<Document>) match.get("transactions");
      List<TransactionDto> transactions = transactionDocuments.stream()
              .map(TransactionDto::fromDocument)
              .collect(Collectors.toList());
      userDto.setTransactions(transactions);
    }
    return userDto;
  }
}
