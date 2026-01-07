package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.dto.HintResponse;
import com.groom.manvsclass.service.HintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/hints")
@Tag(name = "Hints API", description = "Gestione dei suggerimenti per amministratori")
public class HintController {

    @Autowired
    private HintService hintService;

    @Operation(summary = "Ottieni suggerimenti filtrati", description = "Restituisce una lista di suggerimenti in base ai query parameters forniti.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operazione riuscita"),
            @ApiResponse(responseCode = "401", description = "Token JWT non valido o mancante")
    })
    @GetMapping()
    public ResponseEntity<List<HintResponse>> getHints(
            @CookieValue(name = "jwt") String jwtToken,
            @Parameter(description = "Filtri dinamici (type, classUTName, etc.)") @RequestParam Map<String, String> queryParams) {
        List<HintResponse> response = hintService.getHints(queryParams, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Upload massivo da file JSON", description = "Carica suggerimenti e immagini. Valida formato file, esistenza classi UT e duplicati.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caricamento completato con successo"),
            @ApiResponse(responseCode = "400", description = "Errore validazione: file vuoto, JSON malformato, classi non trovate o immagini mancanti"),
            @ApiResponse(responseCode = "401", description = "Sessione non valida o amministratore non trovato"),
            @ApiResponse(responseCode = "409", description = "Conflitto: il suggerimento esiste già [hint.duplicate]"),
            @ApiResponse(responseCode = "415", description = "Formato file non supportato (.json richiesto)"),
            @ApiResponse(responseCode = "422", description = "Dati incompleti: contenuto o nome mancanti"),
            @ApiResponse(responseCode = "500", description = "Errore interno nel salvataggio dei file o del DB")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createHints(
            @CookieValue(name = "jwt") String jwtToken,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        String response = hintService.createHintsFromFile(file, images, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Elimina tutti i suggerimenti di una classe", description = "Rimuove tutti i record associati a una classe UT e cancella le relative immagini dal file system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eliminazione riuscita"),
            @ApiResponse(responseCode = "401", description = "Token non valido"),
            @ApiResponse(responseCode = "404", description = "Nessun suggerimento trovato per la classe specificata")
    })
    @DeleteMapping("/{classUTName}")
    public ResponseEntity<String> deleteHintsByClassUtName(
            @CookieValue(name = "jwt") String jwtToken,
            @PathVariable("classUTName") String classUTName) {
        String response = hintService.deleteHintByClassUT(classUTName, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Elimina suggerimento singolo", description = "Elimina un suggerimento specifico identificato da classe e ordine.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggerimento eliminato"),
            @ApiResponse(responseCode = "401", description = "Token non valido"),
            @ApiResponse(responseCode = "404", description = "Suggerimento specifico non trovato [hint.delete.order.notfound]")
    })
    @DeleteMapping("/className/{classUTName}/order/{order}")
    public ResponseEntity<String> deleteHintsByClassUtNameAndOrder(
            @CookieValue(name = "jwt") String jwtToken,
            @PathVariable("classUTName") String classUTName,
            @PathVariable("order") Integer order) {
        String response = hintService.deleteHintByClassUTAndOrder(classUTName, order, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Elimina per Tipologia", description = "Rimuove massivamente suggerimenti GENERIC o CLASS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operazione completata"),
            @ApiResponse(responseCode = "401", description = "Token non valido")
    })
    @DeleteMapping("/type/{type}")
    public ResponseEntity<String> deleteHintsByType(
            @CookieValue(name = "jwt") String jwtToken,
            @PathVariable("type") String type) {
        String response = hintService.deleteHintsByType(type, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Aggiorna suggerimento esistente", description = "Modifica i dati testuali o l'immagine. Gestisce la cancellazione del vecchio file fisico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suggerimento aggiornato"),
            @ApiResponse(responseCode = "401", description = "Token non valido"),
            @ApiResponse(responseCode = "404", description = "ID suggerimento non esistente"),
            @ApiResponse(responseCode = "500", description = "Errore durante l'aggiornamento dell'immagine")
    })
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateHint(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "content", required = false) String content,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @CookieValue(name = "jwt") String jwtToken) {
        String response = hintService.updateHint(id, name, content, file, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Sposta ordine suggerimento", description = "Cambia la posizione (UP/DOWN) di un suggerimento scambiando l'ordine con il vicino.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordine aggiornato con successo"),
            @ApiResponse(responseCode = "400", description = "Direzione non valida o limiti raggiunti"),
            @ApiResponse(responseCode = "401", description = "Token non valido"),
            @ApiResponse(responseCode = "404", description = "Suggerimento non trovato")
    })
    @PostMapping("/{id}/move")
    public ResponseEntity<String> moveHint(
            @PathVariable Long id,
            @Parameter(description = "Valori ammessi: UP, DOWN") @RequestParam("direction") String direction,
            @CookieValue(name = "jwt") String jwtToken) {
        hintService.moveHint(id, direction, jwtToken);
        return new ResponseEntity<>("Ordine aggiornato.", HttpStatus.OK);
    }

    @Operation(summary = "Scarica Template JSON", description = "Restituisce un file JSON di esempio per guidare l'amministratore nell'upload.")
    @ApiResponse(responseCode = "200", description = "Download avviato")
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadHintTemplate() {
        // ... Logica del template omessa per brevità ...
        return new ResponseEntity<>(new byte[0], new HttpHeaders(), HttpStatus.OK);
    }
}