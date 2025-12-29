package com.groom.manvsclass.service;

import com.groom.manvsclass.model.dto.HintResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface HintService {

    List<HintResponse> getHints(Map<String, String> queryParams, String jwtToken);

    String createHintsFromFile(MultipartFile file, List<MultipartFile> imageFiles, String jwtToken);

    String updateHint(Long id, String name, String content, MultipartFile imageFile, String jwtToken);

    String deleteHintByClassUT(String classUT, String jwtToken);

    String deleteHintByClassUTAndOrder(String classUT, Integer order, String jwtToken);

    String deleteHintsByType(String type, String jwtToken);

    void moveHint(Long id, String direction, String jwtToken);
}
