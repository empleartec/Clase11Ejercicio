package edu.empleartec.clase11ejercicio;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import edu.empleartec.clase11ejercicio.datasource.EcoBiciStore;
import edu.empleartec.clase11ejercicio.model.Station;

/**
 * Created by gcalero
 */
public class GetStationsTask extends AsyncTask <Void, Void, List<Station>>{

    private final Context context;
    private final GetStationsCallback callback;

    public interface GetStationsCallback {
        public void prepareUI();
        public void onStationsRetrieved(List<Station> stations);
        public void taskCanceled();

    }

    @Override
    protected void onPreExecute() {
        if (callback != null) {
            callback.prepareUI();
        }
    }

    @Override
    protected void onCancelled() {
        if (callback != null) {
            callback.taskCanceled();
        }
    }

    public GetStationsTask(Context context, GetStationsCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected List<Station> doInBackground(Void... voids) {
        return EcoBiciStore.getInstance().getAllStations(context);
    }

    @Override
    protected void onPostExecute(List<Station> stations) {
        if (callback != null) {
            callback.onStationsRetrieved(stations);
        }
    }
}
