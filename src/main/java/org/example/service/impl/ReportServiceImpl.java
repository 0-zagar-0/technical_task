package org.example.service.impl;

import org.example.service.ReportService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportServiceImpl implements ReportService {
    private static final int INDEX_OPERATION = 0;
    private static final int INDEX_PRICE = 1;
    private static final int INDEX_SIZE = 2;
    private static final int INDEX_ORDER = 3;
    private static final String O_OPERATION = "o";
    private static final String Q_OPERATION = "q";
    private static final String U_OPERATION = "u";
    private static final String BID_ORDER = "bid";
    private static final String ASK_ORDER = "ask";
    private static final String BEST_BID = "best_bid";
    private static final String BEST_ASK = "best_ask";
    private static final String SIZE = "size";
    private static final String COMMA = ",";
    private static final String BUY = "buy";
    private static final String SELL = "sell";

    @Override
    public List<String> reportData(List<String> operations) {
        List<String> report = new ArrayList<>();
        List<String> allOperation = new ArrayList<>();
        String bestBid = null;
        String bestAsk = null;

        for (String operation : operations) {
            String[] operationParse = operation.split(COMMA);
            allOperation.add(addAllOperation(operationParse).get());

            bestBid = initializeBid(operationParse, bestBid);
            bestBid = oSellOperation(operationParse, bestBid);
            bestBid = checkBestBid(operationParse, bestBid);

            bestAsk = initializeAsk(operationParse, bestAsk);
            bestAsk = oBuyOperation(operationParse, bestAsk);
            bestAsk = checkBestAsk(operationParse, bestAsk);

            findBestBidFromAllOperation(allOperation, bestBid, bestAsk);

            Optional<String> data = getData(allOperation, operationParse, bestBid, bestAsk);
            data.ifPresent(report::add);
        }
        return report;
    }

    private String oSellOperation(String[] operation, String bestBid) {
        if (operation[INDEX_OPERATION].equals(O_OPERATION) && operation[1].equals(SELL)) {
            String[] bid = bestBid.split(COMMA);
            int result = Integer.parseInt(bid[1]) - Integer.parseInt(operation[2]);
            bid[1] = String.valueOf(result);
            return String.join(COMMA, bid[0], bid[1]);
        }
        return bestBid;
    }

    private void findBestBidFromAllOperation(List<String> allOperations, String bestBid, String bestAsk) {
        for (int i = 0; i < allOperations.size(); i++) {
            if (bestBid != null && allOperations.get(i).split(COMMA)[0].equals(bestBid.split(COMMA)[0])) {
                allOperations.set(i, bestBid);
            }
            if (bestAsk != null && allOperations.get(i).split(COMMA)[0].equals(bestAsk.split(COMMA)[0])) {
                allOperations.set(i, bestAsk);
            }
        }
    }

    private String oBuyOperation(String[] operation, String bestAsk) {
        if (operation[INDEX_OPERATION].equals(O_OPERATION) && operation[1].equals(BUY)) {
            String[] bid = bestAsk.split(COMMA);
            int result = Integer.parseInt(bid[1]) - Integer.parseInt(operation[2]);
            bid[1] = String.valueOf(result);
            return String.join(COMMA, bid[0], bid[1]);
        }
        return bestAsk;
    }

    private Optional<String> addAllOperation(String[] operations) {
        String operation = "";
        if (operations[INDEX_OPERATION].equals(U_OPERATION)) {
            operation = String.join(COMMA, operations[INDEX_PRICE], operations[INDEX_SIZE]);
        }
        return Optional.of(operation);
    }

    private String initializeBid(String[] operation, String bestBid) {
        String bid;
        if (bestBid == null
                && operation[INDEX_OPERATION].equals(U_OPERATION)
                && operation[INDEX_ORDER].equals(BID_ORDER)) {
            bid = String.join(COMMA, operation[INDEX_PRICE], operation[INDEX_SIZE]);
            return bid;
        }
        return bestBid;
    }

    private String initializeAsk(String[] operation, String bestAsk) {
        String ask;
        if (bestAsk == null
                && operation[INDEX_OPERATION].equals(U_OPERATION)
                && operation[INDEX_ORDER].equals(ASK_ORDER)) {
            ask = String.join(COMMA, operation[INDEX_PRICE], operation[INDEX_SIZE]);
            return ask;
        }
        return bestAsk;
    }

    private String checkBestBid(String[] operation, String bestBid) {
        String bid;
        if (operation[INDEX_OPERATION].equals(U_OPERATION)
                && operation[3].equals(BID_ORDER)
                && Integer.parseInt(operation[INDEX_SIZE]) != 0
                && Integer.parseInt(operation[INDEX_PRICE])
                > Integer.parseInt(bestBid.split(COMMA)[0])) {

            bid = String.join(COMMA, operation[INDEX_PRICE], operation[INDEX_SIZE]);
            return bid;
        }
        return bestBid;

    }

    private String checkBestAsk(String[] operation, String bestAsk) {
        String ask;
        if (operation[INDEX_OPERATION].equals(U_OPERATION)
                && operation[3].equals("ask")
                && Integer.parseInt(operation[INDEX_SIZE]) != 0
                && Integer.parseInt(operation[INDEX_PRICE])
                > Integer.parseInt(bestAsk.split(COMMA)[0])) {

            ask = String.join(COMMA, operation[INDEX_PRICE], operation[INDEX_SIZE]);
            return ask;
        }
        return bestAsk;
    }


    private Optional<String> getData(List<String> allOperations,
                                     String[] operation,
                                     String bestBid,
                                     String bestAsk) {
        if (operation[INDEX_OPERATION].equals(Q_OPERATION)
                && operation[1].equals(BEST_BID)) {
            return Optional.ofNullable(bestBid);
        } else if (operation[INDEX_OPERATION].equals(Q_OPERATION)
                && operation[1].equals(BEST_ASK)) {
            return Optional.ofNullable(bestAsk);
        } else if (operation[INDEX_OPERATION].equals(Q_OPERATION)
                && operation[1].equals(SIZE)) {
            return Optional.ofNullable(getSizeFromPrice(allOperations, operation[2]));
        }
        return Optional.empty();
    }

    private String getSizeFromPrice(List<String> operations, String price) {
        for (String op : operations) {
            String[] operation = op.split(COMMA);

            if (operation[0].equals(price)) {
                return operation[1];
            }
        }
        return null;
    }
}
