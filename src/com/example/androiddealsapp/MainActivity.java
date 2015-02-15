package com.example.androiddealsapp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	Spinner countrySpinner, citySpinner;
	Button submit;
	Map<String,List> countryCityMap;
	String selectedCountry, selectedCity;
	List<String> countryList;
	String countryUrl = "http://api.groupon.com/v2/divisions.xml?client_id=your oauth id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new callCountryApi().execute(countryUrl);
		display(countryCityMap);
		
		
		setListenerOnSpinner();
		submit = (Button)findViewById(R.id.submit);
		
		submit.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		System.out.println("Going to move from main activity to deals activity");
		Toast.makeText(getApplicationContext(), "Country : " + selectedCountry + " City : " + selectedCity  , Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, DealsActivity.class);
		intent.putExtra("COUNTRY", selectedCountry);
		intent.putExtra("CITY", selectedCity);
		startActivity(intent);
		
	}
	
	public void setListenerOnSpinner(){
		countrySpinner = (Spinner)findViewById(R.id.countryspinner);
		citySpinner = (Spinner)findViewById(R.id.cityspinner);
		countrySpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view1,
					int position, long id) {
				// TODO Auto-generated method stub
				selectedCountry = countryList.get(position);
				System.out.println("Country is  ======" + selectedCountry);
								
				@SuppressWarnings("unchecked")
				final
				List<String> listCity = countryCityMap.get(selectedCountry);
				
				ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getApplicationContext(),
		                android.R.layout.simple_spinner_item, listCity);
				cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				cityAdapter.notifyDataSetChanged();
				citySpinner.setAdapter(cityAdapter);
				citySpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						selectedCity = listCity.get(arg2);
						System.out.println("City is ======" + selectedCity);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
			

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void listItemsOnSpinner(){
		if(countryCityMap != null){
			countrySpinner = (Spinner)findViewById(R.id.countryspinner);
			
			//new callCountryApi().execute(countryUrl);
			countryList = new ArrayList<String>(countryCityMap.keySet());
			
			ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryList);
			countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			countrySpinner.setAdapter(countryAdapter);
			
			
			
			
		}
		
	}
	
	public static void display(Map<String, List> displayMap){
		if(displayMap != null){
			Set<String> countrySet = displayMap.keySet();
			for(String countryStr : countrySet) {
				System.out.println("Country ======> " + countryStr);
				List<String> cityL = displayMap.get(countryStr);
				for(String cityStr : cityL) {
					System.out.println(cityStr);
				}
			}
			
			
		}
	}
	
	private class callCountryApi extends AsyncTask<String, String, String>{

		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlSt = params[0]; //this is the url to call the country api
			InputStream inSt = null;
			
			//REST GET
			try {
				URL url = new URL(urlSt);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				inSt = new BufferedInputStream (urlConnection.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}
			
			
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			countryCityMap = new HashMap<String, List>();
			
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inSt);
				
				NodeList nList = doc.getElementsByTagName("division");
				
				
				List<String> cityList = null;
				for(int ind=0; ind<nList.getLength(); ind++) {
					Node node = (Node) nList.item(ind);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						NodeList childList = node.getChildNodes();
						String country="";
						String cityId = "";
						for(int innerIndex = 0; innerIndex < childList.getLength(); innerIndex++) {
							Node childNode = (Node) childList.item(innerIndex);
							String nodeName = childNode.getNodeName();
							String nodeValue = childNode.getTextContent();
							if (nodeName.equals("country")) {
								country = nodeValue;
							} else if (nodeName.equals("id")) {
								cityId = nodeValue;
							}
						}
						if (countryCityMap.containsKey(country)) {
							cityList = countryCityMap.get(country);
						} else {
							cityList = new ArrayList<String>();
							countryCityMap.put(country, cityList);
						}
						cityList.add(cityId);
					}
					
				}
				
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			display(countryCityMap);
			listItemsOnSpinner();
		}
		
		
	}

}
