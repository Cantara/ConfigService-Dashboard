/**
 * Created by huy on 6/28/2016.
 */
'use strict';

/* Services */

angular.module('app')
    .provider("CSService",  function () {



       this.configure = function (value) {
            //some initialization if required here

        }

        this.$get = ['ClientStatus', 'ClientDetail', 'Application', 'ApplicationConfig', 'ApplicationDetail', '$http', '$q', '$resource', '$location', '$rootScope', 'ConstantValues', 'CacheFactory', 'browserDetectionService', function (ClientStatus, ClientDetail, Application , ApplicationConfig, ApplicationDetail, $http, $q, $resource, $location, $rootScope, ConstantValues, CacheFactory,browserDetectionService) {

            var aliasmap=null;


            
            var clientCache = CacheFactory('clientCache', {
                maxAge: ConstantValues.cacheMaxAge, // Items added to this cache expire after 15 minutes.
                cacheFlushInterval: ConstantValues.cacheAutoFlushInterval, // This cache will clear itself every hour.
                deleteOnExpire: 'aggressive', // Items will be deleted from this cache right when they expire.
                storageMode: 'localStorage' // This cache will use `localStorage`.
            });

            var service = {


            };

            //fetch from the server
            service.getAllClientStatuses = function () {

                function handleClientResponse(clientStatus) {
                    if (clientStatus.client != null && clientStatus.latestClientHeartbeatData != null) {
                        var result = new ClientStatus(clientStatus);
                        service.applyFriendlyName(result.client);
                        return result;
                    }
                }


                if(browserDetectionService.isNotIE()) {

                    return $http.get('client/', {cache: clientCache})
                        .then(function (response) {
                            var a = response.data.map(handleClientResponse);
                            return a.filter(function(item, index, array){
                                return item;
                            });
                        });
                } else {
                    
                    return $http.get('client/?random='+new Date().getTime(), {cache: clientCache})
                        .then(function (response) {

                            return response.data.map(handleClientResponse);
                            

                        });
                }

            };

            service.clearAllCache= function () {
                clientCache.removeAll();

            }

            service.clearCache_ClientList = function () {
                clientCache.remove('client/');

            }

            service.clearCache_ClientList_Of_OneApplication = function (artifactId) {
                clientCache.remove('application/' + artifactId + "/status/");

            }

            service.getClientDetail = function (clientId){

                return $q.all([$http.get('client/' + clientId + "/status/"), $http.get('client/' + clientId + "/env/"), $http.get('client/' + clientId + "/config/"),  $http.get('client/' + clientId + "/events/")])
                    .then(function (response) {
                        var status = response[0].data;
                        var env = response[1].data;
                        var config = response[2].data;
                        var events = response[3].data;
                        var result = new ClientDetail(status, env, config, events);
                        service.applyFriendlyName(result.status.client);
                        return result;

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

            service.getApplicationConfig = function(applicationid){
                return  $http.get('application/' + applicationid + "/config/").then(function (response) {
                  
                    return response.data;
                });
            }
            
            service.getApplicationConfigByConfigId = function(configid){
                return  $http.get('application/config/' + configid).then(function (response) {
                   
                    return response.data;
                });
            }
            
            service.getApplicationArtifactIdByConfigId = function(configid){
                return  $http.get('application/config/findartifactid/' + configid).then(function (response) {
                   
                    return response.data;
                });
            }

            service.getApplicationDetail = function (id, artifactId){
         	
                if(browserDetectionService.isNotIE()) {
                    if(artifactId!=null && id!=null) {
                    	
                    	
                        return $q.all([$http.get('application/' + artifactId + "/status/", {cache: clientCache}), $http.get('application/' + id + "/config/"),  $http.get('application/' + id + "/config/list")]).then(function (response) {
                            var status = response[0].data;
                            var the_latest_config = response[1].data;
                            var all_configs = response[2].data;
                            return new ApplicationDetail(id, artifactId, status, the_latest_config, all_configs);
                        }, function(re){
                        	 return $http.get('application/' + artifactId + "/status/", {cache: clientCache}).then(function (response) {
                        		  return new ApplicationDetail(id, artifactId, response.data, null, null);
                            });
                        	
                        });
                    }
                } else {

                    if(artifactId!=null && id!=null) {
                        return $q.all([$http.get('application/' + artifactId + "/status/?random="+new Date().getTime(), {cache: clientCache}), $http.get('application/' + id + "/config/"), $http.get('application/' + id + "/config/list")]).then(function (response) {
                            var status = response[0].data;
                            var the_latest_config = response[1].data;
                            var all_configs = response[2].data;
                            return new ApplicationDetail(id, artifactId, status, the_latest_config, all_configs);
                        },  function(re){
                       	 	return $http.get('application/' + artifactId + "/status/", {cache: clientCache}).then(function (response) {
                       	 		return new ApplicationDetail(id, artifactId, response.data, null, null);
                       	 });
                   	
                   });
                    }
                }
             

            };

           
           /*
            service.addApplication_ = function(applicationDetail){
            	var appConfigToSave = angular.copy(applicationDetail);
            	return service.addApplication(appConfigToSave.artifactId , JSON.stringify(appConfigToSave.configJsonContent));
            }*/
            
         
            service.addNewApplicationConfigToAnApp = function(artifactId, configJsonContent){

                  return $http.get('application/' + artifactId).then(function (response){
                	  if(configJsonContent) {
                		  var appId = null;
                		  if(response.data){
                			  appId = response.data.id;
                			  return service.createANewConfigForThisApp(appId, configJsonContent);
                		  } else {
                			  //artifact does not exist, create new
                			  return  $http.post('application/', {'artifactId' : artifactId}).then(function (response2) {
                				  appId = response2.data.id;
                				  return service.createANewConfigForThisApp(appId, configJsonContent);
                			  });
                		  }

                	  }
                    	
                    	return response;
                    });
               
            }
            
            service.getApplicationByArtifactId = function(artifactId){
            	 return $http.get('application/' + artifactId).then(function (response){
                 	return response;
                 });
            }
            
            service.createANewConfigForThisApp = function(appId, json) {

            	return $http.post('application/' +  appId + "/config/", json).then(function (response3) {                   	
            		return response3;
            	});

            }

            service.updateApplicationConfig = function (appId, configId, configJsonContent) {                    
                    if(configId){
                    	 return $http.put('application/' + appId + "/config/" +  configId , configJsonContent).then(function (response) {
                             return response;
                         });
                    } else {
                    	return $http.post('application/' +  appId+ "/config/", configJsonContent).then(function (response2) {
                            return response2;
                        });
                    }                  
            }
 
            service.removeApplicationConfig = function(configId){
                return $http.delete("application/config/" + configId);
            }

            service.removeApplication = function(applicationId){
                return $http.delete("application/" + applicationId);
            }
            
            service.canRemoveThisApp = function(applicationId){
                return $http.get("application/canDeleteApp/" + applicationId);
            }
            
            service.canRemoveThisAppConfig = function(configId){
                return $http.get("application/canDeleteAppConfig/" + configId);
            }
            
            service.saveAlias = function (clientid, alias) {
                return $http.post('client/alias/' + clientid + "/" +  alias,{}).then(function (response) {
                    if(response.data.success) {
                        //save to the map
                        aliasmap[clientid] = alias;
                    }

                    return response;
                });
            }

            service.applyFriendlyName=function(client){

            	
                if(aliasmap!=null){

                    if(client.clientId in aliasmap) {
                        client.alias = aliasmap[client.clientId];
                    }

                } else {
                    $http.get('client/aliasmap')
                        .then(function (response) {
                            aliasmap = response.data;
                            if(client.clientId in aliasmap) {
                                client.alias = aliasmap[client.clientId];
                            }});

                }


            }

            service.getIgnoreList=function(client){

                    return $http.get('client/ignoredClients')
                        .then(function (response) {
                            return response.data;
                        });


            }

            service.ignoreClient = function (clientid) {
                return $http.post('client/ignore/' + clientid,{"ignore":"true"}).then(function (response) {

                    return response;
                });
            }
            

            service.getAllConfigs = function(){
            	 return $http.get('application/config')
                 .then(function (response) {
                	
                     return response.data.map(function (config) {

                         return new ApplicationConfig(config);

                         
                     });
                 });
 
            }

            service.getConfigByConfigId = function(configId){
	           	 return $http.get('application/config/' + configId).then(function (response) {
	           		 return response;
	              });

           }
            
            service.updateClient = function(client) {
            	  if (client) {
                      
                 	  return $http.put('client/' + client.clientId, client).then(function (response) {
                          return response;
                      });
                     
                  }
            }
            
            service.updateClientList = function(clientList) {
          	  if (clientList) {
                    
               	  return $http.put('client/updateClientList', clientList).then(function (response) {
                        return response;
                    });
                   
                }
          }

            return service;
        }];


    });
