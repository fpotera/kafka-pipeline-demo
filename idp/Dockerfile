FROM quay.io/keycloak/keycloak AS idp

HEALTHCHECK --interval=20s --timeout=3s CMD curl -f http://localhost:8080/ || exit 1

FROM idp AS test-idp
