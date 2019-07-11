package com.avraam.smartlist.models;

import android.os.AsyncTask;

import com.avraam.smartlist.viewModels.RetrieveInformation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupInformation extends AsyncTask<Void, Void, Void> {

    private String words;

    protected Void doInBackground(Void... voids)
    {

        try
        {

            Document doc = Jsoup.connect("https://www.zapmarket.co.il/?query="+RetrieveInformation.barcode).get();
            Elements title = doc.getElementsByAttribute("title").next().next().next().next();
            //Elements price = doc.getElementsByClass("prodPrice right5px");

            words = "Product: "+title.text();
        }
        catch (Exception e){ e.printStackTrace();}
        return null;

    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        RetrieveInformation.description.setText(words);
    }
}


