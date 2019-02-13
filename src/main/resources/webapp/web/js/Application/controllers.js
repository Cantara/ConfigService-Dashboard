/**
 * Created by huy on 6/27/2016.
 */
angular.module('Application')
.controller('ApplicationListviewController', ['$scope', 'AuthenticationService', 'CSService', '$location', function ($scope, AuthenticationService, CSService, $location) {



	$scope.goto = function (application) {
		$location.path('/applications/' + application.id + "/" + application.artifactId);
	}

	$scope.applications = [];
	$scope.displayedCollection = [];

	var init = function () {
		$scope.dataLoading = true;
		CSService.getAllApplications().then(function (data) {

			$scope.applications = data;
			$scope.displayedCollection = [].concat($scope.applications);
			$scope.dataLoading = false;

			angular.forEach($scope.displayedCollection, function(value, key) {

				CSService.getApplicationConfig(value.id).then(function(appConfig){

					value.setAppConfig(appConfig);
				});
			});

		});


	}

	init();

}]);

angular.module('Application')
.controller('ApplicationDetailController', ['$scope', '$route', 'ApplicationService', 'ClientStatus', '$routeParams', '$timeout', '$location', '$window', 'toastr', 'CSService', '$interval', 'ConstantValues', '$q', function ($scope, $route, ApplicationService, ClientStatus, $routeParams, $timeout, $location, $window, toastr, CSService, $interval, ConstantValues, $q) {

	$scope.refresh = function () {

		toastr.success('Refreshing clients...');
		CSService.clearCache_ClientList_Of_OneApplication($routeParams.artifactId);
		fetchClients();

		$interval.cancel(theInterval);
		theInterval = $interval(function(){

			CSService.clearCache_ClientList_Of_OneApplication($routeParams.artifactId);
			fetchClients(); //refresh clients automatically in 60 seconds


		}.bind(this), ConstantValues.clientsAutoUpdateInterval);
	}

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


	$scope.editMode = false;
	$scope.setEdit = function () {
		$scope.editMode = !$scope.editMode;
	}

	$scope.create = function () {
		$scope.submitted = true;
		if ($scope.applicationForm.$invalid) return;

		ApplicationService.save($scope.applicationDetail).then(function (response) {           	
			if(response.status === 200) {
				$scope.submitted = false;
				init();
				$scope.applicationForm.$setPristine();
				window.history.go(-1);

				toastr.success('Application was created successfully!');
			} else {
				toastr.error('Creation failed: ' + response.data);
			}
		});
	}

	$scope.update = function () {

		ApplicationService.save($scope.applicationDetail).then(function (response) {

			if(response.status === 200) {
				$scope.editMode = false;
				$route.reload();
				toastr.success('Update successfully');
			} else {
				toastr.error('Update failed: ' + response.data);
			}
		});
	}
	/*     TODO: undo this block if we want to remove a specific config
	 *     In theory, one application can have many different configurations which should be kept in the table application_configs
	 * 	   For simplicity, we only use one configuration for one application as of now but the UI can be upgraded in the future
	 *
                $scope.canRemoveThisConfig = function () {
                    return ApplicationService.canRemoveThisConfig();
                }
                $scope.removeApplicationConfig = function () {
                    ApplicationService.removeApplicationConfig().then(function (response) {
                        if(response.status === 204) {
                            toastr.success('Delete successfully');
                            $location.path('/applications/');

                        } else {
                            toastr.error('Delete failed: ' + response.data);
                        }
                    })
                }
	 */

	$scope.canRemoveThisApp = false;
	$scope.canRemoveThisAppConfig = false;

	//removing an app also means to remove its configurations
	var canRemoveThisAppCheck = function () {
		if($scope.applicationDetail.id) {
			return ApplicationService.canRemoveThisApp($scope.applicationDetail.id).then(function(response){

				$scope.canRemoveThisApp = response.data;
				return response.data;
			});
		}
	}

	var canRemoveThisAppConfigCheck = function (){
		//return ($scope.clientStatuses === undefined || $scope.clientStatuses.length == 0);
		if($scope.applicationDetail.configData!= undefined){
			ApplicationService.canRemoveThisAppConfig($scope.applicationDetail.configData.selectedConfig.id).then(function(response){

				$scope.canRemoveThisAppConfig = response.data;
				return response.data;
			});
		}
	}

	$scope.removeApplication = function () {
		ApplicationService.removeApplication($scope.applicationDetail.id).then(function (response) {
			if(response.status === 204 || response.status === 404) {
				toastr.success('Delete successfully');
				$location.path('/applications/');

			} else {
				toastr.error('Delete failed: ' + response.data);
			}
		}, function(response){
			if(response.status === 204 || response.status === 404) {
				toastr.success('Delete successfully');
				$location.path('/applications/');

			} else {
				toastr.error('Delete failed: ' + response.data);
			}
		})
	}

	$scope.removeApplicationConfig = function () {
		ApplicationService.removeApplicationConfig($scope.applicationDetail.configData.selectedConfig.id).then(function (response) {
			if(response.status === 204 || response.status === 404) {
				toastr.success('Delete successfully');
				$location.path('/applications/');

			} else {
				toastr.error('Delete failed: ' + response.data);
			}
		}, function(response){
			if(response.status === 204 || response.status === 404) {
				toastr.success('Delete successfully');
				$location.path('/applications/');

			} else {
				toastr.error('Delete failed: ' + response.data);
			}
		})
	}

	/*
        $scope.hasError = function (modelController, error) {
            return (modelController.$dirty || $scope.submitted) && error;
        };*/

	var loadClientStatuses = function () {
		

			CSService.getIgnoreList().then(function (ignoreList) {

				var clientStatuses = [];
				if ($scope.applicationDetail.status != null) {

					var allClientHeartbeatData = $scope.applicationDetail.status.allClientHeartbeatData;
					$scope.green = 0;
					$scope.red = 0;
					$scope.yellow = 0;

					for (var k in allClientHeartbeatData) {


						if(ignoreList.indexOf(k) == -1) {

							if (typeof allClientHeartbeatData[k] !== 'function') {
								if($scope.applicationDetail.configData.selectedConfig.id === allClientHeartbeatData[k].applicationConfigId) {

									var clientStatus = new ClientStatus({
										"client": {
											"clientId": k,
											"alias": k,
											"applicationConfigId": allClientHeartbeatData[k].applicationConfigId,
											"ignored": false
										},
										"latestClientHeartbeatData": allClientHeartbeatData[k]
									});

									CSService.applyFriendlyName(clientStatus.client);
									var color = clientStatus.status;
									if (color === 'green') {
										$scope.green++;
									} else if (color === 'red') {
										$scope.red++;
									} else if (color === 'yellow') {
										$scope.yellow++;
									}
									clientStatuses.push(clientStatus);
								}

							}
						}
					}

				}

				$scope.clientStatuses = clientStatuses;

			});

		





	}

	var theInterval;

	$scope.$on('$destroy', function () {
		$interval.cancel(theInterval);
	});


	var init = function () {
		$scope.openNewTab = true;
		$scope.dataLoading = true;

		CSService.clearCache_ClientList_Of_OneApplication($routeParams.artifactId);
		fetchClients();

		theInterval = $interval(function(){

			CSService.clearCache_ClientList_Of_OneApplication($routeParams.artifactId);
			fetchClients(); //refresh clients automatically in 60 seconds


		}.bind(this), ConstantValues.clientsAutoUpdateInterval);


	}

	$scope.print = function (json) {
		return JSON.stringify(json, null, 2);
	};

	$scope.configChange = function (){
		loadClientStatuses();    
		canRemoveThisAppConfigCheck();
	}

	var startTime=0;
//	$scope.selectedConfig = null;

	var fetchClients = function(){

		startTime = new Date().getTime();
		console.log("start fetching clients...");
		CSService.clearCache_ClientList_Of_OneApplication($routeParams.artifactId);
		ApplicationService.getApplicationDetail($routeParams.id, $routeParams.artifactId, $routeParams.configId).then(function (data) {

			var completeTime = new Date().getTime() - startTime;
			console.log("finished in " + completeTime + " seconds" );
			console.log("next update in "  + (ConstantValues.clientsAutoUpdateInterval/1000) +  " seconds");


			//some results
			$scope.applicationDetail = data;

			canRemoveThisAppCheck();
			
			canRemoveThisAppConfigCheck();

			loadClientStatuses();
			
			$scope.dataLoading = false;


		}, function () {

		});

	}

	$scope.configData = {};
	$scope.configEditStatus = {selectFromExistingConfig : true};

	$scope.showAppConfigEditor = function (){
		$('#appConfigSelectModal').modal('show');
		$scope.configData.selectedConfig = null;
		CSService.getAllConfigs().then(function (data) {
			$scope.configData.availableConfigs = data;
		});

		$scope.configEditStatus.artifactId = $scope.applicationDetail.artifactId;
		$scope.configEditStatus.selectedAppConfig = JSON.parse(JSON.stringify($scope.applicationDetail.configData.selectedConfig));


	}

	$scope.moveClients = function(){
		if($scope.configEditStatus.selectFromExistingConfig){
			return $scope.moveToConfig($scope.configData.selectedConfig.id);
		} else {
			CSService.addApplication($scope.configEditStatus.artifactId, JSON.stringify($scope.configEditStatus.selectedAppConfig)).then(function(response){
				return $scope.moveToConfig(response.data.id);
			});
		}
	}

	$scope.moveToConfig = function(configId){

		var d = $q.defer();

		return ApplicationService.updateClientList($scope.clientStatuses, configId).then(function(res){
			toastr.success('Update successfully. The status will be updated shortly in a few minutes.',  {timeOut: 10000});
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


	init();

}]);