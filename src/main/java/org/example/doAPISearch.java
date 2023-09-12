package tw.org.iii.project;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class doAPISearch {
    private int id;
    private String idStr;
    private String coStr;
    private String priceStr;
    private int idInt;
    private double priceDouble;
    private String body;
    public doAPISearch(int id){
        this.id = id;
    }

    public void GetStockData(){
        try {
            String jsonBOdy = ToSearchAPI(id);
            JSONObject jsonObj = new JSONObject(jsonBOdy);
            JSONArray msgArray = jsonObj.getJSONArray("msgArray");
            // 輸出結果
            JSONObject stockData = msgArray.getJSONObject(0);

// 取得 "n" 欄位的值
            idStr = stockData.getString("c");
            coStr = stockData.getString("n");
            priceStr = stockData.getString("z");
            idInt = Integer.valueOf(idStr);
            priceDouble = Double.parseDouble(priceStr);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public String ToSearchAPI(int id)throws Exception{
        body = "APIContent"+":"+id;
        //建立HttpClient實例
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1) // http 1.1
                .connectTimeout(Duration.ofSeconds(5)) // timeout after 5 seconds
                .sslContext(disabledSSLContext()) // disable SSL verify
                .build();

        // 臺灣證券交易所0056個股日成交資訊API
        String url = "https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_"+id+".tw&json=1&delay=0&_=1635167108897";
        //https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx
        //https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_2330.tw&json=1&delay=0&_=1635167108897
        // 建立HttpRequest請求
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        // 發送請求並接收回應
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //取得回應主體內容
        String body = response.body();
        System.out.println("API ok");
        System.out.println(body);

        return body;
    }
//"https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_"+id+".tw&json=1&delay=0&_=1635167108897"
    private static SSLContext disabledSSLContext() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("TLS"); // https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#sslcontext-algorithms
        sslContext.init(
                null,
                new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                }, new SecureRandom());
        return sslContext;
    }

    public String getCoStr() {
        return coStr;
    }

    public int getIdInt() {
        return idInt;
    }

    public double getPriceDouble() {
        return priceDouble;
    }
}
