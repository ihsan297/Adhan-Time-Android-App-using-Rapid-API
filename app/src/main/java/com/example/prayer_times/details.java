package com.example.prayer_times;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class details extends AppCompatActivity {
    TextView tv,city;
    String cit;
    TextView fajar,duhar,asar,maghrib,isha;
    ImageView fajarPtr,duharPtr,asarPtr,maghribPtr,ishaPtr;
    String fjr,dhr,asr,mghrb,ish;
    ImageButton playButton;
    LinearLayout layout;
    ImageView cityPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        cityPic=findViewById(R.id.cityPic);
        fajarPtr=findViewById(R.id.fajar_ptr);
        duharPtr=findViewById(R.id.duhar_ptr);
        asarPtr=findViewById(R.id.asar_ptr);
        maghribPtr=findViewById(R.id.maghrib_ptr);
        ishaPtr=findViewById(R.id.ishaPtr);
        city=findViewById(R.id.tv_selectedCity);
        fajar=findViewById(R.id.fjrtime);
        duhar=findViewById(R.id.zhrtime);
        asar=findViewById(R.id.asrtime);
        maghrib=findViewById(R.id.mghrbtime);
        isha=findViewById(R.id.ishatime);
        layout=findViewById(R.id.prayerTimesLayout);



        playButton=findViewById(R.id.adhanPlay);//for playig adhan
        final Intent intent=getIntent();
        String intentCity=intent.getStringExtra("city"); //geting city from intent

        city.setText(intentCity);
        tv=findViewById(R.id.info);
         cit=city.getText().toString();
		 //loading image for apporiate city 
         if(intentCity.equals("Lahore")){
			 //calling task for image that is loaded from server
             new Task().execute("https://cdn.pixabay.com/photo/2020/01/25/18/45/badshahi-mosque-4793138_960_720.jpg");
         }
         else if(intentCity.equals("Islamabad")){
             new Task().execute("https://cdn.pixabay.com/photo/2019/07/10/18/42/faisal-mosque-4329241_960_720.jpg");
         }
         else if(intentCity.equals("Karachi")){
             new Task().execute("https://cdn.pixabay.com/photo/2020/07/13/19/30/pakistan-5401823_960_720.jpg");
         }
         else if(intentCity.equals("Sahiwal")){
             new Task().execute("https://lh3.googleusercontent.com/p/AF1QipNCq_gbdKLRtjdx1A3cxNnp8AeeSprJig4c4Ptd=s1600-w228");
         }
         else {
             new Task().execute("https://cdn.pixabay.com/photo/2019/05/21/19/32/ramadhan-4219913_960_720.jpg");
         }
	///callig task for adhan api

        new MyTask().execute();


	//calling servce for playing adhan on button click

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(details.this,AdhanService.class));

            }
        });


    }
	//async task for calling network operations for images
    private class Task extends AsyncTask<String,Void,Void>{
        Bitmap bmp;

        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                Log.d("String",strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            cityPic.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }
//task for API CALLS ADHAN TIMES
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;
        ProgressDialog progressDialog;//

        HttpResponse<String> response;//response strings
        public MyTask(){
            progressDialog=new ProgressDialog(details.this);
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Doing something, please wait.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                progressDialog.show();;
				//calling ap
                response = Unirest.get("https://aladhan.p.rapidapi.com/timingsByCity?city="+cit+"&country=pakistan")
                        .header("x-rapidapi-key", "c6e70037a0mshe9d19d275a12732p157066jsn01184d9f8e13")
                        .header("x-rapidapi-host", "aladhan.p.rapidapi.com")
                        .asString();
                Log.d("Responce",response.getBody());

            } catch (UnirestException e) {
                e.printStackTrace();
                result = e.toString();
                Log.d("Error",result);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
			///called after api gives response
			//loading adhan text views with times
            String map=response.getBody();
            Log.d("Reponse",map.substring(45,50));
            fjr=map.substring(53,58);
            Log.d("Fajarrr",map.substring(53,58));
            Log.d("Duhar",map.substring(87,92));
            dhr=map.substring(87,92);
            Log.d("Asar",map.substring(101,106));
            asr=map.substring(101,106);
            Log.d("Maghrb",map.substring(136,141));
            mghrb=map.substring(136,141);
            Log.d("Isha",map.substring(151,156));
            ish=map.substring(151,156);
            fajar.setText(fjr);
            duhar.setText(dhr);
            asar.setText(asr);
            maghrib.setText(mghrb);
            isha.setText(ish);
            progressDialog.dismiss();
            Date time= Calendar.getInstance().getTime();
            int currentHr=time.getHours();
            if(currentHr<Integer.parseInt(dhr.substring(0,2))){
                fajarPtr.setVisibility(View.VISIBLE);
            }
            else if(currentHr<Integer.parseInt(asr.substring(0,2))){
                duharPtr.setVisibility(View.VISIBLE);
            }
            else if(currentHr<Integer.parseInt(mghrb.substring(0,2))){
                asarPtr.setVisibility(View.VISIBLE);
            }
            else if(currentHr<Integer.parseInt(ish.substring(0,2))){
               maghribPtr.setVisibility(View.VISIBLE);
            }









            else if(currentHr>Integer.parseInt(mghrb.substring(0,2))){
                ishaPtr.setVisibility(View.VISIBLE);
            }

            layout.setVisibility(View.VISIBLE);


            super.onPostExecute(aVoid);


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
