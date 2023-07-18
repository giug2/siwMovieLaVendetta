package it.uniroma3.siw.controller.session;

import it.uniroma3.siw.model.Credentials;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionData {

    private User user;
    
    private Credentials credentials;
    
    @Autowired
    private CredentialsService credentialsService;

//    @Autowired
//    private UserRepository userRepository;

    
    
    public Credentials getLoggedCredentials(){
        this.update();
        return this.credentials;
    }

    public User getLoggedUser(){
        try{
            this.update();
        }catch(ClassCastException e){
            return null;
        }
        return this.user;
    }

    private void update(){
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails loggedUserDetails = (UserDetails) object;
        this.credentials = this.credentialsService.getCredentials(loggedUserDetails.getUsername());
        this.user = this.credentials.getUser();
    }
}
