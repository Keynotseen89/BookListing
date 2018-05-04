/**
 * author: Quinatzin Sintora;
 * data: 05/02/2018
 */
package com.example.quinatzin.booklisting;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Books> {

    /**
     * Default constructor
     *
     * @param context
     * @param resource
     */
    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // create object of Book to obtain position
        Books bookData = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // obtain textView of both title and author id
        ImageView imageView = convertView.findViewById(R.id.image_viewID);
        TextView titleString = convertView.findViewById(R.id.title_id);
        TextView authorString = convertView.findViewById(R.id.author_id);

        // set both "Title & Author" to values of
        // bookData
        //imageView.setImageURI(Uri.parse(bookData.image));
        //imageView.setImageDrawable(bookData.image);
        imageView.setImageBitmap(bookData.bitmap);

        titleString.setText(bookData.title);
        authorString.setText(bookData.author);

        // return the view of List Item
        return convertView;

    }

}

