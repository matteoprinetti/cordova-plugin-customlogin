cordova.define("cordova-plugin-customlogin.AuthHandler", function (require, exports, module) {
	AuthHandler = function (seed, success, error) {
		if (cordova.platformId == "android") {
			cordova.exec(success, error, "AuthHandler", "setSeed", [seed]);
		}
	}

	if (!cordova.plugins) {
		cordova.plugins = {};
	}
	if (!cordova.plugins.AuthHandler) {
		cordova.plugins.AuthHandler = AuthHandler;
	}


});
;