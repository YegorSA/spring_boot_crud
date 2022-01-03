package ru.javamentor.spring_boot_crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javamentor.spring_boot_crud.model.Role;
import ru.javamentor.spring_boot_crud.model.User;
import ru.javamentor.spring_boot_crud.repository.RoleRepository;
import ru.javamentor.spring_boot_crud.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
    }

    @PostConstruct
    public void init() {
        if (roleRepository.findAll().isEmpty()) {
            HashSet<Role> allRoles = Stream.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER")).collect(Collectors.toCollection(HashSet::new));
            roleRepository.saveAll(allRoles);
        }
        if (userRepository.findAll().isEmpty()) {
            System.out.println(roleRepository.findByName("ROLE_USER"));
            roleRepository.findAll().forEach(System.out::println);
            User defaultUser = new User("Иван", "Иванов", "ivan", "ivan", "ivan007@mail.ru", Set.of(roleRepository.findByName("ROLE_USER")), true);
            User defaultAdmin = new User("ADMIN", "ADMIN", "ADMIN", "ADMIN", "admin@mail.ru", Set.copyOf(roleRepository.findAll()), true);
            create(defaultUser);
            create(defaultAdmin);
        }
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void create(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void deleteById(long entityId) {
        userRepository.deleteById(entityId);
    }
}
