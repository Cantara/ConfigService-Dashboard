/**
 * Created by huy on 6/27/2016.
 */
angular.module('Client')
    .controller('ClientListviewController',['$scope', 'CSService', '$location', function ($scope, CSService, $location) {

        var UpdateStatusIntervalPromise;

        $scope.goto = function (clientStatus) {
            $location.path('/clients/' + clientStatus.client.clientId);
        }


        var init = function () {

            $scope.isAdmin=false;
            $scope.dataLoading = true;
            CSService.getAllClientStatuses().then(function (data) {

                $scope.green = 0;
                $scope.red = 0;
                $scope.yellow = 0;
                $scope.clock = Date.now();
                $scope.clientStatuses = data;
                $scope.dataLoading = false;
                angular.forEach($scope.clientStatuses, function(value, key) {
                    var color = value.status;
                    if(color === 'green'){
                      $scope.green ++;
                    } else if(color === 'red'){
                        $scope.red ++;
                    } else if(color === 'yellow'){
                        $scope.yellow ++;
                    }
                });

            });


        }

        init();

    }]);

angular.module('Client')
    .controller('ClientDetailController', ['$scope', 'ClientService',  '$routeParams', '$timeout', function ($scope, ClientService, $routeParams, $timeout) {


        var init = function () {
            if ($routeParams.id) {
                $scope.dataLoading = true;
                ClientService.getClientDetail($routeParams.id).then(function (data) {
                    $scope.clientDetail = data;
                    $scope.dataLoading = false;
                }, function () {

                });

            }
        }

        init();

    }]);