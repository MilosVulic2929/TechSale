package com.metropolitan.techsale.order;

import com.metropolitan.techsale.utils.AbstractRetrofitService;

public class OrderServiceImpl extends AbstractRetrofitService {

    private OrderService orderService;

    public OrderServiceImpl() {
        super("http://10.0.2.2:8090/");  //TODO vulic - warning: promeni za genny (10.0.3.2)
        orderService = retrofit.create(OrderService.class);
    }

    public OrderService getOrderService() {
        return orderService;
    }
}
