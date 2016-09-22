/**
 * Created by huy on 6/27/2016.
 */

angular.module('app').constant('ConstantValues', {

    cacheMaxAge: 5*60*1000, //cache is kept in 5 mins
    cacheAutoFlushInterval:60*60*1000, // This cache will clear itself every hour.
    greenTimeOut: 15*60 *1000, // 15 mins gets yellow
    redTimeout: 2*24*60*60*1000, //yellow before 2 days, after which comes the red
    clientsAutoUpdateInterval: 5*60*1000 //clear cache and auto fetch occurs each 5 minutes

});
angular.module('app').config(function ($routeProvider, CSServiceProvider, $httpProvider) {

    CSServiceProvider.configure("something");

    $routeProvider.when('/login', {
        controller: 'LoginController',
        templateUrl: 'partials/login.html',
        hideMenus: true
    }).when('/', {
        templateUrl: 'partials/clients.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ClientListviewController'
    }).when('/clients', {
        templateUrl: 'partials/clients.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ClientListviewController'
    }).when('/clients/:id', {
        templateUrl: 'partials/client.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ClientDetailController'
    }).when('/applications', {
        templateUrl: 'partials/applications.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ApplicationListviewController'
    }).when('/applications/:id/:artifactId', {
        templateUrl: 'partials/application.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ApplicationDetailController'
    }).when('/application/new', {
        templateUrl: 'partials/application-edit.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ApplicationDetailController'
    }).when('/application/edit/:id', {
        templateUrl: 'partials/application-edit.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ApplicationDetailController'
    });


}).run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });
    }]);


