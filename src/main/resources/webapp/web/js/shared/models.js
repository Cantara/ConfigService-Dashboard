/**
 * Created by huy on 6/29/2016.
 */
'use strict';

/* Model classes */
angular.module('app')
    .factory('Client', function () {
        function Client(args) {

            this.clientId = args.clientId;
            this.applicationConfigId = args.applicationConfigId;
            this.autoUpgrade = args.autoUpgrade;
        }

        return Client;
    });
angular.module('app')
    .factory('ClientHeartbeatData', function () {
        function ClientHeartbeatData(args) {

            this.artifactId = args.artifactId;
            this.tags=args.tags;
            this.clientName=args.clientName;
            this.configLastChanged=args.configLastChanged;
            this.applicationConfigId=args.applicationConfigId;
            this.timeOfContact=args.timeOfContact;
        }

        return ClientHeartbeatData;
    });


angular.module('app')
    .factory('ClientStatus', ['Client', 'ClientHeartbeatData', function (Client, ClientHeartbeatData) {
        function ClientStatus(args) {
            this.client = new Client(args.client);
            this.latestClientHeartbeatData = new ClientHeartbeatData(args.latestClientHeartbeatData);
            this.status = 'red';
            var diff = Date.now() - new Date(this.latestClientHeartbeatData.timeOfContact);
            var lastSeen = diff / 1000;
            if(lastSeen < 480) {
                this.status = 'green';
                this.color = '#A0CF89';
            } else if(lastSeen > 480 && lastSeen < 60*60*24) {
                this.status = 'yellow';
                this.color = '#FFFF75';
            } else {
                this.status = 'red';
                this.color = '#FF572E';
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
            var logs = this.eventGroups['jau']['files']['logs/jau.log']['tags']['jau']['events'];
            return JSON.stringify(logs, null, 2);
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
        };
        return ClientDetail;
    }]);

/* Model classes */
angular.module('app')
    .factory('Application', function () {
        function Application(args) {

            this.id = args.id;
            this.artifactId = args.artifactId;

        }

        return Application;
    });

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
        function ApplicationDetail(id, artifactId, status, config) {
            this.id = id;
            this.artifactId = artifactId;
            if (status != null) {
                this.status = new ApplicationStatus(status);
            }
            if(config!=null) {
                this.config = new ApplicationConfig(config);
                this.configJsonContent = JSON.stringify(this.config, null, 2);
            }
        };
        return ApplicationDetail;
    }]);