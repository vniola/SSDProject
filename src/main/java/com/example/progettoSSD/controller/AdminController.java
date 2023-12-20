package com.example.progettoSSD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.progettoSSD.entity.CompletedHomeworks;
import com.example.progettoSSD.entity.Homework;
import com.example.progettoSSD.service.HomeworkService;
import com.example.progettoSSD.repository.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private HomeworkService HomeworkService;
    @Autowired
    private HomeworkRepository HomeworkRepository;
    
    @GetMapping("/viewHomework")
    public ResponseEntity<List<Homework>> viewHomework() {
        List<Homework> homeworkList = HomeworkService.getAllHomework();
        return new ResponseEntity<>(homeworkList, HttpStatus.OK);
    }
    

    @GetMapping("/viewCompletedHomeworks")
    public ResponseEntity<List<CompletedHomeworks>> viewCompletedHomeworks() {
        List<CompletedHomeworks> completedHomeworksList = HomeworkService.getCompletedHomeworks();
        return new ResponseEntity<>(completedHomeworksList, HttpStatus.OK);
    }
    
    @PostMapping("/sendHomework")
    @PreAuthorize("hasAuthority('ROLE_user')")
    public ResponseEntity<String> sendHomework(@RequestBody CompletedHomeworks completedHomeworks) {
        try {
            // Esegui il salvataggio nel database MongoDB
            HomeworkService.saveComplHomeworks(completedHomeworks);
            return new ResponseEntity<>("Homework completato salvato con successo", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il salvataggio dell'homework completato", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getUserId")
    public ResponseEntity<Map<String, String>> getUserId(@AuthenticationPrincipal Authentication authentication) {       
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/addHomework")
    public ResponseEntity<Homework> addHomework(
            @RequestParam("nome") String nome,
            @RequestParam("deadline") String deadline,
            @RequestParam("id") String id,
            @RequestParam("traccia") String traccia) throws IOException {

        // Creazione dell'oggetto Homework con i dati forniti
        Homework newHomework = new Homework();
        newHomework.setNome(nome);
        newHomework.setDeadline(deadline);
        newHomework.setId(id);
        newHomework.setTraccia(traccia);

        // Puoi aggiungere il salvataggio del file in GridFS se necessario

        // Salva l'homework nel database
        HomeworkService.saveHomework(newHomework);

        return new ResponseEntity<>(newHomework, HttpStatus.CREATED);
    }

        @DeleteMapping("/deleteHomework/{nome}")
    public ResponseEntity<String> deleteHomework(@PathVariable("nome") String nome) {
        HomeworkService.deleteHomeworkByName(nome);
        return new ResponseEntity<>("Homework eliminato con successo", HttpStatus.OK);
    }

    @GetMapping("/getHomeworkTraccia/{nome}")
    public ResponseEntity<String> getHomeworkContent(@PathVariable("nome") String nome) {
        try {
            Homework homework = HomeworkRepository.findByNome(nome);

            if (homework == null) {
                return new ResponseEntity<>("Homework non trovato", HttpStatus.NOT_FOUND);
            }

            String content = homework.getTraccia();

            if (content == null) {
                return new ResponseEntity<>("Contenuto non disponibile", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del contenuto dell'homework", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   
}