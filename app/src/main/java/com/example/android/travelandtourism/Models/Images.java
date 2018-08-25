package com.example.android.travelandtourism.Models;
import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class Images  implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("path")
    @Expose
    private String path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
