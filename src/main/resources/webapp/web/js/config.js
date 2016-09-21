/**
 * Created by huy on 6/27/2016.
 */

angular.module('app').constant('ConstantValues', {

    cacheMaxAge: 4*60*1000, //cache is kept in 4 minutes
    cacheAutoFlushInterval:60*60*1000, // This cache will clear itself every hour.
    greenTimeOut: 5*60 *1000, //5 minutes
    redTimeout: 10*60*1000, //10 minutes
    clientsAutoUpdateInterval: 2*60*1000 //refresh cache in 2 minutes
    
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


