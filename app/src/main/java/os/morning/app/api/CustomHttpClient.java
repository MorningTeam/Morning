package os.morning.app.api;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;


/**
 * 提供全局HTTPClient对象
 * @author ZiQin
 * @version 1.0
 * @created 2014/7/25
 */
public class CustomHttpClient {
    private static HttpClient customHttpClient ;

    private CustomHttpClient(){
    }
    public static synchronized HttpClient getCustomHttpClient(){
        if (customHttpClient == null){
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params,true);

            ConnManagerParams.setTimeout(params,1000);
            HttpConnectionParams.setConnectionTimeout(params,5000);
            HttpConnectionParams.setSoTimeout(params,10000);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),443));

            ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(params,schemeRegistry);

            customHttpClient = new DefaultHttpClient(clientConnectionManager,params);
        }

        return customHttpClient ;
    }
}
