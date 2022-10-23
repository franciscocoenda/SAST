package es.technest.security.api.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.springframework.util.StringUtils.isEmpty;

@Log4j2
@Service
public class ActuatorUserDetailsService implements UserDetailsService {

    @Value("${management.security-actuators.usersfile}")
    private String actuatorUsersPropertiesFile;

    @Override
    public UserDetails loadUserByUsername(String requestUsername) {

        return loadPropertiesFile(actuatorUsersPropertiesFile)
                .flatMap(properties -> loadUserFromProperties(requestUsername, properties))
                .orElseThrow(() -> new UsernameNotFoundException(requestUsername));
    }

    Optional<UserDetails> loadUserFromProperties(String requestUsername, Properties properties) {

        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        // split and trim
        String[] roles = properties.getProperty("roles").split("\\s*,\\s*");

        if (!isEmpty(username) && !isEmpty(password) && requestUsername.equals(username)) {
            return Optional.of(User.withUsername(username)
                    .password(password)
                    .roles(roles)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<Properties> loadPropertiesFile(String propertiesFile) {
        try {
            Resource resource = new FileSystemResource(propertiesFile);
            return Optional.of(PropertiesLoaderUtils.loadProperties(resource));

        } catch (IOException e) {
            log.error("Unable to read properties from file=" + actuatorUsersPropertiesFile, e);
        }
        return Optional.empty();
    }
}
