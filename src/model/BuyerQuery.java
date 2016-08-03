package model;


/**
 * Created by zhaoz on 2016/7/26.
 */
public class BuyerQuery extends Query{
    public String buyerId;
    public long startTime;
    public long endTime;

    public BuyerQuery(String buyerId, String start, String end, String result){
        this.Type = Query.BUYER;
        this.buyerId = buyerId;
        this.startTime = Long.parseLong(start.trim());
        this.endTime = Long.parseLong(end.trim());
        this.result = result;
    }

    @Override
    public String toString() {
        return "BuyerQuery{" +
                "buyerId='" + buyerId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                super.toString();
    }
/*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( BUYER_TSRANGE_QUERY+" { buyerId: "+buyerId);
        sb.append(", TS Range: "+startTime+" - "+endTime+" }");
        return sb.toString();
    }*/
}
