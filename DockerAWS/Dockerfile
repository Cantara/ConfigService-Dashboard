FROM cantara/alpine-zulu-jdk8

RUN apk add --update \
    groff \
    python \
    python-dev \
    py-pip \
    bash \
  && pip install awscli-cwlogs \
  && rm -rf /var/cache/apk/*

ENV USER=appuser
ENV HOME=/home/$USER
RUN adduser -S $USER

## Copy configuration overrides and run script
## Copy application itself
## PREREQUISITE:
##    Docker cannot access contents outside, so this must be run when building:
##    $ cp -p "../target/$(ls -t ../target/*.jar | grep -v /orig | head -1)" app.jar
COPY config_override/* $HOME/config_override/
COPY toRoot/* app.jar $HOME/

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
