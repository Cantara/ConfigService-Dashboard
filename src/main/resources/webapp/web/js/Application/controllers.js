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
    .controller('ApplicationDetailController', ['$scope', '$route', 'ApplicationService', 'ClientStatus', '$routeParams', '$timeout', '$location', 'toastr', 'CSService', function ($scope, $route, ApplicationService, ClientStatus, $routeParams, $timeout, $location, toastr, CSService) {

        $scope.refresh = function () {
            CSService.clearCache_ApplicationStatusCache($routeParams.artifactId);
            init();

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
                for (var k in allClientHeartbeatData) {
                    if (typeof allClientHeartbeatData[k] !== 'function') {
                        var clientStatus = new ClientStatus({
                            "client": {"clientId": k, "applicationConfigId": applicationConfigId},
                            "latestClientHeartbeatData": allClientHeartbeatData[k]
                        });
                        clientStatuses.push(clientStatus);
                    }
                }
            }
            return clientStatuses;
        }

        var init = function () {

            $scope.dataLoading = true;
            ApplicationService.getApplicationDetail($routeParams.id, $routeParams.artifactId).then(function (data) {
                $scope.applicationDetail = data;
                $scope.clientStatuses = extractClientStatuses(data);
                $scope.dataLoading = false;
            }, function () {

            });


        }

        init();

    }]);