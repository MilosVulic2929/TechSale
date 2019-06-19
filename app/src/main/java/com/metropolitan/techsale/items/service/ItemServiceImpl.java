package com.metropolitan.techsale.items.service;

import com.metropolitan.techsale.utils.AbstractRetrofitService;

public class ItemServiceImpl extends AbstractRetrofitService {

    private ItemsService itemsService;

    public ItemServiceImpl() {
        super("http://10.0.2.2:8090/"); //TODO vulic - warning: pogledaj za genny koji treba url inace puca
        this.itemsService = retrofit.create(ItemsService.class);
    }

    public ItemsService getItemService(){
        return itemsService;
    }

}
