package com.example.toiletlog;


import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    Context mContext;
    List<PhotoItem> mPhotos;
    LocationData locationData;

    DatabaseReference db_ref;

    public ImageAdapter(Context context, List<PhotoItem> items, LocationData locationData){
        mContext = context;
        mPhotos = items;
        this.locationData = locationData;
        db_ref = FirebaseDatabase.getInstance().getReference("Locations");
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_view_info_panel, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        PhotoItem itemCurrent = mPhotos.get(position);
        Picasso.get().load(itemCurrent.getUrl()).fit().centerCrop().into(holder.imageView);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ShowDeleteLocationDialog(itemCurrent);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ImageViewHolder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_adapter);
        }
    }

    void ShowDeleteLocationDialog(PhotoItem item){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Remove selected image");
        alertDialogBuilder.setMessage("Confirm remove selected image").setCancelable(false)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Delete item from db
                        locationData.photoItemList.remove(item);
                        UpdateLocationInfo(locationData);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialogBuilder.create().show();
    }

    void UpdateLocationInfo(LocationData locationData){
        String child = GetAddressStreet(
                locationData.getLatitude(),
                locationData.getLongitude()
        ).replace(".", "");
        db_ref.child(child).setValue(locationData);
    }

    String GetAddressStreet(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> address;
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return address.get(0).getThoroughfare() + " " +
                address.get(0).getSubThoroughfare() + " " +
                address.get(0).getLocality();
    }
}
