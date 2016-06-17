package com.purvik.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	
	
	private ProgressDialog pDialog;
	
    private static String url = "http://purvik.com/videos.php";
    
    private static final int REQUEST_CODE = 1;
    
    //JSON Node Names
    private static final String TAG_VIDEOS = "videos";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_Y_LINK = "y_link";
    private static final String TAG_DESC = "desc";
    
    JSONArray videos = null;
	 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> videoList;
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
        //setContentView(R.layout.activity_main);  
        
//         Log.i("--111--","JSON");
        
//        New Programming
        videoList = new ArrayList<HashMap<String, String>>();
		 
        ListView lv = getListView();
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                
                String desc = ((TextView) view.findViewById(R.id.desc))
                        .getText().toString();
                
                String y_link = ((TextView)view.findViewById(R.id.y_link))
                		.getText().toString();
                
                Toast.makeText(getApplicationContext(), "Name: " + name +
                		"\n E-Mail:" + desc +
                		"\n y_link" + y_link
                		, Toast.LENGTH_LONG).show();
                
                
                
                Intent playerIntent = new Intent(MainActivity.this, PlayerActivity.class);
                playerIntent.putExtra("y_link", y_link);
                playerIntent.putExtra("name", name);
                playerIntent.putExtra("desc", desc);
                
               startActivityForResult(playerIntent, REQUEST_CODE);
                
                //new GetVideos().execute();
                
                
            }
        });
   
        new GetVideos().execute();
        
	}  
	
	//New Programming
	/**
     * Async task class to get json by making HTTP call
     * */
    private class GetVideos extends AsyncTask<Void, Void, Void> {
    	
    	
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
//            Log.i("--222--","JSON");
            
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Video List,Wait..");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
        @Override
        protected Void doInBackground(Void... arg0) {
        	
//        	Log.i("--333--","JSON");
        	
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
//            Log.d("Response: ", "> " + jsonStr);
//            Toast.makeText(getApplicationContext(), "" + jsonStr, Toast.LENGTH_LONG).show();
 
            if (jsonStr != null) {
                try {
                	
//                	Log.i("--444--","JSON");
                	
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    videos = jsonObj.getJSONArray(TAG_VIDEOS);
 
                    // looping through All Contacts
                    for (int i = 0; i < videos.length(); i++) {
                    	
                    	//Take JSON Objects individually
                        JSONObject jObj = videos.getJSONObject(i);
                         
                        String id = jObj.getString(TAG_ID);
                        String name = jObj.getString(TAG_NAME);
                        String y_link = jObj.getString(TAG_Y_LINK);
                        String desc = jObj.getString(TAG_DESC);
                        
                      /*  Log.d("ID: ", "-- " + id);
                        Log.d("Name: ", "-- " + name);
                        Log.d("Y_link: ", "-- " + y_link);
                        Log.d("Description: ", "-- " + desc);
                        */
                        
                        
                        // tmp hashmap for single video
                        HashMap<String, String> video = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        video.put(TAG_ID, id);
                        video.put(TAG_NAME, name);
                        video.put(TAG_Y_LINK, y_link);
                        video.put(TAG_DESC, desc);
                        
                        
 
                        // adding contact to contact list
                        videoList.add(video);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, videoList,
                    R.layout.list_view, new String[] { TAG_NAME, TAG_DESC, TAG_Y_LINK},
                    	new int[] { R.id.name,
                            R.id.desc, R.id.y_link });
 
            setListAdapter(adapter);
        }
 
    }

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
