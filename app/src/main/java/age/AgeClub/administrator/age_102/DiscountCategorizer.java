package age.AgeClub.administrator.age_102;

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


    private DiscountAdapter discountAdapter;
    private Context context;

    public DiscountCategorizer(Context context, DiscountAdapter discountAdapter){
        this.context = context;
        this.discountAdapter = discountAdapter;
        getDiscountsData();
    }

    private void getDiscountsData(){
        String url = "http://176.235.178.215/ageClub/Discounts/";
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

        ArrayList <Discount> categoryA = new ArrayList<>(); //Giyim
        ArrayList <Discount> categoryB = new ArrayList<>(); //Eğlence
        ArrayList <Discount> categoryC = new ArrayList<>(); //Sağlık
        ArrayList <Discount> allCategories = new ArrayList<>();

        for(int i = 0; i < d.size(); i++){
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

        DiscountCategoriesSingleton dcs = DiscountCategoriesSingleton.getInstance();

        dcs.setCategoryA(categoryA);
        dcs.setCategoryB(categoryB);
        dcs.setCategoryC(categoryC);
        dcs.setAllCategories(allCategories);
        dcs.setInstance();
    }

}
