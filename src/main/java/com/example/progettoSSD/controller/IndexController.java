package com.example.progettoSSD.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
 
@Controller
public class IndexController {
 
    /* ############################## METODI ACCESSIBILI DA CHIUNQUE ################################################################################# */
    @GetMapping("/")
    public String home() {
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
 
        // Ottieni gli authorities (ruoli/autorizzazioni)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
 
        // Fai qualcosa con le informazioni ottenute (es. log, debug)
        System.out.println("Username: " + username);
        System.out.println("Authorities: " + authorities);
 
        return "index"; // Nome del file HTML da restituire (senza estensione)
    }
     
    @GetMapping("/viewHomeworkPage")
    public String showViewHomeworkPage() {
        return "viewHomeworkPage"; // Nome della pagina HTML da visualizzare
    }
     @GetMapping("/viewCompletedHomeworks")
    public String showViewCompletedHomeworks() {
        return "viewCompletedHomeworks"; // Nome della pagina HTML da visualizzare
    }

/* ############################## METODI ACCESSIBILI DA CHIUNQUE ################################################################################# */
 

/* ####################### METODI DI ACCESSO CON RUOLO ######################################################## */
    @GetMapping("/home")
    @PreAuthorize("hasAuthority('ROLE_user')")
    public String mostraSchermataUtente(Model model, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
        String userinfo = authentication.getName();
        model.addAttribute("userinfo", userinfo);
        return "home";
    }

    @GetMapping("/editor")
    @PreAuthorize("hasAuthority('ROLE_user')")
    public String mostraEditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
        return "editor";
    }
 
    @GetMapping("/riservata")
    @PreAuthorize("hasRole('ROLE_admin')")
    public String mostraSchermataHomework() {
        return "riservata";
    }
    @GetMapping("/schermatahomework")
    @PreAuthorize("hasRole('ROLE_admin')")
    public String mostraSchermatahomework() {
        return "schermatahomework";
    }
/* ####################### FINE DEI METODI DI ACCESSO CON RUOLO ######################################################## */


/* ####################### METODI DI ACCESSO CON autenticazione ######################################################## */
    @GetMapping("/AccessDenied")
        public String accessdenied() {
            return "AccessDenied";
        }
    @GetMapping("/logout")
        public String logoutSuccess() {
            return "logout";
        }
/* ####################### FINE DEI METODI DI ACCESSO CON autenticazione ######################################################## */
 
}