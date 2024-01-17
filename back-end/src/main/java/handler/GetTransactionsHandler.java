package handler;

import com.google.gson.Gson;
import dao.TransactionDao;
import dto.TransactionDto;
import handler.AuthFilter.AuthResult;
import org.bson.Document;
import request.ParsedRequest;
import response.CustomHttpResponse;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetTransactionsHandler implements BaseHandler {

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
    HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
    Gson gson = new Gson();

    try {
      AuthResult authResult = AuthFilter.doFilter(request);

      if (!authResult.isLoggedIn) {
        RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(false, null, "User is not authenticated");

        return responseBuilder.setHeader("Content-Type", "application/json")
                .setStatus("401 Unauthorized")
                .setVersion("HTTP/1.1")
                .setBody(response);
      }

      TransactionDao transactionDao = TransactionDao.getInstance();
      List<TransactionDto> transactions = transactionDao.query(new Document("userId", authResult.userName));

      RestApiAppResponse<TransactionDto> response = new RestApiAppResponse<>(true, transactions, "Transactions retrieved successfully");

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

