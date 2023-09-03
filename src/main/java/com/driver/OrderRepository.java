package com.driver;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderMap = new HashMap<>();
    HashMap<String,DeliveryPartner> partnerMap = new HashMap<>();
    HashMap<String, List<String>> partnerOrderMap = new HashMap<>();
    HashMap<String,String> orderPartnerMap = new HashMap<>();


    public void addOrder(Order order)
    {


        orderMap.put(order.getId(),order);
    }

    public void addPartner(String id)
    {
        DeliveryPartner deliveryPartner = new DeliveryPartner(id);
        partnerMap.put(id,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId,String partnerId)
    {
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId))
        {
            orderPartnerMap.put(orderId,partnerId);
            orderMap.put(orderId,orderMap.get(orderId));
            partnerMap.put(partnerId,partnerMap.get(partnerId));
            if(partnerOrderMap.containsKey(partnerId))
            {
                List<String> temp = partnerOrderMap.get(partnerId);
                temp.add(orderId);
                partnerOrderMap.put(partnerId,temp);
            }
            else {
                List<String> temp = new ArrayList<>();
                temp.add(orderId);
                partnerOrderMap.put(partnerId,temp);
            }

            DeliveryPartner dp = partnerMap.get(partnerId);
            int deliverys = dp.getNumberOfOrders();
            dp.setNumberOfOrders(deliverys+1);

        }
    }

    public Order getOrderById(String orderId)
    {
        if(orderMap.containsKey(orderId))
        {
            return orderMap.get(orderId);
        }

        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (partnerMap.containsKey(partnerId)) {
            return partnerMap.get(partnerId);
        }
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId)
    {
        if(partnerOrderMap.containsKey(partnerId))
        {
            return partnerOrderMap.get(partnerId).size();
        }

        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        if(partnerOrderMap.containsKey(partnerId))
        {
            return partnerOrderMap.get(partnerId);
        }

        return new ArrayList<>();
    }

    public List<String> getAllOrders()
    {
        List<String> temp = new ArrayList<>();
        for(String i:orderMap.keySet())
        {
            temp.add(i);
        }

        return temp;
    }

    public int getCountOfUnassignedOrders()
    {
        int count = orderMap.size()-orderPartnerMap.size();

        return count;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId)
    {
        String []temp = time.split(":");
        int hour = Integer.valueOf(temp[0]);
        int min = Integer.valueOf(temp[1]);
        int givenTime = (60*hour)+min;

        List<String> list = partnerOrderMap.get(partnerId);
        int count =0;
        for(String i:list)
        {
            Order order = orderMap.get(i);
            int delTime = order.getDeliveryTime();
            if(delTime>givenTime)
            {
                count++;
            }
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        String time = "";
        List<String> list = partnerOrderMap.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = orderMap.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour = deliveryTime / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }
        int min = deliveryTime % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }
        time = sHour + ":" + sMin;
        return time;
    }

    public void deletePartnerById(String partnerId)
    {
        List<String> list = partnerOrderMap.get(partnerId);
        for(String i:list)
        {
            orderPartnerMap.remove(i);
        }

        if(partnerMap.containsKey(partnerId))
        {
            partnerMap.remove(partnerId);
        }

        partnerOrderMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId)
    {


        orderMap.remove(orderId);
        String parent = orderPartnerMap.get(orderId);
        orderPartnerMap.remove(orderId);
        List<String> list = new ArrayList<>();
        for(String i:list)
        {
            if(orderId.equals(i))
            {
                list.remove(i);
            }
        }
        partnerOrderMap.put(parent,list);

    }


















}