package com.metropolitan.techsale.currency;

import com.metropolitan.techsale.utils.AbstractRetrofitService;

public class CurrencyConverterServiceImpl extends AbstractRetrofitService {

    private CurrencyConverterService currencyConverterService;

    public CurrencyConverterServiceImpl() {
        super("http://data.fixer.io/api/");
        this.currencyConverterService = this.retrofit.create(CurrencyConverterService.class);
    }

    public CurrencyConverterService getCurrencyConverterService() {
        return currencyConverterService;
    }
}