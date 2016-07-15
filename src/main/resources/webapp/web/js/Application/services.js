/**
 * Created by huy on 6/29/2016.
 */
'use strict';

angular.module('Application')
    .factory("ApplicationService", ['CSService', '$q', function (CSService, $q) {
        var service = {};

        service.getApplicationDetail = function (id, artifactId) {
            if (id && artifactId) {

                return CSService.getApplicationDetail(id, artifactId).then(function (applicationDetail) {
                    return applicationDetail;
                });
            } else {
                return null;
            }
        };

        return service;
    }]);
