FROM docker.io/library/alpine AS idp-config1

RUN apk upgrade
RUN apk add --no-cache brotli brotli-dev libssh2 nghttp2-dev curl bash && rm -fr /var/cache/apk/*

COPY config /config

RUN chmod +x /config/local-configure-keycloak.sh

ENTRYPOINT '/config/local-configure-keycloak.sh'

FROM python AS idp-config

WORKDIR /usr/src/app

COPY config/requirements.txt ./
COPY config/configure-keycloak.py ./
COPY config/test-realm.json ./

RUN pip install --no-cache-dir -r requirements.txt

CMD [ "python", "./configure-keycloak.py" ]



