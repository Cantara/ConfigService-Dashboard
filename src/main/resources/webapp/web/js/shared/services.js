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

        this.$get = ['ClientStatus', 'ClientDetail', 'Application', 'ApplicationDetail', '$http', '$q', '$resource', '$location', '$rootScope', 'ConstantValues', 'CacheFactory', 'browserDetectionService', function (ClientStatus, ClientDetail, Application , ApplicationDetail, $http, $q, $resource, $location, $rootScope, ConstantValues, CacheFactory,browserDetectionService) {

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

            service.getApplicationDetail = function (id, artifactId){

                if(browserDetectionService.isNotIE()) {
                    if(artifactId!=null && id!=null) {
                        return $q.all([$http.get('application/' + artifactId + "/status/", {cache: clientCache}), $http.get('application/' + id + "/config/")]).then(function (response) {
                            var status = response[0].data;
                            var config = response[1].data;
                            return new ApplicationDetail(id, artifactId, status, config);
                        });
                    }
                } else {

                    if(artifactId!=null && id!=null) {
                        return $q.all([$http.get('application/' + artifactId + "/status/?random="+new Date().getTime(), {cache: clientCache}), $http.get('application/' + id + "/config/")]).then(function (response) {
                            var status = response[0].data;
                            var config = response[1].data;
                            return new ApplicationDetail(id, artifactId, status, config);
                        });
                    }
                }

               /* if(artifactId!=null && id!=null) {
                    return $q.all([$http.get('application/' + artifactId + "/status/"), $http.get('application/' + id + "/config/")]).then(function (response) {
                        var status = response[0].data;
                        var config = response[1].data;
                        return new ApplicationDetail(id, artifactId, status, config);
                        });
                } else if(id!=null) {
                    return $q.all([$http.get('application/' + id + "/config/")]).then(function (response) {
                        var config = response[0].data;
                        return new ApplicationDetail(id, artifactId, null, config);
                    });
                }*/

            };

            service.addApplicationConfig = function(applicationDetail){
                if(applicationDetail.artifactId){
                    var appConfigToSave = angular.copy(applicationDetail);
                    
                    return $http.post('application/', {'artifactId' : appConfigToSave.artifactId}).then(function (response) {
                    	if(appConfigToSave.configJsonContent!=null){
	                        return $http.post('application/' +  response.data.id + "/config/", appConfigToSave.configJsonContent).then(function (response2) {
	                            return response2;
	                        });
                    	} else {
                    		return response;
                    	}
                    });
                }
            }

            service.updateApplicationConfig = function (applicationDetail) {
                if (applicationDetail.id) {
                    var appConfigToSave = angular.copy(applicationDetail);
                    
                    if(typeof appConfigToSave.config.id != "undefined"){
                    	 return $http.put('application/' + applicationDetail.id + "/config/" +  applicationDetail.config.id , appConfigToSave.configJsonContent).then(function (response) {
                             return response;
                         });
                    } else {
                    	return $http.post('application/' +  applicationDetail.id+ "/config/", appConfigToSave.configJsonContent).then(function (response2) {
                            return response2;
                        });
                    }
                   
                }
            }

            service.removeApplicationConfig = function(applicationId, configId){
                return $http.delete("application/" + applicationId  + "/config/" + configId);
            }

            service.removeApplication = function(applicationId){
                return $http.delete("application/" + applicationId);
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
                return $http.post('client/ignore/' + clientid,{}).then(function (response) {

                    return response;
                });
            }



            return service;
        }];


    });
