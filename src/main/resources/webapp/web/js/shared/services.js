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

        this.$get = ['ClientStatus', 'ClientDetail', 'Application', 'ApplicationDetail', '$http', '$q', '$resource', '$location', '$rootScope', 'ConstantValues', 'CacheFactory', function (ClientStatus, ClientDetail, Application , ApplicationDetail, $http, $q, $resource, $location, $rootScope, ConstantValues, CacheFactory) {

            var myAwesomeCache = CacheFactory('myAwesomeCache', {
                maxAge: ConstantValues.cacheMaxAge, // Items added to this cache expire after 15 minutes.
                cacheFlushInterval: ConstantValues.cacheAutoFlushInterval, // This cache will clear itself every hour.
                deleteOnExpire: 'aggressive', // Items will be deleted from this cache right when they expire.
                storageMode: 'localStorage' // This cache will use `localStorage`.
            });

            var service = {

            };
            //fetch from the server
            service.getAllClientStatuses = function () {

                return $http.get('client/', {cache:myAwesomeCache})
                    .then(function (response) {

                        return response.data.map(function (clientStatus) {
                            var result = new ClientStatus(clientStatus);
                            return result;
                        });

                    });

            };

            service.clearCache_ClientList = function () {
                myAwesomeCache.remove('client/');
            }

            service.clearCache_ApplicationStatusCache = function (artifactId) {
                myAwesomeCache.remove('application/' + artifactId + "/status/");
            }


            service.getClientDetail = function (clientId){

                return $q.all([$http.get('client/' + clientId + "/status/",  {cache:myAwesomeCache}), $http.get('client/' + clientId + "/env/"), $http.get('client/' + clientId + "/config/"),  $http.get('client/' + clientId + "/events/")])
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


            service.getApplicationConfig = function(applicationid){
                return  $http.get('application/' + applicationid + "/config/").then(function (response) {
                    return response.data;
                });
            }

            service.getApplicationDetail = function (id, artifactId){



                if(artifactId!=null && id!=null) {
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
                }

            };

            service.addApplicationConfig = function(applicationDetail){
                if(applicationDetail.artifactId){
                    var appConfigToSave = angular.copy(applicationDetail);
                    
                    return $http.post('application/', {'artifactId' : appConfigToSave.artifactId}).then(function (response) {
                    	if(appConfigToSave.configJsonContent!=null){
	                        return $http.post('application/' +  response.data.id + "/config/", appConfigToSave.configJsonContent).then(function (response2) {
	                            return response2.data;
	                        });
                    	} else {
                    		return response.data;
                    	}
                    });
                }
            }

            service.updateApplicationConfig = function (applicationDetail) {
                if (applicationDetail.id) {
                    var appConfigToSave = angular.copy(applicationDetail);
                    console.log(appConfigToSave);
                    if(typeof appConfigToSave.config.id != "undefined"){
                    	 return $http.put('application/' + applicationDetail.id + "/config/" +  applicationDetail.config.id , appConfigToSave.configJsonContent).then(function (response) {
                             return applicationDetail;
                         });
                    } else {
                    	return $http.post('application/' +  applicationDetail.id+ "/config/", appConfigToSave.configJsonContent).then(function (response2) {
                            return response2.data;
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



            return service;
        }];

        var init = function () {

        };

        init();
    });
