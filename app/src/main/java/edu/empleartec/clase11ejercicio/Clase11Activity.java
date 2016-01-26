package edu.empleartec.clase11ejercicio;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.empleartec.clase11ejercicio.model.Station;

public class Clase11Activity extends AppCompatActivity
                             implements GetStationsTask.GetStationsCallback,
                                OnMapReadyCallback {

    private static final String TAG = "Clase11Activity";
    private ProgressDialog progressDialog;

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase11);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clase11, menu);
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

    @Override
    public void prepareUI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_stations));
        progressDialog.show();
    }

    @Override
    public void onStationsRetrieved(List<Station> stations) {
        // TODO: mostrarlos en el mapa
        progressDialog.dismiss();
        progressDialog = null;

        if (mGoogleMap==null) return;

        BitmapDescriptor bikeGreen = BitmapDescriptorFactory.fromResource(R.drawable.bike_green_xxhdpi);
        BitmapDescriptor bikeOrange = BitmapDescriptorFactory.fromResource(R.drawable.bike_orange_xxhdpi);
        BitmapDescriptor bikeBlue = BitmapDescriptorFactory.fromResource(R.drawable.bike_blue_xxhdpi);
        // stations + map !
        for (Station station: stations) {
            LatLng latLng = new LatLng(
                    station.location.getLatitude(),
                    station.location.getLongitude());
            BitmapDescriptor icon = null;
            if (station.freeSlots==0) {
                icon = bikeOrange;
            } else if (station.freeSlots < ((float)station.totalSlots)/2) {
                icon = bikeGreen;
            } else {
                icon = bikeBlue;
            }

            mGoogleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(station.name)
                            .snippet(station.locationText)
                            .icon(icon)
            );

        }
        Station lastStation = stations.get(stations.size()-1);
        Location lastStationLoc = lastStation.location;
        LatLng lastStationLatLng = new LatLng(
                lastStationLoc.getLatitude(), lastStationLoc.getLongitude());

        mGoogleMap.animateCamera(
                CameraUpdateFactory
                        .newLatLngZoom(lastStationLatLng, 14));

        mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void taskCanceled() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        new GetStationsTask(this, this).execute();
    }
}
