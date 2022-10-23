package es.technest.security.api.integration.hashicorp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CubbyHoleDataResponseDto {
    String request_id;
    Object data;
}
