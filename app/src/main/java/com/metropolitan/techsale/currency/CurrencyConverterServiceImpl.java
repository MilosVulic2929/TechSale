package com.metropolitan.techsale.currency;

public class CurrencyConverterServiceImpl extends CurrencyConverterBaseService {

    private CurrencyConverterService currencyConverterService;

    public CurrencyConverterServiceImpl() {
        super();
        this.currencyConverterService = this.retrofit.create(CurrencyConverterService.class);
    }

    public CurrencyConverterService getCurrencyConverterService() {
        return currencyConverterService;
    }
}