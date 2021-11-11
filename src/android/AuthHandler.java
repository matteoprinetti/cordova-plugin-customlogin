package com.migros.plugins;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;

public class AuthHandler extends CordovaPlugin {
	
	private String seed = "";


    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
		if ("setSeed".equals(action)) {
			try {
				seed = args.getString(0);
			 	
				callbackContext.success("Seed set.");
				
			} catch (Exception e) {
				callbackContext.error("Error updating credentials: " + e.getMessage());
			}
			return true;
		}
		return false;
	}
	
	
    public boolean onReceivedHttpAuthRequest(CordovaWebView view, final ICordovaHttpAuthHandler handler, String host, String realm) {
        
		//view.loadUrl("javascript:log('onReceivedHttpAuthRequest: " + username + "');");

		// decrypt the seed ..

		String username="";
		String pasword ="";

		handler.proceed(username, password);
		
		return true;

    }
}