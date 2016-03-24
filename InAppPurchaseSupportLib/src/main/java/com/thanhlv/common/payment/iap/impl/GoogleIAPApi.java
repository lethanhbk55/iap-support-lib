package com.thanhlv.common.payment.iap.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.apache.commons.io.IOUtils;

import com.thanhlv.common.payment.iap.IAPApi;
import com.thanhlv.common.payment.iap.IAPResponse;
import com.thanhlv.common.payment.iap.exceptions.VerifyDigitalSignatureException;

public class GoogleIAPApi implements IAPApi {
	private static Decoder DECODER = Base64.getDecoder();
	private byte[] signature;
	private byte[] publicKey;

	public GoogleIAPApi(byte[] publicKey, byte[] signature) {
		this.signature = signature;
		this.publicKey = publicKey;
	}

	public GoogleIAPApi(String publishKeyBase64, String signatureBase64) {
		setPublishKeyBase64(publishKeyBase64);
		setSignatureBase64(signatureBase64);
	}

	public GoogleIAPApi(InputStream publicKeyInputStream, byte[] signature) throws IOException {
		this.signature = signature;
		setPublishKeyBase64(loadInputStream(publicKeyInputStream));
	}

	public GoogleIAPApi(InputStream publicKeyInputStream, String signatureBase64) throws IOException {
		setSignatureBase64(signatureBase64);
		setPublishKeyBase64(loadInputStream(publicKeyInputStream));
	}

	private String loadInputStream(InputStream is) throws IOException {
		try (StringWriter sw = new StringWriter()) {
			IOUtils.copy(is, sw);
			return sw.toString();
		}
	}

	@Override
	public IAPResponse verifyTransaction(Object receiptData) throws Exception {
		String data = "";
		if (receiptData instanceof String) {
			data = (String) receiptData;
		} else {
			data = receiptData.toString();
		}

		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(publicKey));
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initVerify(pubKey);
		sig.update(data.getBytes());
		boolean verify = sig.verify(signature);
		if (verify) {
			return new IAPResponse() {

				@Override
				public Object getData() {
					return receiptData;
				}
			};
		} else {
			throw new VerifyDigitalSignatureException();
		}
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}

	public void setPublishKeyBase64(String publishKeyBase64) {
		this.publicKey = DECODER.decode(publishKeyBase64);
	}

	public void setSignatureBase64(String signatureBase64) {
		this.signature = DECODER.decode(signatureBase64);
	}

}
