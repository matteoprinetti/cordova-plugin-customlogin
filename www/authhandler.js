AuthHandler = function(username, password, success, error) {
	if(cordova.platformId == "android"){
		cordova.exec(success, error, "AuthHandler", "login", [username, password]);
	}
}

if(!cordova.plugins) {
	cordova.plugins = {};
}
if(!cordova.plugins.AuthHandler) {
	cordova.plugins.AuthHandler = AuthHandler;
}

