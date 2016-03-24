import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.thanhlv.common.payment.iap.IAPApi;
import com.thanhlv.common.payment.iap.IAPResponse;
import com.thanhlv.common.payment.iap.impl.GoogleIAPApi;

public class TestGoogleIAP {
	public static void main(String[] args) throws IOException {
		FileInputStream pubicKeyFile = new FileInputStream(new File("resource/public.key"));
		String signature = "D2A3YnIbOtgp8K1dEPe7Tu7DMD/sOwk3Ml8ktkILKrdPYHTv1CgBM5KTtUaVi1Xa0wgp9NAl/JweGoC5T9cCh/fTqo6aBMrA1neVXTaUUl8ftLDuqDyLvD/tq7bSzLvEFJgKU82eFazts2mZkLSOSeD9bFjLixNYYf2jophFEe2G+4M8kg0LZJpqHZuW0kZ9gUp2XS1R7cSvP9d+e4zzA8xGW+LQkBqWePkPBXwnWSqll1ZYLTV204SLjBFVxfEdSNa+EDROyFo8BBbZku1fhcx7bzy1uMzrXRvp3DJyvT0/8vAdHCNw6mbp2+CQ4SQNOfmz/WGo831TpeaZf8olig==";
		IAPApi api = new GoogleIAPApi(pubicKeyFile, signature);
		String receiptData = "{\"orderId\":\"12999763169054705758.1391353119152795\",\"packageName\":\"com.gamebaivip.gamebai\",\"productId\":\"com.gamebaivip.gamebai.vang1\",\"purchaseTime\":1428384521370,\"purchaseState\":0,\"developerPayload\":\"nguyencongtuyen\",\"purchaseToken\":\"dcloaloegdjmgcddijgphfob.AO-J1OwAgaU60fAW40CVZGI61WkgxeU6nwr_FynmC3ZOAW4TLNxGjPUXQ7ZjoXGLa2g9T560roKN5sqXcc4fRO68Go6pmoFQF7PrMvm_IMPay8Gl_AIA0dNLDtUHeHpwyA9dfHzxmU5FR_cBXRKCCihmT9bYRtMK_A\"}";
		try {
			IAPResponse response = api.verifyTransaction(receiptData);
			System.out.println(response.getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
