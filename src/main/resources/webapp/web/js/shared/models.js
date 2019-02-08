/**
 * Created by huy on 6/29/2016.
 */
'use strict';

/* Model classes */
angular.module('app')
    .factory('Client', function () {
        function Client(args) {
            this.clientId = args.clientId;
            this.alias = args.clientId;
            this.applicationConfigId = args.applicationConfigId;
            this.autoUpgrade = args.autoUpgrade;
            this.ignored = false;
        }

        return Client;
    });
angular.module('app')
    .factory('ClientHeartbeatData', function () {
        function ClientHeartbeatData(args) {
            if(args!=null) {
                this.artifactId = args.artifactId;
                this.tags = args.tags;
                this.clientName = args.clientName;
                this.configLastChanged = args.configLastChanged;
                this.applicationConfigId = args.applicationConfigId;
                this.timeOfContact = args.timeOfContact;
            }
        }

        return ClientHeartbeatData;
    });


angular.module('app')
    .factory('ClientStatus', ['Client', 'ClientHeartbeatData', 'ConstantValues', function (Client, ClientHeartbeatData, ConstantValues) {
        function ClientStatus(args) {
            this.client = new Client(args.client);
            this.latestClientHeartbeatData = new ClientHeartbeatData(args.latestClientHeartbeatData);
            if(this.latestClientHeartbeatData){         	 
            	 this.client.alias = this.latestClientHeartbeatData.clientName;
            }
            this.status = 'red';
            var diff = Date.now() - new Date(this.latestClientHeartbeatData.timeOfContact);
            var lastSeen = diff;
            if(lastSeen < ConstantValues.greenTimeOut) {
                this.status = 'green';
                this.bgcolor = '#4CAF50';
                this.fgcolor = '#191919';
            } else if(lastSeen > ConstantValues.greenTimeOut && lastSeen < ConstantValues.redTimeout) {
                this.status = 'yellow';
                this.bgcolor = '#FDD835';
                this.fgcolor = '#191919';
            } else {
                this.status = 'red';
                this.bgcolor = '#FF5722';
                this.fgcolor = 'white';
            }
        };

        return ClientStatus;
}]);

angular.module('app')
    .factory('ClientEnvironment', [function () {
        function ClientEnvironment(args) {
            this.envInfo = args.envInfo;
            this.timestamp = args.timestamp;
        };

        ClientEnvironment.prototype.display = function () {
            return JSON.stringify(this, null, 2);
        }

        return ClientEnvironment;
    }]);

angular.module('app')
    .factory('ExtractedEventsStore', [function () {
        function ExtractedEventsStore(args) {
            this.eventGroups = args.eventGroups;
        };

        ExtractedEventsStore.prototype.display = function () {
            //var logs = this.eventGroups['jau']['files']['logs/jau.log']['tags']['jau']['events'];
            return JSON.stringify(this.eventGroups, null, 2);
        }

        return ExtractedEventsStore;
    }]);
angular.module('app')
    .factory('ApplicationConfig', [function () {
        function ApplicationConfig(args) {
            this.id = args.id;
            this.name = args.name;
            this.lastChanged = args.lastChanged;
            this.downloadItems = args.downloadItems;
            this.configurationStores = args.configurationStores;
            this.eventExtractionConfigs = args.eventExtractionConfigs;
            this.startServiceScript = args.startServiceScript;
        };

        ApplicationConfig.prototype.display = function () {
            return JSON.stringify(this, null, 2);
        }
        return ApplicationConfig;
    }]);


angular.module('app')
    .factory('ClientDetail', ['ClientStatus', 'ClientEnvironment', 'ApplicationConfig', 'ExtractedEventsStore', function (ClientStatus, ClientEnvironment, ApplicationConfig, ExtractedEventsStore) {
        function ClientDetail(status, env, config, events) {
            this.status = new ClientStatus(status);
            this.env = new ClientEnvironment(env);
            this.config = new ApplicationConfig(config);
            this.events = new ExtractedEventsStore(events);
            
            /* This is not in use because we already implement this logic from server side (clientStatus.latestClientHeartbeatData.clientName)
             * 
             * 
            if(this.env.envInfo){
            	var computerName = this.env.envInfo.COMPUTERNAME;
            	var internalIp = JSON.stringify(this.env.envInfo).match(/\"networkinterface_.*\:.*\"(\d+\.\d+\.\d+\.\d+)\"/)[1];
            	var wrapped_os = this.env.envInfo.WRAPPER_OS ? this.env.envInfo.WRAPPER_OS :  this.env.envInfo.OS;
            	
            	//make up a meaningful name for this client
            	this.status.client.alias = computerName + ' - ' + internalIp + ' - ' + wrapped_os;  
            	console.log(this.status.client.alias);
            }*/
            
        };
        return ClientDetail;
    }]);

/* Model classes */
angular.module('app')
    .factory('Application', ['ApplicationConfig', function (ApplicationConfig) {
        function Application(args) {

            this.id = args.id;
            this.artifactId = args.artifactId;
            this.appConfig = null;
        }

        Application.prototype.setAppConfig = function (appConfig) {
            this.appConfig = new ApplicationConfig(appConfig);
        }

        return Application;
    }]);

angular.module('app')
    .factory('ApplicationStatus', function () {
        function ApplicationStatus(args) {

            this.numberOfRegisteredClients = args.numberOfRegisteredClients;
            this.seenInTheLastHourCount = args.seenInTheLastHourCount;
            this.seenInTheLastHour = args.seenInTheLastHour;
            this.allClientHeartbeatData = args.allClientHeartbeatData;
        }

        ApplicationStatus.prototype.display = function () {
            return JSON.stringify(this, null, 2);
        }

        return ApplicationStatus;
    });

angular.module('app')
    .factory('ApplicationDetail', [ 'ApplicationStatus', 'ApplicationConfig', function ( ApplicationStatus, ApplicationConfig) {
        function ApplicationDetail(id, artifactId, status, the_latest_config, all_configs) {
            this.id = id;
            this.artifactId = artifactId;
            if (status != null) {
                this.status = new ApplicationStatus(status);
            }
            
            if(all_configs!=null) {
                var configs = all_configs.map(function (config) {
                    return new ApplicationConfig(config);
                });
                
                this.configData = {
                    	availableConfigs : configs,
                    	selectedConfig : new ApplicationConfig(the_latest_config)        	
                 };
            }
            
            

        };
        return ApplicationDetail;
    }]);