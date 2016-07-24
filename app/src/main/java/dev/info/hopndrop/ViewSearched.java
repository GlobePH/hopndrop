package dev.info.hopndrop;

import android.Manifest;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewSearched extends ListActivity implements AdapterView.OnItemClickListener, DirectionFinderListener {

    ArrayList<HashMap<String, String>> busList;
    Intent intent;

    private SQLiteHandler db;
    ProgressDialog pDialog;
    String token;
    private static final String TAG_ID = "id";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_BUS_NO = "bus_no";
    private static final String TAG_DEPARTURE_TIME = "departure_time";
    private static final String TAG_ROUTE = "routes_id";
    private SessionManager session;
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    String origin,destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_searched);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        db = new SQLiteHandler(this.getApplicationContext());
        session = new SessionManager(this.getApplicationContext());
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        //etOrigin = (EditText) findViewById(R.id.etOrigin);
        //etDestination = (EditText) findViewById(R.id.etDestination);
        sendRequest();

        // Session manager
       
        HashMap<String, String> user = db.getUserDetails();
        token = user.get("token");
        busList = new ArrayList<HashMap<String, String>>();
        busList.clear();
        getBus();
    }

    private void sendRequest() {


        origin = session.getOrigin();
         destination = session.getDestination();


        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

           /* originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));*/
        }
    }

    private void getBus() {
        // Tag used to cancel the request
        String tag_string_req = "get_bus";
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Buss ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_BUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Bus Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String result = jObj.getString("buses");

                    if (!result.equals("")) {
                        JSONArray bus = jObj.getJSONArray("buses");

                        for (int i = 0; i < bus.length(); i++) {
                            JSONObject c = bus.getJSONObject(i);

                            // Storing each json item in variable
                            String id = c.getString("id");
                            String route = c.getString("routes_id");
                            String user_id = c.getString("user_id");
                            String bus_no = c.getString("bus_no");
                            String departure_time = c.getString("departure_time");


                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ID, id);
                            map.put(TAG_ROUTE, route);
                            map.put(TAG_USER_ID, user_id);
                            map.put(TAG_BUS_NO, bus_no);
                            map.put(TAG_DEPARTURE_TIME, departure_time);

                            busList.add(map);
                        }


                        initializeList();


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Login Failed!", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                // Posting parameters to login url
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //params.put("password", password);

                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initializeList() {



        this.runOnUiThread(new Runnable() {
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        ViewSearched.this, busList,
                        R.layout.list_bus,
                        new String[]{TAG_ID, TAG_ROUTE, TAG_USER_ID, TAG_BUS_NO, TAG_DEPARTURE_TIME},
                        new    int[]{R.id.pid, R.id.route, R.id.user_id, R.id.bus_no, R.id.departure_time});
                setListAdapter(adapter);
            }

        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
