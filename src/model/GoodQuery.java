package model;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by zhaoz on 2016/7/26.
 */
public class GoodQuery extends Query{
    public String goodId;
    public String salerId; //pointless
    public Collection<String> keys;

    public GoodQuery(String goodId, String[] keys, String result){
        this.Type = Query.GOOD;
        this.goodId = goodId;
        this.keys = Arrays.asList(keys);
        this.salerId = "";
        this.result = result;
    }

    @Override
    public String toString() {
        return "GoodQuery{" +
                "goodId='" + goodId + '\'' +
                ", salerId='" + salerId + '\'' +
                ", keys=" + keys +
                " " + super.toString();
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( GOOD_QUERY+" { goodId: "+goodId);
        sb.append(", keys: "+keys+" }");
        sb.append("")
        return sb.toString();
    }*/
}
