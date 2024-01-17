package dto;

import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;


public class ContactsDto extends BaseDto{
    private String firstName;
    private String lastName;
    private String phone;
    private String accountId;

    public ContactsDto(){
        super(); //calls the super class constuctor 'baseDto'
    }

    public ContactsDto(String uniqueId, String firstName, String lastName, String phone, String accountId){
        super(uniqueId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.accountId = accountId;
    }

    public ContactsDto(String uniqueId) {
        super(uniqueId);
    }

    public String getFirstName(){
        return firstName;
    }


    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }


    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getPhone(){
        return phone;
    }


    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getAccountId(){
        return accountId;
    }


    public void setAccountId(String accountId){
        this.accountId = accountId;
    }
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("firstName", firstName);
        document.append("lastName", lastName);
        document.append("phone", phone);
        document.append("accountId", accountId);
        document.append("uniqueId", getUniqueId());
        return document;
    }

    public static ContactsDto fromDocument(Document document) {
        ContactsDto contactsDto = new ContactsDto(document.getString("uniqueId"));
        contactsDto.setFirstName(document.getString("firstName"));
        contactsDto.setLastName(document.getString("lastName"));
        contactsDto.setPhone(document.getString("phone"));
        contactsDto.setAccountId(document.getString("accountId"));
        return contactsDto;
    }
}
//working on it
