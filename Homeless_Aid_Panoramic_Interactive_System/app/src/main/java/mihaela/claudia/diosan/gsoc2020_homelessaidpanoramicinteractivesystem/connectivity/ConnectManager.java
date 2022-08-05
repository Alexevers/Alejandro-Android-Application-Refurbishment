package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

public class ConnectManager {
    static String getConnectivityStatusString(Context context) {
        String status;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = context.getResources().getString(R.string.wifi_connected);
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = context.getResources().getString(R.string.mobile_connected);
                return status;
            }
        } else {
            status = context.getResources().getString(R.string.no_network_operating);
            return status;
        }
        return "";
    }

}
