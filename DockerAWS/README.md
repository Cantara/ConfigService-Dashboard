Docker image for running ConfigService-Dashboard on Amazon ECS.
=======================================================================
If you want to package the properties with the .jar file, add the property file in the appropriate folder.
This image can also be deployed without packaging the properties with the .jar file.
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
