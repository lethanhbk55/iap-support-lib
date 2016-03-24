package com.thanhlv.common.payment.iap.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.thanhlv.common.payment.iap.IAPApi;
import com.thanhlv.common.payment.iap.IAPResponse;

public class AppleIAPApi implements IAPApi {
	private static final String VERIFY_RECEIPT_TEST_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
	private static final String VERIFY_RECEIPT_URL = "https://buy.itunes.apple.com/verifyReceipt";

	private String defaultUrl;
	private String testUrl;
	private boolean debug = false;

	public AppleIAPApi() {
		this(VERIFY_RECEIPT_URL, VERIFY_RECEIPT_TEST_URL);
	}

	public AppleIAPApi(boolean debug) {
		this();
		setDebug(debug);
	}

	public AppleIAPApi(String defaultUrl, String testUrl) {
		this.setDefaultUrl(defaultUrl);
		this.setTestUrl(testUrl);
	}

	public AppleIAPApi(String defaultUrl, String testUrl, boolean debug) {
		this(defaultUrl, testUrl);
		setDebug(debug);
	}

	@Override
	public IAPResponse verifyTransaction(Object receiptData) throws Exception {
		String data = "";
		if (receiptData instanceof String) {
			data = (String) receiptData;
		} else {
			data = receiptData.toString();
		}

		String url = debug ? testUrl : defaultUrl;
		String params = "{\"receipt-data\":\"" + data + "\"}";
		String response = this.sendHttpRequest(url, params);
		return new IAPResponse() {

			@Override
			public Object getData() {
				return response;
			}
		};
	}

	private String sendHttpRequest(String url, String data) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public String getTestUrl() {
		return testUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
