package edu.bd.ewu.cse489_2019_1_60_085_lab03;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

public class singupActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        sharedPref = this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        EditText name = findViewById(R.id.singupName);
        EditText userID = findViewById(R.id.signupUserID);
        EditText password = findViewById(R.id.signupPassword);
        EditText rePassword = findViewById(R.id.signupRePassword);
        EditText email = findViewById(R.id.signupEmail);
        EditText phone = findViewById(R.id.signupPhone);
        CheckBox rememberID = findViewById(R.id.checkBoxUserID);
        CheckBox rememberPass = findViewById(R.id.checkBoxPassword);
        Button loginBtn = findViewById(R.id.loginButton);
        Button exitBtn = findViewById(R.id.exitButton);
        Button goBtn = findViewById(R.id.goButton);

        sharedPref = this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String HasuserID = sharedPref.getString("userid", "");
        String HasPassword = sharedPref.getString("password", "");
        if(HasuserID.isEmpty()==false){
            TableRow row1 = findViewById(R.id.row1);
            row1.setVisibility(View.GONE);

            TableRow row2 = findViewById(R.id.row2);
            row2.setVisibility(View.GONE);

            TableRow row3 = findViewById(R.id.row3);
            row3.setVisibility(View.GONE);

            TableRow row6 = findViewById(R.id.row6);
            row6.setVisibility(View.GONE);

            TableRow row7 = findViewById(R.id.row7);
            row7.setVisibility(View.GONE);
            userID.setText(HasuserID);
            password.setText(HasPassword);
        }

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user=userID.getText().toString();
                String pass=password.getText().toString();
                String userid = sharedPref.getString("userid", "");
                String userPassword = sharedPref.getString("password", "");

                Intent i = new Intent(singupActivity.this, upcomingEventActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginText = loginBtn.getText().toString();
                if(loginText.contains("Login")){
                    TableRow row1 = findViewById(R.id.row1);
                    row1.setVisibility(View.GONE);

                    TableRow row2 = findViewById(R.id.row2);
                    row2.setVisibility(View.GONE);

                    TableRow row3 = findViewById(R.id.row3);
                    row3.setVisibility(View.GONE);

                    TableRow row6 = findViewById(R.id.row6);
                    row6.setVisibility(View.GONE);

                    TableRow row7 = findViewById(R.id.row7);
                    row7.setVisibility(View.GONE);


                    loginBtn.setText("Signup");


                }
                else{
                    TableRow row1 = findViewById(R.id.row1);
                    row1.setVisibility(View.VISIBLE);

                    TableRow row2 = findViewById(R.id.row2);
                    row2.setVisibility(View.VISIBLE);

                    TableRow row3 = findViewById(R.id.row3);
                    row3.setVisibility(View.VISIBLE);

                    TableRow row6 = findViewById(R.id.row6);
                    row6.setVisibility(View.VISIBLE);

                    TableRow row7 = findViewById(R.id.row7);
                    row7.setVisibility(View.VISIBLE);

                    TableRow row8 = findViewById(R.id.row8);
                    row8.setVisibility(View.VISIBLE);

                    LinearLayout row9 = findViewById(R.id.row9);
                    row9.setVisibility(View.VISIBLE);

                    loginBtn.setText("Login");
                    String Name = name.getText().toString();
                    String mail=email.getText().toString();
                    String Phone=phone.getText().toString();
                    String ID=userID.getText().toString();
                    String pass=password.getText().toString();


                    showDialog("Account created succesfully","Signup Info",Name,mail,Phone,ID,pass,"OK","Back");
                }
            }
        });
    }


    private void showDialog(String message, String title,String name,String email, String phone,String userID,String password,String btn1,String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        //Util.getInstance().deleteByKey(MainActivity.this, key);
                      SharedPreferences sharedPref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                      SharedPreferences.Editor prefsEditor = sharedPref.edit();
                      prefsEditor.putString("name", name);
                      prefsEditor.putString("email", email);
                      prefsEditor.putString("phone", phone);
                      prefsEditor.putString("userid", userID);
                      prefsEditor.putString("password", password);
                      prefsEditor.apply();

                    }
                })
                .setNegativeButton(btn2, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("Error Dialog");
        alert.show();
    }
}
