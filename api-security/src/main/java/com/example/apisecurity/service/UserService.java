package com.example.apisecurity.service;

import com.example.apisecurity.data.PasswordRecovery;
import com.example.apisecurity.data.Token;
import com.example.apisecurity.data.User;
import com.example.apisecurity.data.UserRepo;
import com.example.apisecurity.exception.BadCredentialError;
import com.example.apisecurity.exception.EmailNotFoundError;
import com.example.apisecurity.exception.PasswordDoNotMatchError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String ACCESS_SECRET = "K9Eqb27-0Gf0ELLnOYfJARmtZAe3xHjJl5oy1j0t5s0";
    private static final String REFRESH_SECRET = "K9Eqb27-0Gf0ELLnOYfJARmtZAe3xHjJl5oy1j0t5s0";
    private final PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;
    private final MailService mailService;

    public User register(String fistName,String lastName,String password,String confirmPassword,String email){
        if (!Objects.equals(password,confirmPassword)){
            throw new PasswordDoNotMatchError();
        }
        return userRepo.save(
                new User(null,fistName,lastName,email,passwordEncoder.encode(password)));
    }

    public Login login(String email, String password) {
        var user = userRepo.findByEmail(email)
                .orElseThrow((EmailNotFoundError::new));

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialError();
        }
//        return Jwt.of(user.getId(), 10L, ACCESS_SECRET);
        var login = Login.of(user.getId(), ACCESS_SECRET,REFRESH_SECRET);
        var refreshToken = login.getRefreshToken();
        /*System.out.println("====================="+refreshToken.getToken()+" \n"+
                refreshToken.getIssuedAt() + " \n"+
                refreshToken.getExpiredAt());*/
        user.addToken(new Token(
                refreshToken.getToken(),
                refreshToken.getIssuedAt(),
                refreshToken.getExpiredAt()
        ));
        userRepo.save(user);
        return login;
    }
    public User getUserFromToken(String token){
        return userRepo.findById(Jwt.from(token,ACCESS_SECRET).getUserId())
                .orElseThrow(() -> new
                        UsernameNotFoundException("User Name Not found !!"));
    }

    public Login refreshAccess(String refreshToken){
        var refreshJwt = Jwt.from(refreshToken,REFRESH_SECRET);
        var user = userRepo.findUserIdAndTokenByRefreshToken(
                refreshJwt.getUserId(),
                refreshJwt.getToken(),
                refreshJwt.getExpiredAt()
        ).orElseThrow(EntityNotFoundException::new);
        return Login.of(user.getId(),
                ACCESS_SECRET,REFRESH_SECRET);
    }
    @Transactional
    public Boolean logout(String refreshToken){
        var jwt = Jwt.from(refreshToken,REFRESH_SECRET);
        User user = userRepo.findById(jwt.getUserId())
                .orElseThrow(EntityNotFoundException::new);
        Set<Token> tokens = user.removeToken(refreshToken);
        user.setTokens(tokens);
        userRepo.save(user);

      /*  boolean tokenRemove= user.removeTokenIf(token ->
                Objects.equals(refreshToken,token.getRefreshToken()));
        System.out.println("Remove Token======================: "+tokenRemove);
        if (tokenRemove){
            user.setTokens(user.getTokens());
            userRepo.save(user);
        }*/
        return true;
    }
    @Transactional
    public void forgetPassword(String email,String url) {
        var uuid = UUID.randomUUID().toString().replace("-","");
        User user =userRepo.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setToken(uuid);
        user.addPasswordRecovery(passwordRecovery);
        userRepo.save(user);
        mailService.sendMail(email,url,uuid);
    }
    @Transactional
    public void resetPassword(String token,String password,String confirmPassword){
        if (!Objects.equals(password,confirmPassword)){
            throw new PasswordDoNotMatchError();
        }
        User user = userRepo.findUserByPasswordRecoveriesToken(token)
                .orElseThrow(EntityNotFoundException::new);

        user.setPassword(passwordEncoder.encode(password));
        Set<PasswordRecovery> passwordRecoveries = user.removePasswordRecovery(token);
        user.setPasswordRecoveries(passwordRecoveries);
        userRepo.save(user);
//        user.removePasswordRecovery(token);
    }
}











