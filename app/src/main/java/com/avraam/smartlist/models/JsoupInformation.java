package com.avraam.smartlist.models;

import android.os.AsyncTask;
import android.util.Log;

import com.avraam.smartlist.viewModels.Activities.AddProduct;
import com.avraam.smartlist.viewModels.Activities.RetrieveInformation;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;

import io.opencensus.internal.StringUtils;

public class JsoupInformation extends AsyncTask<Void, Void, Void> {

    private String productTitle;
    private String productDescription;
    private String productPrice;




    protected Void doInBackground(Void... voids)
    {

        try
        {


            Document doc = Jsoup.connect("https://chp.co.il/%D7%99%D7%A9%D7%A8%D7%90%D7%9C/0/0/"+RetrieveInformation.barcode+"/0").get();
            Element title = doc.getElementsByTag("title").first();
            Elements prices = doc.getElementsByClass("line-odd");
            String[] downServers = null;
            Elements table= prices.select("tr");
            String product_price = table.first().text();
          /*  for(Element index : table)
            {
                prodcutPrice+=index+"\n";
            }*/

            if(!product_price.isEmpty())
            {
                downServers = product_price.split(" ");
            }

            System.out.println(Arrays.toString(downServers));



          /*  for(int i=0;i<downServers.length;i++){
                System.out.println(downServers[i]);
            }
*/


            String productName= title.text().substring(29,title.text().length()-1);
            productPrice = downServers[downServers.length-1];




            productTitle = "שם המוצר: "+productName;
            productDescription = productTitle+ "\n"+"המחיר הינו: "+productPrice;



            //AddProduct.comparePrices = prodcutPrice;
        }
        catch (Exception e){ e.printStackTrace();}
        return null;

    }


    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        RetrieveInformation.title.setText(productDescription);
        RetrieveInformation.price = productPrice;
        RetrieveInformation.description = productTitle;
        //RetrieveInformation.prodcutPic.setImageURI(Uri.parse(urlAddress));
    }



}


