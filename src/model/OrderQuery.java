package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by zhaoz on 2016/7/26.
 */
public class OrderQuery extends Query{
    public long orderId;
    public Collection<String> keys;

    public OrderQuery(String orderId, String [] keys, String result){
        this.Type = Query.ORDER;
        this.orderId = Long.parseLong(orderId.trim());
        this.keys = Arrays.asList(keys);
        this.result = result;
    }

    @Override
    public String toString() {
        return "OrderQuery{" +
                "orderId=" + orderId +
                ", keys=" + keys +
                ", result='" + result + '\'' +
                '}';
    }
}
