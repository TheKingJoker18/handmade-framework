package utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class ModelView {
    String url;
    HashMap<String, Object> data;

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void initializeData() {
        this.setData(new HashMap<String, Object>());
    }

    public ModelView() {
        this.initializeData();
    }
    public ModelView(String url, HashMap<String, Object> data) {
        this.setUrl(url);
        this.setData(data);
    }

    public void addObject(String name, Object value) {
        HashMap<String, Object> data = this.getData();
        data.put(name, value);
        this.setData(data);
    }

    public void prepareModelView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = this.getUrl();
        HashMap<String, Object> data = this.getData();

        // Adding "../" to avoid that the framework will try to find the view in the AnnotationController name
        RequestDispatcher dispatcher = request.getRequestDispatcher("../" + url);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        dispatcher.forward(request, response);
    }

    public String toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

}
