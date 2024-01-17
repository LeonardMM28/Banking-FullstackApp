package handler;

import com.google.gson.Gson;
import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import dto.UserDto;
import dto.TransferRequestDto;
import org.bson.Document;
import org.bson.types.ObjectId;
import request.ParsedRequest;
import response.CustomHttpResponse;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransferHandler implements BaseHandler {

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

            TransferRequestDto transferRequest = gson.fromJson(request.getBody(), TransferRequestDto.class);
            System.out.println(gson.toJson(transferRequest));


            if (transferRequest.amount <= 0) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Invalid transfer amount");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDao userDao = UserDao.getInstance();
            List<UserDto> fromUserList = userDao.query(new Document("userName", authResult.userName));
            System.out.println(gson.toJson(fromUserList));

            if (fromUserList.isEmpty()) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "User not found");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("404 Not Found")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDto fromUser = fromUserList.get(0);

            if (fromUser.getBalance() < transferRequest.amount) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Insufficient funds");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("400 Bad Request")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }


            List<UserDto> toUserList = userDao.query(new Document("userName", transferRequest.toId));

            if(!toUserList.isEmpty()){
                toUserList = userDao.query(new Document("userName", transferRequest.toId));
            }else if(toUserList.isEmpty()){
                toUserList =userDao.query(new Document("_id", new ObjectId(transferRequest.toId)));
            }

            System.out.println(gson.toJson(toUserList));

            if (toUserList.isEmpty()) {
                RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "Recipient user not found");

                return responseBuilder.setHeader("Content-Type", "application/json")
                        .setStatus("404 Not Found")
                        .setVersion("HTTP/1.1")
                        .setBody(response);
            }

            UserDto toUser = toUserList.get(0);

            TransactionDto transferTransaction = new TransactionDto();
            transferTransaction.setUserId(fromUser.getUserName());
            transferTransaction.setToId(transferRequest.toId);
            transferTransaction.setAmount(transferRequest.amount);
            transferTransaction.setTransactionType(TransactionType.Transfer);

            TransactionDao transactionDao = TransactionDao.getInstance();
            transactionDao.insert(transferTransaction.toDocument());

            fromUser.setBalance(fromUser.getBalance() - transferRequest.amount);
            toUser.setBalance(toUser.getBalance() + transferRequest.amount);

            if(!toUserList.isEmpty()){
                userDao.update(new Document("userName", authResult.userName), fromUser.toDocument());
                userDao.update(new Document("userName", toUser.getUserName()), toUser.toDocument());
            }else if(toUserList.isEmpty()){
                userDao.update(new Document("userName", authResult.userName), fromUser.toDocument());
                userDao.update(new Document("_id", new ObjectId(transferRequest.toId)), toUser.toDocument());
            }

            // Successful transfer
            RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(true, null, "Transfer was successful");

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
