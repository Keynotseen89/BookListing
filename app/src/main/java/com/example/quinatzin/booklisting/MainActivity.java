/**
 * author: Quinatzin Sintora
 * date: 05/02/2018
 */
package com.example.quinatzin.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String SEARCH = "booksSearchResults";

    private EditText searchBooks;
    private ImageButton searchButton;
    private ListView listView;
    private TextView textView;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtain each values by id
        searchBooks = findViewById(R.id.editText_id);
        searchButton = findViewById(R.id.imageButton);
        textView = findViewById(R.id.textView_id);

        // create a new adapter
        adapter = new CustomAdapter(this, -1);

        //set listView to new adapter
        listView = findViewById(R.id.listView_id);
        listView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnectionOpen()){
                    DownloadFilesTask task = new DownloadFilesTask();
                    task.execute();
                }else{
                    Toast.makeText(MainActivity.this, "Error no connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(savedInstanceState != null){
            Books[] bookData = (Books[]) savedInstanceState.getParcelableArray(SEARCH);
            adapter.addAll(bookData);
        }
    }

    /**
     * check if there is Internet Connection
     *
     * @return networkInformation connection
     */
    private boolean isConnectionOpen() {

        // get connection
        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // get network connection information
        NetworkInfo networkInfo = connection.getActiveNetworkInfo();

        // return networkInfo
        return networkInfo.isConnectedOrConnecting();
    }

    /**
     * updata Ui ListView adapter
     *
     * @param bookData
     */
    private void updateUi(List<Books> bookData) {

        // check if List is empty if so
        // set display a Message in textView
        if (bookData.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            // if List is not empty hide textView
            textView.setVisibility(View.GONE);
        }

        // clear adapter
        // populate adapter with bookData
        adapter.clear();
        adapter.addAll(bookData);
    }

    /**
     * get user input
     *
     * @return searchBooks
     */
    private String getInput() {
        return searchBooks.getText().toString();
    }

    private String getHttpRequest() {
        // url used to search for books
        final String url = "  https://www.googleapis.com/books/v1/volumes?q=search+";
        // trim user input and replace space with "+"
        String userInput = getInput().trim().replace("\\s+", "+");
        // combine both url and userInput into one String
        String newUrl = url + userInput;

        //return newUrl
        return newUrl;
    }


    /**
     * AsyncTask used for populating Lists of books
     */
    private class DownloadFilesTask extends AsyncTask<URL, Void, List<Books>> {

        protected List<Books> doInBackground(URL... urls) {
            // obtain url to search in
            URL url = createURL(getHttpRequest());

            // empty string to populate with values
            String jsonResponse = "";

            try {
                // get connection and apply values
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //
            List<Books> bookData = parseJson(jsonResponse);
            return bookData;
        }

        /**
         * @param bookData
         */
        protected void onPostExecute(List<Books> bookData) {
            // check if List is null if so
            // return null
            if (bookData == null) {
                return;
            }
            // update the UI with Data from List
            updateUi(bookData);
        }

        /**
         * Create URL
         *
         * @param urlString
         * @return
         */
        private URL createURL(String urlString) {
            try {
                //return URL with inputValues
                return new URL(urlString);
            } catch (MalformedURLException e) {
                // check if value is incorrect if so
                // return null and stackTrace
                e.printStackTrace();
                return null;
            }
        }

        /**
         * obtain HttpRequest
         *
         * @param url
         * @return
         * @throws IOException
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // if url request is null return empty string
            if (url == null) {
                return jsonResponse;
            }

            // give value of null to connection
            HttpURLConnection urlConnection = null;
            // give value of inputStream to null
            InputStream inputStream = null;

            try {
                // add a value of openConnection to variable
                urlConnection = (HttpURLConnection) url.openConnection();

                // setRequest to get to obtain search values
                urlConnection.setRequestMethod("GET");

                // set read time for obtaining data
                urlConnection.setReadTimeout(10000 /* milliseconds */);

                // set connection timeout
                urlConnection.setConnectTimeout(15000 /* milliseconds */);

                // connect the value
                urlConnection.connect();

                // check if connection successful
                if (urlConnection.getResponseCode() == 200) {
                    // obtain inputString
                    inputStream = urlConnection.getInputStream();
                    // set jsonResponse to the inputStream read data
                    jsonResponse = readData(inputStream);
                } else {
                    // if error  display in log for debugging
                    Log.e("mainActivity", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                // check if connection is not null
                // disconnect connection
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                // check if inputStream is not null
                // close inputStream
                if (inputStream != null) {
                    inputStream.close();
                }
            }

            // return jsonResponse
            return jsonResponse;
        }

        /**
         * @param input
         * @return outputString of stream information
         * @throws IOException
         */
        private String readData(InputStream input) throws IOException {
            StringBuilder outputString = new StringBuilder();

            // check if input Stream is not null
            if (input != null) {

                // add value from InputStream to InputStreamReader
                InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.forName("UTF-8"));

                // add value of inputStreamReader into BufferedReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                // add value of each line in BufferedReader into line
                String line = reader.readLine();

                // while loop if line is not null
                while (line != null) {
                    // append each line to StringBuilder of outputString
                    outputString.append(line);
                    // read each line
                    line = reader.readLine();
                }
            }

            // return the new value of outputString to a string
            return outputString.toString();
        }

        /**
         * parse the JSON and add to List of bookData
         *
         * @param json
         * @return List
         */
        private List<Books> parseJson(String json) {
            if (json == null) {
                return null;
            }
            List<Books> bookData = QueryData.dataExtration(json);
            return bookData;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Books[] books = new Books[adapter.getCount()];
        for (int i = 0; i < books.length; i++) {
            books[i] = adapter.getItem(i);
        }
        outState.putParcelableArray(SEARCH, (Parcelable[]) books);
    }

}








