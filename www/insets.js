var run = require("cordova/exec");

var AndroidInsets = {
    hasCutout: function(success, error) {
        run(success, error, "AndroidInsets", "hasCutout");
    },

    setLayout: function(success, error) {
        run(success, error, "AndroidInsets", "setLayout");
    },

    getInsetTop: function (success, error) {
        run(success, error, "AndroidInsets", "getInsetsTop");
    },
    
    getInsetRight: function (success, error) {
        run(success, error, "AndroidInsets", "getInsetsRight");
    },
    
    getInsetBottom: function (success, error) {
        run(success, error, "AndroidInsets", "getInsetsBottom");
    },
    
    getInsetLeft: function (success, error) {
        run(success, error, "AndroidInsets", "getInsetsLeft");
    }

};


module.exports = AndroidInsets;