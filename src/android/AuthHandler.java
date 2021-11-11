package com.jrow2286.plugins;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;

public class AuthHandler extends CordovaPlugin {
	
	private String username = "";
	private String password = "";

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
		if ("login".equals(action)) {
			try {
				username = args.getString(0);
				password = args.getString(1);
				
				callbackContext.success("Credentials updated.");
				
			} catch (Exception e) {
				callbackContext.error("Error updating credentials: " + e.getMessage());
			}
			return true;
		}
		return false;
	}
	
	
    public boolean onReceivedHttpAuthRequest(CordovaWebView view, final ICordovaHttpAuthHandler handler, String host, String realm) {
        
		//view.loadUrl("javascript:log('onReceivedHttpAuthRequest: " + username + "');");
		handler.proceed(username, password);
		
		return true;

    }
}