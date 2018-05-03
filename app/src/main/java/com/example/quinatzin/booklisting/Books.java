/**
 * author: Quinatzin Sintora
 * Date: 05/02/2018
 */
package com.example.quinatzin.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

public class Books implements Parcelable {

    public String author;
    public String title;

    /**
     * Default constructor
     *
     * @param author
     * @param title
     */
    public Books(String author, String title) {
        this.author = author;
        this.title = title;
    }

    /**
     * Book Parcel for reading author and
     * title of books
     *
     * @param in
     */
    protected Books(Parcel in) {
        author = in.readString();
        title = in.readString();
    }

    /**
     * Parcelable method used for arrays of JSON
     * of Books retrieved
     */
    public static final Parcelable.Creator<Books> CREATOR
            = new Parcelable.Creator<Books>() {
        public Books createFromParcel(Parcel in) {
            return new Books(in);
        }

        public Books[] newArray(int size) {
            return new Books[size];
        }
    };

    /**
     * @return author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return title name
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return amount of content
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * write in parcel into author and title
     * of string
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(title);

    }
}
