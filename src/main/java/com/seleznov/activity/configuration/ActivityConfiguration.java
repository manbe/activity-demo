package com.seleznov.activity.configuration;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivityConfiguration {

        @Bean
        public InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

            return new InitializingBean() {
                public void afterPropertiesSet() throws Exception {

                    Group group = identityService.newGroup("user");
                    group.setName("users");
                    group.setType("security-role");
                    identityService.saveGroup(group);

                    User admin = identityService.newUser("admin");
                    admin.setPassword("admin");
                    identityService.saveUser(admin);

                }
            };
        }
}
