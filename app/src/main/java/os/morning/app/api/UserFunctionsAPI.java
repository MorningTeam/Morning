package os.morning.app.api;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务端接口处理API
 */
public class UserFunctionsAPI {

    private JSONParser jsonParser;

    //URL of the servlet API
    private static String loginURL = "***";
    private static String getDeptURL = "***";

    private static String login_tag = "LOGIN";
    private static String contact_tag = "CONTACTS";


    // constructor
    public UserFunctionsAPI(){
        jsonParser = new JSONParser();
    }


    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public JSONObject loginUser(String username, String password){
        Log.i("account ","username : " + username + " , password : " + password);
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    /**
     * 获取用户通讯录
     * @param flag
     * @param deptcode
     * @return
     */
    public JSONObject getContacts(String flag, String deptcode){
        Log.i("CONTACTS ","flag : " + flag + " , deptcode : " + deptcode);
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", contact_tag));
        params.add(new BasicNameValuePair("flag", flag));
        if("2".equalsIgnoreCase(flag.trim()))
            params.add(new BasicNameValuePair("deptcode", deptcode));

        JSONObject json = jsonParser.getJSONFromUrl(getDeptURL, params);
        return json;
    }


}

