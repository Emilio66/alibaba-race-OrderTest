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

    public GoodQuery(String goodId, String[] keys){
        this.Type = Query.GOOD;
        this.goodId = goodId;
        this.keys = Arrays.asList(keys);
        this.salerId = "";
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( GOOD_QUERY+" { goodId: "+goodId);
        sb.append(", keys: "+keys+" }");
        return sb.toString();
    }
}
