package os.morning.app.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import os.morning.app.R;
import os.morning.app.api.UserFunctionsAPI;
import os.morning.app.bean.Dept;

/**
 * List of Dept
 * 部门列表
 * @author ZiQin
 * @version 1.0
 * @created 2014/7/22
 */
public class DeptListFragment extends BaseFragment {

    protected FragmentTransaction ftraction ;
    Activity context ;
    UserFunctionsAPI userFunction;

    @Override
    public int getTitleResourceId() {
        return R.string.title_dept_listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dept_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        ftraction = getFragmentManager().beginTransaction();
        initCards();
    }


    private void initCards() {
        new ProcessGetContacts().execute();
    }

    public class ListCard extends Card {

        protected String mTitleMain;
        protected Dept deptObj;

        public ListCard(Context context,Dept deptObj) {
            super(context, R.layout.dept_list_inner_content);
            this.mTitleMain=deptObj.getDeptName();
            this.deptObj = deptObj ;
            init();
        }
        public String getmTitleMain(){
            return this.mTitleMain;
        }

        private void init(){

            //Add ClickListener
            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    BaseFragment detailList = new ContactsListFragment() ;
                    Bundle args = new Bundle();
                    args.putString("deptno", deptObj.getDeptId());
                    detailList.setArguments(args);
                    ftraction.replace(R.id.main,detailList);
                    ftraction.addToBackStack(null);
                    ftraction.commit();
                }
            });

            //Set the card inner text
            setTitle(mTitleMain);
        }

    }


    /*
     * Async Task 获取通讯录信息.
     */
    private class ProcessGetContacts extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected JSONObject doInBackground(String... args) {

            userFunction = new UserFunctionsAPI();
            JSONObject json = userFunction.getContacts("1", null);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            ArrayList<Card> cards = new ArrayList<Card>();
            ListCard card ;
            Dept deptObj ;
            try {
                JSONObject dept = json.getJSONObject("contactInfo");
                Iterator<String> iter = dept.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = dept.get(key).toString();
                    deptObj = new Dept(key,value);
                    Log.i("value", value.toString());
                    card = new ListCard(getActivity(),deptObj);
                    cards.add(card);
                }
            }catch (JSONException e){
                Log.e("CONTACTS", e.getMessage());
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(context,cards);
            CardListView listView = (CardListView) context.findViewById(R.id.dept_list_content);
            if (listView!=null){
                listView.setAdapter(mCardArrayAdapter);
            }
        }
    }

}
