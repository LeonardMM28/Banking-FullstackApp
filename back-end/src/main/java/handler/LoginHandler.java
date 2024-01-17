package handler;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.AuthDao;
import dao.UserDao;
import dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import request.ParsedRequest;
import org.bson.Document;
import response.CustomHttpResponse;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

class LoginDto {
  String userName;
  String password;
}

public class LoginHandler implements BaseHandler {

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
    HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
    Gson gson = new Gson();

    try {
      LoginDto loginData = gson.fromJson(request.getBody(), LoginDto.class);

      String userName = loginData.userName;
      String password = loginData.password;

      String hashedPassword = DigestUtils.sha256Hex(password);

      UserDao userDao = UserDao.getInstance();
      List<UserDto> users = userDao.query(new Document("userName", userName).append("password", hashedPassword));

      if (users.isEmpty()) {
        RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(false, null, "Invalid username or password");

        return responseBuilder.setHeader("Content-Type", "application/json")
                .setStatus("401 Unauthorized")
                .setVersion("HTTP/1.1")
                .setBody(response);
      } else {
        long expireTime = System.currentTimeMillis() + 360000;

        RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(true, List.of(users.get(0)), "Login successful");

        AuthDao authDao = AuthDao.getInstance();
        authDao.storeAuthToken(userName, hashedPassword, expireTime);

        return responseBuilder.setHeader("Content-Type", "application/json")
                .setHeader("Set-Cookie","auth=" + hashedPassword)
                .setStatus("200 OK")
                .setVersion("HTTP/1.1")
                .setBody(response);
      }
    } catch (Exception e) {
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
