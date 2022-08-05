package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.maps;


import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class OnMapAndViewReadyListener implements ViewTreeObserver.OnGlobalLayoutListener, OnMapReadyCallback {

    /** A listener that needs to wait for both the GoogleMap and the View to be initialized. */
    public interface OnGlobalLayoutAndMapReadyListener {
        void onMapReady(GoogleMap googleMap);
    }

    private final SupportMapFragment mapFragment;
    private final View mapView;
    private final OnGlobalLayoutAndMapReadyListener devCallback;

    private boolean isViewReady;
    private boolean isMapReady;
    private GoogleMap googleMap;

    public OnMapAndViewReadyListener(
            SupportMapFragment mapFragment, OnGlobalLayoutAndMapReadyListener devCallback) {
        this.mapFragment = mapFragment;
        mapView = mapFragment.getView();
        this.devCallback = devCallback;
        isViewReady = false;
        isMapReady = false;
        googleMap = null;

        registerListeners();
    }

    private void registerListeners() {
        // View layout.
        if ((mapView.getWidth() != 0) && (mapView.getHeight() != 0)) {
            // View has already completed layout.
            isViewReady = true;
        } else {
            // Map has not undergone layout, register a View observer.
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        // GoogleMap. Note if the GoogleMap is already ready it will still fire the callback later.
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // NOTE: The GoogleMap API specifies the listener is removed just prior to invocation.
        this.googleMap = googleMap;
        isMapReady = true;
        fireCallbackIfReady();
    }

    @SuppressWarnings("deprecation")  // We use the new method when supported
    @SuppressLint("NewApi")  // We check which build version we are using.
    @Override
    public void onGlobalLayout() {
        // Remove our listener.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        isViewReady = true;
        fireCallbackIfReady();
    }

    private void fireCallbackIfReady() {
        if (isViewReady && isMapReady) {
            devCallback.onMapReady(googleMap);
        }
    }
}

