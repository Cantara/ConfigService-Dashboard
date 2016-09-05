/**
 * Created by huy on 6/27/2016.
 */
angular.module('Client')
    .controller('ClientListviewController',['$scope', 'CSService', '$location', '$interval', 'ConstantValues', 'toastr', function ($scope, CSService, $location, $interval, ConstantValues, toastr) {

        var UpdateStatusIntervalPromise;

        $scope.goto = function (clientStatus) {
            $location.path('/clients/' + clientStatus.client.clientId);
        }

        $scope.refresh = function () {
            toastr.success('Force updating clients...');
            CSService.clearCache_ClientList();
            fetchClients();
            
            //do again
            $interval.cancel(theInterval);
            theInterval = $interval(function(){

            	CSService.clearCache_ClientList();
                fetchClients(); //refresh clients automatically in 60 seconds

            }.bind(this), ConstantValues.clientsAutoUpdateInterval);
            
        }

        var init = function () {

            $scope.isAdmin=false;
            $scope.dataLoading = true;
            fetchClients();

            theInterval = $interval(function(){

            	CSService.clearCache_ClientList();
                fetchClients(); //refresh clients automatically in 60 seconds


            }.bind(this), ConstantValues.clientsAutoUpdateInterval);


        }

        var theInterval;
        var startTime;

        $scope.$on('$destroy', function () {
            $interval.cancel(theInterval);
        });


        var fetchClients = function(){
            startTime = new Date().getTime();
            console.log("start updating...");
            CSService.getAllClientStatuses().then(function (data) {

                var completeTime = new Date().getTime() - startTime;
                //toastr.success('load completed in ' + (completeTime/1000) + " seconds");
                console.log("finished in " + (completeTime/1000) + " seconds" );
                console.log("next update in "  + (ConstantValues.clientsAutoUpdateInterval/1000) +  " seconds");

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
    .controller('ClientDetailController', ['$scope', 'ClientService',  '$routeParams', '$timeout', 'toastr', function ($scope, ClientService, $routeParams, $timeout, toastr) {

        $scope.editMode = false;

        var aliasBeforeEdit;
        $scope.toggleEdit=function(){

            if($scope.editMode === true) {
                $scope.clientDetail.status.client.alias = aliasBeforeEdit;
            } else {
                aliasBeforeEdit =  $scope.clientDetail.status.client.alias;
            }

            $scope.editMode = !$scope.editMode;
        }

        $scope.saveEdit=function(alias){

            ClientService.saveAlias(alias).then(function (response) {

                if(response.data.success) {
                    $scope.editMode = false;
                    toastr.success('Update successfully');
                } else {
                    toastr.error('Update failed: ' + response.message);
                }
            });

        }


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