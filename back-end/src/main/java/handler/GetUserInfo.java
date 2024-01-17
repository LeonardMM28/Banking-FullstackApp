package handler;

import com.google.gson.Gson;
import dao.UserDao;
import dto.UserDto;
import handler.AuthFilter.AuthResult;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class GetUserInfo implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        Gson gson = new Gson();

        try {
            AuthResult authResult = AuthFilter.doFilter(request);

            if (!authResult.isLoggedIn) {
                RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(
                        false,
                        null,
                        "User is not authenticated");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("401 Unauthorized")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDao userDao = UserDao.getInstance();
            Document filter = new Document("userName", authResult.userName);
            List<UserDto> users = userDao.query(filter);

            if (users.isEmpty()) {
                RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(
                        false,
                        null,
                        "User not found");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("404 Not Found")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDto user = users.get(0);
            RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(
                    true,
                    List.of(user),
                    "User information retrieved successfully");
            System.out.println(gson.toJson(List.of(user)));

            return responseBuilder.setHeader("Content-Type", "application/json")
                    .setStatus("200 OK")
                    .setVersion("HTTP/1.1")
                    .setBody(response);

        } catch (Exception e) {
            RestApiAppResponse<UserDto> errorResponse = new RestApiAppResponse<>(
                    false,
                    null,
                    "An error occurred: " + e.getMessage());

            return responseBuilder.setHeader("Content-Type", "application/json")
                    .setStatus("500 Internal Server Error")
                    .setVersion("HTTP/1.1")
                    .setBody(errorResponse);
        }
    }
}
