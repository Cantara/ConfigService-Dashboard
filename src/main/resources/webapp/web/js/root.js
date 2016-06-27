/**
 * Created by huy on 6/27/2016.
 */
'use strict';

angular.module('app')
    .controller('RootController', ['$scope', '$modal', function ($scope, $modal) {



        $scope.$on('$routeChangeSuccess', function (event, current, previous) {
            $scope.currentRoute = current;
            $scope.routeHasError = false;
        });

        $scope.$on('$routeChangeError', function (event, current, previous, error) {
            if (error.status === 404) {
                $scope.routeHasError = true;
                $scope.routeError = current.routeErrorMessage;
            }

        });


    }]);
