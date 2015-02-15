package com.example.androiddealsapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DealsActivity extends Activity{
	
	ArrayList<Deal> result;
	String selectedCountry, selectedCity;
	public static final String dealsUrl = "http://api.groupon.com/v2/deals.xml?client_id=your oauth id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dealspage);
		
		Bundle extras = getIntent().getExtras();
		if(extras!= null){
			selectedCountry =  extras.getString("COUNTRY");
			selectedCity = extras.getString("CITY");
			
			System.out.println("Selected Country and city " + selectedCountry +" " + selectedCity);
			String newUrl = dealsUrl +  "&name="+selectedCity+"&country="+selectedCountry; 
			System.out.println("New url -----"+ newUrl );
			
			new DownloadDeals().execute(newUrl);
			
		}
		
	}
	private class DownloadDeals extends AsyncTask<String, String, ArrayAdapter<String>>{

		List<String> dealIdList;
		List<String> dealTitleList;
		protected ArrayAdapter<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String dealsUrlSt = params[0];
			InputStream instr = null;
			
				URL dealUrl;
				try {
					dealUrl = new URL(dealsUrlSt);
					HttpURLConnection urlConnection =  (HttpURLConnection)dealUrl.openConnection();
					instr = new BufferedInputStream(urlConnection.getInputStream());
					
					XmlPullParserFactory pullParserFactory;
					pullParserFactory = XmlPullParserFactory.newInstance();
					XmlPullParser parser = pullParserFactory.newPullParser();
			        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		            parser.setInput(urlConnection.getInputStream(), null);
		            result = parseXML(parser);
		            
		            for(Deal deal: result){
		            	if(dealIdList == null){
		            		dealIdList = new ArrayList<String>();
		            	}
		            	if(dealTitleList == null){
		            		dealTitleList = new ArrayList<String>();
		            	}
		            	dealIdList.add(deal.getId());
		            	System.out.println("DEALS id is ------" + deal.getId());
		            	dealTitleList.add(deal.getShortAnnouncementTitle());
		            
		            }
					
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			//dealsAdapter = new ArrayAdapter<String>(DealsActivity.this, android.R.layout.simple_list_item_1, result);
			//return dealsAdapter;
				
			
			ArrayAdapter<String> dealsAdapter = new ArrayAdapter<String>(DealsActivity.this, android.R.layout.simple_list_item_1, dealTitleList);
			return dealsAdapter;
			
		}
		
		@Override
		protected void onPostExecute(ArrayAdapter<String> dealsAdapter){
			
			ListView dealListView = (ListView)findViewById(R.id.dealsList);
			dealListView.setAdapter(dealsAdapter);
			//dealListView.setAdapter(result);
			
			/*dealListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
				
					Intent i = new Intent(getApplicationContext(), SpecificDealPage.class);
					i.putExtra("link", )
				}
				
			});
*/		}

		

		
		
		
	}

	
	private ArrayList<Deal> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
	{
		ArrayList<Deal> dealList = null;
        int eventType = parser.getEventType();
        Deal currentDeal = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	dealList = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("deal")){
                        currentDeal = new Deal();
                    } else if (currentDeal != null){
                        if (name.equals("id")){
                            currentDeal.setId(parser.nextText());
                        } else if (name.equals("smallImageUrl")){
                        	currentDeal.setSmallImageUrl(parser.nextText());
                        } else if (name.equals("shortAnnouncementTitle")){
                            currentDeal.setShortAnnouncementTitle(parser.nextText());
                        }  
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("deal") && currentDeal != null){
                    	dealList.add(currentDeal);
                    } 
            }
            eventType = parser.next();
        }

        for(Deal deal : dealList) {
        	System.out.println("Id ===> " + deal.getId());
        	System.out.println("getShortAnnouncementTitle ===> " + deal.getShortAnnouncementTitle());
        }
        return dealList;
	}

	
	
}
