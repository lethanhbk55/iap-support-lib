package com.thanhlv.common.payment.iap;

public interface IAPApi {
	IAPResponse verifyTransaction(Object receiptData) throws Exception;
}
