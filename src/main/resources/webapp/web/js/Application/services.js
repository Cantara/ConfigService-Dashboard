/**
 * Created by huy on 6/29/2016.
 */
'use strict';

angular.module('Application')
    .factory("ApplicationService", ['CSService', 'ApplicationDetail', 'ApplicationConfig', '$q', '$http', function (CSService, ApplicationDetail, ApplicationConfig, $q, $http) {
        var service = {};
        var currentApplicationDetail;
        var newAppConfig;
        
        service.getApplicationDetail = function (id, artifactId, configId) {
        	 
            if (id) {
                return CSService.getApplicationDetail(id, artifactId).then(function (applicationDetail) {
                    currentApplicationDetail = applicationDetail;
                    newAppConfig = false;
                    return currentApplicationDetail;
                });
            } else {
            	if(configId && artifactId) {
	            	return CSService.getConfigByConfigId(configId).then(function(response){
	            		 currentApplicationDetail = new ApplicationDetail();
	                     if(artifactId){
	                     	currentApplicationDetail.artifactId = artifactId;
	                     }
	//                     if(configId){
	//                     	currentApplicationDetail.configJsonContent = CSService.getConfigByConfigId(configId);
	//                     }
	                     currentApplicationDetail.configJsonContent = new ApplicationConfig(response.data);
	                     delete currentApplicationDetail.configJsonContent.id;
	                     delete currentApplicationDetail.configJsonContent.lastChanged;
	                    
	                     newAppConfig = true;
	                     //return $q.when(currentApplicationDetail);
	                     return currentApplicationDetail;
	            	});
	            } else {
	            	
	            	currentApplicationDetail = new ApplicationDetail();
	            	newAppConfig = true;
	            	return $q.when(currentApplicationDetail);
	            }
               
                
              
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
        
        service.canRemoveThisApp = function(id){
           
        	/*if(currentApplicationDetail!=null) {

                return typeof currentApplicationDetail.status === "undefined" ||
                    (typeof currentApplicationDetail.status != "undefined" && currentApplicationDetail.status.seenInTheLastHourCount == 0);

            } else {
                return false;
            }*/
        	return CSService.canRemoveThisApp(id);
        	
        }
        service.canRemoveThisAppConfig = function(id){
            
        	/*if(currentApplicationDetail!=null) {

                return typeof currentApplicationDetail.status === "undefined" ||
                    (typeof currentApplicationDetail.status != "undefined" && currentApplicationDetail.status.seenInTheLastHourCount == 0);

            } else {
                return false;
            }*/
        	return CSService.canRemoveThisAppConfig(id);
        	
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

        service.removeApplication = function (id) { 
             return CSService.removeApplication(id);
        }
        
        service.removeApplicationConfig = function (id) { 
        	
            return CSService.removeApplicationConfig(id);
        }
        
        
        
        service.save = function (app) {
        	
            var promise = newAppConfig ? CSService.addApplication(app.artifactId, app.configJsonContent) : CSService.updateApplicationConfig(app);
            promise.then(function (data) {
                newAppConfig = false;
            });
            return promise;
        };
        
        service.updateClientList = function (clientStatuses, configId) {
        	var clients = [];
        	 angular.forEach(clientStatuses, function(value, key) {
        		 var client = angular.copy(value.client);
        		 client.applicationConfigId = configId;
        		 delete client.alias;
                 delete client.ignored;
                 clients.push(client);
             });
        	
        	 return CSService.updateClientList(clients);
        };


        return service;
    }]);
