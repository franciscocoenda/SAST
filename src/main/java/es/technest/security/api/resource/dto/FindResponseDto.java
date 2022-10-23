package es.technest.security.api.resource.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

@Value
public class FindResponseDto {

    FindFiltersDto filters;

    Page<ResourceDto> results;
}
