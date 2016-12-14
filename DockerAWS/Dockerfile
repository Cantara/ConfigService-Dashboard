FROM cantara/alpine-zulu-jdk8

RUN apk add --update \
    groff \
    python \
    python-dev \
    py-pip \
    bash \
  && pip install awscli-cwlogs \
  && rm -rf /var/cache/apk/*

ENV USER=appuser \
    HOME=/home/appuser
ARG DOCKER_TAG=ConfigService-Dashboard-0.4.2
RUN adduser -S $USER

#http://mvnrepo.cantara.no/service/local/artifact/maven/redirect?r=snapshots&g=no.cantara.csdb&a=ConfigService-Dashboard&v=LATEST
## Copy configuration overrides and run script
## Copy application itself
## PREREQUISITE:
##    Docker cannot access contents outside, so this must be run when building:
##    $ cp -p "../target/$(ls -t ../target/*.jar | grep -v /orig | head -1)" app.jar
COPY config_override/* $HOME/config_override/
COPY toRoot/* $HOME/
RUN if [ $DOCKER_TAG = "latest" ]; then \
      curl -L -o $HOME/app.jar "http://mvnrepo.cantara.no/service/local/artifact/maven/redirect?r=snapshots&g=no.cantara.csdb&a=ConfigService-Dashboard&v=LATEST"; \
    else \
      curl -L -o $HOME/app.jar "http://mvnrepo.cantara.no/content/repositories/releases/no/cantara/csdb/ConfigService-Dashboard/`echo $DOCKER_TAG | cut -d \- -f3`/$DOCKER_TAG.jar"; \
    fi


#curl -SL -o $HOME/app.jar http://mvnrepo.cantara.no/content/repositories/releases/no/cantara/csdb/ConfigService-Dashboard/`echo $TAG | cut -d \- -f3`/$TAG.jar
#ADD http://mvnrepo.cantara.no/content/repositories/releases/no/cantara/csdb/ConfigService-Dashboard/`echo $TAG | cut -d \- -f3`/$TAG.jar $HOME/app.jar
#COPY toRoot/* app.jar $HOME/

## Create directory for holding application logs and configure permissions
## Configure permissions
RUN mkdir -p $HOME/logs && \
    chmod 755 $HOME/*.sh && \
    chown -R $USER $HOME

## Map data volume container?
VOLUME ["$HOME/config_override", "$HOME/logs"]

## Expose application port
EXPOSE 8087

USER $USER
WORKDIR /home/$USER
CMD ["./runapp.sh"]
