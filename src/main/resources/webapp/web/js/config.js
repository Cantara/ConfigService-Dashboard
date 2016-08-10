/**
 * Created by huy on 6/27/2016.
 */
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
