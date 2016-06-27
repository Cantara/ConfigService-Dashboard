# ConfigService-Dashboard

A Dashboard and admin GUI for ConfigService

##  Documentation:

* https://wiki.cantara.no/pages/viewpage.action?pageId=44237435



## Run on ConfigService-Dashboard

1. wget -o cs-dashboard.jar http://mvnrepo.cantara.no/content/repositories/snapshots/no/cantara/csdb/ConfigService-Dashboard/0.1-beta-1-SNAPSHOT/ConfigService-Dashboard-0.1-beta-1-20160627.114037-1.jar
2. java -Dconfigservice.url=http://configservice-host:port/jau/ -Dconfigservice.username=xx -Dconfigservice.password=yy -jar cs-dashboard.jar
3. Access http://localhost:8087/dashboard/


## Configuration

* CONFIGSERVICE_PASSWORD = Configuration.getString("configservice.password");
* CONFIGSERVICE_USERNAME = Configuration.getString("configservice.username");
* CONFIGSERVICE_URL = Configuration.getString("configservice.url");

