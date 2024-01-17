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

public class SellDogeHandler implements BaseHandler {

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
            Double dogeAmountToSell = requestData.get("dogeAmount");
            Double dogePrice = requestData.get("dogePrice");

            if (dogeAmountToSell <= 0 || dogePrice <= 0) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Invalid DOGE amount or price to sell");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDao userDao = UserDao.getInstance();
            UserDto user = userDao.query(new Document("userName", authResult.userName)).get(0);

            double totalCost = dogeAmountToSell * dogePrice;

            if (user.getDOGE() < dogeAmountToSell) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Insufficient DOGE");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            TransactionDto sellTransaction = new TransactionDto();
            sellTransaction.setUserId(authResult.userName);
            sellTransaction.setTransactionType(TransactionType.Sell);
            sellTransaction.setAmount(totalCost);
            sellTransaction.setCryptoType("DOGE");
            sellTransaction.setCryptoPrice(dogePrice);

            TransactionDao transactionDao = TransactionDao.getInstance();
            transactionDao.insert(sellTransaction.toDocument());

            user.setBalance(user.getBalance() + totalCost);
            user.setDOGE(user.getDOGE() - dogeAmountToSell);

            userDao.updateUserBalance(user);
            userDao.updateUserDOGEAmount(user);

            List<TransactionDto> userTransactions = user.getTransactions();
            if (userTransactions == null) {
                userTransactions = new ArrayList<>();
            }
            userTransactions.add(sellTransaction);
            user.setTransactions(userTransactions);

            RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(true, null, "DOGE sale successful");

            return responseBuilder.setHeader("Content-Type", "application/json")
                    .setStatus("200 OK")
                    .setVersion("HTTP/1.1")
                    .setBody(response);

        } catch (Exception e) {
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
