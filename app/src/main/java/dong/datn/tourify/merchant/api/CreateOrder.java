package dong.datn.tourify.merchant.api;

import org.json.JSONObject;

import java.util.Date;

import dong.datn.tourify.merchant.helper.Helpers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CreateOrder {
    public static final int APP_ID = 553;
    public static final String MAC_KEY = "9phuAOYhan4urywHTh0ndEXiV3pKHr5Q";
    public static final String URL_CREATE_ORDER = "https://sandbox.zalopay.com.vn/v001/tpe/createorder";
    private class CreateOrderData {
        String AppId;
        String AppUser;
        String AppTime;
        String Amount;
        String AppTransId;
        String EmbedData;
        String Items;
        String BankCode;
        String Description;
        String Mac;

        private CreateOrderData(String amount) throws Exception {
            long appTime = new Date().getTime();
            Object AppInfo;
            AppId = String.valueOf(APP_ID);
            AppUser = "Android_Demo";
            AppTime = String.valueOf(appTime);
            Amount = amount;
            AppTransId = Helpers.getAppTransId();
            EmbedData = "{}";
            Items = "[]";
            BankCode = "zalopayapp";
            Description = "Merchant pay for order #" + Helpers.getAppTransId();
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    this.AppUser,
                    this.Amount,
                    this.AppTime,
                    this.EmbedData,
                    this.Items);

            Mac = Helpers.getMac(MAC_KEY, inputHMac);
        }
    }

     public JSONObject createOrder(String amount) throws Exception {
        CreateOrderData input = new CreateOrderData(amount);

        RequestBody formBody = new FormBody.Builder()
                .add("appid", input.AppId)
                .add("appuser", input.AppUser)
                .add("apptime", input.AppTime)
                .add("amount", input.Amount)
                .add("apptransid", input.AppTransId)
                .add("embeddata", input.EmbedData)
                .add("item", input.Items)
                .add("bankcode", input.BankCode)
                .add("description", input.Description)
                .add("mac", input.Mac)
                .build();

        JSONObject data = HttpProvider.sendPost(URL_CREATE_ORDER, formBody);
        return data;
    }
}

