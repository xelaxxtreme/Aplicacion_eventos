package my.wab.festejaper.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import my.wab.festejaper.R;

public class ubicacion extends Fragment {

    String[] datos = new String[10];
    String Departamento,Provincia,Direccion,Local;
    private GoogleMap mMap;

    private AdView mAdView;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Geocoder geo =new Geocoder(getContext());
            try{
                List<Address>adress =geo.getFromLocationName(Direccion+", "+Provincia+","+Departamento,1);
                mMap =googleMap;
                LatLng localizacion =new LatLng(adress.get(0).getLatitude(),adress.get(0).getLongitude());
                mMap.addMarker(new MarkerOptions().position(localizacion).title(Local));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(localizacion));
                CameraPosition posicicionCamara =new CameraPosition.Builder()
                        .target(localizacion)
                        .zoom(17)
                        .bearing(0)
                        .tilt(25)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(posicicionCamara));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(getArguments()!=null)
        {
            datos = getArguments().getStringArray("datos");
        }

        Departamento = datos[2];
        Provincia = datos[9];
        Direccion = datos [3];
        Local = datos[7];
        View view =  inflater.inflate(R.layout.fragment_ubicacion, container, false);
        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}