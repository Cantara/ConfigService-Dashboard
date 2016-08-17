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
        'Authentication', 'angular-cache'
    ]);

angular.module('Client', []);
angular.module('Application',[]);
angular.module('Authentication',[]);