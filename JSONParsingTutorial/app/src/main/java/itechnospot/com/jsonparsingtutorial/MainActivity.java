package itechnospot.com.jsonparsingtutorial;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog progressDialog;

    private static String url = "http://itechnospot.com/temp/testuser.json";

    private static final String TAG_USERS = "users";
    private static final String TAG_USER ="User";
    private static final String TAG_ID = "id";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_FNAME = "fname";
    private static final String TAG_LNAME = "lname";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILE = "mobile";
    private static final String TAG_GENDER = "gender";

    JSONArray users = null;

    private TextView responseTv;
    private static String resp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        responseTv = (TextView) findViewById(R.id.responseTv);
        new ParseJSON().execute();

    }

    private class ParseJSON extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show the progress dialog for loading activity
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {


            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url,ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);


            if(jsonStr!=null) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    users = jsonObject.getJSONArray(TAG_USERS);

                    for(int i=0;i<users.length();i++){
                        JSONObject u = users.getJSONObject(i);

                        JSONObject usr = u.getJSONObject(TAG_USER);

                        String id = usr.getString(TAG_ID);
                        String name = usr.getString(TAG_NAME);
                        String email = usr.getString(TAG_EMAIL);
                        String fname = usr.getString(TAG_FNAME);
                        String lname = usr.getString(TAG_LNAME);
                        String gender = usr.getString(TAG_GENDER);
                        String mobile = usr.getString(TAG_MOBILE);
                        resp = "ID : " + id + "\nName : " + name + "\nEmail : " + email
                                + "\nFirst Name : " + fname + "\nLast Name : " + lname
                                + "\nGender : " + gender + "\nMobile : " + mobile;


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseTv.setText(resp);
                                Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_LONG).show();
                            }
                        });



                    }



                }catch (JSONException jex){
                    Log.d("Error -> ",jex.toString());
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
