/**
 * Created by huy on 6/28/2016.
 */
'use strict';

/* Services */

angular.module('app')
    .provider("CSService", function () {


        this.configure = function (value) {
            //some initialization if required here
        }

        this.$get = ['ClientStatus', 'ClientDetail', 'Application', 'ApplicationDetail', '$http', '$q', '$resource', '$location', '$rootScope', function (ClientStatus, ClientDetail, Application , ApplicationDetail, $http, $q, $resource, $location, $rootScope) {
            var service = {

            };
            //fetch from the server
            service.getAllClientStatuses = function () {

                    return $http.get('client/')
                        .then(function (response) {
                            return response.data.map(function (clientStatus) {
                                return new ClientStatus(clientStatus);
                            });
                        });

            };
            service.getClientDetail = function (clientId){

                return $q.all([$http.get('client/' + clientId + "/status/"), $http.get('client/' + clientId + "/env/"), $http.get('client/' + clientId + "/config/"),  $http.get('client/' + clientId + "/events/")])
                    .then(function (response) {
                        var status = response[0].data;
                        var env = response[1].data;
                        var config = response[2].data;
                        var events = response[3].data;
                        return new ClientDetail(status, env, config, events);
                    });

            };

            service.getAllApplications = function () {

                return $http.get('application/')
                    .then(function (response) {
                        return response.data.map(function (app) {
                            return new Application(app);
                        });
                    });

            };
            service.getApplicationDetail = function (id, artifactId){


                return $q.all([$http.get('application/' + artifactId + "/status/"), $http.get('application/' + id + "/config/")])
                    .then(function (response) {
                        var status = response[0].data;
                        var config = response[1].data;
                        return new ApplicationDetail(id, artifactId, status, config);
                    });

            };

            return service;
        }];

        var init = function () {
        };

        init();
    });