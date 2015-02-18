package com.tabner.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.String;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model){
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,	DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		return "home";
	}
	
	public class Arr {
		private List<String> testArray;

		public void setTestArray(List<String> testArray) {
			this.testArray = testArray;
		}

		public List<String> getTestArray() {
			return testArray;
		}
	}
	
	public enum State{
		AK,AL,AR,AZ,CA,CO,CT,DC,DE,FL,GA,HI,IA,ID,IL,IN,KS,KY,LA,MA,MD,ME,MI,MN,MO,MS,MT,NC,ND,NE,NH,NJ,NM,NV,NY,OH,OK,OR,PA,RI,SC,SD,TN,TX,UT,VA,VT,WA,WI,WV,WY
	}

	@RequestMapping(value = "/file", method = RequestMethod.POST )
	@ResponseBody
	public ModelAndView PDFtoJson(@RequestParam(value="file") String[] file, ModelAndView mv, HttpServletRequest request) throws IOException, JSONException, ParseException {
		System.out.println(file);
		ArrayList<Object> listjson = new ArrayList<Object>(); 
		ArrayList<Object> listxml = new ArrayList<Object>(); 
		
		for(String filename:file){
			System.out.println(filename);
			
			boxpd box = new boxpd();
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			//System.out.println(desktop);
			File f = new File(desktop+"/"+filename); 
			String WholeDoc = box.getContent(f);
			System.out.println(WholeDoc);
			
			
			
			String stringgc = "TABNER INC";
			String stringgc1 = "11020 DAVID TAYLOR DR";
			String stringgc2 = "STE 430";
			String stringgc3 = "CHARLOTTE NC 28262";
			String stringgc4 = "(980)939-1144";
			WholeDoc = WholeDoc.replaceAll(stringgc, "");
			WholeDoc = WholeDoc.replaceAll(stringgc1, "");
			WholeDoc = WholeDoc.replaceAll(stringgc2, "");
			WholeDoc = WholeDoc.replaceAll(stringgc3, "");	
			WholeDoc = WholeDoc.replaceAll(stringgc4, "");	
			System.out.println(WholeDoc);
			
			String[] linesinWholeDoc = WholeDoc.split("@@@@");
			//System.out.println(linesinWholeDoc);
			System.out.println("******************************************************************************************************");
			for (String eachline : linesinWholeDoc) {
				

				Map<String, String> map = new LinkedHashMap<String, String>();
				//Multimap<String, String> map1 = LinkedListMultimap.create();
				Map<String, String> map1 = new LinkedHashMap<String, String>();
				
				
				if(eachline != null && !eachline.isEmpty()){
					System.out.println(eachline);
					Pattern pattern = Pattern.compile("NET");
					Matcher matcher = pattern.matcher(eachline);
					if (matcher.find()) {
						eachline = eachline.substring(0, matcher.start());
						eachline = eachline.trim().replaceAll("         @+", "@");
					    //String string2 = str.substring(matcher.end());
					}
					//System.out.println("******************************************************************************************************");
					//System.out.println(eachline);
					String eachlineaftertrim = eachline.trim().replaceAll("@+", "@");		
					String text = eachlineaftertrim;
					//System.out.println(eachline);
					Pattern p = Pattern.compile(Pattern.quote("") + "(.*?)" + Pattern.quote("Check stub for the period"));
					Matcher m = p.matcher(text);
					while (m.find()) {
						String name = m.group(1);
//						name=name.replace("[^\\s ]", "");
//						name = name.replaceAll("[^\\w]", "");
						name = name.replaceAll("\\d", "");
						name = name.replaceAll("()-@", "");
						name = name.replaceAll("\\(.*?\\) ?", "");
//						name.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "+");
						//System.out.println(name);
						map.put("Name", name);
					}
					String text1 = eachlineaftertrim;
					Pattern p1 = Pattern.compile(Pattern.quote("Check stub for the period") + "(.*?)" + Pattern.quote("@"));
					Matcher m1 = p1.matcher(text1);
					while (m1.find()) {
						String startDt = m1.group(1);
						map.put("StartDate", startDt);
					}	
					String text2 = eachlineaftertrim;
					Pattern p2 = Pattern.compile(Pattern.quote("to") + "(.*?)" + Pattern.quote("@"));
					Matcher m2 = p2.matcher(text2);
					while (m2.find()) {
						String endDt = m2.group(1);
						map.put("EndDate", endDt);
					}
					String text3 = eachlineaftertrim;
					Pattern p3 = Pattern.compile(Pattern.quote("with a pay date of") + "(.*?)" + Pattern.quote("@"));
					Matcher m3 = p3.matcher(text3);
					while (m3.find()) {
						String payDt = m3.group(1);
						String cleanpayDt = payDt.replaceAll("[,]", "");
						String trimst = cleanpayDt.trim();
						DateFormat formatter = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH); 
						Date date = (Date)formatter.parse(trimst);
						SimpleDateFormat newFormat = new SimpleDateFormat("MMddyyyy");
						String finalString = newFormat.format(date);
						//System.out.println("PAYDATE:"+finalString);
						map.put("PayDate", finalString);
					}
					String text4 = eachlineaftertrim;
					Pattern p4 = Pattern.compile(Pattern.quote("***-") + "(.*?)" + Pattern.quote("@"));
					Matcher m4 = p4.matcher(text4);
					while (m4.find()) {
						String ssn = m4.group(1);
						System.out.println(ssn);
						
						//ssn = ssn.substring(ssn.length()-4);
						ssn = ssn.substring(0, Math.min(ssn.length(), 7));
						ssn = ssn.substring(ssn.length()-4);
						map.put("SSN", ssn);
					}
					String text6 = eachlineaftertrim;
					Pattern p6 = Pattern.compile(Pattern.quote("FITWH") + "(.*?)" + Pattern.quote("@Department"));
					Matcher m6 = p6.matcher(text6);
					while (m6.find()) {
						String fitwhr = m6.group(1);
						String fitwh = fitwhr.substring(0, Math.min(4, fitwhr.length()));
						map.put("FITWH", fitwh);
						mv.addObject("fitwh", fitwh);
					}
					String text7 = eachlineaftertrim;
					Pattern p7 = Pattern.compile(Pattern.quote("Department # ") + "(.*?)" + Pattern.quote("@"));
					Matcher m7 = p7.matcher(text7);
					while (m7.find()) {
						String state = m7.group(1);
						

						state = state.substring(0, Math.min(state.length(), 10));
						state = state.substring(state.length()-7);
						
//						String state = stater.substring(0, Math.min(7, stater.length()));
//						state = state.substring(state.length()-6);
						map.put("State", state);
					}
					String text8 = eachlineaftertrim;
					Pattern p8 = Pattern.compile(Pattern.quote("Employee #") + "(.*?)" + Pattern.quote("FITWH "));
					Matcher m8 = p8.matcher(text8);
					while (m8.find()) {
						String emp = m8.group(1);
						Pattern pp = Pattern.compile("-?\\d+");
						Matcher mm = pp.matcher(emp);
						while (mm.find()) {
							map.put("Employee", mm.group());
						}
					}
					String text9 = eachlineaftertrim;
					Pattern p9 = Pattern.compile(Pattern.quote("Department #") + "(.*?)" + Pattern.quote("@"));
					Matcher m9 = p9.matcher(text9);
					while (m9.find()) {
						String dept = m9.group(1);
						Pattern pp = Pattern.compile("-?\\d+");
						Matcher mm = pp.matcher(dept);
						while (mm.find()) {
							map.put("Department", mm.group());
							break;
						}
					}
					String text10 = eachlineaftertrim;
					if(text10 != null && !text10.isEmpty()){
						
					}
					System.out.println(text10);
					String mySubstring = "@BI-";
					text10 = text10.substring(text10.indexOf(mySubstring)+1);
				    System.out.println(text10);
				    //System.out.println(text10);
				    String text111 = text10.replace("$", "");
				    String text11 = text111.replace("YTD HR/UNIT", "Ytd-HR/UNIT");
				    //System.out.println(text11);
				    
				    String textf = " @"+text11;
				    
				    String[] result = textf.split("@");
				    /*for (String sresult : result) {
						//System.out.println(sresult);
					}*/
				    String line1 = result[1].trim().replaceAll(" +", " ");
				    String line2 = result[2].replaceAll("   ", "    ");
				    System.out.println("AFTER   :" + line1);
				    System.out.println("AFTER1  :" + result[2]);
				    line1 = line1.replaceFirst("CURRENT", "Current1");
//				    line1 = line1.replaceFirst("CURRENT", "Current2");
				    line1 = line1.replaceFirst("YTD", "Ytd1");
			    line1 = line1.replaceFirst("YTD", "Ytd2");
				    line1 = line1.replaceFirst("YTD", "Ytd3");
				    String[] line1words = line1.split(" ");
				    String[] line2words = line2.split(" ");
				    System.out.println(line1words.length);
				    System.out.println(line2words.length);
				    
				    int intline1=0;	
					for (String stringinline1words : line1words) {
//						String clean = line2words[intline1].replaceAll("[,]", "");
//						map.put(stringinline1words, clean);
//						intline1++;
						
						
			    		switch (intline1) {
			            case 0:  String d = stringinline1words;
			            		 String Biw = line2words[intline1].replaceAll("[,]", "");
								 map.put(d, Biw);
			                     break;
			            case 1:  String rate = stringinline1words;		
								 String ratvaluew = line2words[intline1].replaceAll("[,]", "");
						 		 map.put(rate, ratvaluew);
			                     break;
			            case 2:  String houtunit = stringinline1words;
			            		 String houtunitvalue = line2words[intline1].replaceAll("[,]", "");
								 map.put(houtunit, houtunitvalue);
			                     break;
			            case 3:  String current = stringinline1words;
			            		 String currentvalue = line2words[intline1].replaceAll("[,]", "");
				 		         map.put(current, currentvalue);
			                     break;
			            case 4:  String ytdhunit= stringinline1words;
			            		 String ytdhunitvalue = line2words[intline1].replaceAll("[,]", "");
						         map.put(ytdhunit, ytdhunitvalue);
			                     break;
			            case 5:  String ytd= stringinline1words;
			            		 String ytdvalue = line2words[intline1].replaceAll("[,]", "");
						         map.put(ytd, ytdvalue);
			                     break;
			                     
			            case 6:  
				            	if(line2words[6] != null && !line2words[6].isEmpty()){				            		
				            		 String ded = "Ded"+line2words[6];
				            		 String dedvalue = line2words[6+1].replaceAll("[,]", "");
									 map.put(ded, dedvalue);
				                     break;
				                }
			            	
			            case 7:  //String c2 = "\"Current"+linewords[0]+"\"";		
								 //String stringsinlineword = stringsinlinewords.replaceAll("[,]", "");
						 		// map1.put(c2,  "\"" + stringsinlineword + "\"");
			                     break;
			            case 8:  
			            	    if(line2words[6] != null && !line2words[6].isEmpty()){					            		
				            		String ytdd = "YTD"+line2words[6];
				            		 String valueytdd = line2words[8];
									 map.put(ytdd, valueytdd);
				            	}
			            		
								 for( int i = 3; i < result.length; i++)
								    {
								    	if(i>=3){
									        String element = result[i];
										    String line = element.trim().replaceAll(" +", " ");
										    String[] linewords = line.split(" ");
										    System.out.println(linewords.length);
										    int x= 0;
										    
										    
										    
										    if(linewords.length==9){
										    	for (String stringsinlinewords : linewords) {
										    		switch (x) {
										    		case 0:  String de = "Ded"+stringsinlinewords;
										            		 String value = linewords[1].replaceAll("[,]", "");
															 map.put(de, value);
										                     break;
										            case 1:  break;
										            case 2:  String yt = "YTD"+linewords[0];
										            		 String valueyt = linewords[2];
															 map.put(yt, valueyt);
										                     break;
										            case 3:  String de1 = "Ded"+stringsinlinewords;
										            		 String value1 = linewords[4].replaceAll("[,]", "");
															 map.put(de1, value1);
										                     break;
										            case 4:  break;
										            case 5:  String yt1 = "YTD"+linewords[3];
										            		 String valueyt1 = linewords[5];
															 map.put(yt1, valueyt1);
										                     break;
										            case 6:  if(linewords[6].equals("FITWH") || linewords[6].equals("MED") || linewords[6].equals("SOC") ){
													    			String w = "TAX"+linewords[6];
																	map1.put(w,   stringsinlinewords);
													    		 }
													    		 else if(linewords[6].length()==2){
													    			try{
													    				if(State.valueOf(linewords[6]) != null){
													    					String value11 = map1.get("TAXSTATE");
													    					if (value11 != null) {
													    						linewords[6] = "STATE1";
																    			String w = "TAX"+linewords[6];
																				map1.put(w,   stringsinlinewords);
													    					    
													    					} else {
													    						linewords[6] = "STATE";
																    			String w = "TAX"+linewords[6];
																				map1.put(w,   stringsinlinewords);
													    					}
													    				}
													    				else{
													    					linewords[6] = "STATE2";
															    			String w = "TAX"+linewords[6];
																			map1.put(w,   stringsinlinewords);
													    				}
													    			}
													    			catch(IllegalArgumentException e){
													    				e.printStackTrace();
													    			}
													    		 }
													    		 else{
													    			if(linewords[6].length()>2){
													    				linewords[6] = "STATEADDL";
														    			String w = "TAX"+linewords[6];
																		map1.put(w,   stringsinlinewords);
													    			}
													    		 }	
										                     break;
										            case 7:  String c= "CURRENT"+linewords[6];
											                 String cvalue = stringsinlinewords.replaceAll("[,]", "");
													         map1.put(c,  cvalue );
										                     break;
										            case 8:  String y= "YTD"+linewords[6];
											                 String yvalue = stringsinlinewords.replaceAll("[,]", "");
													         map1.put(y,   yvalue);
										                     break;
										    		}
											    	x++;
												}
										    }
										    
										    
										    
										    if(linewords.length==6){
										    	for (String stringsinlinewords : linewords) {
										    		switch (x) {
										            case 0:  String de = "Ded"+stringsinlinewords;
										            		 String value = linewords[1].replaceAll("[,]", "");
															 map.put(de, value);
										                     break;
										            case 1:  break;
										            case 2:  String yt = "YTD"+linewords[0];
										            		 String valueyt = linewords[2];
															 map.put(yt, valueyt);
										                     break;
										            case 3:  if(linewords[3].equals("FITWH") || linewords[3].equals("MED") || linewords[3].equals("SOC") ){
													    			String w = "TAX"+linewords[3];
																	map1.put(w,   stringsinlinewords);
													    		 }
													    		 else if(linewords[3].length()==2){
													    			try{
													    				if(State.valueOf(linewords[3]) != null){
													    					String value1 = map1.get("TAXSTATE");
													    					if (value1 != null) {
													    						linewords[3] = "STATE1";
																    			String w = "TAX"+linewords[3];
																				map1.put(w,   stringsinlinewords);
													    					    
													    					} else {
													    						linewords[3] = "STATE";
																    			String w = "TAX"+linewords[3];
																				map1.put(w,   stringsinlinewords);
													    					}
													    				}
													    				else{
													    					linewords[3] = "STATE2";
															    			String w = "TAX"+linewords[3];
																			map1.put(w,   stringsinlinewords);
													    				}
													    			}
													    			catch(IllegalArgumentException e){
													    				e.printStackTrace();
													    			}
													    		 }
													    		 else{
													    			if(linewords[3].length()>2){
													    				linewords[3] = "STATEADDL";
														    			String w = "TAX"+linewords[3];
																		map1.put(w,   stringsinlinewords);
													    			}
													    		 }	
										                     break;
										            case 4:  String c= "CURRENT"+linewords[3];
											                 String cvalue = stringsinlinewords.replaceAll("[,]", "");
													         map1.put(c,  cvalue );
										                     break;
										            case 5:  String y= "YTD"+linewords[3];
											                 String yvalue = stringsinlinewords.replaceAll("[,]", "");
													         map1.put(y,   yvalue);
										                     break;
										    		}
											    	x++;
												}
										    }
										    if(linewords.length==3){
										    	for (String stringinlinewords : linewords) {
											    	if(x==0){
											    		if(linewords[0].equals("FITWH") || linewords[0].equals("MED") || linewords[0].equals("SOC") ){
											    			String w = "TAX"+linewords[0];
															map1.put(w,  stringinlinewords);
											    		}
											    		else if(linewords[0].length()==2){
											    			try{
											    				if(State.valueOf(linewords[0]) != null){
											    					String value = map1.get("TAXSTATE");
											    					if (value != null) {
												    					linewords[0] = "STATE1";
														    			String w = "TAX"+linewords[0];
																		map1.put(w,  stringinlinewords);
											    					    
											    					} else {
												    					linewords[0] = "STATE";
														    			String w = "TAX"+linewords[0];
																		map1.put(w,  stringinlinewords);
											    					}
											    				}
											    				else{
											    					linewords[0] = "STATE2";
													    			String w = "TAX"+linewords[0];
																	map1.put(w,  stringinlinewords);
											    				}
											    			}
											    			catch(IllegalArgumentException e){
											    				e.printStackTrace();
											    			}
											    		}
											    		else{
											    			if(linewords[0].length()>2){
									    					System.out.println(linewords[0]);
											    			linewords[0] = "STATEADDL";
											    			String w = "TAX"+linewords[0];
															map1.put(w,  stringinlinewords);
											    			}
											    		}
											    		
											    	}else if(x==1){
											    		String w= "CURRENT"+linewords[0];
										                String currvalue = stringinlinewords.replaceAll("[,]", "");
														map1.put(w,  currvalue);
											    	}else{
											    		String w= "YTD"+linewords[0];
										                String ytvalue = stringinlinewords.replaceAll("[,]", "");
														map1.put(w,  ytvalue);
											    	}
											    	x++;
												}
										    }
										    if(linewords.length==5){
										    	for (String stringsinlinewords : linewords) {
										    		if(linewords[2].matches(".*\\d.*")){
										    			switch (x) {
											            case 0:  String de = "Ded"+stringsinlinewords;
											            		 String value = linewords[1].replaceAll("[,]", "");
																 map1.put(de, value);
											                     break;											            
											            case 1:  break;
											            case 2:  String yt = "YTD"+linewords[0];
										            			 String valueyt = linewords[2];
																 map1.put(yt, valueyt);
											            case 3:  if(linewords[3].equals("FITWH") || linewords[3].equals("MED") || linewords[3].equals("SOC") ){
													    			String w = "TAX"+linewords[3];
																	map1.put(w,  stringsinlinewords);
													    		 }
													    		 else if(linewords[3].length()==2){
													    			try{
													    				if(State.valueOf(linewords[3]) != null){
													    					String value1 = map1.get("TAXSTATE");
													    					if (value1.isEmpty()) {
													    						linewords[3] = "STATE";
																    			String w = "TAX"+linewords[3];
																				map1.put(w,  stringsinlinewords);
													    					    
													    					} else {
													    						linewords[3] = "STATE1";
																    			String w = "TAX"+linewords[3];
																				map1.put(w,  stringsinlinewords);
													    					}
													    				}
													    				else{
													    					linewords[3] = "STATE2";
															    			String w = "TAX"+linewords[3];
																			map1.put(w,  stringsinlinewords);
													    				}
													    			}
													    			catch(IllegalArgumentException e){
													    				e.printStackTrace();
													    			}
													    		 }
													    		 else{
													    			if(linewords[3].length()>2){
													    				linewords[3] = "STATEADDL";
														    			String w = "TAX"+linewords[3];
																		map1.put(w,  stringsinlinewords);
													    			}
													    		 }										            		 
											                     break;
											            case 4:  String y= "YTD"+linewords[3];
												                 String yvalue = stringsinlinewords.replaceAll("[,]", "");
														         map1.put(y,  yvalue);
											                     break;
											    		}
												    	x++;										    			
										    		}
										    		else{
											    		switch (x) {
											            case 0:  String de = "Ded"+stringsinlinewords;
											            		 String value = " ";
																 map.put(de, value);
											                     break;
											            case 1:  String yt = "YTD"+linewords[0];
										            			 String valueyt = linewords[1];
																 map.put(yt, valueyt);
											                     break;
											            case 2:  if(linewords[2].equals("FITWH") || linewords[2].equals("MED") || linewords[2].equals("SOC") ){
													    			String w = "TAX"+linewords[2];
																	map1.put(w,  stringsinlinewords);
													    		 }
													    		 else if(linewords[2].length()==2){
													    			try{
													    				if(State.valueOf(linewords[2]) != null){
													    					String value1 = map1.get("TAXSTATE");
													    					if (value1.isEmpty()) {
													    						linewords[2] = "STATE";
																    			String w = "TAX"+linewords[2];
																				map1.put(w,  stringsinlinewords);
													    					    
													    					} else {
													    						linewords[2] = "STATE1";
																    			String w = "TAX"+linewords[2];
																				map1.put(w,  stringsinlinewords);
													    					}
													    				}
													    				else{
													    					linewords[2] = "STATE2";
															    			String w = "TAX"+linewords[2];
																			map1.put(w,  stringsinlinewords);
													    				}
													    			}
													    			catch(IllegalArgumentException e){
													    				e.printStackTrace();
													    			}
													    		 }
													    		 else{
													    			if(linewords[2].length()>2){
													    				linewords[2] = "STATEADDL";
														    			String w = "TAX"+linewords[2];
																		map1.put(w,  stringsinlinewords);
													    			}
													    		 }						
											                     break;
											            case 3:  String c= "CURRENT"+linewords[2];
												                 String cvalue = stringsinlinewords.replaceAll("[,]", "");
														         map1.put(c,  cvalue);											            		 
											                     break;
											            case 4:  String y= "YTD"+linewords[2];
												                 String yvalue = stringsinlinewords.replaceAll("[,]", "");
														         map1.put(y,  yvalue);
											                     break;
											    		}
												    	x++;
										    		}
												}
										    }
										    
										    if(linewords.length==2){
										    	for (String stringsinlinewords : linewords) {
										    		switch (x) {
										            case 0:  if(linewords[0].equals("FITWH") || linewords[0].equals("MED") || linewords[0].equals("SOC") ){
													    			String w = "TAX"+linewords[0];
																	map1.put(w,   stringsinlinewords);
													    		 }
													    		 else if(linewords[0].length()==2){
													    			try{
													    				if(State.valueOf(linewords[0]) != null){
													    					String value1 = map1.get("TAXSTATE");
													    					if (value1 != null) {
													    						linewords[0] = "STATE1";
																    			String w = "TAX"+linewords[0];
																				map1.put(w,   stringsinlinewords);
													    					    
													    					} else {
													    						linewords[0] = "STATE";
																    			String w = "TAX"+linewords[0];
																				map1.put(w,   stringsinlinewords);
													    					}
													    				}
													    				else{
													    					linewords[0] = "STATE2";
															    			String w = "TAX"+linewords[0];
																			map1.put(w,   stringsinlinewords);
													    				}
													    			}
													    			catch(IllegalArgumentException e){
													    				e.printStackTrace();
													    			}
													    		 }
													    		 else{
													    			if(linewords[0].length()>2){
													    				linewords[0] = "STATEADDL";
														    			String w = "TAX"+linewords[0];
																		map1.put(w,   stringsinlinewords);
													    			}
													    		 }			
										                     break;
										            case 1:  String y= "YTD"+linewords[0];
											                 String yvalue = stringsinlinewords.replaceAll("[,]", "");
													         map1.put(y,   yvalue);
										                     break;
										    		}
											    	x++;
												}
										    }
										    
								    	}
								    }
								 
								 
								 
			                     break;
			            case 9:  //String tax = stringinline1words;
			            	     String tax = "TAX"+line2words[9];
			       		 		 String taxvalue = line2words[9].replaceAll("[,]", "");
				 		         map.put("TAXX", "");
				 		         map.put(tax, taxvalue);
			                     break;
			            case 10: //String currenttax = stringinline1words;
			            	     String currenttax = "CURRENT"+line2words[9];
				                 String currenttaxvalue = line2words[10].replaceAll("[,]", "");
						         map.put(currenttax,  currenttaxvalue);
			                     break;
			            case 11: String ytdtax= "YTD"+line2words[9];
				                 String ytdtaxvalue = line2words[11].replaceAll("[,]", "");
						         map.put(ytdtax, ytdtaxvalue);
			                     break;
			            
			    		}
			    		intline1++;
				    
					}
				    	
				    
				    
				   
				    
				   
					Gson gson = new GsonBuilder().create();
					System.out.println(map);
					System.out.println(new JSONObject(map));
					
					String json = gson.toJson(map); 				
					String xml = XML.toString(json);
					System.out.println(xml);
					
					JSONObject combined = new JSONObject();
					combined.put("Object1", json);
					combined.put("Object2", map1);
	
					System.out.println(combined);
					listjson.add(combined);
					
					listxml.add(xml);		
					
					
				}	
			}
		}
		
		mv.addObject("list",listjson);
		mv.setViewName("text");
		return mv;
	}

	// Pdf parsing
	public class boxpd {

		final String getContent(File f) throws IOException {
			// setType("PDF");
			Reader reader = null;
			PDDocument pdfDocument = null;
			FileInputStream fis = null;
			String contents = null;
			try {
				System.out.println("Getting contents from PDF: " + f.getName());
				fis = new FileInputStream(f);
				PDFParser parser = new PDFParser(fis);
				parser.parse();
				pdfDocument = parser.getPDDocument();
				PDFTextStripper stripper = new PDFTextStripper();
				stripper.getLineSeparator();
				stripper.setSortByPosition( true );
				stripper.setLineSeparator("@");
				contents = stripper.getText(pdfDocument);
				reader = new StringReader(contents);
//				System.out.println(reader);
			} catch (IOException e) {
				System.out.println("Error: Can't open file: " + f.getName());
			} finally {
				fis.close();
				pdfDocument.close();
			}
			return contents;
		}

	}

}
