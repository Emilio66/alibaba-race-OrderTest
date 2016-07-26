package model;

/**
 * Created by zhaoz on 2016/7/26.
 */
public class SumQuery extends Query{
    public String goodId;
    public String key;

    public SumQuery(String goodId, String key){
        this.Type = Query.SUM;
        this.goodId = goodId;
        this.key = key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( SUM_QUERY+" { goodId: "+goodId);
        sb.append(", key: "+key+" }");
        return sb.toString();
    }
}
