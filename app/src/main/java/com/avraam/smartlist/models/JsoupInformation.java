package com.avraam.smartlist.models;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.avraam.smartlist.viewModels.RetrieveInformation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class JsoupInformation extends AsyncTask<Void, Void, Void> {

    private String words;
    private String urlAddress;

    protected Void doInBackground(Void... voids)
    {

        try
        {

            Document doc = Jsoup.connect("https://chp.co.il/%D7%99%D7%A9%D7%A8%D7%90%D7%9C/0/0/"+RetrieveInformation.barcode+"/0").get();
            Element title = doc.getElementsByTag("title").first();
            Elements url = doc.getElementsByTag("img").next().next().next().next().next().next().select("src");
            urlAddress = url.text();
            Log.d("Url",url.text());
            System.out.println(urlAddress);

            String productName= title.text().substring(29,title.text().length()-1);
            words = "Product: "+ productName;
        }
        catch (Exception e){ e.printStackTrace();}
        return null;

    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        RetrieveInformation.description.setText(words);
        RetrieveInformation.prodcutPic.setImageURI(Uri.parse(urlAddress));
    }
}


