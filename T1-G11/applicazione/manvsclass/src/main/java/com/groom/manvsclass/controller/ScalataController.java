package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.entity.ScalataEntity;
import com.groom.manvsclass.service.ScalataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
public class ScalataController {

    private final ScalataService scalataService;

    public ScalataController(ScalataService scalataService) {
        this.scalataService = scalataService;
    }

    @PostMapping("/configureScalata")
    public ResponseEntity<?> uploadScalata(@RequestBody ScalataEntity scalataEntity, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
        return scalataService.uploadScalata(scalataEntity, jwt);
    }

    @GetMapping("/scalate_list")
    @ResponseBody
    public ResponseEntity<?> listScalate() {
        return scalataService.listScalate();
    }

    @DeleteMapping("delete_scalata/{scalataName}")
    @ResponseBody
    public ResponseEntity<?> deleteScalataByName(@PathVariable String scalataName, @CookieValue(name = "jwt", required = false) String jwt) {
        return scalataService.deleteScalataByName(scalataName, jwt);
    }

    @GetMapping("/retrieve_scalata/{scalataName}")
    @ResponseBody
    public ResponseEntity<?> retrieveScalataByName(@PathVariable String scalataName) {
        return scalataService.retrieveScalataByName(scalataName);
    }
}
