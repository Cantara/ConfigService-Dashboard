Docker image for running ConfigService-Dashboard on Amazon ECS.
=======================================================================
This image will by default pack the latest ConfigService-Dashboard-jar (SNAPSHOT).
--build-arg DOCKER_TAG=ConfigService-Dashboard-0.5.1 may be used to pack a specific release-version. The format matches the git tag format.
The configuration can be overridden by passing a file with the `--env-file` command when running the image.
E.g:
```
--env-file application_override.properties
```
Alternatively, the properties can be overridden by passing them seperately
E.g:
```
-e configservice.username=admin -e configservice.password=configservice
```


## DockerAlpine environment variables

####Credentials
Task-roles should be used, but you may also pass in the Access key id and access key through env-variables.
For application/logging to CW
* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY

####Logging
Where to put logs. See aws-cloudwatch.conf and config_override/logback.xml
* AWS_CLOUDWATCH_LOGGING_ENABLED default to false. Set to "true" if you wish to enable
* AWS_CLOUDWATCH_REGION (region the logs should be sent to, e.g. eu-west-1):
* AWS_LOG_GROUP (Log group to create log stream for app-log)
* AWS_INOUT_LOG_GROUP (Log group to create inout-log stream)
* LOGBACK_CANTARA_LEVEL (Loglevel of no.cantara logs. Defaults to info if not set)

####Configservice
If nothing is set, the application tries with whatever is put in src/main/resources/configservice.properties.
If it fails default properties are used. If configservice.allow.fallback.to.local.config is set to false the application fails during startup if any CS problems occurs while fetching configuration.
* configservice.url
* configservice.username
* configservice.password
* configservice.artifactid
* configservice.clientid
* configservice.configuration.store.directory
* configservice.allow.fallback.to.local.config
