package handler;

import com.google.gson.Gson;
import dao.ContactsDao;
import dto.ContactsDto;
import org.bson.Document;
import org.bson.types.ObjectId;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class CreateContactsHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        // TODO
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        Gson gson = new Gson();

        try {
            ContactsDto newContacts = gson.fromJson(request.getBody(), ContactsDto.class);

            String uniqueId = ObjectId.get().toString();
            newContacts.setUniqueId(uniqueId);

            ContactsDao contactsDao = ContactsDao.getInstance();
            List<ContactsDto> existingContacts = contactsDao.query(new Document("accountId", newContacts.getAccountId()));

            if (existingContacts.isEmpty()) {
                contactsDao.insert(newContacts.toDocument());

                RestApiAppResponse<ContactsDto> response = new RestApiAppResponse<>(true, null, "Contacts created successfully");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("200 OK")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            } else {
                RestApiAppResponse<ContactsDto> response = new RestApiAppResponse<>(false, null, "Contacts AccountId already taken");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("200 OK")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }
        } catch (Exception e) {
            // Handle any exceptions or errors here
            RestApiAppResponse<ContactsDto> errorResponse = new RestApiAppResponse<>(
                    false,
                    null,
                    "An error occurred: " + e.getMessage()
            );

            return responseBuilder.setHeader("Content-Type", "application/json")
                    .setStatus("500 Internal Server Error")
                    .setVersion("HTTP/1.1")
                    .setBody(errorResponse);
        }
    }
}
