package com.twinkles.asaptrading.config;

import com.twinkles.asaptrading.entity.User;
import com.twinkles.asaptrading.enums.RoleType;
import com.twinkles.asaptrading.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SetupDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        if(userRepository.findUserByEmail("adeyinkawale13@gmail.com").isEmpty()){
            User user = new User("Adewale Adeyinka","adeyinkawale13@gmail.com", passwordEncoder.encode("password1234#"), "09021806462", RoleType.ROLE_ADMIN);
            user.setCreatedDate(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
