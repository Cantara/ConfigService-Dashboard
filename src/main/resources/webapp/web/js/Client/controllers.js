/**
 * Created by huy on 6/27/2016.
 */
angular.module('Client')
    .controller('ClientListviewController',['$scope', 'CSService', '$location', '$window', '$interval', 'ConstantValues', 'toastr', function ($scope, CSService, $location, $window, $interval, ConstantValues, toastr) {

        var UpdateStatusIntervalPromise;

        $scope.checkboxModel = {
            value : false
        };

        $scope.goto = function (clientStatus) {
            if($scope.checkboxModel.value) {

                var baseLen = $location.absUrl().length - $location.url().length;
                var base = $location.absUrl().substr(0, baseLen);
                $window.open(base+ "/clients/" + clientStatus.client.clientId, '_blank');
            } else {
                $location.path('/clients/' + clientStatus.client.clientId);
            }
        }

        $scope.refresh = function () {
            toastr.success('Refreshing clients...');
            CSService.clearCache_ClientList();
            fetchClients();
            
            //do again
            $interval.cancel(theInterval);
            theInterval = $interval(function(){

            	CSService.clearCache_ClientList();
                fetchClients(); //refresh clients automatically in 60 seconds

            }.bind(this), ConstantValues.clientsAutoUpdateInterval);
            
        }

        var init = function () {
            $scope.isAdmin=false;
            $scope.dataLoading = true;
            fetchClients();

            theInterval = $interval(function(){

            	CSService.clearCache_ClientList();
                fetchClients(); //refresh clients automatically in 60 seconds


            }.bind(this), ConstantValues.clientsAutoUpdateInterval);


        }

        var theInterval;
        var startTime;

        $scope.$on('$destroy', function () {
            $interval.cancel(theInterval);
        });


        $scope.clientCountModel = {
            green : 0,
            red: 0,
            yellow:0,
            total: 0
        };

        var fetchClients = function(){
            startTime = new Date().getTime();
            console.log("start updating...");
            CSService.getAllClientStatuses().then(function (data) {

                var completeTime = new Date().getTime() - startTime;
                //toastr.success('load completed in ' + (completeTime/1000) + " seconds");
                console.log("finished in " + (completeTime/1000) + " seconds" );
                console.log("next update in "  + (ConstantValues.clientsAutoUpdateInterval/1000) +  " seconds");
                $scope.green = 0;
                $scope.red = 0;
                $scope.yellow = 0;
                $scope.clientStatuses = data;
                $scope.dataLoading = false;
                angular.forEach($scope.clientStatuses, function(value, key) {
                    if(!value.client.ignored) {
                        var color = value.status;
                         if(color === 'green'){
                        	$scope.green ++;
                    	} else if(color === 'red'){
                        	$scope.red ++;
                    	} else if(color === 'yellow'){
                        	$scope.yellow ++;
                    	}
                    }
                });


            });
        }

        init();

    }]);

angular.module('Client')
    .controller('ClientDetailController', ['$scope', 'CSService', 'ClientService',  '$routeParams', '$timeout',  'ConstantValues', 'toastr', '$interval', '$filter', '$q', '$log', '$document', '$location', function ($scope, CSService, ClientService, $routeParams, $timeout, ConstantValues, toastr, $interval,  $filter, $q, $log, $document, $location) {

    	$scope.applicationConfigs = [];
    	
    

        $scope.saveEdit=function(alias){
        	var d = $q.defer();
            if (alias==='') {
            	d.resolve('The display name shoud not be empty')
            } else {
            	 ClientService.saveAlias(alias).then(function (response) {

                     if(response.data.success) {
                         toastr.success('Update successfully');
                         d.resolve();
                     } else {
                         toastr.error('Update failed: ' + response.message);
                         d.reject('Server error!');
                     }
                 }, function(error){
                	 if(error.statusText){
                		 d.reject(error.statusText);
                	 } else {
                		 d.reject('Server error!');
                	 }
                 });
            }
            return d.promise;

        }
    
        
        
          
          $scope.moveToConfig = function(configId){
        	  
        	    var d = $q.defer();
        	    
        	    var clientToSave = angular.copy($scope.clientDetail.status.client);
        	    if (clientToSave) {
        	    	clientToSave.applicationConfigId = configId; //move to the new config id
                    delete clientToSave.alias;
                    delete clientToSave.ignored;
          	  }
        	    
        	    ClientService.updateClient(clientToSave).then(function(res){
        	    	 toastr.success('Update successfully. The client heart beat status will be updated soon.',  {timeOut: 15000});
        	    	      	    	
        	    	 $('#appConfigSelectModal').modal('hide');
        	    	 d.resolve();
        	    	 location.reload();
        	    	
        	    }, function(err){
        	    	if(err.statusText){
        	    		d.resolve(err.statusText)
        	    	} else {
        	    		d.reject('Server error!');
        	    	}
        	    	
        	    });
        	    return d.promise;
        	 
          }
          
          $scope.refresh = function(){
        	  
        	  updateClientStatus();
          }
          
          
          /*
          $scope.$watch('clientDetail.config.id', function(newVal, oldVal) {
            if (newVal !== oldVal) {
              var selected = $filter('filter')($scope.applicationConfigs, {id: $scope.clientDetail.config.id});
              $scope.clientDetail.config.name = selected.length ? selected[0].name : null;
            }
          });*/
       

        $scope.ignoreMe = function(){
            ClientService.ignoreClient().then(function (response) {

                if(response.data.success) {
                    toastr.success('Client was hidden successfully');
                    $location.path('/clients');
                } else {
                    toastr.error('Request failed: ' + response.message);
                }
            });
        }

        var theInterval;
        var startTime;

        $scope.$on('$destroy', function () {
            $interval.cancel(theInterval);
        });


        
        var init = function () {
            if ($routeParams.id) {
                $scope.dataLoading = true;
                updateClientStatus();
               
                theInterval = $interval(function(){               	
                	updateClientStatus(); //refresh clients automatically in 30 seconds
                }.bind(this), ConstantValues.clientAutoUpdateInterval);
            }
        }
        
        $scope.configData = {};
        
        
        var updateClientStatus = function(){
            startTime = new Date().getTime();
            console.log("start updating...");
            ClientService.getClientDetail($routeParams.id).then(function (data) {
                $scope.clientDetail = data;
                $scope.dataLoading = false;
                  
                $scope.configEditStatus.selectedAppConfig = JSON.parse(JSON.stringify($scope.clientDetail.config));
                
                delete $scope.configEditStatus.selectedAppConfig.id;
                delete $scope.configEditStatus.selectedAppConfig.lastChanged;
                
                console.log("client status updated...");
                //toastr.success('client status updated...');
                
            }, function (response) {
            	if(response.statusText){
            		toastr.error(response.statusText);
            	} else {
            		toastr.error('Server error! We can not retrieve the client status!');
            	}
            });
        }

        init();
        
        $scope.configEditStatus = {selectFromExistingConfig : true};
        
        $scope.showAppConfigEditor = function (){
        	$('#appConfigSelectModal').modal('show');
        	
        	CSService.getAllConfigs().then(function (data) {
        		 $scope.configData.availableConfigs = data;
            });
        	
        	CSService.getApplicationArtifactIdByConfigId($scope.clientDetail.config.id).then(function(data) {
        		$scope.configEditStatus.artifactId = data;
        	});
        	
        }
        
        $scope.modalConfigChange = function () {
    		CSService.getApplicationArtifactIdByConfigId($scope.configData.selectedConfig.id).then(function(data) {
    			$scope.configData.artifactId = data;
    		});
    	}
        
        $scope.print = function (json) {
    		return JSON.stringify(json, null, 2);
    	};

        
        $scope.moveClient = function (){
        	if($scope.configEditStatus.selectFromExistingConfig){
        		return $scope.moveToConfig($scope.configData.selectedConfig.id);
        	} else {
        		CSService.addApplication($scope.configEditStatus.artifactId, JSON.stringify($scope.configEditStatus.selectedAppConfig)).then(function(response){
        			return $scope.moveToConfig(response.data.id);
        		});
        	}
        }
        
        $scope.goto = function () {
        	return CSService.getApplicationByArtifactId($scope.clientDetail.status.latestClientHeartbeatData.artifactId).then(function(response){
        		if(response.data){
              		var appId = response.data.id;
              		$location.path('/applications/' + appId + "/" + $scope.clientDetail.status.latestClientHeartbeatData.artifactId);
              	}
        	});
        	
            
        }

        
        

    	
    	
    }]);