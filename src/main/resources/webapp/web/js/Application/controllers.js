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
    .controller('ApplicationDetailController', ['$scope', 'ApplicationService', 'ClientStatus', '$routeParams', '$timeout', function ($scope, ApplicationService, ClientStatus, $routeParams, $timeout) {

        var extractClientStatuses = function (appStatus) {
            var applicationConfigId = appStatus.config.id;
            var allClientHeartbeatData = appStatus.status.allClientHeartbeatData;
            var clientStatuses = [];
            for (var k in allClientHeartbeatData) {
                if (typeof allClientHeartbeatData[k] !== 'function') {
                    var clientStatus = new ClientStatus({
                        "client": {"clientId": k, "applicationConfigId": applicationConfigId},
                        "latestClientHeartbeatData": allClientHeartbeatData[k]
                    });
                    clientStatuses.push(clientStatus);
                }
            }
            return clientStatuses;
        }

        var init = function () {
            if ($routeParams.artifactId && $routeParams.id) {


                ApplicationService.getApplicationDetail($routeParams.id, $routeParams.artifactId).then(function (data) {
                    $scope.applicationDetail = data;
                    $scope.clientStatuses = extractClientStatuses(data);
                }, function () {

                });

            }

        }

        init();

    }]);