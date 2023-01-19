/**  Copyright 2023 Florin Potera
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.bluzy.kafkapipelinedemo.commons.idp.clients.common.model.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenIDConfiguration {
    private String issuer;

    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty("token_endpoint")
    private String tokenEndpoint;

    @JsonProperty("introspection_endpoint")
    private String introspectionEndpoint;

    @JsonProperty("end_session_endpoint")
    private String endSessionEndpoint;

    @JsonProperty("userinfo_endpoint")
    private String userinfoEndpoint;

    @JsonProperty("frontchannel_logout_session_supported")
    private boolean frontchannelLogoutSessionSupported;

    @JsonProperty("frontchannel_logout_supported")
    private boolean frontchannelLogoutSupported;

    @JsonProperty("jwks_uri")
    private String jwksUri;

    @JsonProperty("check_session_iframe")
    private String checkSessionIframe;

    @JsonProperty("grant_types_supported")
    private String[] grantTypesSupported;

    @JsonProperty("response_types_supported")
    private String[] responseTypesSupported;

    @JsonProperty("subject_types_supported")
    private String[] subjectTypesSupported;

    @JsonProperty("id_token_signing_alg_values_supported")
    private String[] idTokenSigningAlgValuesSupported;

    @JsonProperty("id_token_encryption_alg_values_supported")
    private String[] idTokenEncryptionAlgValuesSupported;

    @JsonProperty("id_token_encryption_enc_values_supported")
    private String[] idTokenEncryptionEncValuesSupported;

    @JsonProperty("userinfo_signing_alg_values_supported")
    private String[] userinfoSigningAlgValuesSupported;

    @JsonProperty("request_object_signing_alg_values_supported")
    private String[] requestObjectSigningAlgValuesSupported;

    @JsonProperty("request_object_encryption_alg_values_supported")
    private String[] requestObjectEncryptionAlgValuesSupported;

    @JsonProperty("request_object_encryption_enc_values_supported")
    private String[] requestObjectEncryptionEncValuesSupported;

    @JsonProperty("response_modes_supported")
    private String[] responseModesSupported;

    @JsonProperty("registration_endpoint")
    private String registrationEndpoint;

    @JsonProperty("token_endpoint_auth_methods_supported")
    private String[] tokenEndpointAuthMethodsSupported;

    @JsonProperty("token_endpoint_auth_signing_alg_values_supported")
    private String[] tokenEndpointAuthSigningAlgValuesSupported;

    @JsonProperty("introspection_endpoint_auth_methods_supported")
    private String[] introspectionEndpointAuthMethodsSupported;

    @JsonProperty("introspection_endpoint_auth_signing_alg_values_supported")
    private String[] introspectionEndpointAuthSigningAlgValuesSupported;

    @JsonProperty("authorization_signing_alg_values_supported")
    private String[] authorizationSigningAlgValuesSupported;

    @JsonProperty("authorization_encryption_alg_values_supported")
    private String[] authorizationEncryptionAlgValuesSupported;

    @JsonProperty("authorization_encryption_enc_values_supported")
    private String[] authorizationEncryptionEncValuesSupported;

    @JsonProperty("claims_supported")
    private String[] claimsSupported;

    @JsonProperty("claim_types_supported")
    private String[] claimTypesSupported;

    @JsonProperty("claims_parameter_supported")
    private boolean claimsParameterSupported;

    @JsonProperty("scopes_supported")
    private String[] scopesSupported;

    @JsonProperty("request_parameter_supported")
    private boolean requestParameterSupported;

    @JsonProperty("request_uri_parameter_supported")
    private boolean requestUriParameterSupported;

    @JsonProperty("require_request_uri_registration")
    private boolean requireRequestUriRegistration;

    @JsonProperty("code_challenge_methods_supported")
    private String[] codeChallengeMethodsSupported;

    @JsonProperty("tls_client_certificate_bound_access_tokens")
    private boolean tlsClientCertificateBoundAccessTokens;

    @JsonProperty("revocation_endpoint")
    private String revocationEndpoint;

    @JsonProperty("revocation_endpoint_auth_methods_supported")
    private String[] revocationEndpointAuthMethodsSupported;

    @JsonProperty("revocation_endpoint_auth_signing_alg_values_supported")
    private String[] revocationEndpointAuthSigningAlgValuesSupported;

    @JsonProperty("backchannel_logout_supported")
    private boolean backchannelLogoutSupported;

    @JsonProperty("backchannel_logout_session_supported")
    private boolean backchannelLogoutSessionSupported;

    @JsonProperty("device_authorization_endpoint")
    private String deviceSuthorizationEndpoint;

    @JsonProperty("backchannel_token_delivery_modes_supported")
    private String[] backchannelTokenDeliveryModesSupported;

    @JsonProperty("backchannel_authentication_endpoint")
    private String backchannelAuthenticationEndpoint;

    @JsonProperty("backchannel_authentication_request_signing_alg_values_supported")
    private String[] backchannelAuthenticationRequestSigningAlgValuesSupported;

    @JsonProperty("require_pushed_authorization_requests")
    private boolean requirePushedAuthorizationRequests;

    @JsonProperty("pushed_authorization_request_endpoint")
    private String pushedAuthorizationRequestEndpoint;

    @JsonProperty("mtls_endpoint_aliases")
    private MtlsEndpointAliases mtlsEndpointAliases;

    @Data
    public class MtlsEndpointAliases {

        @JsonProperty("token_endpoint")
        private String tokenEndpoint;

        @JsonProperty("revocation_endpoint")
        private String revocationEndpoint;

        @JsonProperty("introspection_endpoint")
        private String introspectionEndpoint;

        @JsonProperty("device_authorization_endpoint")
        private String deviceSuthorizationEndpoint;

        @JsonProperty("registration_endpoint")
        private String registrationEndpoint;

        @JsonProperty("userinfo_endpoint")
        private String userinfoEndpoint;

        @JsonProperty("pushed_authorization_request_endpoint")
        private String pushedAuthorizationRequestEndpoint;

        @JsonProperty("backchannel_authentication_endpoint")
        private String backchannelAuthenticationEndpoint;
    }
}
