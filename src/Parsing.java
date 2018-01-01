import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Parsing {
	
	public static final String BASE_FOLDER_PATH = "C:/Pankaj/uFony/Eclipse Tracker/DataParsing/";
	public static final String WEATHER_FILE = BASE_FOLDER_PATH + "weather.csv";

	public static void main(String [] arg){
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.metoffice.gov.uk/climate/uk/summaries/datasets#yearOrdered").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Elements links = doc.select("a[href]");  
		Parsing.downloadAllFile(links);
		Parsing.genarateCSVFile();
	} 
	
	public static void genarateCSVFile(){
		Parsing.checkFilePath(Parsing.WEATHER_FILE);
		try {
			PrintWriter writer = new PrintWriter(Parsing.WEATHER_FILE);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Parsing.writeToFile("region_code,weather_param,year,​ ​key,​ ​value");
		
		//For UK
		Parsing.writeRegionAndWeatherData("UK","Tmax");
		Parsing.writeRegionAndWeatherData("UK","Tmin");
		Parsing.writeRegionAndWeatherData("UK","Tmean");
		Parsing.writeRegionAndWeatherData("UK","Sunshine");
		Parsing.writeRegionAndWeatherData("UK","Rainfall");
		
		//For England
		Parsing.writeRegionAndWeatherData("England","Tmax");
		Parsing.writeRegionAndWeatherData("England","Tmin");
		Parsing.writeRegionAndWeatherData("England","Tmean");
		Parsing.writeRegionAndWeatherData("England","Sunshine");
		Parsing.writeRegionAndWeatherData("England","Rainfall");
		
		//For Wales
		Parsing.writeRegionAndWeatherData("Wales","Tmax");
		Parsing.writeRegionAndWeatherData("Wales","Tmin");
		Parsing.writeRegionAndWeatherData("Wales","Tmean");
		Parsing.writeRegionAndWeatherData("Wales","Sunshine");
		Parsing.writeRegionAndWeatherData("Wales","Rainfall");
		
		//For Scotland
		Parsing.writeRegionAndWeatherData("Scotland","Tmax");
		Parsing.writeRegionAndWeatherData("Scotland","Tmin");
		Parsing.writeRegionAndWeatherData("Scotland","Tmean");
		Parsing.writeRegionAndWeatherData("Scotland","Sunshine");
		Parsing.writeRegionAndWeatherData("Scotland","Rainfall");
		
	}
	
	public static void writeRegionAndWeatherData(String regionCode, String weatherParam){
		
		try {
			FileInputStream fstream = new FileInputStream(Parsing.BASE_FOLDER_PATH + weatherParam+"/"+regionCode+".txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			boolean isNewFile = false;
			while ((strLine = br.readLine()) != null){
				if(strLine.contains("Year") && strLine.contains("JAN")
						&& strLine.contains("FEB") && strLine.contains("MAR")
						&& strLine.contains("APR") && strLine.contains("MAY")
						&& strLine.contains("JUN") && strLine.contains("JUL")
						&& strLine.contains("AUG") && strLine.contains("SEP")
						&& strLine.contains("OCT") && strLine.contains("NOV")
						&& strLine.contains("DEC")){
					isNewFile = true;
					continue;
				}
				
				if(weatherParam.equals("Tmax")){
					weatherParam = "Max​ ​temp";
				}else if(weatherParam.equals("Tmin")){
					weatherParam = "Min temp";
				}else if(weatherParam.equals("Tmean")){
					weatherParam = "Mean​ ​temp";
				}
				
				if(isNewFile){
					String firstSort = strLine.replaceAll("   "," "); // Remove 2 white space
					String secoundSort = firstSort.replace("  "," "); // Remove 1 white space
					String thirdSort = secoundSort.replace(" ",",");
				
					String[] yearData = thirdSort.split("\\s*(=>|,|\\s)\\s*");
					Parsing.writeToWeatherFile(regionCode, weatherParam, yearData);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeToWeatherFile(String regionCode, String weatherParam, String[] yearData){
		String year = yearData[0];
		String jan = (yearData[1].isEmpty() ? "N/A" : yearData[1]);
		String feb = (yearData[2].isEmpty() ? "N/A" : yearData[2]);
		String mar = (yearData[3].isEmpty() ? "N/A" : yearData[3]);
		String apr = (yearData[4].isEmpty() ? "N/A" : yearData[4]);
		String may = (yearData[5].isEmpty() ? "N/A" : yearData[5]);
		String jun = (yearData[6].isEmpty() ? "N/A" : yearData[6]);
		String jul = (yearData[7].isEmpty() ? "N/A" : yearData[7]);
		String aug = (yearData[8].isEmpty() ? "N/A" : yearData[8]);
		String sep = (yearData[9].isEmpty() ? "N/A" : yearData[9]);
		String oct = (yearData[10].isEmpty() ? "N/A" : yearData[10]);
		String nov = (yearData[11].isEmpty() ? "N/A" : yearData[11]);
		String dec = (yearData[12].isEmpty() ? "N/A" : yearData[12]);
		
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",JAN,"+ jan);
		Parsing.writeToFile( regionCode + ","+ weatherParam + ","+ year +",FEB,"+ feb);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",MAR,"+ mar);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",APR,"+ apr);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",MAY,"+ may);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",JUN,"+ jun);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",JUL,"+ jul);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",AUG,"+ aug);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",SEP,"+ sep);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",OCT,"+ oct);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",NOV,"+ nov);
		Parsing.writeToFile(regionCode + ","+ weatherParam + ","+ year +",DEC,"+ dec);
	}
	
	public static void writeToFile(String content){
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(WEATHER_FILE, true))) {
			bw.write(content+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadAllFile(Elements links){
		for (Element link : links) {  
		    String fromFile = link.attr("href");
		    String toFile = "";
		    if(fromFile.contains("date/UK.txt") || fromFile.contains("date/England.txt") 
		    		|| fromFile.contains("date/Wales.txt") || fromFile.contains("date/Scotland.txt")){
		    	
		    	if(fromFile.contains("Tmax")){
		    		toFile = Parsing.BASE_FOLDER_PATH + "Tmax/"+ Parsing.getFileNameFromUrl(fromFile) +".txt";
			    }else if(fromFile.contains("Tmin")){
		    		toFile = Parsing.BASE_FOLDER_PATH + "Tmin/"+ Parsing.getFileNameFromUrl(fromFile) +".txt";
			    }else if(fromFile.contains("Tmean")){
		    		toFile = Parsing.BASE_FOLDER_PATH + "Tmean/"+ Parsing.getFileNameFromUrl(fromFile) +".txt";
			    }else if(fromFile.contains("Sunshine")){
		    		toFile = Parsing.BASE_FOLDER_PATH + "Sunshine/"+ Parsing.getFileNameFromUrl(fromFile) +".txt";
			    }else if(fromFile.contains("Rainfall")){
		    		toFile = Parsing.BASE_FOLDER_PATH + "Rainfall/"+ Parsing.getFileNameFromUrl(fromFile) +".txt";
			    }
				
		    	if(!toFile.isEmpty()){
		    		Parsing.checkFilePath(toFile);
		    		Parsing.downloadAndSaveFile(fromFile, toFile);  
		    	}
		    }
		} 
	}
	
	@SuppressWarnings("resource")
	public static void checkFilePath(String toFile){
		try {
			File file = new File(toFile);
			file.getParentFile().mkdirs();
			new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getFileNameFromUrl(String url){
		String fileName = url.substring( url.lastIndexOf('/')+1, url.length() );
		String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
		return fileNameWithoutExtn;
	}
	
	public static void downloadAndSaveFile(String fromFile, String toFile){
		
		try {
			URL url = new URL(fromFile);
			HttpURLConnection huc = (HttpURLConnection)url.openConnection();
	        int statusCode = huc.getResponseCode(); 
	        //get response code
	        //System.out.println("statusCode = "+statusCode);
	        // if file is moved, then pick new URL
	        if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
	                || statusCode == HttpURLConnection.HTTP_MOVED_PERM){ 
	        	fromFile = huc.getHeaderField("Location");
	            url = new URL(fromFile);
	            huc = (HttpURLConnection)url.openConnection();
	        }
	        //System.out.println(fromFile);  
	        InputStream is = huc.getInputStream();
	        BufferedInputStream bis = new BufferedInputStream(is);
	        @SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(toFile);
	        int i = 0;
	        while ((i = bis.read()) != -1)
	            fos.write(i);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
