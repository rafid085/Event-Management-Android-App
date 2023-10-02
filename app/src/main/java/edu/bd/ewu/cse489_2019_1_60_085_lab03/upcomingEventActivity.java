package edu.bd.ewu.cse489_2019_1_60_085_lab03;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class upcomingEventActivity extends Activity {
    ArrayList<Event> eventList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_event);

        loadDataFromRemote();
        loadData();

        Button btnCreate = findViewById(R.id.createNewEvent);
        Button btnExit = findViewById(R.id.exitUpEvent);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(upcomingEventActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
    private void loadDataFromRemote(){
        String[] keys = {"action","id","semester"};
        String[] values = {"restore","2019-1-60-085","2022-2"};
        httpRequest(keys, values);

    }
    private void loadData(){
        KeyValueDB db= new KeyValueDB(getApplicationContext());
        Cursor c = db.getAllKeyValues();
        while(c.moveToNext()){
            String key=c.getString(0);
            String value=c.getString(1);
            System.out.println("Value for DB : "+value);
            if(value.isEmpty()==false){
                String [] values2 = value.split(",");
                System.out.println("Values = "+values2.toString());
                String setEventName=values2[0];
                String setEventPlace=values2[1];
                String setEventType=values2[2];
                String setEventDate=values2[3];
                String setEventCapacity=values2[4];
                String setEventBudget=values2[5];
                String setEventEmail=values2[6];
                String setEventPhone=values2[7];
                String setEventDescription=values2[8];
                System.out.println("Values = "+setEventName+" "+setEventDate+" "+setEventPlace);
                Event e = new Event(key,setEventName,setEventPlace,setEventDate,setEventCapacity,setEventBudget,setEventEmail,setEventPhone,setEventDescription,setEventType);
                eventList.add(e);
                System.out.println();
            }

        }
        ListView lv = findViewById(R.id.eventList);
        CustomEventAdapter ca = new CustomEventAdapter(this,eventList);
        lv.setAdapter(ca);
    }

    private void httpRequest(final String keys[],final String values[]){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... param){
                try{
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    for(int i=0;i<keys.length;i++){
                        params.add(new BasicNameValuePair(keys[i],values[i]));
                    }
                    String data = JSONParser.getInstance().makeHttpRequest("https://muthosoft.com/univ/cse489/index.php","POST",params);
                    return data;
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return null;
            }
            @Override
            protected  void onPostExecute(String data){
                KeyValueDB kdb= new KeyValueDB(getApplicationContext());
                if(data!= null){
                    try{
                        System.out.println("Values are : "+data);
                        JSONObject json = new JSONObject(data);
                        if(json.has("events")){
                            JSONArray ja = json.getJSONArray("events");
                            for(int i = 0; i <ja.length();i++){
                                System.out.println("Hello from loop");
                                JSONObject job=ja.getJSONObject(i);
                                String key = job.getString("key");
                                String value = job.getString("value");
                                System.out.println("This is key="+key);
                                System.out.println("This is key="+value);
                                if(key.contains(".com")==false){
                                    if(kdb.getValueByKey(key)==null){
                                        kdb.insertKeyValue(key,value);
                                        System.out.println("heeloo from upcoming load data server");
                                    }
                                }
                            }
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
