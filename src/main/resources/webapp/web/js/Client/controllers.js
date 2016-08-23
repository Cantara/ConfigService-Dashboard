/**
 * Created by huy on 6/27/2016.
 */
angular.module('Client')
    .controller('ClientListviewController',['$scope', 'CSService', '$location', '$interval', 'ConstantValues', function ($scope, CSService, $location, $interval, ConstantValues) {

        var UpdateStatusIntervalPromise;

        $scope.goto = function (clientStatus) {
            $location.path('/clients/' + clientStatus.client.clientId);
        }

        $scope.refresh = function () {

            CSService.clearCache_ClientList();
            fetchClients();

        }

        var init = function () {

            $scope.isAdmin=false;
            $scope.dataLoading = true;
            fetchClients();

            theInterval = $interval(function(){
            	var startTime = new Date().getTime();
            	console.log("start updating...");
            	CSService.clearCache_ClientList();
                fetchClients(); //refresh clients automatically in 60 minutes
                console.log("finished in " + (new Date().getTime() - startTime)/1000 + " seconds" );
                console.log("next update in "  + (ConstantValues.clientsAutoUpdateInterval/1000) +  " seconds");

            }.bind(this), ConstantValues.clientsAutoUpdateInterval);


        }

        var theInterval;

        $scope.$on('$destroy', function () {
            $interval.cancel(theInterval);
        });


        var fetchClients = function(){

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