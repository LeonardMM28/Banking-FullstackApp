package handler;

import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

public interface BaseHandler {

    HttpResponseBuilder handleRequest(ParsedRequest request);
}
