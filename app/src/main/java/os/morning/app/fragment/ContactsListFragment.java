package os.morning.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import os.morning.app.R;
import os.morning.app.api.UserFunctionsAPI;
import os.morning.app.bean.User;

/**
 * List of Contacts
 * 联系人列表
 * @author ZiQin
 * @version 1.0
 * @created 2014/7/22
 */

public class ContactsListFragment extends BaseFragment {

    @Override
    public int getTitleResourceId() {
        return R.string.title_contacts_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String deptno = getArguments().getString("deptno");
        initCards(deptno);
    }


    private void initCards(String deptno) {
        new loadPersons(deptno).execute();
    }


    /**
     *    填充联系人列表
     */
    private Card init_dept_contacts(User user) {
        final User current_user = user;
        //Create a Card
        Card card = new Card(getActivity());
        card.setInnerLayout(R.layout.contact_list_inner_content);

        // 注册直接拨打电话事件
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Pattern p = Pattern.compile("\\d+?");
                Matcher match = p.matcher(current_user.getMobile());
                //正则验证输入的是否为数字
                if(match.matches()){
                    Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + current_user.getMobile()));
                    getActivity().startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "号码错误", Toast.LENGTH_LONG).show();
                }
            }
        });


        card.setTitle(user.getUserName());
        return card;
    }

    /**
     *  加载部门联系人
     */
    class loadPersons extends AsyncTask<String,String,JSONObject> {
        private String deptcode ;
        JSONObject contactsFromDept ;
        private UserFunctionsAPI userFunction;
        @Override
        protected JSONObject doInBackground(String... strings) {
            userFunction = new UserFunctionsAPI();
            contactsFromDept = userFunction.getContacts("2", deptcode);
            return contactsFromDept;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                loadChild(contactsFromDept.getJSONObject("contactInfo").getJSONArray(deptcode));
            }catch(JSONException e){
                ;
            }
        }

        loadPersons(){

        }
        loadPersons(String deptcode){
            this.deptcode = deptcode;
        }

        /**
         * 获取每一个部门下的所有人员信息
         * @param persons        人员信息数组
         */
        private void loadChild(JSONArray persons) {

            ArrayList<Card> cards = new ArrayList<Card>();

            int len = persons.length();
            try {
                JSONObject person;
                User user ;
                for (int i = 0; i < len; i++) {
                    person = persons.getJSONObject(i);
                    user = new User();
                    user.setDeptId(person.getString("deptid"));
                    user.setDeptName(person.getString("deptname"));
                    user.setEmail(person.getString("email"));
                    user.setMobile(person.getString("mobile"));
                    user.setUserNo(person.getString("userno"));
                    user.setUserName(person.getString("username"));

                    Card card = init_dept_contacts(user);
                    cards.add(card);
                }
            }catch (JSONException e){
                Log.e("add person to dept",e.getMessage());
            }
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.contacts_list);
            if (listView!=null){
                listView.setAdapter(mCardArrayAdapter);
            }
        }
    }

}
