FROM docker.io/curlimages/curl AS idp-config

USER root

RUN mkdir -p /config
WORKDIR /config
COPY ./config/ ./

ENTRYPOINT bash -c '/config/local-configure-keycloak.sh'
