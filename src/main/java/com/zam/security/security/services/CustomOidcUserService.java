package com.zam.security.security.services;

import com.zam.security.entities.ClientEntity;
import com.zam.security.entities.ShopCartEntity;
import com.zam.security.entities.UserEntity;
import com.zam.security.repositories.ClientRepository;
import com.zam.security.repositories.RoleRepository;
import com.zam.security.repositories.ShopCartRepository;
import com.zam.security.repositories.UserRepository;
import com.zam.security.security.customs.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ShopCartRepository shopCartRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Carga el usuario usando el OidcUserService base
        OidcUser oidcUser = super.loadUser(userRequest);

        // Accede a los datos del usuario desde el ID token
        Map<String, Object> userClaims = oidcUser.getIdToken().getClaims();

        // Extrae los datos del usuario, por ejemplo, el correo electrónico
        String email = (String) userClaims.get("email");
        String firstName = (String) userClaims.get("given_name");
        String lastName = (String) userClaims.get("family_name");
        String profilePicture = (String) userClaims.get("picture");

        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .email(email)
                            .password(null)
                            .isEnabled(true)
                            .accountNoExpired(true)
                            .accountNoLocked(true)
                            .credentialNoExpired(true)
                            .roleList(Set.of(roleRepository.findById(1).orElseThrow(() -> {
                                return new EntityNotFoundException("Role not found");
                            })))
                            .build();
                    UserEntity userCreated = userRepository.save(newUser);
                    ClientEntity clientToCreate = ClientEntity.builder()
                            .user(userCreated)
                            .profilePicture(profilePicture)
                            .firstName(firstName)
                            .lastName(lastName)
                            .build();
                    ClientEntity clientCreated = clientRepository.save(clientToCreate);
                    ShopCartEntity shopCartToCreate = ShopCartEntity.builder()
                            .client(clientCreated)
                            .creationDate(LocalDate.now())
                            .discount(0)
                            .build();
                    shopCartRepository.save(shopCartToCreate);
                    return userCreated;
                });
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return new DefaultOidcUser(customUserDetails.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

}
