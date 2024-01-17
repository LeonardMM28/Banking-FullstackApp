package handler;

import com.mongodb.client.MongoCollection;
import dao.MongoConnection;
import dao.UserDao;
import dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import request.ParsedRequest;
import org.bson.Document;
import response.CustomHttpResponse;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class CreateUserHandler implements BaseHandler {

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
    // TODO
    HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
    Gson gson = new Gson();

    try {
      UserDto newUser = gson.fromJson(request.getBody(), UserDto.class);

      String uniqueId = ObjectId.get().toString();
      newUser.setUniqueId(uniqueId);

      newUser.setBTC(0.0d);
      newUser.setETH(0.0d);
      newUser.setLTC(0.0d);
      newUser.setDOGE(0.0d);
      newUser.setXLM(0.0d);

      newUser.setPassword(DigestUtils.sha256Hex(newUser.getPassword()));

      UserDao userDao = UserDao.getInstance();
      List<UserDto> existingUsers = userDao.query(new Document("userName", newUser.getUserName()));

      if (existingUsers.isEmpty()) {
        userDao.insert(newUser.toDocument());

        RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(true, null, "User created successfully");

        return responseBuilder.setHeader("Content-Type", "application/json")
                .setStatus("200 OK")
                .setVersion("HTTP/1.1")
                .setBody(response);
      } else {
        RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(false, null, "Username already taken");

        return responseBuilder.setHeader("Content-Type", "application/json")
                .setStatus("200 OK")
                .setVersion("HTTP/1.1")
                .setBody(response);
      }
    } catch (Exception e) {
      // Handle any exceptions or errors here
      RestApiAppResponse<UserDto> errorResponse = new RestApiAppResponse<>(
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