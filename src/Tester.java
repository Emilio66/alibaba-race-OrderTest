import com.alibaba.middleware.race.OrderSystem;
import com.alibaba.middleware.race.OrderSystemImpl;
import model.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhaoz on 2016/7/26.
 * 1. 订单/商品/买家文件，数据存储位置输入 (配置文件
 * 2. 查询case 输入
 * 3. 多线程运行
 */
public class Tester {
    public static void main(String args[]) {
        List<String> buyerFiles = new ArrayList<String>();
        List<String> goodFiles = new ArrayList<String>();
        List<String> orderFiles = new ArrayList<String>();
        List<String> storePath = new ArrayList<String>();

        buyerFiles.add("/home/hadoop/tb/disk1/buyer.0.0");//disk1
        goodFiles.add("/home/hadoop/tb/disk1/good.0.0");
        orderFiles.add("/home/hadoop/tb/disk1/order.0.3");
        orderFiles.add("/home/hadoop/tb/disk1/order.0.0");

        buyerFiles.add("/home/hadoop/tb/disk2/buyer.1.1");//disk2
        goodFiles.add("/home/hadoop/tb/disk2/good.1.1");
        orderFiles.add("/home/hadoop/tb/disk2/order.1.1");

        goodFiles.add("/home/hadoop/tb/disk3/good.2.2");//disk3
        orderFiles.add("/home/hadoop/tb/disk3/order.2.2");

        storePath.add("/home/hadoop/store/disk1/");

        final OrderSystem os = new OrderSystemImpl();
        try {

            SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(time.format(new Date()));

            long start = System.currentTimeMillis();

            os.construct(orderFiles, buyerFiles, goodFiles, storePath);

            long end = System.currentTimeMillis();

            System.out.println("-- Constructing takes " + (end - start) + " ms");


            System.out.println("-------------- Query Start ------------------");

            System.out.println(time.format(new Date()));

            //get case file from argument
            if (args.length > 0) {
                File caseFile = new File(args[0]);
                int num = 50; //default 50 queries
                if (args.length > 1) {
                    num = Integer.parseInt(args[1]);
                }
                final List<Query> queries = new Parser(caseFile).generateQueries(num);

                //final List<Query> queries = new Parser(caseFile).getAllQueries();
                start = System.currentTimeMillis();

                //new a thread pool to execute query
                ExecutorService executorService = Executors.newFixedThreadPool(3);
                final int finalNum = queries.size();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        long finalStart = System.currentTimeMillis();
                        for (Query query : queries) {
                            switch (query.Type) {

                                case Query.ORDER:
                                    os.queryOrder(((OrderQuery) query).orderId, ((OrderQuery) query).keys);
                                    System.out.println((OrderQuery) query);
                                    break;

                                case Query.GOOD:
                                    //query saler's good info, saler-good is unique, no need for saler id
                                    //os.queryOrdersBySaler("", ((GoodQuery) query).goodId, ((GoodQuery) query).keys);
                                   // System.out.println((GoodQuery)query);
                                    break;

                                case Query.BUYER:
                                    //os.queryOrdersByBuyer(((BuyerQuery) query).startTime, ((BuyerQuery) query).endTime,
                                      //      ((BuyerQuery) query).buyerId);
                                    //System.out.println((BuyerQuery)query);
                                    break;
                                case Query.SUM:
                                    //os.sumOrdersByGood(((SumQuery) query).goodId, ((SumQuery) query).key);
                                   // System.out.println((SumQuery)query);
                                    break;

                            }
                        }
                            long end = System.currentTimeMillis();
                            System.out.println(finalNum + " queries  takes " + (end - finalStart) + " ms ---");
                    }
                });

                end = System.currentTimeMillis();
                System.out.println( "concurrent queries  takes " + (end - start) + " ms ---");
            } else {
                System.out.println("--- static query----");
                // 用例
                long orderid = 609670049;
                System.out.println("\n查询订单号为" + orderid + "的订单");
                System.out.println(os.queryOrder(orderid, null));

                System.out.println("\n查询订单号为" + orderid + "的订单，查询的keys为空，返回订单，但没有kv数据");
                System.out.println(os.queryOrder(orderid, new ArrayList<String>()));

                System.out.println("\n查询订单号为" + orderid
                        + "的订单的contactphone, buyerid, foo, done, price,description字段");
                List<String> queryingKeys = new ArrayList<String>();
                queryingKeys.add("contactphone");
                queryingKeys.add("buyerid");
                queryingKeys.add("foo");
                queryingKeys.add("done");
                queryingKeys.add("price");
                queryingKeys.add("description");
                OrderSystem.Result result = os.queryOrder(orderid, queryingKeys);
                System.out.println(result);
                System.out.println("\n查询订单号不存在的订单");
                result = os.queryOrder(1111, queryingKeys);
                if (result == null) {
                    System.out.println(1111 + " order not exist");
                }

                String buyerid = "wx-a0e0-6bda77db73ca";
                long startTime = 1462018520;
                long endTime = 1473999229;
                System.out.println("\n查询买家ID为" + buyerid + "的一定时间范围内的订单");
                Iterator<OrderSystem.Result> it = os.queryOrdersByBuyer(startTime, endTime, buyerid);
                while (it.hasNext()) {
                    System.out.println(it.next());
                }

                String goodid = "gd-b972-6926df8128c3";
                String salerid = "almm_47766ea0-b8c0-4616-b3c8-35bc4433af13";
                System.out.println("\n查询商品id为" + goodid + "，商家id为" + salerid + "的订单");
                queryingKeys.clear();
                queryingKeys.add("a_o_30709a_g_32587");
                it = os.queryOrdersBySaler(salerid, goodid, queryingKeys);
                while (it.hasNext()) {
                    System.out.println(it.next());
                }
                goodid = "gd-80fa-bc88216aa5be";
                System.out.println("\n查询商品id为" + goodid + "，商家id为" + salerid + "的订单");
                queryingKeys.clear();
                queryingKeys.add("address");
                it = os.queryOrdersBySaler(salerid, goodid, queryingKeys);
                while (it.hasNext()) {
                    System.out.println(it.next());
                }


                goodid = "aye-9c37-838aa50d1f1e";
                String attr = "a_g_5814";
                System.out.println("\n对商品id为" + goodid + "的 " + attr + "字段求和");
                System.out.println(os.sumOrdersByGood(goodid, attr));

                attr = "done";
                System.out.println("\n对商品id为" + goodid + "的 " + attr + "字段求和");
                OrderSystem.KeyValue sum = os.sumOrdersByGood(goodid, attr);
                if (sum == null) {
                    System.out.println("由于该字段是布尔类型，返回值是null");
                }

                attr = "foo";
                System.out.println("\n对商品id为" + goodid + "的 " + attr + "字段求和");
                sum = os.sumOrdersByGood(goodid, attr);
                if (sum == null) {
                    System.out.println("由于该字段不存在，返回值是null");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
