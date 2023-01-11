FROM docker.io/library/alpine AS idp-config

RUN apk upgrade
RUN apk add --no-cache brotli brotli-dev libssh2 nghttp2-dev curl bash && rm -fr /var/cache/apk/*

COPY config /config

RUN chmod +x /config/local-configure-keycloak.sh

ENTRYPOINT '/config/local-configure-keycloak.sh'
