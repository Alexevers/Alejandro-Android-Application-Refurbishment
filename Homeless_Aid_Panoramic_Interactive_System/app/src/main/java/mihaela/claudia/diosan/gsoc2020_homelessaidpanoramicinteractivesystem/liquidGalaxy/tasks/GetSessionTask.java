package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.jcraft.jsch.Session;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGUtils;

public  class GetSessionTask extends AsyncTask<Void, Void, Void> {
    Activity activity;
    Session session;

    public GetSessionTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        session = LGUtils.getSession(activity);
        return null;
    }

    @Override
    protected void onPostExecute(Void success) {
        super.onPostExecute(success);
    }
}
