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
            AuthFilter.AuthResult authResult = AuthFilter.doFilter(request);

            if (!authResult.isLoggedIn) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "User is not authenticated");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("401 Unauthorized")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            Map<String, Double> requestData = gson.fromJson(request.getBody(), Map.class);
            Double xlmAmountToBuy = requestData.get("xlmAmount");
            Double xlmPrice = requestData.get("xlmPrice");

            if (xlmAmountToBuy <= 0 || xlmPrice <= 0) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Invalid XLM amount or price to buy");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDao userDao = UserDao.getInstance();
            UserDto user = userDao.query(new Document("userName", authResult.userName)).get(0);

            double totalCost = xlmAmountToBuy * xlmPrice;

            if (user.getBalance() < totalCost) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Insufficient funds");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            TransactionDto buyTransaction = new TransactionDto();
            buyTransaction.setUserId(authResult.userName);
            buyTransaction.setTransactionType(TransactionType.Buy);
            buyTransaction.setAmount(totalCost);
            buyTransaction.setCryptoType("XLM");
            buyTransaction.setCryptoPrice(xlmPrice);

            TransactionDao transactionDao = TransactionDao.getInstance();
            transactionDao.insert(buyTransaction.toDocument());

            user.setBalance(user.getBalance() - totalCost);
            user.setXLM(user.getXLM() + xlmAmountToBuy);

            userDao.updateUserBalance(user);
            userDao.updateUserXLMAmount(user);

            List<TransactionDto> userTransactions = user.getTransactions();
            if (userTransactions == null) {
                userTransactions = new ArrayList<>();
            }
            userTransactions.add(buyTransaction);
            user.setTransactions(userTransactions);

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
