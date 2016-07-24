package dev.info.hopndrop;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import dev.info.hopndrop.Route;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
