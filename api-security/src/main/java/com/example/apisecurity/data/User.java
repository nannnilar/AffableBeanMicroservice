package com.example.apisecurity.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String firstName;
    private String lastName;
    private String  email;
    private String password;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user",orphanRemoval = true)
    private Set<Token> tokens = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user",orphanRemoval = true)
    private Set<PasswordRecovery> passwordRecoveries = new HashSet<>();

    public void addToken(Token token){
        token.setUser(this);
        tokens.add(token);
    }

    public User(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public Boolean removeTokenIf(Predicate<? super Token> predicate){
        return this.tokens.removeIf(predicate);
    }
    public Set<Token> removeToken(String refreshToken){
        Iterator<Token> itr = tokens.iterator();
        while (itr.hasNext()){
            Token token = itr.next();
            if (token.getRefreshToken().equals(refreshToken)){
                token.setUser(null);
                itr.remove();
            }
        }
        return tokens;
    }
    public void addPasswordRecovery(PasswordRecovery passwordRecovery){
        passwordRecovery.setUser(this);
        this.passwordRecoveries.add(passwordRecovery);
    }

    public Set<PasswordRecovery> removePasswordRecovery(String token){
        Iterator<PasswordRecovery> itr = passwordRecoveries.iterator();
        while (itr.hasNext()){
            PasswordRecovery passwordRecovery = itr.next();
            if (passwordRecovery.getToken().equals(token)){
                passwordRecovery.setUser(null);
                itr.remove();
            }
        }
        return passwordRecoveries;
    }
}
