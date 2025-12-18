package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.dto.HintResponse;
import com.groom.manvsclass.service.HintService;
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
public class HintController {

    @Autowired
    private HintService hintService;

    @GetMapping()
    public ResponseEntity<List<HintResponse>> getHints(
            @CookieValue(name = "jwt") String jwtToken,
            @RequestParam() Map<String, String> queryParams) {
        List<HintResponse> response = hintService.getHints(queryParams, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> createHints(
            @CookieValue(name = "jwt") String jwtToken,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        String response = hintService.createHintsFromFile(file, images, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{classUTName}")
    public ResponseEntity<String> deleteHintsByClassUtName(
            @CookieValue(name = "jwt") String jwtToken,
            @PathVariable("classUTName") String classUTName) {
        String response = hintService.deleteHintByClassUT(classUTName, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/className/{classUTName}/order/{order}")
    public ResponseEntity<String> deleteHintsByClassUtNameAndOrder(
            @CookieValue(name = "jwt") String jwtToken,
            @PathVariable("classUTName") String classUTName,
            @PathVariable("order") Integer order) {
        String response = hintService.deleteHintByClassUTAndOrder(classUTName, order, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/type/{type}")
    public ResponseEntity<String> deleteHintsByType(
            @CookieValue(name = "jwt") String jwtToken,
            @PathVariable("type") String type) {

        String response = hintService.deleteHintsByType(type, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateHint(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("content") String content,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @CookieValue(name = "jwt") String jwtToken) {

        String response = hintService.updateHint(id, name, content, file, jwtToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<String> moveHint(
            @PathVariable Long id,
            @RequestParam("direction") String direction,
            @CookieValue(name = "jwt") String jwtToken) {
        hintService.moveHint(id, direction, jwtToken);
        return new ResponseEntity<>("Ordine aggiornato.", HttpStatus.OK);
    }

    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadHintTemplate() {
        String jsonTemplate = """
            [
              {
                "type": "GENERIC",
                "name": "Titolo del suggerimento generico",
                "content": "Testo del contenuto...",
                "imageUri": "nome_immagine.png"
              },
              {
                "type": "CLASS",
                "classUTName": "NomeClasse.java",
                "name": "Titolo del suggerimento specifico",
                "content": "Testo del contenuto per la classe...",
                "imageUri": ""
              }
            ]""";

        byte[] content = jsonTemplate.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=hint_template.json");
        headers.setContentLength(content.length);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
