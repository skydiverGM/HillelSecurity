package imaks.hillelsecurity.service;

import imaks.hillelsecurity.dto.AuthDTO;
import imaks.hillelsecurity.entity.Role;
import imaks.hillelsecurity.entity.User;
import imaks.hillelsecurity.exception.UserNotFoundException;
import imaks.hillelsecurity.repo.RoleRepo;
import imaks.hillelsecurity.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public User create(User user) {
        String hashPass = bCryptEncoder.encode(user.getPassword());
        user.setPassword(hashPass);

        Role role = roleRepo.findByName("USER");
        user.setRoles(Set.of(role));

        return userRepo.save(user);
    }

    public String authenticate(AuthDTO dto) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails.getUsername());
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User get(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found", HttpStatus.NOT_FOUND));
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }
}
