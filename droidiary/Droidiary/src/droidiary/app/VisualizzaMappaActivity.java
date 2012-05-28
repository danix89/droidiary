package droidiary.app;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
 
public class VisualizzaMappaActivity extends MapActivity 
{    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizzamappa);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        DroidiaryItemizedOverlay itemizedoverlay = new DroidiaryItemizedOverlay(drawable, this);
        GeoPoint point;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());    
        try {
        	Log.w("danix", "cacchio");
            List<Address> addresses = geoCoder.getFromLocationName("via melito", 5);
            String add = "";
            Log.w("danix", "addresses.size = " + addresses.size());
            if (addresses.size() > 0) {
                point = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
                        (int) (addresses.get(0).getLongitude() * 1E6));
                OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
                MapController mc = mapView.getController(); ;
                itemizedoverlay.addOverlay(overlayitem);

                mc.animateTo(point);    
                mapView.invalidate();
            }    
        } catch (IOException e) {
        	Log.w("danix", e.getLocalizedMessage());
        }
        
        mapOverlays.add(itemizedoverlay);
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }    
}