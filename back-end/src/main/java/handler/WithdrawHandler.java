package handler;

import com.google.gson.Gson;
import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.CustomHttpResponse;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WithdrawHandler  implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        //todo
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        Gson gson = new Gson();

        try {
            // Check the user's authentication
            AuthFilter.AuthResult authResult = AuthFilter.doFilter(request);

            if (!authResult.isLoggedIn) {
                // User is not authenticated, return an unauthorized response
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "User is not authenticated");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("401 Unauthorized")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            // Parse the JSON request body to TransactionDto
            TransactionDto transaction = gson.fromJson(request.getBody(), TransactionDto.class);

            if (transaction.getAmount() <= 0) {
                // Handle invalid deposit amount
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Invalid amount to withdraw");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            // Create a withd transaction
            transaction.setUserId(authResult.userName); // Assuming that userName is the userId
            transaction.setTransactionType(TransactionType.Withdraw);

            // Insert the with into the database
            TransactionDao transactionDao = TransactionDao.getInstance();
            transactionDao.insert(transaction.toDocument());

            // Update the user's balance
            UserDao userDao = UserDao.getInstance();
            UserDto user = userDao.query(new Document("userName", authResult.userName)).get(0);

            if(user.getBalance() < transaction.getAmount()){
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Insufficient funds");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            user.setBalance(user.getBalance() - transaction.getAmount());

            // Update the user's balance in the database
            userDao.update(new Document("userName", authResult.userName), user.toDocument());

            List<TransactionDto> userTransactions = user.getTransactions();
            if (userTransactions == null) {
                userTransactions = new ArrayList<>();
            }
            userTransactions.add(transaction);
            user.setTransactions(userTransactions);

            // Successful deposit
            RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(true, null, "Withdraw was made successfully");

            return responseBuilder.setHeader("Content-Type", "application/json")
                    .setStatus("200 OK")
                    .setVersion("HTTP/1.1")
                    .setBody(response);
        } catch (Exception e) {
            // Handle any exceptions or errors here
            RestApiAppResponse<TransactionDto> errorResponse = new RestApiAppResponse<>(
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
