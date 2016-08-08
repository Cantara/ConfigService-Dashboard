/**
 * Created by huy on 6/27/2016.
 */
angular.module('Application')
    .controller('ApplicationListviewController', ['$scope', 'CSService', '$location', function ($scope, CSService, $location) {



        $scope.goto = function (application) {
            $location.path('/applications/' + application.id + "/" + application.artifactId);
        }


        var init = function () {

            CSService.getAllApplications().then(function (data) {

                $scope.applications = data;


            });


        }

        init();

    }]);

angular.module('Application')
    .controller('ApplicationDetailController', ['$scope', '$route', 'ApplicationService', 'ClientStatus', '$routeParams', '$timeout', '$location', 'toastr', function ($scope, $route, ApplicationService, ClientStatus, $routeParams, $timeout, $location, toastr) {

        $scope.goto = function (clientStatus) {
        	console.log(clientStatus);
            $location.path('/clients/' + clientStatus.client.clientId);
        }

        $scope.editMode = false;
        $scope.setEdit = function () {
            $scope.editMode = !$scope.editMode;
        }

        $scope.create = function () {
            $scope.submitted = true;
            if ($scope.applicationForm.$invalid) return;

            ApplicationService.save().then(function (data) {

                $scope.submitted = false;
                init();
                $scope.applicationForm.$setPristine();
                toastr.success('Application was created successfully!');
            });
        }

        $scope.update = function () {

            ApplicationService.save().then(function (data) {
                $scope.editMode = false;
                $route.reload();
                toastr.success('Update successfully');
            });
        }

        $scope.canRemoveThisConfig = function () {
            return ApplicationService.canRemoveThisConfig();
        }
        $scope.removeApplicationConfig = function () {
            ApplicationService.removeApplicationConfig().then(function (data) {
                $route.reload();
                toastr.success('Delete successfully');
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


            ApplicationService.getApplicationDetail($routeParams.id, $routeParams.artifactId).then(function (data) {
                $scope.applicationDetail = data;
                $scope.clientStatuses = extractClientStatuses(data);
            }, function () {

            });


        }

        init();

    }]);