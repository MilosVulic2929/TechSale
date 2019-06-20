package com.metropolitan.techsale.items.service;

import com.metropolitan.techsale.utils.AbstractRetrofitService;

public class ItemServiceImpl extends AbstractRetrofitService {

    private ItemsService itemsService;

    public ItemServiceImpl() {
        super("http://10.0.3.2:8090/"); //TODO set za geny (10.0.3.2)
        this.itemsService = retrofit.create(ItemsService.class);
    }

    public ItemsService getItemService(){
        return itemsService;
    }

}
