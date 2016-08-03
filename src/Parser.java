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

    private static final int BUFF_SIZE = 40 << 20;
    private FastReader reader;

    public Parser(File file, long startPos) {
        this.reader = new FastReader(file, startPos, BUFF_SIZE);
        System.out.println("New Parser: " + file.getName() + ", start " + startPos);
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
                int end = keys.indexOf(']');//can be empty
                String[] keyList = keys.substring(start + 1, end).split(",");

                StringBuilder sb = new StringBuilder();
                while ((data = reader.nextLine()) != null && data.length > 1) {
                    sb.append(new String(data));
                }
                return new OrderQuery(orderId, keyList, sb.toString());

            } else if (SUM_QUERY.equals(line)) {

                String[] goodEntry = new String(reader.nextLine()).split(":");
                String goodId = goodEntry[1].trim();

                String[] keyEntry = new String(reader.nextLine()).split(":");
                String keys = keyEntry[1];
                int start = keys.indexOf('[');
                int end = keys.indexOf(']')-1; //cann't be null
                String key = keys.substring(start + 1, end).trim();

                StringBuilder sb = new StringBuilder();
                while ((data = reader.nextLine()) != null && data.length > 1) {
                    sb.append(new String(data));
                }
                return new SumQuery(goodId, key, sb.toString());

            } else if (GOOD_QUERY.equals(line)) {

                reader.nextLine();  //saler id is useless
                String[] goodEntry = new String(reader.nextLine()).split(":");
                String goodId = goodEntry[1].trim();

                String[] keyEntry = new String(reader.nextLine()).split(":");
                String keys = keyEntry[1];
                int start = keys.indexOf('[');
                int end = keys.indexOf(']');
                String[] keyList = keys.substring(start + 1, end).split(",");
                StringBuilder sb = new StringBuilder();
                while ((data = reader.nextLine()) != null && data.length > 1) {
                    sb.append(new String(data));
                }
                return new GoodQuery(goodId, keyList, sb.toString());

            } else if (BUYER_TSRANGE_QUERY.equals(line)) {

                String[] buyerEntry = new String(reader.nextLine()).split(":");
                String[] startEntry = new String(reader.nextLine()).split(":");
                String[] endEntry = new String(reader.nextLine()).split(":");

                String buyerId = buyerEntry[1].trim();
                String startTime = startEntry[1];
                String endTime = endEntry[1];
                StringBuilder sb = new StringBuilder();
                while ((data = reader.nextLine()) != null && data.length > 1) {
                    sb.append(new String(data));
                }
                return new BuyerQuery(buyerId, startTime, endTime, sb.toString());
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


    public List<Query> getAllQueries() {
        List<Query> queryList = new ArrayList<Query>(1024);
        Query query;

        while ((query = nextQuery()) != null) {
            queryList.add(query);
        }

        return queryList;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        //String filename = "case.txt";
        String filename = "/home/hadoop/tb/fixed_case/case.0";
        if (args.length > 0)
            filename = args[0];
        List<Query> queryList = new Parser(new File(filename)).getAllQueries();

        int n = 0;
        for (Query query : queryList) {
            System.out.println((n++) + " : " + query);
        }
        long end = System.currentTimeMillis();
        System.out.println("Parsing case takes: " + (end - start) + " ms");
    }
}
