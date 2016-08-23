/**
 * Created by huy on 8/23/2016.
 */
angular.module('app').factory('browserDetectionService', function() {

        var myservice = {};


        myservice.isNotIE = function(){
            return myservice.getIEVersion()===false;
        };

        myservice.getIEVersion = function() {
            var ua = window.navigator.userAgent;

            // Test values; Uncomment to check result …

            // IE 10
            // ua = 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)';

            // IE 11
            // ua = 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko';

            // Edge 12 (Spartan)
            // ua = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0';

            // Edge 13
            // ua = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586';

            var msie = ua.indexOf('MSIE ');
            if (msie > 0) {
                // IE 10 or older => return version number
                return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
            }

            var trident = ua.indexOf('Trident/');
            if (trident > 0) {
                // IE 11 => return version number
                var rv = ua.indexOf('rv:');
                return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
            }

            var edge = ua.indexOf('Edge/');
            if (edge > 0) {
                // Edge (IE 12+) => return version number
                return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
            }

            // other browser
            return false;
        };

        myservice.isCompatible= function () {

            var browserInfo = navigator.userAgent;
            var browserFlags =  {};

            browserFlags.ISFF = browserInfo.indexOf('Firefox') != -1;
            browserFlags.ISOPERA = browserInfo.indexOf('Opera') != -1;
            browserFlags.ISCHROME = browserInfo.indexOf('Chrome') != -1;
            browserFlags.ISSAFARI = browserInfo.indexOf('Safari') != -1 && !browserFlags.ISCHROME;
            browserFlags.ISWEBKIT = browserInfo.indexOf('WebKit') != -1;

            browserFlags.ISIE = browserInfo.indexOf('Trident') > 0 || navigator.userAgent.indexOf('MSIE') > 0;
            browserFlags.ISIE6 = browserInfo.indexOf('MSIE 6') > 0;
            browserFlags.ISIE7 = browserInfo.indexOf('MSIE 7') > 0;
            browserFlags.ISIE8 = browserInfo.indexOf('MSIE 8') > 0;
            browserFlags.ISIE9 = browserInfo.indexOf('MSIE 9') > 0;
            browserFlags.ISIE10 = browserInfo.indexOf('MSIE 10') > 0;
            browserFlags.ISOLD = browserFlags.ISIE6 || browserFlags.ISIE7 || browserFlags.ISIE8 || browserFlags.ISIE9; // MUST be here

            browserFlags.ISIE11UP = browserInfo.indexOf('MSIE') == -1 && browserInfo.indexOf('Trident') > 0;
            browserFlags.ISIE10UP = browserFlags.ISIE10 || browserFlags.ISIE11UP;
            browserFlags.ISIE9UP = browserFlags.ISIE9 || browserFlags.ISIE10UP;

            return !browserFlags.ISOLD;
        };


    return myservice;

});