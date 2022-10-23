package es.technest.security.api.integration.hashicorp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CubbyHoleTokenResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    static public class CubbyHoleTokenResponseAuthDto {
        private String client_token;
        private String accessor;
        private List<String> policies;
        private Boolean renewable;
        private Long lease_duration;
    }

    private CubbyHoleTokenResponseAuthDto auth;
}
