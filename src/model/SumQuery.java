package model;

/**
 * Created by zhaoz on 2016/7/26.
 */
public class SumQuery extends Query{
    public String goodId;
    public String key;

    public SumQuery(String goodId, String key, String result){
        this.Type = Query.SUM;
        this.goodId = goodId;
        this.key = key;
        this.result = result;
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( SUM_QUERY+" { goodId: "+goodId);
        sb.append(", key: "+key+" }");
        return sb.toString();
    }*/

    @Override
    public String toString() {
        return "SumQuery{" +
                "goodId='" + goodId + '\'' +
                ", key='" + key + '\''
               + result;
    }
}
