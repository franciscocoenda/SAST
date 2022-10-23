package es.technest.security.api.resource.dto;

import lombok.Value;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;


@Value
public class FindFiltersDto {

    @NotNull
    ZonedDateTime queryDate;

    Integer page;

    Integer size;

    Map<String, Sort.Direction> sort;

    Set<String> references;

}