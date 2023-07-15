package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private SessionData sessionData;

    @ModelAttribute("userDetails")
    public User getUser() {

        try{
            return this.sessionData.getLoggedUser();
        }
        catch (ClassCastException e){
            return null;
        }
    }

    @ModelAttribute("credentials")
    public Credentials getCredentials(){
        try{
            return this.sessionData.getLoggedCredentials();
        }catch(ClassCastException e){
            return null;
        }
    }
}