package handler;

import request.ParsedRequest;

public class HandlerFactory {
    // routes based on the path. Add your custom handlers here
    public static BaseHandler getHandler(ParsedRequest request) {
        return switch (request.getPath()) {
            // todo!
            case "/createUser" -> new CreateUserHandler();
            case "/login" -> new LoginHandler();
            case  "/getTransactions" -> new GetTransactionsHandler();
            case  "/getUser" -> new GetUserInfo();
            case "/createDeposit" -> new CreateDepositHandler();
            case  "/withdraw" -> new WithdrawHandler();
            case "/transfer" -> new TransferHandler();
            case "/logout" -> new LogOutHandler();
            case "/buyBTC" -> new BuyBtcHandler();
            case "/buyDOGE" -> new BuyDogeHandler();
            case "/buyETH" -> new BuyEthHandler();
            case "/buyLTC" -> new BuyLtcHandler();
            case "/buyXLM" -> new BuyXlmHandler();
            case "/sellBTC" -> new SellBtcHandler();
            case "/sellDOGE" -> new SellDogeHandler();
            case "/sellETH" -> new SellEthHandler();
            case "/sellLTC" -> new SellLtcHandler();
            case "/sellXLM" -> new SellXlmHandler();
            default -> new FallbackHandler();
        };
    }

}
