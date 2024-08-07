package org.acme.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.microprofile.jwt.Claims;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenerateToken {

    public String getToken(String[] group, String username){
        String token =
           Jwt.issuer("https://example.com/issuer") 
             .upn(username) 
             .groups(new HashSet<>(Arrays.asList(group))) 
           .sign();

        return token;
    }
    
}
