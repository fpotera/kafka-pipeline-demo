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
package io.bluzy.kafkapipelinedemo.commons.events.model;

public enum MessageTypes {

    CANCEL_TAKEOUT_SESSION(CANCEL_TAKEOUT_SESSION.class),
    COMPOSITE_SCOPE_ELEMENTS_CHANGED(COMPOSITE_SCOPE_ELEMENTS_CHANGED.class),
    CONSENT_GROUP_CLIENT_IDS_CHANGED(CONSENT_GROUP_CLIENT_IDS_CHANGED.class),
    DELETE(io.bluzy.kafkapipelinedemo.commons.events.model.DELETE.class),
    DOCUMENT_CONSENT_CHANGED(DOCUMENT_CONSENT_CHANGED.class),
    GRANT_SCOPE_CONSENT(io.bluzy.kafkapipelinedemo.commons.events.model.GRANT_SCOPE_CONSENT.class),
    INSERT(io.bluzy.kafkapipelinedemo.commons.events.model.INSERT.class),
    RESET_PASSWORD(io.bluzy.kafkapipelinedemo.commons.events.model.RESET_PASSWORD.class),
    REVOKE_SCOPE_CONSENT(io.bluzy.kafkapipelinedemo.commons.events.model.REVOKE_SCOPE_CONSENT.class),
    START_TAKEOUT_SESSION(START_TAKEOUT_SESSION.class),
    UPDATE(UPDATE.class),
    UPDATE_MARKETING_PERMISSION(UPDATE_MARKETING_PERMISSION.class),
    VERIFIED_DATA_UPDATED(VERIFIED_DATA_UPDATED.class),
    VERIFIED_DATA_STATUS(VERIFIED_DATA_STATUS.class),
    _UNKNOWN_(Type.class);

    private final Class<? extends Type> clazz;

    MessageTypes(Class<? extends Type> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Type> getType() {
        return clazz;
    }
}
