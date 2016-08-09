'use strict';
angular.module('appe2e', ['app', 'ngMockE2E'])
    .run(function ($httpBackend) {


        $httpBackend.whenGET(/^partials\//).passThrough();
        $httpBackend.whenGET('client/').respond([{"client":{"clientId":"ea15657a-62f4-4db5-a9a6-cc4340d3c708","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","autoUpgrade":true},"latestClientHeartbeatData":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T22:05:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-06-29T19:38:32.590Z"}},{"client":{"clientId":"ec30e40a-0c08-47b5-9710-7fcac6926d78","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","autoUpgrade":true},"latestClientHeartbeatData":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T19:40:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-06-27T20:00:12.235Z"}},{"client":{"clientId":"bed9e97b-2090-4fe0-bfac-ab44252151e6","applicationConfigId":"b2435492-e011-4d15-b2a3-815395608fa7","autoUpgrade":true},"latestClientHeartbeatData":{"artifactId":"myApplication","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T22:05:18.994Z","applicationConfigId":"b2435492-e011-4d15-b2a3-815395608fa7","timeOfContact":"2016-06-29T16:32:23.076Z"}},{"client":{"clientId":"fac23d46-5ed9-4275-b677-d9f1ccaebc06","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","autoUpgrade":true},"latestClientHeartbeatData":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T22:05:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-06-29T19:38:15.241Z"}}]);
        $httpBackend.whenGET(new RegExp('client/.*/env')).respond({"envInfo":{"HISTCONTROL":"ignoredups","HISTSIZE":"1000","HOME":"/home/JAU2","HOSTNAME":"ip-172-31-57-144","LANG":"en_US.UTF-8","LESSOPEN":"||/usr/bin/lesspipe.sh %s","LESS_TERMCAP_mb":"\u001B[01;31m","LESS_TERMCAP_md":"\u001B[01;38;5;208m","LESS_TERMCAP_me":"\u001B[0m","LESS_TERMCAP_se":"\u001B[0m","LESS_TERMCAP_ue":"\u001B[0m","LESS_TERMCAP_us":"\u001B[04;38;5;111m","LOGNAME":"JAU2","LS_COLORS":"rs=0:di=38;5;27:ln=38;5;51:mh=44;38;5;15:pi=40;38;5;11:so=38;5;13:do=38;5;5:bd=48;5;232;38;5;11:cd=48;5;232;38;5;3:or=48;5;232;38;5;9:mi=05;48;5;232;38;5;15:su=48;5;196;38;5;15:sg=48;5;11;38;5;16:ca=48;5;196;38;5;226:tw=48;5;10;38;5;16:ow=48;5;10;38;5;21:st=48;5;21;38;5;15:ex=38;5;34:*.tar=38;5;9:*.tgz=38;5;9:*.arc=38;5;9:*.arj=38;5;9:*.taz=38;5;9:*.lha=38;5;9:*.lz4=38;5;9:*.lzh=38;5;9:*.lzma=38;5;9:*.tlz=38;5;9:*.txz=38;5;9:*.tzo=38;5;9:*.t7z=38;5;9:*.zip=38;5;9:*.z=38;5;9:*.Z=38;5;9:*.dz=38;5;9:*.gz=38;5;9:*.lrz=38;5;9:*.lz=38;5;9:*.lzo=38;5;9:*.xz=38;5;9:*.bz2=38;5;9:*.bz=38;5;9:*.tbz=38;5;9:*.tbz2=38;5;9:*.tz=38;5;9:*.deb=38;5;9:*.rpm=38;5;9:*.jar=38;5;9:*.war=38;5;9:*.ear=38;5;9:*.sar=38;5;9:*.rar=38;5;9:*.alz=38;5;9:*.ace=38;5;9:*.zoo=38;5;9:*.cpio=38;5;9:*.7z=38;5;9:*.rz=38;5;9:*.cab=38;5;9:*.jpg=38;5;13:*.jpeg=38;5;13:*.gif=38;5;13:*.bmp=38;5;13:*.pbm=38;5;13:*.pgm=38;5;13:*.ppm=38;5;13:*.tga=38;5;13:*.xbm=38;5;13:*.xpm=38;5;13:*.tif=38;5;13:*.tiff=38;5;13:*.png=38;5;13:*.svg=38;5;13:*.svgz=38;5;13:*.mng=38;5;13:*.pcx=38;5;13:*.mov=38;5;13:*.mpg=38;5;13:*.mpeg=38;5;13:*.m2v=38;5;13:*.mkv=38;5;13:*.webm=38;5;13:*.ogm=38;5;13:*.mp4=38;5;13:*.m4v=38;5;13:*.mp4v=38;5;13:*.vob=38;5;13:*.qt=38;5;13:*.nuv=38;5;13:*.wmv=38;5;13:*.asf=38;5;13:*.rm=38;5;13:*.rmvb=38;5;13:*.flc=38;5;13:*.avi=38;5;13:*.fli=38;5;13:*.flv=38;5;13:*.gl=38;5;13:*.dl=38;5;13:*.xcf=38;5;13:*.xwd=38;5;13:*.yuv=38;5;13:*.cgm=38;5;13:*.emf=38;5;13:*.axv=38;5;13:*.anx=38;5;13:*.ogv=38;5;13:*.ogx=38;5;13:*.aac=38;5;45:*.au=38;5;45:*.flac=38;5;45:*.mid=38;5;45:*.midi=38;5;45:*.mka=38;5;45:*.mp3=38;5;45:*.mpc=38;5;45:*.ogg=38;5;45:*.ra=38;5;45:*.wav=38;5;45:*.axa=38;5;45:*.oga=38;5;45:*.spx=38;5;45:*.xspf=38;5;45:","MAIL":"/var/spool/mail/JAU2","NLSPATH":"/usr/dt/lib/nls/msg/%L/%N.cat","PATH":"/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin","PWD":"/home/JAU2","SHELL":"/bin/bash","SHLVL":"2","TERM":"xterm-256color","USER":"JAU2","XFILESEARCHPATH":"/usr/dt/app-defaults/%L/Dt","_":"/usr/bin/nohup","applicationState":"{clientId=ec30e40a-0c08-47b5-9710-7fcac6926d78, eventExtractionConfigs=[], lastChanged=2016-06-27T19:40:18.994Z, command=java -jar dropwizard-hello-world-application-1.0.jar}","jau.version":"0.8-beta-6-SNAPSHOT","networkinterface_eth0":"172.31.57.144","processIsRunning":"false","processIsRunning timestamp":"Mon Jun 27 20:00:12 UTC 2016"},"timestamp":"2016-06-27T20:00:12.235Z"});
        $httpBackend.whenGET(new RegExp('client/.*/config')).respond({"id":"f9e14326-b9df-46ba-826f-afad3392cf54","name":"whydah-dropwizard-demo-1.0","lastChanged":"2016-06-27T22:05:18.994Z","downloadItems":[{"url":"http://mvnrepo.cantara.no/service/local/repositories/releases/content/no/cantara/dropwizard-hello-world-application/1.0/dropwizard-hello-world-application-1.0.jar","username":null,"password":null,"metadata":{"groupId":"no.cantara","artifactId":"dropwizard-hello-world-application","version":"1.0","packaging":"jar","lastUpdated":null,"buildNumber":null}}],"configurationStores":[{"fileName":"hello-world.yml","properties":{"version":"0.8-beta-5-SNAPSHOT"}}],"eventExtractionConfigs":[{"groupName":"jau","tags":[{"tagName":"jau","regex":".*","filePath":"logs/jau.log"}]}],"startServiceScript":"java -jar dropwizard-hello-world-application-1.0.jar"});
        $httpBackend.whenGET(new RegExp('client/.*/events')).respond({"eventGroups":{"jau":{"files":{"logs/jau.log":{"tags":{"jau":{"events":["2016-07-07T08:47:24.391+0000 [pool-1-thread-9496] TRACE n.c.jau.eventextraction.EventRepo - New events added. Number of events in repo now: 14","2016-07-07T08:47:24.391+0000 [pool-1-thread-9496] TRACE n.c.j.e.CommandExtractEventsFromFile - Line 6597 was the last line read from file logs/jau.log","2016-07-07T08:47:24.402+0000 [pool-2-thread-1] DEBUG n.c.jau.coms.CheckForUpdateHelper - No updated config.","2016-07-07T08:48:03.117+0000 [pool-2-thread-1] DEBUG no.cantara.jau.JavaAutoUpdater - Checking if process is running...","2016-07-07T08:48:03.117+0000 [pool-2-thread-1] DEBUG no.cantara.jau.JavaAutoUpdater - Process is not running - restarting... clientId=fac23d46-5ed9-4275-b677-d9f1ccaebc06, lastChanged=2016-06-27T22:05:18.994Z, command=[java, -jar, dropwizard-hello-world-application-1.0.jar]","2016-07-07T08:48:04.118+0000 [pool-2-thread-1] DEBUG n.c.j.d.LastRunningProcessFileUtil - Wrote pid=2495 to file=last-running-process.txt","2016-07-07T08:48:24.402+0000 [pool-2-thread-1] DEBUG n.c.jau.coms.CheckForUpdateHelper - Checking for updates. Inside lambda.","2016-07-07T08:48:24.403+0000 [pool-2-thread-1] DEBUG n.cantara.jau.util.PropertiesHelper - application-env.properties not found on classpath.","2016-07-07T08:48:24.403+0000 [pool-2-thread-1] DEBUG n.c.j.e.EventExtractorService - Extracting events.","2016-07-07T08:48:24.403+0000 [pool-1-thread-9497] TRACE n.c.j.e.EventExtractor - logs/jau.log is modified since last extraction. Extracting...","2016-07-07T08:48:24.403+0000 [pool-1-thread-9497] TRACE n.c.j.e.CommandExtractEventsFromFile - Start reading from line 6597"]}}}}}}});
        $httpBackend.whenGET(new RegExp('client/.*/status')).respond({"client":{"clientId":"fac23d46-5ed9-4275-b677-d9f1ccaebc06","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","autoUpgrade":true},"latestClientHeartbeatData":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T22:05:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-07-07T11:26:25.689Z"}});


        $httpBackend.whenGET('application/').respond([{"id":"cb7d7b32-b05d-48b6-86e5-74aad18d702b","artifactId":"cantara-demo"},{"id":"0e139a12-57c0-4a48-8999-7f32c63ff9ad","artifactId":"myApplication"}]);
        $httpBackend.whenGET(new RegExp('application/.*/status')).respond({"numberOfRegisteredClients":4,"seenInTheLastHourCount":2,"seenInTheLastHour":["ea15657a-62f4-4db5-a9a6-cc4340d3c708","fac23d46-5ed9-4275-b677-d9f1ccaebc06"],"allClientHeartbeatData":{"ea15657a-62f4-4db5-a9a6-cc4340d3c708":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T22:05:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-07-14T12:12:52.893Z"},"fac23d46-5ed9-4275-b677-d9f1ccaebc06":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T22:05:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-07-14T12:12:57.451Z"},"bed9e97b-2090-4fe0-bfac-ab44252151e6":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T22:05:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-06-28T14:40:09.043Z"},"ec30e40a-0c08-47b5-9710-7fcac6926d78":{"artifactId":"cantara-demo","tags":null,"clientName":"local-jau","configLastChanged":"2016-06-27T19:40:18.994Z","applicationConfigId":"f9e14326-b9df-46ba-826f-afad3392cf54","timeOfContact":"2016-06-27T20:00:12.235Z"}}});
        $httpBackend.whenGET(new RegExp('application/.*/config')).respond({"id":"f9e14326-b9df-46ba-826f-afad3392cf54","name":"whydah-dropwizard-demo-1.0","lastChanged":"2016-06-27T22:05:18.994Z","downloadItems":[{"url":"http://mvnrepo.cantara.no/service/local/repositories/releases/content/no/cantara/dropwizard-hello-world-application/1.0/dropwizard-hello-world-application-1.0.jar","username":null,"password":null,"metadata":{"groupId":"no.cantara","artifactId":"dropwizard-hello-world-application","version":"1.0","packaging":"jar","lastUpdated":null,"buildNumber":null}}],"configurationStores":[{"fileName":"hello-world.yml","properties":{"version":"0.8-beta-5-SNAPSHOT"}}],"eventExtractionConfigs":[{"groupName":"jau","tags":[{"tagName":"jau","regex":".*","filePath":"logs/jau.log"}]}],"startServiceScript":"java -jar dropwizard-hello-world-application-1.0.jar"});
        $httpBackend.whenPUT(new RegExp('application/.*/config/.*')).respond({"id":"f9e14326-b9df-46ba-826f-afad3392cf54","name":"whydah-dropwizard-demo-1.0","lastChanged":"2016-06-27T22:05:18.994Z","downloadItems":[{"url":"http://mvnrepo.cantara.no/service/local/repositories/releases/content/no/cantara/dropwizard-hello-world-application/1.0/dropwizard-hello-world-application-1.0.jar","username":null,"password":null,"metadata":{"groupId":"no.cantara","artifactId":"dropwizard-hello-world-application","version":"1.0","packaging":"jar","lastUpdated":null,"buildNumber":null}}],"configurationStores":[{"fileName":"hello-world.yml","properties":{"version":"0.8-beta-5-SNAPSHOT"}}],"eventExtractionConfigs":[{"groupName":"jau","tags":[{"tagName":"jau","regex":".*","filePath":"logs/jau.log"}]}],"startServiceScript":"java -jar dropwizard-hello-world-application-1.0.jar"});
        $httpBackend.whenPOST('application/').respond({"id": "1caf9c43-0b08-41fc-86bb-ce88b44e2c22", "artifactId": "hello-world-service"});
        $httpBackend.whenPOST('/authenticate').respond({"success":true});
    });