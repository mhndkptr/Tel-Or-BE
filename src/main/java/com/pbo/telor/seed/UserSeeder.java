package com.pbo.telor.seed;

import com.pbo.telor.model.UserEntity;
import com.pbo.telor.model.UserEntity.Role;
import com.pbo.telor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@dev.com").isEmpty()) {
            UserEntity admin = UserEntity.builder()
                    .fullname("Administrator")
                    .email("admin@dev.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("===> Admin user created!");
        } else {
            System.out.println("===> Admin user already exists.");
        }
    }
}
