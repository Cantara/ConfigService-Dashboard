/**
 * Created by huy on 6/29/2016.
 */
'use strict';

angular.module('Application')
    .factory("ApplicationService", ['CSService', 'ApplicationDetail', '$q', function (CSService, ApplicationDetail, $q) {
        var service = {};
        var currentApplicationDetail;
        var newAppConfig;
        
        service.getApplicationDetail = function (id, artifactId) {
            if (id) {
                return CSService.getApplicationDetail(id, artifactId).then(function (applicationDetail) {
                    currentApplicationDetail = applicationDetail;
                    newAppConfig = false;
                    return currentApplicationDetail;
                });
            } else {
                currentApplicationDetail = new ApplicationDetail();
                newAppConfig = true;
                return $q.when(currentApplicationDetail);
            }
        };

        /* This is renamed to service.canRemoveThisApp()
         * 
         * 
        service.canRemoveThisConfig = function(){
            if(currentApplicationDetail!=null) {

                return typeof currentApplicationDetail.status.seenInTheLastHourCount === "undefined" ||
                    (typeof currentApplicationDetail.status != "undefined" && currentApplicationDetail.status.seenInTheLastHourCount == 0);

            } else {
                return false;
            }
        }*/
        
        service.canRemoveThisApp = function(){
            if(currentApplicationDetail!=null) {

                return typeof currentApplicationDetail.status === "undefined" ||
                    (typeof currentApplicationDetail.status != "undefined" && currentApplicationDetail.status.seenInTheLastHourCount == 0);

            } else {
                return false;
            }
        }

        /* This is renamed to service.removeApplication()
         * removeApplicationConfig() is to delete a particular configuration (which can be implemented in the future)
         * 
         * 
        service.removeApplicationConfig = function () {
            if(typeof currentApplicationDetail.config.id != "undefined" ){
                return CSService.removeApplicationConfig(currentApplicationDetail.id, currentApplicationDetail.config.id);
            } else {
                return CSService.removeApplication(currentApplicationDetail.id);
            }
        }*/

        service.removeApplication = function () { 
             return CSService.removeApplication(currentApplicationDetail.id);
        }
        
        service.save = function () {
            var promise = newAppConfig ? CSService.addApplication(currentApplicationDetail) : CSService.updateApplicationConfig(currentApplicationDetail);
            promise.then(function (data) {
                newAppConfig = false;
            });
            return promise;
        };

        return service;
    }]);
