/**
 * Created by huy on 6/27/2016.
 */
'use strict';

angular.module('app',
    [   'smart-table',
        'toastr',
        'monospaced.elastic',
        'ngRoute',
        'ngSanitize',
        'ui.bootstrap',
        'ngAnimate',
        'ngMessages',
        'ngResource',
        'ngRoute',
        'ngCookies',
        'Client',
        'Application',
        'Authentication', 'angular-cache', 'browserDetectionService', 'xeditable'
    ]);

angular.module('browserDetectionService',[]);
angular.module('Client', []);
angular.module('Application',[]);
angular.module('Authentication',[]);

angular.module('app').run(function(editableOptions, editableThemes) {
	 editableThemes.bs3.inputClass = 'input-sm';
	  editableThemes.bs3.buttonsClass = 'btn-sm';
	  editableOptions.theme = 'bs3';
});