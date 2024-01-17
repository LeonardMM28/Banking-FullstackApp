package handler;

import com.google.gson.Gson;
import dao.AuthDao;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

public class LogOutHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();

        try {
            String authToken = request.getCookieValue("auth");

            if (authToken == null) {
                RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(
                        false,
                        null,
                        "User is not logged in"
                );

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("401 Unauthorized")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            responseBuilder.setHeader("Set-Cookie", "auth=; Max-Age=0");

            AuthDao authDao = AuthDao.getInstance();
            authDao.removeAuthToken(authToken);

            responseBuilder.setHeader("Location", "/Tresor");

            RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(
                    true,
                    null,
                    "Logout successful"
            );

            return responseBuilder.setHeader("Content-Type", "application/json")
                    .setStatus("302 Found")
                    .setVersion("HTTP/1.1")
                    .setBody(response);

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

