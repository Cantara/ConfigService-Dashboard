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
            var lastSeen = diff/ 1000;
            if(lastSeen<120) {
                this.status = 'green';
                this.color = '#A0CF89';
            } else if(lastSeen > 120 && lastSeen <240) {
                this.status = 'yellow';
                this.color = '#FFFF75';
            } else {
                this.status = 'red';
                this.color = '#FF572E';
            }
        };


        return ClientStatus;
}]);

