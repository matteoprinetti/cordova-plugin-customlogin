# Apache Cordova Auth Handler
Allows user to set credentials for HttpAuthRequest handler via javascript using Apache Cordova

## Supported Platforms

- Android

## Supported Authentication Methods

- Basic
- Digest
- NTLM

### Android

This plugin lets you store a username and password that are passed to the onReceivedHttpAuthRequest handler.

Call this before any XHR requests so that the username and password are used:

```
function success(str){
	console.log(str);
}
function error(str){
	console.log(str);
}
cordova.plugins.AuthHandler(username, password, success, error);
```