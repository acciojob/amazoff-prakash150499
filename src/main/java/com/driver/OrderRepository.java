package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderMap=new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    HashMap<String , String>orderPartnerMap=new HashMap<>();
    HashMap<String , List<String>>partnerOrderMap=new HashMap<>();

    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        deliveryPartnerMap.put(partnerId,deliveryPartner);

    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
        orderPartnerMap.put(orderId,partnerId);
        associateOrderWithPartner(orderId,partnerId);
    }

    private void associateOrderWithPartner(String orderId, String partnerId) {
        if(partnerOrderMap.containsKey(partnerId))
        {
            List<String>orders=partnerOrderMap.get(partnerId);
            orders.add(orderId);
            partnerOrderMap.put(partnerId,orders);
        }
        else
        {
            List<String>orders=new ArrayList<>();
            orders.add(orderId);
            partnerOrderMap.put(partnerId,orders);
        }
    }

    public Order getOrderById(String orderId) {
        Order order=orderMap.get(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner partner=deliveryPartnerMap.get(partnerId);
        return partner;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner partner=deliveryPartnerMap.get(partnerId);
        return partner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
    List<String>orderList=partnerOrderMap.get(partnerId);
        return orderList;
    }

    public List<String> getAllOrders() {
        List<String>allOrders=new ArrayList<>(orderMap.keySet());
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        int totalOrders=orderMap.size();
        int assignedOrders=orderPartnerMap.size();
        int unassignedOrders=totalOrders-assignedOrders;
        return unassignedOrders;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String[] parts = time.split(":");
        int currentTime=0;
        int ordersLeft=0;
        currentTime+=Integer.parseInt(parts[0])*60;
        currentTime+=Integer.parseInt(parts[1]);
        List<String>orders=partnerOrderMap.get(partnerId);
        for(String orderId:orders)
        {
            Order currentOrder=orderMap.get(orderId);
            if(currentOrder.getDeliveryTime()>currentTime)
            {
                ordersLeft++;
            }
        }
        return ordersLeft;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String>partnerOrders=partnerOrderMap.get(partnerId);
        int lastTime=0;
        for(String orderId:partnerOrders)
        {

            Order currentOrder=orderMap.get(orderId);
            int currentTime=currentOrder.getDeliveryTime();
            lastTime=Math.max(lastTime,currentTime);

        }
        // Converting time to string
        String time="";
        int hours=lastTime/60;
        int mins=lastTime%60;
        if(hours<10)
        {
            time+="0";
            time+=hours;
        }
        else
        {
            time+=hours;
        }
        time+=":";
        if(mins<10)
        {
            time+="0";
            time+=mins;
        }
        else
        {
            time+=mins;
        }
        return time;
    }

    public void deletePartnerById(String partnerId) {
        List<String>partnerOrders=partnerOrderMap.get(partnerId);
        for(String order:partnerOrders)
        {
            orderPartnerMap.remove(order);
        }
        partnerOrderMap.remove(partnerId);

    }

    public void deleteOrderById(String orderId) {
        orderMap.remove(orderId);
        orderPartnerMap.remove(orderId);
    }
}
