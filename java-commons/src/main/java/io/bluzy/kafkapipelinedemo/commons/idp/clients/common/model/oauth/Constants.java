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

public interface Constants {
    public String OPENID_SCOPE_NAME = "openid";
    public String PHONE_SCOPE_NAME = "phone";
    public String ROLES_SCOPE_NAME = "roles";
    public String MICROPROFILE_JWT_SCOPE_NAME = "microprofile-jwt";
    public String EMAIL_SCOPE_NAME = "email";
    public String WEB_ORIGINS_SCOPE_NAME = "web-origins";
    public String OFFLINE_ACCESS_SCOPE_NAME = "offline_access";
    public String ADDRESS_SCOPE_NAME = "address";
    public String PROFILE_SCOPE_NAME = "profile";

    public String ISS_CLAIM_NAME = "iss";
    public String SUB_CLAIM_NAME = "sub";
    public String AUD_CLAIM_NAME = "aud";
    public String IAT_CLAIM_NAME = "iat";
    public String EXP_CLAIM_NAME = "exp";
    public String AUTH_TIME_CLAIM_NAME = "auth_time";
    public String NAME_CLAIM_NAME = "name";
    public String EMAIL_CLAIM_NAME = "email";
    public String GIVEN_NAME_CLAIM_NAME = "given_name";
    public String FAMILY_NAME_CLAIM_NAME = "family_name";
    public String PREFERRED_USERNAME_CLAIM_NAME = "preferred_username";
    public String ACR_CLAIM_NAME = "acr";

    public String AUTHORIZATION_CODE_GRANT_TYPE_NAME = "authorization_code";
    public String IMPLICIT_GRANT_TYPE_NAME = "implicit";
    public String REFRESH_TOKEN_GRANT_TYPE_NAME = "refresh_token";
    public String PASSWORD_GRANT_TYPE_NAME = "password";
    public String CLIENT_CREDENTIALS_GRANT_TYPE_NAME = "client_credentials";
    public String DEVICE_CODE_GRANT_TYPE_NAME = "urn:ietf:params:oauth:grant-type:device_code";
    public String CIBA_GRANT_TYPE_NAME = "urn:openid:params:grant-type:ciba";
}
