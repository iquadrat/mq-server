package org.povworld.mq;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import io.prometheus.client.Gauge;

public class BitcoinMetrics {
    
    static final Gauge priceUsd =
            Gauge.build().namespace("mq").name("btcusd_price").help("The bitcoin price in USD").create().setChild(new Gauge.Child() {
                @Override
                public double get() {
                    try {
                        return fetchBitcoinUsd();
                    } catch (Exception e) {
                        return Double.NaN;
                    }
                }
            }).register();
    
    static final Gauge totalBidUsd =
            Gauge.build().namespace("mq").name("btcusd_total_bid").help("Total USD in bids.").create().setChild(new Gauge.Child() {
                @Override
                public double get() {
                    try {
                        return fetchBitcoinUsdTotalBids();
                    } catch (Exception e) {
                        return Double.NaN;
                    }
                }
            }).register();
    
    static final Gauge totalAskUsd =
            Gauge.build().namespace("mq").name("btcusd_total_ask").help("Total BTC in asks.").create().setChild(new Gauge.Child() {
                @Override
                public double get() {
                    try {
                        return fetchBitcoinUsdTotalAsks();
                    } catch (Exception e) {
                        return Double.NaN;
                    }
                }
            }).register();
    
    public static double fetchBitcoinUsd() throws IOException, URISyntaxException {
        JSONObject o = new JSONObject(HttpUtil.get("https://www.bitstamp.net/api/v2/ticker/btcusd/"));
        return o.getDouble("last");
    }
    
    public static double fetchBitcoinUsdTotalBids() throws IOException, URISyntaxException {
        JSONObject o = new JSONObject(HttpUtil.get("https://www.bitstamp.net/api/v2/order_book/btcusd/"));
        double totalSize = 0;
        for (Object x: o.getJSONArray("bids")) {
            JSONArray pair = (JSONArray)x;
            double size = pair.getDouble(0) * pair.getDouble(1);
            totalSize += size;
        }
        return totalSize;
    }
    
    public static double fetchBitcoinUsdTotalAsks() throws IOException, URISyntaxException {
        JSONObject o = new JSONObject(HttpUtil.get("https://www.bitstamp.net/api/v2/order_book/btcusd/"));
        double totalSize = 0;
        for (Object x: o.getJSONArray("asks")) {
            JSONArray pair = (JSONArray)x;
            double size = pair.getDouble(1);
            totalSize += size;
        }
        return totalSize;
    }
    
    public static void registerMetrics() {}
    
    private BitcoinMetrics() {}
    
}
