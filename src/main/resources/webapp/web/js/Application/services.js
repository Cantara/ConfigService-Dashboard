/**
 * Created by huy on 6/29/2016.
 */
'use strict';

angular.module('Application')
    .factory("ApplicationService", ['CSService', 'ApplicationDetail', 'ApplicationConfig', '$q', '$http', function (CSService, ApplicationDetail, ApplicationConfig, $q, $http) {
        var service = {};
        var currentApplicationDetail;
        
        service.getApplicationDetail = function (id, artifactId, configId) {
        	 
            if (id) {
                return CSService.getApplicationDetail(id, artifactId).then(function (applicationDetail) {
                    currentApplicationDetail = applicationDetail;
                    return currentApplicationDetail;
                });
            } else {
            	if(configId && artifactId) {
	            	return CSService.getConfigByConfigId(configId).then(function(response){
	            		 currentApplicationDetail = new ApplicationDetail();
	                     if(artifactId){
	                     	currentApplicationDetail.artifactId = artifactId;
	                     }

	                     currentApplicationDetail.configJsonContent = new ApplicationConfig(response.data);
	                     delete currentApplicationDetail.configJsonContent.id;
	                     delete currentApplicationDetail.configJsonContent.lastChanged;

	                     return currentApplicationDetail;
	            	});
	            } else {
	            	
	            	currentApplicationDetail = new ApplicationDetail();
	            	return $q.when(currentApplicationDetail);
	            }
               
                
              
            }
        };

      
        
        service.canRemoveThisApp = function(id){      
        	return CSService.canRemoveThisApp(id);	
        }
        
        service.canRemoveThisAppConfig = function(id){        
        	return CSService.canRemoveThisAppConfig(id);
        }
        
        service.removeApplication = function (id) { 
             return CSService.removeApplication(id);
        }
        
        service.removeApplicationConfig = function (id) {         	
            return CSService.removeApplicationConfig(id);
        }
        
  
        service.createAppConfig = function(artifactId, configJsonContent){
        	return CSService.addNewApplicationConfigToAnApp(artifactId, configJsonContent);
        }
        
        service.updateAppConfig = function(appId, configId, configJsonContent){
        	return CSService.updateApplicationConfig(appId, configId, configJsonContent);
        }
        
        
        
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
