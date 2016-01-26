package edu.empleartec.clase11ejercicio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import edu.empleartec.clase11ejercicio.model.Station;

public class Clase11Activity extends AppCompatActivity implements GetStationsTask.GetStationsCallback {

    private static final String TAG = "Clase11Activity";
    private ProgressDialog progressDialog;

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

        new GetStationsTask(this, this).execute();
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
    public void stations(List<Station> stations) {
        // TODO: mostrarlos en el mapa
        progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    public void taskCanceled() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}
