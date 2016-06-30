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

        this.$get = ['ClientStatus', '$http', '$q', '$resource', '$location', '$rootScope', function (ClientStatus, $http, $q, $resource, $location, $rootScope) {
            var service = {

            };
            //fetch from the server
            service.getclientStatuses = function () {

                    return $http.get('client/')
                        .then(function (response) {
                            return response.data.map(function (clientStatus) {
                                return new ClientStatus(clientStatus);
                            });
                        });

            };

            return service;
        }];

        var init = function () {
        };

        init();
    });