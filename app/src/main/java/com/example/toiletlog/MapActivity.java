package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    String androidID;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Navbar navbar = new Navbar();

    TextView addressLabel;
    FloatingActionButton btn_addNewLocation;
    ImageView locationSetter_img;
    boolean isLocationSetterVisible;

    LinearLayout locationBtnContainer;
    boolean isLocationBtnContainerVisible;
    Button btn_chooseLocation, btn_currentLocation;

    LinearLayout confirmBtnContainer;
    boolean isConfirmBtnContainerVisible;
    Button btn_confirmLocation, btn_cancelLocation;

    FrameLayout infoPanel;
    TextView addressField;
    ImageView closeInfoPanelBtn;
    Button btn_removeLocation, btn_rate;
    Marker currentlySelectedMarker;
    RatingBar generalRatingBar;
    TextView txt_rate, txt_reviewCount;


    Button btn_cancel_rate;
    Button btn_confirm_rate;
    RatingBar ratingBar;
    TextView already_reviewed;
    double selectedRatingValue = 0;


    DatabaseReference db_ref;
    List<LocationData> locations = new ArrayList<>();
    Map<String, LocationData> locations2 = new HashMap<>();

    final String KAUNAS_ID = "ChIJQ9NnsXAi50YRvIs3x-DRS2E";

    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap map;
    LatLng kaunas = new LatLng(54.898521, 23.903597);
    final static int REQUEST_CODE = 100;
    boolean firstLoad;

    boolean isUpdating;
    LocationRequest locationRequest;
    LocationCallback locationCallBack;

    Geocoder geocoder;
    List<Address> address;
    private LatLng lastLocation;

    LocationData selectedLocationData = null;

    AlertDialog dialog;

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        firstLoad = true;

        addressLabel = findViewById(R.id.bp_address);
        btn_addNewLocation = findViewById(R.id.add_location);
        btn_addNewLocation.setVisibility(View.VISIBLE);
        locationSetter_img = findViewById(R.id.location_setter);
        isLocationSetterVisible = false;
        locationSetter_img.setVisibility(View.INVISIBLE);

        locationBtnContainer = findViewById(R.id.new_location_container);
        isLocationBtnContainerVisible = false;
        locationBtnContainer.setVisibility(View.INVISIBLE);
        btn_chooseLocation = findViewById(R.id.btn_choose_location);
        btn_currentLocation = findViewById(R.id.btn_current_location);

        confirmBtnContainer = findViewById(R.id.new_location_confirm);
        isConfirmBtnContainerVisible = false;
        confirmBtnContainer.setVisibility(View.INVISIBLE);
        btn_confirmLocation = findViewById(R.id.btn_confirm_location);
        btn_cancelLocation = findViewById(R.id.btn_cancel_location);

        infoPanel = findViewById(R.id.info_panel);
        infoPanel.setVisibility(View.INVISIBLE);
        addressField = findViewById(R.id.address_field);
        closeInfoPanelBtn = findViewById(R.id.btn_close_info_panel);
        btn_removeLocation = findViewById(R.id.btn_remove_location);
        btn_rate = findViewById(R.id.btn_rate);
        generalRatingBar = findViewById(R.id.general_rating_bar);
        txt_rate = findViewById(R.id.rating_label);
        txt_reviewCount = findViewById(R.id.review_count);

        currentlySelectedMarker = null;

        //InstantiateAppBarAndNav();
        navbar.InstantiateAppBarAndNav(this, R.string.title_map);

        db_ref = FirebaseDatabase.getInstance().getReference("Locations");
//        LocationData vilnius = new LocationData("Vilnius", 54.687978, 25.278062);
//        db_ref.child(vilnius.getTitle()).setValue(vilnius);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(1000)
                .build();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_addNewLocation.setOnClickListener(addLocationListener);
        btn_currentLocation.setOnClickListener(addCurrentLocationListener);
        btn_chooseLocation.setOnClickListener(addChosenLocationListener);
        btn_confirmLocation.setOnClickListener(confirmLocationListener);
        btn_cancelLocation.setOnClickListener(cancelLocationListener);

        closeInfoPanelBtn.setOnClickListener(closeInfoPanelBtnListener);
        btn_removeLocation.setOnClickListener(removeLocationBtnListener);
        btn_rate.setOnClickListener(rateBtnListener);


        View customDialog = LayoutInflater.from(MapActivity.this).inflate(R.layout.custom_rating_layout, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapActivity.this);
        dialogBuilder.setView(customDialog);
        btn_cancel_rate = customDialog.findViewById(R.id.btn_canel_rate);
        btn_confirm_rate = customDialog.findViewById(R.id.btn_confirm_rate);
        ratingBar = customDialog.findViewById(R.id.rate_bar);
        already_reviewed = customDialog.findViewById(R.id.already_reviewed);
        dialog = dialogBuilder.create();

        btn_cancel_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btn_confirm_rate.setOnClickListener(confirmRateBtnListener);
        ratingBar.setOnRatingBarChangeListener(changeRatingBarListener);

        androidID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        prefs = getSharedPreferences("locationsSharedPrefs", MODE_PRIVATE);
        prefsEditor = prefs.edit();

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.custom_map_style));

//        googleMap.addMarker(new MarkerOptions().position(kaunas).title("Marker in Kaunas"));
        AskPermission();

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    UpdateLocation(location);
                }
            }
        };

        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12.0f));

        locations.clear();
        locations2.clear();

        db_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    LocationData data = snapshot.getValue(LocationData.class);
                    if (data.getRatingItemList() == null){
                        data.ratingItemList = new ArrayList<>();
                    }
//                    locations.add(data);
                    locations2.put(snapshot.getKey(), data);
                    AddMarkerToMap(data, snapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (currentlySelectedMarker != null){
                    currentlySelectedMarker.remove();
                    currentlySelectedMarker = null;
                    selectedLocationData = null;
                    SetInfoPanelVisible(false);
                    SetAddLocationBtnVisible(true);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        LatLng location;
//
//        for(LocationData data : locations){
//            location = new LatLng(data.getLatitude(), data.getLongitude());
//            googleMap.addMarker(new MarkerOptions().position(location).title(data.getTitle()));
//        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (!(isConfirmBtnContainerVisible || isLocationBtnContainerVisible || isLocationSetterVisible || infoPanel.getVisibility() == View.VISIBLE)) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18.0f));
                    googleMap.getUiSettings().setAllGesturesEnabled(false);

                    GetSingleLocation(marker);

                }


                return true;
            }
        });
    }

    void OpenInfoPanel(Marker marker){
        addressField.setText(GetAddressStreet(marker.getPosition()));
        SetInfoPanelVisible(true);
        SetAddLocationBtnVisible(false);
        currentlySelectedMarker = marker;
        SetRating();
    }

    void SetRating(){
        DecimalFormat format = new DecimalFormat("0.0");
        if (selectedLocationData != null){
            List<RatingItem> list = new ArrayList<>(selectedLocationData.getRatingItemList());
            if (list.size() == 0){
                txt_rate.setText("No reviews yet");
                txt_reviewCount.setText("Reviews: 0");
                generalRatingBar.setRating(0);
            }else{
                double sum = 0.0;
                for(RatingItem item : list){
                    sum += item.getRating();
                }
                txt_rate.setText(format.format(sum/list.size()));
                txt_reviewCount.setText("Reviews: "+list.size());
                generalRatingBar.setRating((float) sum/list.size());
            }
        }
    }

    private void GetSingleLocation(Marker marker){

        DatabaseReference db_location  = db_ref.child(GetAddressStreet(marker.getPosition()));
        db_location.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedLocationData = snapshot.getValue(LocationData.class);
                if (selectedLocationData.getRatingItemList() == null){
                    selectedLocationData.ratingItemList = new ArrayList<>();
                }
                OpenInfoPanel(marker);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                selectedLocationData = null;
            }
        });
    }

    private void AddMarkerToMap(double latitude, double longitude, String title) {
        LatLng location = new LatLng(latitude, longitude);
        map.addMarker(
                new MarkerOptions()
                        .position(location)
                        .title(title)
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_location_on_24))
        );
    }
    void AddMarkerToMap(LocationData data, String title){
        AddMarkerToMap(data.getLatitude(), data.getLongitude(), title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
                Message.ShowNotification(MapActivity.this, getApplicationContext(),
                        "Test notification", "Hello " + prefs.getString("name", "") + "!!!");
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (navbar.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            navbar.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingPermission")
    void StartLocationUpdates() {

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
    }

    void StopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    void GetLastLocation(boolean addNewLocation) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

//                        MarkerOptions markerOptions = new MarkerOptions()
//                                .position(latLng)
//                                .title("User location")
//                                .icon(BitmapDescriptorFactory.);
//                        //.icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_location_on_24));
//                        Marker marker = map.addMarker(markerOptions);
                        //lastLocation = latLng;
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

                        if (addNewLocation){
                            AddNewLocation(latLng.latitude, latLng.longitude, GetAddress(latLng) ,GetAddressStreet(latLng));
                        }
                        //StartLocationUpdates();
                    }
                }
            });
            //fusedLocationProviderClient.requestLocationUpdates()
        }

    }

    void ShowLocationAddress(LatLng location){
        String chosenAddress = GetAddress(location);
        //Message.ShowToast(getApplicationContext(), chosenAddress);
        //addressLabel.setText(chosenAddress);
    }

    String GetAddress(double latitude, double longitude){
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return address.get(0).getAddressLine(0);
    }

    String GetAddress(LatLng location){
        return GetAddress(location.latitude, location.longitude);
    }

    String GetAddressStreet(double latitude, double longitude){
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return address.get(0).getThoroughfare() + " " +
                address.get(0).getSubThoroughfare() + " " +
                address.get(0).getLocality();
    }

    String GetAddressStreet(LatLng location){
        return GetAddressStreet(location.latitude, location.longitude);
    }

    void UpdateLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    void AskPermission() {
        ActivityCompat.requestPermissions(MapActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);

                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

                GetLastLocation(false);

            }
            else {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(kaunas, 10.0f));
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    View.OnClickListener addLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Message.ShowToast(getApplicationContext(), "add new");
            ToggleBtnPanel();
        }
    };

    View.OnClickListener addCurrentLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GetLastLocation(true);
            SetBtnPanelVisible(false);
        }
    };

    View.OnClickListener addChosenLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SetLocationCursorVisible(true);
            SetBtnPanelVisible(false);
            SetConfirmBtnPanelVisible(true);
            SetAddLocationBtnVisible(false);
        }
    };

    View.OnClickListener confirmLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SetLocationCursorVisible(false);
            SetAddLocationBtnVisible(true);
            SetBtnPanelVisible(false);
            SetConfirmBtnPanelVisible(false);

            LatLng location = map.getCameraPosition().target;
            AddNewLocation(location.latitude, location.longitude, GetAddress(location), GetAddressStreet(location));
        }
    };

    View.OnClickListener cancelLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SetLocationCursorVisible(false);
            SetAddLocationBtnVisible(true);
            SetBtnPanelVisible(true);
            SetConfirmBtnPanelVisible(false);
        }
    };

    View.OnClickListener closeInfoPanelBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SetInfoPanelVisible(false);
            SetAddLocationBtnVisible(true);
            currentlySelectedMarker = null;
            selectedLocationData = null;
            map.getUiSettings().setAllGesturesEnabled(true);
        }
    };

    View.OnClickListener removeLocationBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Message.ShowToast(getApplicationContext(), "delete");
            //show confirmation dialog
            ShowDeleteLocationDialog();
        }
    };

    View.OnClickListener rateBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(selectedLocationData != null){

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                if (prefs.contains(GetPrefKey())){
                    int i = prefs.getInt(GetPrefKey(), 0);
                    selectedRatingValue = selectedLocationData.ratingItemList.get(i).getRating();
                    btn_confirm_rate.setText("Update");
                    already_reviewed.setVisibility(View.VISIBLE);
                }else {
                    selectedRatingValue = 2.5;
                    btn_confirm_rate.setText("Confirm");
                    already_reviewed.setVisibility(View.GONE);
                }

                ratingBar.setRating((float) selectedRatingValue);

                UpdateLocationInfo(selectedLocationData);
            }
        }
    };

    View.OnClickListener confirmRateBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (prefs.contains(GetPrefKey())){
                selectedLocationData.ratingItemList.set(prefs.getInt(GetPrefKey(), 0), new RatingItem(selectedRatingValue));
            }else {
                selectedLocationData.ratingItemList.add(new RatingItem(selectedRatingValue));
                prefsEditor.putInt(GetPrefKey(), selectedLocationData.ratingItemList.size()-1);
                prefsEditor.commit();
            }
            UpdateLocationInfo(selectedLocationData);
            GetSingleLocation(currentlySelectedMarker);
            dialog.cancel();
        }
    };

    RatingBar.OnRatingBarChangeListener changeRatingBarListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            selectedRatingValue = (double) v;
        }
    };

    String GetPrefKey(){

        return androidID + selectedLocationData.getTitle();
    }

    void ShowDeleteLocationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
        alertDialogBuilder.setTitle("Remove selected location");
        alertDialogBuilder.setMessage("Confirm remove selected location").setCancelable(false)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete item from db
                        if (currentlySelectedMarker != null){
                            RemoveLocation();
                            map.getUiSettings().setAllGesturesEnabled(true);
                            Message.ShowToast(getApplicationContext(), "removed");
                        }else {
                            Message.ShowToast(getApplicationContext(), "No marker selected");
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialogBuilder.create().show();
    }

    void ToggleLocationCursor(){
        if (locationSetter_img != null) {
            if (isLocationSetterVisible) {
                SetLocationCursorVisible(false);
            } else {
                SetLocationCursorVisible(true);
            }
        }
    }

    void SetLocationCursorVisible(boolean visible){
        if (locationSetter_img != null) {
            if (!visible) {
                locationSetter_img.setVisibility(View.INVISIBLE);
                isLocationSetterVisible = false;
            } else {
                locationSetter_img.setVisibility(View.VISIBLE);
                isLocationSetterVisible = true;
            }
        }
    }

    void ToggleBtnPanel(){
        if (locationBtnContainer != null){
            if (isLocationBtnContainerVisible){
                SetBtnPanelVisible(false);
            }else{
                SetBtnPanelVisible(true);
            }
        }
    }

    void SetBtnPanelVisible(boolean visible){
        if (locationBtnContainer != null){
            if (!visible){
                locationBtnContainer.setVisibility(View.INVISIBLE);
                btn_addNewLocation.setImageResource(R.drawable.baseline_add_location_24);
                isLocationBtnContainerVisible = false;
            }else{
                locationBtnContainer.setVisibility(View.VISIBLE);
                btn_addNewLocation.setImageResource(R.drawable.baseline_arrow_forward_ios_24);
                isLocationBtnContainerVisible = true;
            }
        }
    }

    void SetConfirmBtnPanelVisible(boolean visible){
        if (confirmBtnContainer != null){
            if (!visible){
                confirmBtnContainer.setVisibility(View.INVISIBLE);
                isConfirmBtnContainerVisible = false;
            }else{
                confirmBtnContainer.setVisibility(View.VISIBLE);
                isConfirmBtnContainerVisible = true;
            }
        }
    }

    void SetAddLocationBtnVisible(boolean visible){
        if (btn_addNewLocation != null){
            if (!visible){
                btn_addNewLocation.setVisibility(View.INVISIBLE);
            }else{
                btn_addNewLocation.setVisibility(View.VISIBLE);
            }
        }
    }

    void SetInfoPanelVisible(boolean visible){
        if (infoPanel != null){
            if (!visible){
                infoPanel.setVisibility(View.INVISIBLE);
            }else{
                infoPanel.setVisibility(View.VISIBLE);
            }
        }
    }

    void AddNewLocation(double latitude, double longitude, String title, String key){

        LocationData data = new LocationData(title, latitude, longitude);
        db_ref.child(key.replace(".", "")).setValue(data);
    }

    void UpdateLocationInfo(LocationData locationData){
        db_ref.child(GetAddressStreet(
                        locationData.getLatitude(),
                        locationData.getLongitude()
                ).replace(".", "")).setValue(locationData);
    }

    void RemoveLocation(){
        db_ref.child(currentlySelectedMarker.getTitle()).removeValue();
    }

}