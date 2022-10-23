package es.technest.security.api.integration.hashicorp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HaStatusResponseDto {

    @JsonProperty("ha_enabled")
    private boolean haEnabled;

    @JsonProperty("is_self")
    private boolean isSelf;

    @JsonProperty("leader_address")
    private String leaderAddress;

    @JsonProperty("leader_cluster_address")
    private String leaderClusterAddress;

    @JsonProperty("performance_standby")
    private boolean performanceStandby;

    @JsonProperty("performance_standby_last_remote_wal")
    private Integer performanceStandbyLastRemoteWal;
}
