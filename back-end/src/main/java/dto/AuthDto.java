package dto;

import org.bson.Document;

public class AuthDto extends BaseDto{

  private String userName;
  private Long expireTime;
  private String hash;

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public String getUserName() {
    return userName;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public String getHash() {
    return hash;
  }

  @Override
  public Document toDocument() {
    Document document = new Document();
    document.append("userName", userName)
            .append("expireTime", expireTime)
            .append("hash", hash);
    return document;
  }

  public static AuthDto fromDocument(Document document){
    AuthDto authDto = new AuthDto();
    authDto.setUserName(document.getString("userName"));
    authDto.setExpireTime(document.getLong("expireTime"));
    authDto.setHash(document.getString("hash"));
    return authDto;
  }

}