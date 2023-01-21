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
package io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum GrantTypesSupported {

    REFRESH_TOKEN(Constants.REFRESH_TOKEN_GRANT_TYPE_NAME),
    AUTHORIZATION_CODE(Constants.AUTHORIZATION_CODE_GRANT_TYPE_NAME),
    CLIENT_CREDENTIALS(Constants.CLIENT_CREDENTIALS_GRANT_TYPE_NAME),
    DEVICE_CODE(Constants.DEVICE_CODE_GRANT_TYPE_NAME),
    CIBA(Constants.CIBA_GRANT_TYPE_NAME),
    PASSWORD(Constants.PASSWORD_GRANT_TYPE_NAME),
    IMPLICIT(Constants.IMPLICIT_GRANT_TYPE_NAME),
    UNKNOWN(Constants.UNKNOWN_GRANT_TYPE_NAME);

    private String label;

    GrantTypesSupported(String label) {
        this.label = label;
    }

    @JsonCreator
    public static GrantTypesSupported fromValue(String scope) {
        return switch (scope) {
            case Constants.REFRESH_TOKEN_GRANT_TYPE_NAME -> REFRESH_TOKEN;
            case Constants.AUTHORIZATION_CODE_GRANT_TYPE_NAME -> AUTHORIZATION_CODE;
            case Constants.CLIENT_CREDENTIALS_GRANT_TYPE_NAME -> CLIENT_CREDENTIALS;
            case Constants.DEVICE_CODE_GRANT_TYPE_NAME -> DEVICE_CODE;
            case Constants.CIBA_GRANT_TYPE_NAME -> CIBA;
            case Constants.PASSWORD_GRANT_TYPE_NAME -> PASSWORD;
            case Constants.IMPLICIT_GRANT_TYPE_NAME -> IMPLICIT;
            default -> UNKNOWN;
        };

    }

    @JsonValue
    public String toValue() {
        return this.label;
    }
}
