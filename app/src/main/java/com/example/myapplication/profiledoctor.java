package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class profiledoctor extends AppCompatActivity {
    public static String getExtraId;
    private  String getid;
    TextView temail,tuser,tmajor,tpass;
    ListView listposts;
    ArrayList<String> Arraynames = new ArrayList<String>();
    ArrayList<String> Arraydescriptions = new ArrayList<String>();
    String [] Arrayname,Arraydescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledoctor);
        Intent i = getIntent();
        getExtraId =  i.getStringExtra("Id");
        temail=(TextView) findViewById(R.id.emailus);
        tuser=(TextView) findViewById(R.id.username);
        tmajor=(TextView) findViewById(R.id.majorus);
        tpass=(TextView) findViewById(R.id.passwordus);
        //method userinformaion
        getUserInfo();

        //method get data in list
        listposts=(ListView) findViewById(R.id.posts);
        getData();
    }
    //get user information
    private void getUserInfo(){
        temail.setText(MainActivity.u_Email);
        tuser.setText(MainActivity.username);
        tmajor.setText(MainActivity.u_major);
        tpass.setText(MainActivity.u_passw);

    }

//adapter
    class adapter extends ArrayAdapter<String> {
        Context context;
        String[] array1, array2;
        //decliration variables
        public adapter(Context c, String[] v1, String[] v2) {
            super(c, R.layout.items, R.id.problemname, v1);

            this.context = c;
            this.array1 = v1;
            this.array2 = v2;

        }
        @Override

        public View getView(int position, View v, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.items, parent, false);

            TextView tx1 = row.findViewById(R.id.problemname);
            TextView tx2 = row.findViewById(R.id.describproblem);


            tx1.setText(array1[position]);
            tx2.setText(array2[position]);
            return row;
        }
    }
    //get data for who doctor login
    private void getData() {


        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://10.0.2.2/alluniversity/listposts.php?userId="+getExtraId, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONArray jsonArray = response.getJSONArray("dishs");
                                    getid =    jsonArray.getJSONObject(0).get("Id").toString();

                                    //Toast.makeText(getApplicationContext(), " json Array length = " +jsonArray.length(), Toast.LENGTH_SHORT).show();
                                    Arrayname = new String[jsonArray.length()];
                                    Arraydescription = new String[jsonArray.length()];

                                    for(int i = 0 ; i <= jsonArray.length() ; i++){


                                        String nameprob = jsonArray.getJSONObject(i).get("nameprob").toString();
                                        String describprob = jsonArray.getJSONObject(i).get("describprob").toString();

                                        Arrayname [i] = nameprob;
                                        Arraydescription [i] = describprob;

                                        // do when the data all reseved
                                        if( i == jsonArray.length() -1){
                                            // Toast.makeText(getApplicationContext(), "Array List Names = " +ArrayNames.length, Toast.LENGTH_SHORT).show();
                                            // pass the data to the adapter
                                           adapter adp1 = new adapter(getApplicationContext(),Arrayname,Arraydescription);
                                            listposts.setAdapter(adp1);
                                            listposts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                    String i1=Arrayname[position].toString();
                                                    String i2=Arraydescription[position].toString();
                                                    Intent im=new Intent(getApplicationContext(),update_delete.class);
                                                    //put details from list to updata and delete page
                                                    im.putExtra("idpost",getid);
                                                    im.putExtra("na",i1);
                                                    im.putExtra("de",i2);
                                                    startActivity(im);
                                                }
                                            });
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    //  Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), "Error Response In PHP File ", Toast.LENGTH_SHORT).show();

                    }
                }

                ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<String, String>();
                params.put("userId",getExtraId.toString());

                // you must have in the php file a post method to get this data
                // ex : $_POST['DataToSend'];
                return params;
            }

        };
// Access the RequestQueue through your singleton class.
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

    }


}