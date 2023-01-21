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

public enum ClaimsSupported {
    ISS(Constants.ISS_CLAIM_NAME),
    SUB(Constants.SUB_CLAIM_NAME),
    AUD(Constants.AUD_CLAIM_NAME),
    IAT(Constants.IAT_CLAIM_NAME),
    EXP(Constants.EXP_CLAIM_NAME),
    NAME(Constants.NAME_CLAIM_NAME),
    EMAIL(Constants.EMAIL_CLAIM_NAME),
    GIVEN_NAME(Constants.GIVEN_NAME_CLAIM_NAME),
    FAMILY_NAME(Constants.FAMILY_NAME_CLAIM_NAME),
    PREFERRED_USERNAME(Constants.PREFERRED_USERNAME_CLAIM_NAME),
    ACR(Constants.ACR_CLAIM_NAME),
    AUTH_TIME(Constants.AUTH_TIME_CLAIM_NAME),
    UNKNOWN(Constants.UNKNOWN_CLAIM_NAME);

    private String label;

    ClaimsSupported(String label) {
        this.label = label;
    }

    @JsonCreator
    public static ClaimsSupported fromValue(String scope) {
        return switch (scope) {
            case Constants.ISS_CLAIM_NAME -> ISS;
            case Constants.SUB_CLAIM_NAME -> SUB;
            case Constants.AUD_CLAIM_NAME -> AUD;
            case Constants.IAT_CLAIM_NAME -> IAT;
            case Constants.EXP_CLAIM_NAME -> EXP;
            case Constants.NAME_CLAIM_NAME -> NAME;
            case Constants.EMAIL_CLAIM_NAME -> EMAIL;
            case Constants.GIVEN_NAME_CLAIM_NAME -> GIVEN_NAME;
            case Constants.FAMILY_NAME_CLAIM_NAME -> FAMILY_NAME;
            case Constants.PREFERRED_USERNAME_CLAIM_NAME -> PREFERRED_USERNAME;
            case Constants.ACR_CLAIM_NAME -> ACR;
            case Constants.AUTH_TIME_CLAIM_NAME -> AUTH_TIME;
            default -> UNKNOWN;
        };

    }

    @JsonValue
    public String toValue() {
        return this.label;
    }
}
