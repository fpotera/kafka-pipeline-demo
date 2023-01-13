FROM python:3-slim AS idp-config

WORKDIR /usr/src/app

COPY config/requirements.txt ./
COPY config/configure-keycloak.py ./
COPY config/test-client.json ./

RUN pip install --no-cache-dir -r requirements.txt

CMD [ "python", "./configure-keycloak.py" ]
