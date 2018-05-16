package age.club.administrator.age_101;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DiscountCategorizer {

    private ArrayList <Discount> categoryA; //Giyim
    private ArrayList <Discount> categoryB; //Eğlence
    private ArrayList <Discount> categoryC; //Sağlık
    private ArrayList <Discount> allCategories;
    private ArrayList <Discount> showcase;
    private DiscountAdapter discountAdapter;
    private Context context;

    public DiscountCategorizer(Context context, DiscountAdapter discountAdapter){
        categoryA = new ArrayList<>();
        categoryB = new ArrayList<>();
        categoryC = new ArrayList<>();
        allCategories = new ArrayList<>();
        showcase = new ArrayList<>();
        this.context = context;
        this.discountAdapter = discountAdapter;
        getDiscountsData();
    }

    private void getDiscountsData(){
        String url = "http://212.175.137.237/ageClub/Discounts/";
        StringRequest myStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                passData(response);
                discountAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Toast.makeText(context, "Volley Error!", Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(myStringRequest);
    }

    private void passData(String jsonData){
        Gson gsonDiscounts = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<Discount>>(){}.getType();
        ArrayList<Discount> myDiscounts = gsonDiscounts.fromJson(jsonData, listType);
        categorizeDiscounts(myDiscounts);
    }

    private void categorizeDiscounts(ArrayList<Discount> d){

        for(int i = 0; i < d.size(); i++){
            if(d.get(i).getIsShowcase() == 1){
                showcase.add(d.get(i));
            }
            if(d.get(i).getCampaignCategory().equals("Giyim")){
                categoryA.add(d.get(i));
            }
            else if(d.get(i).getCampaignCategory().equals("Eğlence")){
                categoryB.add(d.get(i));
            }
            else if(d.get(i).getCampaignCategory().equals("Sağlık")){
                categoryC.add(d.get(i));
            }
            allCategories.add(d.get(i));
        }
    }

    public ArrayList<Discount> getAllCategories(){
        return allCategories;
    }

    public ArrayList<Discount> getCategoryA() {
        return categoryA;
    }

    public ArrayList<Discount> getCategoryB() {
        return categoryB;
    }

    public ArrayList<Discount> getCategoryC() {
        return categoryC;
    }

    public ArrayList<Discount> getShowcase() {
        return showcase;
    }
}
