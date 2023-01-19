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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static io.bluzy.kafkapipelinedemo.commons.idp.clients.common.model.oauth.Constants.*;

public enum ClaimsSupported {
    ISS(ISS_CLAIM_NAME),
    SUB(SUB_CLAIM_NAME),
    AUD(AUD_CLAIM_NAME),
    IAT(IAT_CLAIM_NAME),
    EXP(EXP_CLAIM_NAME),
    NAME(NAME_CLAIM_NAME),
    EMAIL(EMAIL_CLAIM_NAME),
    GIVEN_NAME(GIVEN_NAME_CLAIM_NAME),
    FAMILY_NAME(FAMILY_NAME_CLAIM_NAME),
    PREFERRED_USERNAME(PREFERRED_USERNAME_CLAIM_NAME),
    ACR(ACR_CLAIM_NAME),
    AUTH_TIME(AUTH_TIME_CLAIM_NAME);

    private String label;

    ClaimsSupported(String label) {
        this.label = label;
    }

    @JsonCreator
    public static ClaimsSupported fromValue(String scope) {
        return switch (scope) {
            case ISS_CLAIM_NAME -> ISS;
            case SUB_CLAIM_NAME -> SUB;
            case AUD_CLAIM_NAME -> AUD;
            case IAT_CLAIM_NAME -> IAT;
            case EXP_CLAIM_NAME -> EXP;
            case NAME_CLAIM_NAME -> NAME;
            case EMAIL_CLAIM_NAME -> EMAIL;
            case GIVEN_NAME_CLAIM_NAME -> GIVEN_NAME;
            case FAMILY_NAME_CLAIM_NAME -> FAMILY_NAME;
            case PREFERRED_USERNAME_CLAIM_NAME -> PREFERRED_USERNAME;
            case ACR_CLAIM_NAME -> ACR;
            case AUTH_TIME_CLAIM_NAME -> AUTH_TIME;
        };

    }

    @JsonValue
    public String toValue() {
        return this.label;
    }
}
