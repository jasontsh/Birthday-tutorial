package com.android.cis195.birthday;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jason on 10/6/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.UserViewHolder> {

    public static final String TAG = "RecyclerAdapter";
    public static final int REQUEST_TAKE_PHOTO = 1;

    private ArrayList<User> users;
    private AppCompatActivity activity;

    public RecyclerAdapter (ArrayList<User> users, AppCompatActivity activity) {
        this.users = users;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType " + users.get(position).getName() + users.get(position).getUrl());
        return users.get(position).getUrl().isEmpty() ? 1 : 2;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.birthday_swipe_item, parent, false);
            Log.d(TAG, "onCreateViewHolder");
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.birthday_image_swipe_item, parent, false);
            Log.d(TAG, "actually we have an url");
            return new UserImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final User user = users.get(position);
        String text = user.getName() + " Birthday: " + user.getMonth() + " " + user.getDay();
        holder.tv.setText(text);
        Log.d(TAG, "onBindViewHolder");
        if (user.getUrl().isEmpty()) {
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tv.setTag(user);
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        int requestCode = 1;
                        ArrayList<String> permissions = new ArrayList<>(2);
                        if (ContextCompat.checkSelfPermission(activity,
                                Manifest.permission.CAMERA) !=
                                PackageManager.PERMISSION_GRANTED) {
                            permissions.add(Manifest.permission.CAMERA);
                            requestCode++;
                        }
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        ActivityCompat.requestPermissions(activity, permissions.toArray(new String[2]), requestCode);
                    } else if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CAMERA) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 3);
                    } else {
                        takePhotoForUser(user);
                    }
                }
            });
        } else {
            UserImageViewHolder viewHolder = (UserImageViewHolder) holder;
            Picasso.with(activity).load(user.getUrl()).into(viewHolder.iv);
        }
        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper helper = new DBHelper(view.getContext());
                helper.deleteUser(user.getName());
                users.remove(user);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ImageButton ib;

        public UserViewHolder (View v) {
            super(v);
            tv = v.findViewById(R.id.result);
            ib = v.findViewById(R.id.delete);
        }
    }

    public static class UserImageViewHolder extends UserViewHolder {
        public ImageView iv;

        public UserImageViewHolder (View v) {
            super(v);
            iv = v.findViewById(R.id.image);
        }
    }

    public void takePhotoForUser(User user) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(activity, "Something weird happened...", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.android.cis195.birthday",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                user.setUrl(photoURI.toString());
                DBHelper helper = new DBHelper(activity);
                helper.insertUser(user);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
