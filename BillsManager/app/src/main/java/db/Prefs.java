package db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Prefs {
	SharedPreferences sharedPreferences;
	String sharedFileName = "fwprefs";
	String typefaceH1 = "01";
	String typefaceH2 = "02";
	String typefaceN = "03";
	String h1 = "04";
	String h2 = "05";
    String h3 = "06";
	String h1color = "07";
	String h2color = "08";
    String h3color = "09";
	String savedScreenId = "10";
	String lastActiveContentId = "11";
	String lastActiveScreenId = "12";
    String backgroundColour="13";
    String launchData="14";
    String navigationDrawer ="15";
    String authToken ="16";
	String actionBar = "17";
    String likedItemColor = "18";
    public static final String TAG= Prefs.class.getSimpleName();
	public Prefs(Context context) {
		sharedPreferences = context.getSharedPreferences(sharedFileName, 0);
	}
	public void clearAll()
    {
        sharedPreferences.edit().clear();
    }
	//------------------------ Appearance part ----------------------
	public String getFontH1() {
		return sharedPreferences.getString(typefaceH1,"");
	}
	public void setFontH1(String value) {
		sharedPreferences.edit().putString(typefaceH1, value).commit();
	}
	public String getFontH2() {
		return sharedPreferences.getString(typefaceH2,"");
	}
	public void setFontH2(String value) {
		sharedPreferences.edit().putString(typefaceH2, value).commit();
	}
	public String getFontN() {
		return sharedPreferences.getString(typefaceN,"");
	}
	public void setFontN(String value) {
		sharedPreferences.edit().putString(typefaceN, value).commit();
	}
	
	public int getH1() {
		return sharedPreferences.getInt(h1, 20);
	}
	public void setH1(int value) {
		sharedPreferences.edit().putInt(h1, value).commit();
	}
	public String getH1Color() {
		return sharedPreferences.getString(h1color, "");
	}
	public void setH1Color(String value) {
		sharedPreferences.edit().putString(h1color, value).commit();
	}
	public String getH2Color() {
		return sharedPreferences.getString(h2color,"");
	}
	public void setH2Color(String value) {
		sharedPreferences.edit().putString(h2color, value).commit();
	}

    public String getH3Color() {
        return sharedPreferences.getString(h3color,"");
    }
    public void setH3Color(String value) {
        sharedPreferences.edit().putString(h2color, value).commit();
    }
	public int getH2() {
		return sharedPreferences.getInt(h2, 16);
	}
	public void setH2(int value) {
		sharedPreferences.edit().putInt(h2, value).commit();
	}

    public int getH3() {
        return sharedPreferences.getInt(h3,12);
    }
    public void setH3(int value) {
        sharedPreferences.edit().putInt(h3, value).commit();
    }
    public String getCommonBackground() {
        return sharedPreferences.getString(backgroundColour, "#FFFFFF");
    }
    public void setCommonBackground(String value) {
        sharedPreferences.edit().putString(backgroundColour, value).commit();
    }
	//--------------------------- Screen Data ----------------------------------------
	public int getSavedScreenId() {
		return sharedPreferences.getInt(savedScreenId, 0);
	}
	public void setSavedScreenId(int value) {
		sharedPreferences.edit().putInt(savedScreenId, value).commit();
	}
	// this is not required actually...but don't know db saving of content will be there or not
	public int getLastActiveContentId() {
		return sharedPreferences.getInt(lastActiveContentId,0);
	}
	public void setLastActiveContentId(int value) {
		sharedPreferences.edit().putInt(lastActiveContentId, value).commit();
	}
	
	public int getLastActiveScreenId() {
		return sharedPreferences.getInt(lastActiveScreenId,0);
	}
	public void setLastActiveScreenId(int value) {
		sharedPreferences.edit().putInt(lastActiveScreenId, value).commit();
	}
	//---this ends here

    // this is called every time the app starts to check if it has auth token data or not
    public String getAuthToken() {
        return sharedPreferences.getString(authToken, "");
    }
	public void setAuthToken(String value) {
		sharedPreferences.edit().putString(authToken, value).commit();
	}
    // this is called every time the app starts to check if it has launch data or not
    public boolean isLaunchDataAvailable() {
        return sharedPreferences.getBoolean(launchData,false);
    }
    public void setLaunchDataAvailable(boolean value) {
        sharedPreferences.edit().putBoolean(launchData, value).commit();
    }
    /*Method that will be responsible for saving navigation drawer json*/
    public void saveNavigationDrawerJSON(String drawerJSON){
        if(drawerJSON!=null) {
            sharedPreferences.edit().putString(navigationDrawer, drawerJSON).commit();
        }else{
            Log.w(TAG, "Navigation Drawer JSON is null, drawerJSON = " + drawerJSON);
        }
    }
    /*Method that will be responsible for get Navigation drawer JSON */
    public String getNavigationDrawerJSON(){
        return sharedPreferences.getString(navigationDrawer,null);
    }

	public void saveActionBarJson(String actionBarJson){
		if (actionBarJson != null){
			sharedPreferences.edit().putString(actionBar, actionBarJson).commit();}
		else {
			Log.e(TAG, "actionBar JSON is null");
		}

	}
    public String getLikedItemColor(){
        return sharedPreferences.getString(likedItemColor, "#00ff00  ");
    }
    public void setLikedItemColor(String value){
        sharedPreferences.edit().putString(likedItemColor, value).commit();
    }
	public String getActionBarJson(){
		return sharedPreferences.getString(actionBar,null);
	}
}
