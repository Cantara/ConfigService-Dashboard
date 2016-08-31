/**
 * Created by huy on 6/27/2016.
 */
angular.module('Application')
    .controller('ApplicationListviewController', ['$scope', 'AuthenticationService', 'CSService', '$location', function ($scope, AuthenticationService, CSService, $location) {



        $scope.goto = function (application) {
            $location.path('/applications/' + application.id + "/" + application.artifactId);
        }

        $scope.applications = [];
        $scope.displayedCollection = [];
        
        var init = function () {
            $scope.dataLoading = true;
            CSService.getAllApplications().then(function (data) {

                $scope.applications = data;
                $scope.displayedCollection = [].concat($scope.applications);
                $scope.dataLoading = false;

                angular.forEach($scope.displayedCollection, function(value, key) {
                    CSService.getApplicationConfig(value.id).then(function(appConfig){
                        value.setAppConfig(appConfig);
                    });
                });

            });


        }

        init();

    }]);

angular.module('Application')
    .controller('ApplicationDetailController', ['$scope', '$route', 'ApplicationService', 'ClientStatus', '$routeParams', '$timeout', '$location', 'toastr', 'CSService', '$interval', 'ConstantValues', function ($scope, $route, ApplicationService, ClientStatus, $routeParams, $timeout, $location, toastr, CSService, $interval, ConstantValues) {

        $scope.refresh = function () {

            toastr.success('Force updating clients...');
            CSService.clearCache_ClientList_Of_OneApplication($routeParams.artifactId);
            fetchClients();

        }

        $scope.goto = function (clientStatus) {
            $location.path('/clients/' + clientStatus.client.clientId);
        }

        $scope.editMode = false;
        $scope.setEdit = function () {
            $scope.editMode = !$scope.editMode;
        }

        $scope.create = function () {
            $scope.submitted = true;
            if ($scope.applicationForm.$invalid) return;

            ApplicationService.save().then(function (response) {
                if(response.data.startsWith("200")) {
                    $scope.submitted = false;
                    init();
                    $scope.applicationForm.$setPristine();
                    toastr.success('Application was created successfully!');
                } else {
                    toastr.error('Creation failed: ' + response.data);
                }
            });
        }

        $scope.update = function () {

            ApplicationService.save().then(function (response) {

                if(response.data.startsWith("200")) {
                    $scope.editMode = false;
                    $route.reload();
                    toastr.success('Update successfully');
                } else {
                    toastr.error('Update failed: ' + response.data);
                }
            });
        }

        $scope.canRemoveThisConfig = function () {
            return ApplicationService.canRemoveThisConfig();
        }
        $scope.removeApplicationConfig = function () {
            ApplicationService.removeApplicationConfig().then(function (response) {
                if(response.data.startsWith("204")) {
                    toastr.success('Delete successfully');
                    $location.path('/applications/');

                } else {
                    toastr.error('Delete failed: ' + response.data);
                }
            })
        }


        $scope.hasError = function (modelController, error) {
            return (modelController.$dirty || $scope.submitted) && error;
        };

        var extractClientStatuses = function (appStatus) {
            var clientStatuses = [];
            if (appStatus.status != null) {
                var applicationConfigId = appStatus.config.id;
                var allClientHeartbeatData = appStatus.status.allClientHeartbeatData;
                $scope.green = 0;
                $scope.red = 0;
                $scope.yellow = 0;

                for (var k in allClientHeartbeatData) {
                    if (typeof allClientHeartbeatData[k] !== 'function') {
                        var clientStatus = new ClientStatus({
                            "client": {"clientId": k, "applicationConfigId": applicationConfigId},
                            "latestClientHeartbeatData": allClientHeartbeatData[k]
                        });

                        var color = clientStatus.status;
                        if(color === 'green'){
                            $scope.green ++;
                        } else if(color === 'red'){
                            $scope.red ++;
                        } else if(color === 'yellow'){
                            $scope.yellow ++;
                        }
                        clientStatuses.push(clientStatus);
                    }
                }

            }
            return clientStatuses;
        }

        var theInterval;

        $scope.$on('$destroy', function () {
            $interval.cancel(theInterval);
        });


        var init = function () {

            $scope.dataLoading = true;

            fetchClients();

            theInterval = $interval(function(){

                CSService.clearCache_ClientList_Of_OneApplication($routeParams.artifactId);
                fetchClients(); //refresh clients automatically in 60 seconds


            }.bind(this), ConstantValues.clientsAutoUpdateInterval);


        }

        var startTime=0;

        var fetchClients = function(){

            startTime = new Date().getTime();
            console.log("start fetching clients...");
            ApplicationService.getApplicationDetail($routeParams.id, $routeParams.artifactId).then(function (data) {

                var completeTime = new Date().getTime() - startTime;
                console.log("finished in " + completeTime + " seconds" );
                console.log("next update in "  + (ConstantValues.clientsAutoUpdateInterval/1000) +  " seconds");

                //some results
                $scope.applicationDetail = data;
                $scope.clientStatuses = extractClientStatuses(data);
                $scope.dataLoading = false;


            }, function () {

            });

        }

        init();

    }]);