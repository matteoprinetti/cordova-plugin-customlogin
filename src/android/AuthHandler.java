package com.migros.plugins;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.EditText;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AuthHandler extends CordovaPlugin {
	
	private String seed = "";
	private String m_EncryptedText = "";
	private static int BLOCKS = 128;

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
		String password ="";


		// 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
		AlertDialog builder = new AlertDialog.Builder(cordova.getActivity()).create();

// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(host+ " " + realm);
		builder.setTitle("Bitte Logon Scannen");


// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
		//AlertDialog dialog = builder.create();

		final EditText input = new EditText(cordova.getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		input.requestFocus();
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				int a = 1;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
				int a = 1;
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				if(s.length() != 0) {
					m_EncryptedText  = input.getText().toString();

					//LOG.d("customlogin", "decrpyting " + m_EncryptedText);

					byte m_debased[] = Base64.decode(m_EncryptedText,Base64.DEFAULT);

					//LOG.d("customlogin", "debased " + bytesToHex(m_debased) + " seed " + seed);

					try {
						byte[] decrypted = decryptAES(seed,m_debased);
						String result = new String(decrypted, "UTF8");
						//LOG.d("customlogin", "decrypted " +  result);
						String[] parts = result.split(";");
						//LOG.d("customlogin", "user " +  parts[0] + " pwd " + parts[1]);
						handler.proceed(parts[0], parts[1]);
						builder.dismiss();

					} catch (Exception e) {
						LOG.d("customlogin",e.getMessage());
						e.printStackTrace();
					}

					input.setText("");
					}
			}
		});

		builder.setView(input);

		// Set up the buttons
		/*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				m_EncryptedText  = input.getText().toString();
				handler.proceed(username, password);
			}
		}); */

		builder.show();


		return true;

    }
	public static byte[] decryptAES(String _seed, byte[] data) throws Exception {
		//byte[] rawKey = getRawKey(_seed.getBytes("UTF8"));
		LOG.d("customlogin", "AES data: " + bytesToHex(data) );
		LOG.d("customlogin", "AES seed: " + bytesToHex(_seed.getBytes(StandardCharsets.UTF_8) ));


		SecretKeySpec skeySpec = new SecretKeySpec(_seed.getBytes(StandardCharsets.UTF_8), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(_seed.getBytes(StandardCharsets.UTF_8));
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec,ivSpec);
		return cipher.doFinal(data);
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
}