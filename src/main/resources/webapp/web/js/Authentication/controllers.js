/**
 * Created by huy on 8/9/2016.
 */

angular.module('Authentication').controller('LoginController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService', 'CSService',
        function ($scope, $rootScope, $location, AuthenticationService, CSService) {
            // reset login status
            AuthenticationService.ClearCredentials();
            //clear cache
            CSService.clearAllCache();

            $scope.login = function () {
                $scope.dataLoading = true;
                AuthenticationService.Login($scope.username, $scope.password, function(response) {

                    if(response.success) {
                        AuthenticationService.SetCredentials($scope.username, $scope.password, response.role);
                        $location.path('/');
                    } else {
                        $scope.error = response.message;
                        $scope.dataLoading = false;
                    }
                });
            };
        }]);