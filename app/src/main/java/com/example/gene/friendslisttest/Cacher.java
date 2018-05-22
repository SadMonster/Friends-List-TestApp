package com.example.gene.friendslisttest;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class Cacher {


    public static boolean writeCache (String data, Context context, String cachingURL) {

        String fileName = Uri.parse(cachingURL).getLastPathSegment();

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();

        } catch (IOException e) { }

        return true;
    }

    public static String readCache (String cachingURL, Context context) {
        String fileName = Uri.parse(cachingURL).getLastPathSegment();
        StringBuffer output = new StringBuffer();
        FileInputStream fin = null;

        try {
            fin = context.openFileInput(fileName);

            byte[] buffer = new byte[1024];

            int i;
            while ((i = fin.read(buffer)) != -1) {
                output.append(new String(buffer, 0, i));
            }

            fin.close();

        } catch (IOException e) {}

        return output.toString();
    }
}
