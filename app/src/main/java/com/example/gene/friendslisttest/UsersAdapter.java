package com.example.gene.friendslisttest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<FriendModel> {

    private Context context;

    public UsersAdapter(Context context, ArrayList<FriendModel> users) {
        super(context, 0, users);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FriendModel user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.Name);
        tvName.setText(user.name);

        ConstraintLayout LL = convertView.findViewById(R.id.Card);

        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Window.class);
                i.putExtra("name",user.name);
                i.putExtra("email",user.email);
                i.putExtra("birth",user.birthday);
                i.putExtra("avatar",user.avatarUrl);
                context.startActivity(i);
            }
        });

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.thumbnail),context).execute(user.thumbURL);

        return convertView;
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Context context;
        String urldisplay;

        public DownloadImageTask(ImageView bmImage, Context context) {
            this.bmImage = bmImage;
            this.context = context;
        }

        protected Bitmap doInBackground(String... urls) {
            urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) { }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) bmImage.setImageBitmap(result);
        }
    }

}