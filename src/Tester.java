import com.alibaba.middleware.race.OrderSystem;
import com.alibaba.middleware.race.OrderSystemImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhaoz on 2016/7/26.
 * 1. 订单/商品/买家文件，数据存储位置输入 (配置文件
 * 2. 查询case 输入
 * 3. 多线程运行
 */
public class Tester {
    public static void main(String args[]){
        List<String> buyerFiles = new ArrayList<String>();
        List<String> goodFiles = new ArrayList<String>();
        List<String> orderFiles = new ArrayList<String>();
        List<String> storePath = new ArrayList<String>();

        buyerFiles.add("~/tb/buyer.0.0");
        buyerFiles.add("~/tb/buyer.1.1");
        goodFiles.add("~/tb/good.0.0");
        goodFiles.add("~/tb/good.1.1");
        goodFiles.add("~/tb/good.2.2");
        orderFiles.add("~/tb/order.2.2");
        orderFiles.add("~/tb/order.1.1");
        orderFiles.add("~/tb/order.0.3");
        orderFiles.add("~/tb/order.0.0");

        storePath.add("~/store");

        OrderSystem os = new OrderSystemImpl();
        try {
            long start = System.nanoTime();
            os.construct(orderFiles, buyerFiles, goodFiles, storePath);

            long end = System.nanoTime();
            System.out.println("-- Constructing takes "+(end -start) +" ns");

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

            String buyerid = "tb_a99a7956-974d-459f-bb09-b7df63ed3b80";
            long startTime = 1471025622;
            long endTime = 1471219509;
            System.out.println("\n查询买家ID为" + buyerid + "的一定时间范围内的订单");
            Iterator<OrderSystem.Result> it = os.queryOrdersByBuyer(startTime, endTime, buyerid);
            while (it.hasNext()) {
                System.out.println(it.next());
            }

            String goodid = "good_842195f8-ab1a-4b09-a65f-d07bdfd8f8ff";
            String salerid = "almm_47766ea0-b8c0-4616-b3c8-35bc4433af13";
            System.out.println("\n查询商品id为" + goodid + "，商家id为" + salerid + "的订单");
            it = os.queryOrdersBySaler(salerid, goodid, new ArrayList<String>());
            while (it.hasNext()) {
                System.out.println(it.next());
            }

            goodid = "good_d191eeeb-fed1-4334-9c77-3ee6d6d66aff";
            String attr = "app_order_33_0";
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




        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
