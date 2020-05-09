
package dev.pokete.chestinvexport.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BooksData {

    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("books")
    @Expose
    private List<Book> books = null;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

}
