/**
 * Created by huy on 6/27/2016.
 */
angular.module('app').config(function ($routeProvider, CSServiceProvider, $httpProvider) {

    CSServiceProvider.configure("something");

    $routeProvider.when('/', {
        redirectTo: '/clients'
    });

    $routeProvider.when('/clients', {
        templateUrl: 'partials/clients.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ClientListviewController'
    });
    $routeProvider.when('/clients/:id', {
        templateUrl: 'partials/client.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ClientDetailController'
    });
    $routeProvider.when('/applications', {
        templateUrl: 'partials/applications.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ApplicationListviewController'
    });
    $routeProvider.when('/applications/:id/:artifactId', {
        templateUrl: 'partials/application.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ApplicationDetailController'
    });
    $routeProvider.when('/applications/new', {
        templateUrl: 'partials/application-new.html',
        leftNav: 'partials/left-nav-main.html',
        topNav: 'partials/top-nav.html',
        controller: 'ApplicationDetailController'
    });


});
