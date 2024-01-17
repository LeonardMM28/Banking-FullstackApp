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

public class SellXlmHandler implements BaseHandler {

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
            Double xlmAmountToSell = requestData.get("xlmAmount");
            Double xlmPrice = requestData.get("xlmPrice");

            if (xlmAmountToSell <= 0 || xlmPrice <= 0) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Invalid XLM amount or price to sell");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDao userDao = UserDao.getInstance();
            UserDto user = userDao.query(new Document("userName", authResult.userName)).get(0);

            double totalCost = xlmAmountToSell * xlmPrice;

            if (user.getXLM() < xlmAmountToSell) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Insufficient XLM");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            TransactionDto sellTransaction = new TransactionDto();
            sellTransaction.setUserId(authResult.userName);
            sellTransaction.setTransactionType(TransactionType.Sell);
            sellTransaction.setAmount(totalCost);
            sellTransaction.setCryptoType("XLM");
            sellTransaction.setCryptoPrice(xlmPrice);

            TransactionDao transactionDao = TransactionDao.getInstance();
            transactionDao.insert(sellTransaction.toDocument());

            user.setBalance(user.getBalance() + totalCost);
            user.setXLM(user.getXLM() - xlmAmountToSell);

            userDao.updateUserBalance(user);
            userDao.updateUserXLMAmount(user);

            List<TransactionDto> userTransactions = user.getTransactions();
            if (userTransactions == null) {
                userTransactions = new ArrayList<>();
            }
            userTransactions.add(sellTransaction);
            user.setTransactions(userTransactions);

            RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(true, null, "XLM sale successful");

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
