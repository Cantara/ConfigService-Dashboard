/**
 * Created by huy on 6/28/2016.
 */
/// <reference path="services.js" />
'use strict';

angular.module('Client')
    .factory("ClientService", ['$q', 'CSService', function ($q, CSService) {
        var service = {};
        var currentClientId;

        service.saveAlias = function (alias) {
            if(currentClientId) {
                return CSService.saveAlias(currentClientId, alias);
            }
        }

        service.ignoreClient = function () {
            if(currentClientId) {
                return CSService.ignoreClient(currentClientId);
            }
        }

        service.getClientDetail = function (clientId) {
            if (clientId) {
                currentClientId = clientId;
                return CSService.getClientDetail(clientId).then(function (clientDetail) {
                    return clientDetail;
                });
            } else {
                return null;
            }
        };

        return service;
    }]);
