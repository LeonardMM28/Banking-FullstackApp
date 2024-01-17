package handler;

import com.google.gson.Gson;
import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyXlmHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
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

            // Parse the JSON request body to get the BTC amount to buy and the BTC price
            Map<String, Double> requestData = gson.fromJson(request.getBody(), Map.class);
            Double xlmAmountToBuy = requestData.get("xlmAmount");
            Double xlmPrice = requestData.get("xlmPrice");

            if (xlmAmountToBuy <= 0 || xlmPrice <= 0) {
                // Handle invalid BTC amount or price
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Invalid XLM amount or price to buy");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            // Get the user from the database
            UserDao userDao = UserDao.getInstance();
            UserDto user = userDao.query(new Document("userName", authResult.userName)).get(0);

            // Calculate the total cost based on the BTC amount and price
            double totalCost = xlmAmountToBuy * xlmPrice;

            // Check if the user has sufficient funds
            if (user.getBalance() < totalCost) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Insufficient funds");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            // Create a buy BTC transaction
            TransactionDto buyTransaction = new TransactionDto();
            buyTransaction.setUserId(authResult.userName);
            buyTransaction.setTransactionType(TransactionType.Buy);
            buyTransaction.setAmount(totalCost);
            buyTransaction.setCryptoType("XLM");  // Assuming BTC is being bought
            buyTransaction.setCryptoPrice(xlmPrice);

            // Insert the transaction into the database
            TransactionDao transactionDao = TransactionDao.getInstance();
            transactionDao.insert(buyTransaction.toDocument());

            // Update user's balance and BTC amount
            user.setBalance(user.getBalance() - totalCost);
            user.setXLM(user.getXLM() + xlmAmountToBuy);

            // Update the user's balance and BTC amount in the database
            userDao.updateUserBalance(user);
            userDao.updateUserXLMAmount(user);

            // Add the transaction to the user's transaction list
            List<TransactionDto> userTransactions = user.getTransactions();
            if (userTransactions == null) {
                userTransactions = new ArrayList<>();
            }
            userTransactions.add(buyTransaction);
            user.setTransactions(userTransactions);

            // Successful BTC purchase
            RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(true, null, "XLM purchase successful");

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
