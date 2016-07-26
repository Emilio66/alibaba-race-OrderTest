import com.alibaba.middleware.race.util.FastReader;
import model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoz on 2016/7/26.
 * Parsing case file & generate corresponding query
 */
public class Parser {
    public static final String ORDER_QUERY = "CASE:QUERY_ORDER";
    public static final String SUM_QUERY = "CASE:QUERY_GOOD_SUM";
    public static final String GOOD_QUERY = "CASE:QUERY_SALER_GOOD";
    public static final String BUYER_TSRANGE_QUERY = "CASE:QUERY_BUYER_TSRANGE";

    private static final int BUFF_SIZE = 4096;
    private FastReader reader;

    public Parser(File file, long startPos) {
        this.reader = new FastReader(file, startPos, BUFF_SIZE);
        System.out.println("New Parser: "+file.getName()+", start "+startPos);
    }

    public Parser(File file) {
        this(file, 0);  //default: start from begining
    }

    /**
     * parse file, generate a query
     *
     * @return
     */
    public Query nextQuery() {
        String line;
        byte[] data;

        while ((data = reader.nextLine()) != null) {
            if (data.length < 4 || data.length > 100)    //filter out result & empty line
                continue;

            line = new String(data).trim();

            if (ORDER_QUERY.equals(line)) {

                String[] orderEntry = new String(reader.nextLine()).split(":");
                String orderId = orderEntry[1];

                String[] keyEntry = new String(reader.nextLine()).split(":");
                String keys = keyEntry[1];
                int start = keys.indexOf('[');
                int end = keys.indexOf(']');
                String[] keyList = keys.substring(start + 1, end).split(",");

                return new OrderQuery(orderId, keyList);

            } else if (SUM_QUERY.equals(line)) {

                String[] goodEntry = new String(reader.nextLine()).split(":");
                String goodId = goodEntry[1].trim();

                String[] keyEntry = new String(reader.nextLine()).split(":");
                String keys = keyEntry[1];
                int start = keys.indexOf('[');
                int end = keys.indexOf(']');
                String key = keys.substring(start + 1, end).trim();

                return new SumQuery(goodId, key);

            } else if (GOOD_QUERY.equals(line)) {

                reader.nextLine();  //saler id is useless
                String[] goodEntry = new String(reader.nextLine()).split(":");
                String goodId = goodEntry[1].trim();

                String[] keyEntry = new String(reader.nextLine()).split(":");
                String keys = keyEntry[1];
                int start = keys.indexOf('[');
                int end = keys.indexOf(']');
                String[] keyList = keys.substring(start + 1, end).split(",");

                return new GoodQuery(goodId, keyList);

            } else if (BUYER_TSRANGE_QUERY.equals(line)) {

                String[] buyerEntry = new String(reader.nextLine()).split(":");
                String[] startEntry = new String(reader.nextLine()).split(":");
                String[] endEntry = new String(reader.nextLine()).split(":");

                String buyerId = buyerEntry[1].trim();
                String startTime = startEntry[1];
                String endTime = endEntry[1];

                return new BuyerQuery(buyerId, startTime, endTime);
            }

        }

        return null;
    }

    /**
     * generate a series of query with size num
     *
     * @param num
     * @return
     */
    public List<Query> generateQueries(int num) {
        List<Query> queryList = new ArrayList<Query>(num);

        int cnt = 0;
        Query query;

        //no more query means reach the end of the file
        while (cnt < num && ((query = nextQuery()) != null)) {

            queryList.add(query);
            cnt++;
        }

        return queryList;
    }


    public List<Query> getAllQueries(){
        List<Query> queryList = new ArrayList<Query>(1024);
        Query query;

        while ((query = nextQuery()) != null) {
            queryList.add(query);
        }

        return queryList;
    }

    public static void main(String [] args){
        List<Query> queryList = new Parser(new File("case.txt")).getAllQueries();

        int n = 0;
        for (Query query : queryList){
            System.out.println((n++)+" : "+query);
        }
    }
}
