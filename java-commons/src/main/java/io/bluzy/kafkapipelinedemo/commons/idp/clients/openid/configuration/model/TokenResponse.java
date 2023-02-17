package io.bluzy.kafkapipelinedemo.commons.idp.clients.openid.configuration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresInSeconds;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty
    private String scope;
}
