package ricky.maptest;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionCallback {

    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //Simulation data
    LatLng coordinateFighter = new LatLng(23.453505, 120.559029);
    LatLng coordinateFighter2 = new LatLng(23.465081, 120.555362);
    LatLng coordinateFighter3 = new LatLng(23.461312, 120.551867);

    LatLng coordinateFire = new LatLng(23.456858, 120.551261);
    LatLng coordinateMe = new LatLng(23.454866, 120.552614);

    Button userBtn, fighterBtn;
    Polyline polyline;
    PolylineOptions polylineOptions;

    List<Polyline> polylines = new ArrayList<Polyline>();

    LatLng coordinateFighters[] = new LatLng[3];

    ArrayList<LatLng> directionPositionList;
    Leg leg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            // Show rationale and request permission.
//        }


        mMap.addMarker(new MarkerOptions().position(coordinateMe).title("my location"));

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.firefighter);
        mMap.addMarker(new MarkerOptions().position(coordinateFighter).title("fire fighter").icon(icon));

        mMap.addMarker(new MarkerOptions().position(coordinateFighter2).title("fire fighter2").icon(icon));
        mMap.addMarker(new MarkerOptions().position(coordinateFighter3).title("fire fighter3").icon(icon));

        BitmapDescriptor bonfire = BitmapDescriptorFactory.fromResource(R.drawable.bonfire);
//        mMap.addMarker(new MarkerOptions().position(coordinateFire).title("fire").icon(bonfire));
//        mMap.addMarker(new MarkerOptions().position(coordinateFire2).title("fire").icon(bonfire));

        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(coordinateMe, 15);
        mMap.animateCamera(myLocation);
        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(23.457378, 120.551211), new LatLng(23.457975, 120.550220), new LatLng(23.457301, 120.550022), new LatLng(23.456292, 120.550738), new LatLng(23.456989, 120.551753))
                .fillColor(getResources().getColor(R.color.fireArea)).strokeWidth(0));

        initLayout();


    }


    private void initLayout() {
        userBtn = (Button) findViewById(R.id.userBtn);
        fighterBtn = (Button) findViewById(R.id.fighterBtn);
        fighterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = ProgressDialog.show(MapsActivity.this, "", "Loading...",
                        true);
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        calculateAllFighter(coordinateFighter, coordinateFire, Color.CYAN);
                        calculateAllFighter(coordinateFighter3, coordinateFire, Color.CYAN);
                        dialog.dismiss();
                    }
                }, 1500);


            }
        });
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = ProgressDialog.show(MapsActivity.this, "", "Detecting...",
                        true);
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        calculateAllFighter(coordinateFighter, coordinateMe, Color.RED);
                        dialog.dismiss();
                    }
                }, 1500);

            }
        });
    }


    private void calculateAllFighter(final LatLng coordinateA, final LatLng coordinateB, final int color) {
//        if(polyline!=null){
//            polyline.remove();
//        }

        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();
        GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                .from(coordinateA)
                .to(coordinateB)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {

                        String status = direction.getStatus();
                        Log.v("status", "安安" + status);

                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            leg = route.getLegList().get(0);

                            directionPositionList = leg.getDirectionPoint();
                            if (!directionPositionList.get(directionPositionList.size() - 1).equals(coordinateB)) {
                                Log.v("coordinateFighter", "not");
                                directionPositionList.add(coordinateB);
                            }
                            if (!directionPositionList.get(0).equals(coordinateA)) {
                                directionPositionList.add(0, coordinateA);


                            }

                            polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, color);
                            polylines.add(mMap.addPolyline(polylineOptions));

//                            mMap.addPolyline(polylineOptions);

                            Log.v("onDirectionSuccess", "direction.isOK()");
                        } else {
                            Log.v("onDirectionSuccess", "!direction.isOK()");

                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.v("onDirectionFailure", "" + t.toString());

                    }
                });
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }


}
