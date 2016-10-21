/**
 * Created by huy on 6/27/2016.
 */
angular.module('Client')
    .controller('ClientListviewController',['$scope', 'CSService', '$location', '$window', '$interval', 'ConstantValues', 'toastr', function ($scope, CSService, $location, $window, $interval, ConstantValues, toastr) {

        var UpdateStatusIntervalPromise;

        $scope.checkboxModel = {
            value : false
        };

        $scope.goto = function (clientStatus) {
            if($scope.checkboxModel.value) {

                var baseLen = $location.absUrl().length - $location.url().length;
                var base = $location.absUrl().substr(0, baseLen);
                $window.open(base+ "/clients/" + clientStatus.client.clientId, '_blank');
            } else {
                $location.path('/clients/' + clientStatus.client.clientId);
            }
        }

        $scope.refresh = function () {
            toastr.success('Refreshing clients...');
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


        $scope.clientCountModel = {
            green : 0,
            red: 0,
            yellow:0,
            total: 0
        };

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
                $scope.clientStatuses = data;
                $scope.dataLoading = false;
                angular.forEach($scope.clientStatuses, function(value, key) {
                    if(!value.client.ignored) {
                        var color = value.status;
                         if(color === 'green'){
                        	$scope.green ++;
                    	} else if(color === 'red'){
                        	$scope.red ++;
                    	} else if(color === 'yellow'){
                        	$scope.yellow ++;
                    	}
                    }
                });


            });
        }

        init();

    }]);

angular.module('Client')
    .controller('ClientDetailController', ['$scope', 'CSService', 'ClientService',  '$routeParams', '$timeout', 'toastr', function ($scope, CSService, ClientService, $routeParams, $timeout, toastr) {

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
            if (alias==='') return;
            ClientService.saveAlias(alias).then(function (response) {

                if(response.data.success) {
                    $scope.editMode = false;
                    toastr.success('Update successfully');
                } else {
                    toastr.error('Update failed: ' + response.message);
                }
            });

        }

        $scope.ignoreMe = function(){
            ClientService.ignoreClient().then(function (response) {

                if(response.data.success) {
                    toastr.success('Client was removed successfully');
                    $location.path('/clients');
                } else {
                    toastr.error('Request failed: ' + response.message);
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