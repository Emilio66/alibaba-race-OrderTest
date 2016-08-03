package model;

/**
 * Created by zhaoz on 2016/7/26.
 */
public abstract class Query {
    /**
     * Query Type Define
     */
    public static final byte ORDER = 0;
    public static final byte BUYER = 1;
    public static final byte GOOD = 2;
    public static final byte SUM = 3;

    public static final String ORDER_QUERY = "CASE:QUERY_ORDER";
    public static final String SUM_QUERY = "CASE:QUERY_GOOD_SUM";
    public static final String GOOD_QUERY = "CASE:QUERY_SALER_GOOD";
    public static final String BUYER_TSRANGE_QUERY = "CASE:QUERY_BUYER_TSRANGE";

    public byte Type;
    public String result;

    @Override
    public String toString() {
        return
                "result size: '" + (result.split("KV:").length-1) + '\'' +
                '}';
    }
}
