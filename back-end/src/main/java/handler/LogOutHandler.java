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
            // Get the authentication token from the cookie
            String authToken = request.getCookieValue("auth");

            // If the authToken is not present, return an error response
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

            // Delete the cookie from the browser by setting an expired date
            responseBuilder.setHeader("Set-Cookie", "auth=; Max-Age=0");

            // Remove the session from the database
            AuthDao authDao = AuthDao.getInstance();
            authDao.removeAuthToken(authToken);

            // Set the "Location" header to the redirect URL
            responseBuilder.setHeader("Location", "/Tresor");

            // Return a successful logout response
            RestApiAppResponse<UserDto> response = new RestApiAppResponse<>(
                    true,
                    null,
                    "Logout successful"
            );

            return responseBuilder.setHeader("Content-Type", "application/json")
                    .setStatus("302 Found") // Use a 302 status code for redirection
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

