package edu.bd.ewu.cse489_2019_1_60_085_lab03;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText name=findViewById(R.id.name);
        EditText place=findViewById(R.id.place);
        RadioButton indoor=findViewById(R.id.indoor);
        RadioButton outdoor=findViewById(R.id.outdoor);
        RadioButton online=findViewById(R.id.online);
        EditText dateAndTime=findViewById(R.id.dateAndTime);
        EditText capacity=findViewById(R.id.capacity);
        EditText budget=findViewById(R.id.budget);
        EditText email=findViewById(R.id.email);
        EditText phone=findViewById(R.id.phone);
        EditText description=findViewById(R.id.description);

        Button btnCancel= findViewById(R.id.cancelBtn);
        Button btnShare= findViewById(R.id.shareBtn);
        Button btnSave= findViewById(R.id.saveBtn);

        TextView error=findViewById(R.id.errorText);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,upcomingEventActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Share was clicked");
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                int isError=0;
                String ErrorMassage="";
                String personName= name.getText().toString();
                if(personName.length()<5){
                    ErrorMassage=ErrorMassage+" Invalid name ";
                    isError=1;
                }
                String eventPlace=place.getText().toString();
                if(eventPlace.length()<10){
                    ErrorMassage=ErrorMassage+" Invalid place name ";
                    isError=1;
                }

                String type="";
                if(indoor.isSelected()){
                    System.out.println(" Indoor  clicked");
                    type+="indoor";
                }
                if(outdoor.isSelected()){
                    System.out.println("Outdoor clicked");
                    type+="outdoor";
                }
                if(online.isSelected()){
                    System.out.println("Online clicked");
                    type+="online";
                }

                String dateAndtime = dateAndTime.getText().toString();
                if(dateAndtime.length()<9){
                    ErrorMassage=ErrorMassage+" Invalid date";
                }
                String Capacity = capacity.getText().toString();
                if(Capacity.length()<1){
                    ErrorMassage=ErrorMassage+" Invalid Capacity";
                }
                String Budget = budget.getText().toString();
                if(Budget.length()<1){
                    ErrorMassage=ErrorMassage+" Invalid Budget";
                }
                String Email = email.getText().toString();
                if(Email.length()<5){
                    ErrorMassage=ErrorMassage+" Invalid Email";
                }
                String Phone = phone.getText().toString();
                if(Phone.length()<11){
                    ErrorMassage=ErrorMassage+" Invalid Phone";
                }
                String descrip = description.getText().toString();
                if(descrip.length()<11){
                    ErrorMassage=ErrorMassage+" Invalid description";
                }

                if(ErrorMassage.length()>0){
                    error.setText("Please Fill All the Fields in the Form");
                    showDialog(ErrorMassage,"Error","OK","Back","","");
                    System.out.println(ErrorMassage);
                }
                else{
                    String value=personName+","+eventPlace+","+type+","+dateAndtime+","+Capacity+","+Budget+","+Email+","+Phone+","+descrip;
                    showDialog("Do you want to Save this info?","info","Yes","No",personName,value);
                }
            }
        });


    }

    private void showDialog(String message, String title,String btn1,String btn2,String name,String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setCancelable(false).setPositiveButton(btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                System.out.println("Yes Button was pressed");
                dialog.cancel();
                String key = name+"_"+System.currentTimeMillis();
                System.out.println("key for database = "+key);
                KeyValueDB kdb = new KeyValueDB(getApplicationContext());
                boolean b = kdb.insertKeyValue(key,value);
                System.out.println("database insert "+b);

                String[] keys ={"action","id","semester","key","event"};
                String[] values = {"backup","2019-1-60-085","2022-2",key,value};
                httpRequest(keys,values);

                Intent i = new Intent(MainActivity.this,upcomingEventActivity.class);
                startActivity(i);
                finish();
            }
        }).setNegativeButton(btn2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                System.out.println("No Button was pressed");
                dialog.cancel();
            }
        });
        AlertDialog alert= builder.create();
        alert.show();

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
                if(data!= null){
                    try{
                        System.out.println("Main activity Values are : "+data);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}