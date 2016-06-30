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


           
            CSService.getclientStatuses().then(function (data) {

                $scope.green = 0;
                $scope.red = 0;
                $scope.yellow = 0;
                $scope.clock = Date.now();
                $scope.clientStatuses = data;

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
    .controller('ClientDetailController', ['$scope', '$timeout', function ($scope, $timeout) {

        var init = function () {


        }

        init();

    }]);