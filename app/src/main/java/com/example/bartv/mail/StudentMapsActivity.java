package com.example.bartv.mail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class StudentMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        Intent i = getIntent();

        final String zipCode = i.getStringExtra("ZIPCODE");
        final String image = i.getStringExtra("IMAGE");
        final String klas = i.getStringExtra("KLAS");
        final String studentNr = i.getStringExtra("STUDENTNUMBER");
        final String naam = i.getStringExtra("NAAM");


        final Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String uri = "@drawable/" + image;
                int imageResource = this.getResources().getIdentifier(uri, null, this.getPackageName());

                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(imageResource);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 300, 400, false);

                address = addresses.get(0);
                mMap = googleMap;

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                        LinearLayout info = new LinearLayout(context);
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(context);
                        title.setTextColor(Color.BLUE);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(context);
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);
                        return info;
                    }
                });

                LatLng street = new LatLng(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(street).title("Marker by " + naam).snippet("StudentNr: " + studentNr +
                                                                                                "\n" + "Name: " + naam +
                                                                                                "\n" + "Class: " + klas +
                                                                                                "\n" + "Zipcode: " + zipCode).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(street));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // handle exception
        }

    }
}
