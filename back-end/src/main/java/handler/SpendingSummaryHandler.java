package handler;
import com.google.gson.Gson;
import dao.TransactionDao;
import dto.SpendingSummaryDto;
import dto.TransactionDto;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class SpendingSummaryHandler implements BaseHandler {
    private static final TransactionDao transactionDao = TransactionDao.getInstance();

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request){
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        Gson gson = new Gson();

        Double spendingSummary = transactionDao.getSpendingSummaryForUser(extractUserId(request));

        SpendingSummaryDto spendingSummaryDto = new SpendingSummaryDto(spendingSummary);
        RestApiAppResponse<SpendingSummaryDto> responseBody = new RestApiAppResponse<>(
                true, List.of(spendingSummaryDto), "Spending summary retrieved successfully");

        responseBuilder.setBody(responseBody);
        return responseBuilder;
    }
    private String extractUserId(ParsedRequest request) {
        return request.getQueryParam("userId");
    }
    private Double calculateSpendingSummary(List<TransactionDto> transactions) {
        return transactions.stream()
                .filter(TransactionDto::isBuy)
                .mapToDouble(TransactionDto::getAmount)
                .sum();
    }
}