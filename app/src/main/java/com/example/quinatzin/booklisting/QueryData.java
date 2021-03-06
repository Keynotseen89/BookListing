/**
 * author: Quinatzin Sintora
 * date: 05/02/2018
 */
package com.example.quinatzin.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryData {

    public QueryData() {
    }

    /**
     * JSON formatList for saving Authors
     * information
     *
     * @param listAuthors
     * @return
     * @throws JSONException
     */
    public static String formatList(JSONArray listAuthors) throws JSONException {

        String listAuthorsString = null;

        //Check if the JSONArray size is zero
        //if zero return null
        if (listAuthors.length() == 0) {
            return null;
        }

        // Go through the JSON listAuthor and create a
        // format splitting them into section
        for (int index = 0; index < listAuthors.length(); index++) {
            if (index == 0) {
                listAuthorsString = listAuthors.getString(0);
            } else {
                listAuthorsString += ", " + listAuthors.getString(index);
            }
        }
        // return list listAruthorsString with values
        return listAuthorsString;
    }

    /**
     * Create list of books from extracting
     * data into a JSON
     *
     * @param json
     * @return
     */
    public static List<Books> dataExtration(String json) {

        //List for holding booksData
        List<Books> booksData = new ArrayList<>();


        try {
            JSONObject jsonResponse = new JSONObject(json);

            // if totalItems is zero return List of booksData
            if (jsonResponse.getInt("totalItems") == 0) {
                return booksData;
            }

            // obtain items in jsonResponse
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            // read through jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);

                JSONObject bookInfo = bookObject.getJSONObject("volumeInfo");

                String title = bookInfo.getString("title");
                JSONArray authorsArray = bookInfo.getJSONArray("authors");
                String authors = formatList(authorsArray);

                JSONObject imageObject = bookInfo.getJSONObject("imageLinks");
                String imageDisplay = imageObject.getString("thumbnail");
                //String imageLink = bookInfo.getString("imageLinks");
                //String imageDisplay = imageLink + book
                Bitmap bitmap = getBitmapFromURL(imageDisplay);
                //System.out.println("LOG FOR IMAGES: " + " " + imageDisplay);
               // Log.d(" LOG QUERY DATA IMAGE: ", imageDisplay);

                //Bitmap bitmap = BitmapFactory.decodeStream(imageDisplay);
                Books book = new Books(authors, title, bitmap);
                booksData.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return booksData;
    }

    public static Bitmap getBitmapFromURL(String src){
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
}//end of QueryData
