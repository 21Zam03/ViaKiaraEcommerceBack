package com.zam.security.services.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zam.security.entities.ClientEntity;
import com.zam.security.entities.RoleEntity;
import com.zam.security.entities.ShopCartEntity;
import com.zam.security.entities.UserEntity;
import com.zam.security.exceptions.DuplicateException;
import com.zam.security.exceptions.NotFoundException;
import com.zam.security.payload.request.SignInRequest;
import com.zam.security.payload.request.SignUpRequest;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.SignInResponse;
import com.zam.security.payload.response.SignUpResponse;
import com.zam.security.payload.response.UserLoggedResponse;
import com.zam.security.repositories.ClientRepository;
import com.zam.security.repositories.RoleRepository;
import com.zam.security.repositories.ShopCartRepository;
import com.zam.security.repositories.UserRepository;
import com.zam.security.services.AuthService;
import com.zam.security.utils.JwtUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final ShopCartRepository shopCartRepository;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                           ClientRepository clientRepository, ShopCartRepository shopCartRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.clientRepository = clientRepository;
        this.shopCartRepository = shopCartRepository;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateException
                    ("Email "+ signUpRequest.getEmail() +
                            " is already registered in the system");
        }

        Set<RoleEntity> roleList = new HashSet<>();
        signUpRequest.getRoleList().forEach(roleId -> {
            RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow(() ->
                    new NotFoundException("Role with id "+roleId+" was not fond" ));
            roleList.add(roleEntity);
        });

        UserEntity userToCreate = UserEntity.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roleList(roleList)
                .build();

        UserEntity userCreated = userRepository.save(userToCreate);

        ClientEntity clientToCreate = ClientEntity.builder()
                .firstName(signUpRequest.getFistName())
                .lastName(signUpRequest.getLastName())
                .user(userCreated)
                .build();

        ClientEntity clientCreated = clientRepository.save(clientToCreate);

        ShopCartEntity shopCartToCreate = ShopCartEntity.builder()
                .client(clientCreated)
                .creationDate(LocalDate.now())
                .discount(0)
                .build();
        shopCartRepository.save(shopCartToCreate);

        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated.getRoleList().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName()))));

        userCreated.getRoleList()
                .stream()
                .flatMap(roleEntity -> roleEntity.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        
        String accessToken = jwtUtil.createToken(userCreated.getEmail(), authorityList);
        return SignUpResponse.builder()
                .email(userCreated.getEmail())
                .fistName(clientCreated.getFirstName())
                .lastName(clientCreated.getLastName())
                .message("Client was registered successfully")
                .token(accessToken)
                .status(200)
                .build();
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Authentication authentication = authenticateUser(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        ClientEntity client = clientRepository.findByUser(user);

        String accessToken = jwtUtil.createToken(authentication.getName(), authentication.getAuthorities());
        return SignInResponse.builder()
                .email(email)
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .message("User logged successfully")
                .token(accessToken)
                .status(200)
                .build();
    }

    @Override
    public MessageResponse changePassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserEntity userToChangePassword = userRepository.findByEmail(authentication.getName()).orElseThrow(() ->  {
            return new NotFoundException("User not found");
        });

        if (!passwordEncoder.matches(oldPassword, userToChangePassword.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        userToChangePassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToChangePassword);

        return new MessageResponse("Password was changed successfully");
    }

    @Override
    public UserLoggedResponse validateSession(String token) {
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
        String email = jwtUtil.extractUsername(decodedJWT);
        ClientEntity client = clientRepository.findByUserEmail(email);
        return UserLoggedResponse.builder()
                .email(email)
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .profilePicture(client.getProfilePicture())
                .build();
    }

    public Authentication authenticateUser(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }

}
