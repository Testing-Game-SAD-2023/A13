package com.groom.manvsclass.service.implementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.mapper.HintMapper;
import com.groom.manvsclass.model.entity.AdminEntity;
import com.groom.manvsclass.model.entity.HintEntity;
import com.groom.manvsclass.model.dto.Hint;
import com.groom.manvsclass.model.dto.HintResponse;
import com.groom.manvsclass.model.enums.HintTypeEnum;
import com.groom.manvsclass.model.repository.AdminRepository;
import com.groom.manvsclass.model.repository.HintRepository;
import com.groom.manvsclass.model.repository.ClassUTRepository;
import com.groom.manvsclass.service.HintService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.util.HintSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HintServiceImpl implements HintService {

    @Autowired
    private HintRepository hintRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClassUTRepository classUTRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private HintMapper hintMapper;

    @Autowired
    private MessageSource messageSource; // <--- INIEZIONE MESSAGE SOURCE

    @Value("${t1.upload-dir}")
    private String uploadDir;

    @Autowired
    private Validator validator;

    // Helper per recuperare i messaggi in base alla lingua corrente
    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    public List<HintResponse> getHints(Map<String, String> queryParams, String jwtToken) {
        jwtVerify(jwtToken);

        Specification<HintEntity> spec = HintSpecifications.withDynamicQuery(queryParams);
        List<HintEntity> results = hintRepository.findAll(spec);

        if (results.isEmpty()) {
            return Collections.emptyList();
        }

        return hintMapper.toResponseList(results);
    }

    @Override
    @Transactional
    public String createHintsFromFile(MultipartFile file, List<MultipartFile> imageFiles, String jwtToken) {

        jwtVerify(jwtToken);

        String filename = file.getOriginalFilename();
        // Nota: Assicurati che "hint.file.invalid.format" esista nel properties per questo caso
        if (filename != null && !filename.toLowerCase().endsWith(".json")) {
            throw new HttpClientErrorException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, getMessage("hint.file.invalid.format"));
        }

        if (imageFiles == null) {
            imageFiles = Collections.emptyList();
        }

        if (file.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, getMessage("hint.file.empty"));
        }

        // --- VALIDAZIONE TIPO FILE IMMAGINI CON I18N ---
        for (MultipartFile img : imageFiles) {
            String fileName = img.getOriginalFilename();
            // String contentType = img.getContentType(); // Non usato, si può rimuovere se non serve

            if (fileName != null && !isImageFile(fileName)) {
                // USIAMO LA CHIAVE I18N passando il nome del file come parametro
                throw new HttpClientErrorException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        getMessage("hint.image.invalid.type", fileName));
            }
        }

        String adminEmail = jwtService.getAdminFromJwt(jwtToken);
        if (adminEmail == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, getMessage("hint.unauthorized"));
        }

        List<Hint> hintList;
        try {
            hintList = objectMapper.readValue(file.getInputStream(), new TypeReference<List<Hint>>() {});

            // Validazione contenuto JSON (DTO Validation)
            for (Hint hint : hintList) {
                Set<ConstraintViolation<Hint>> violations = validator.validate(hint);

                if (!violations.isEmpty()) {
                    String violationMessage = violations.stream()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .collect(Collectors.joining(", "));

                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, getMessage("hint.file.invalid.content", violationMessage));
                }
            }

        } catch (InvalidFormatException e) {
            // 1. ERRORI DI TIPO (ENUM) CON I18N
            String fieldName = e.getPath().isEmpty() ? "un campo" : e.getPath().get(0).getFieldName();
            String invalidValue = e.getValue().toString();

            // Usiamo la chiave 'hint.file.invalid.enum' passando valore e campo
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    getMessage("hint.file.invalid.enum", invalidValue, fieldName));

        } catch (JsonProcessingException e) {
            // 2. JSON MALFORMATO CON I18N
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, getMessage("hint.file.malformed"));

        } catch (IOException e) {
            // 3. ERRORE GENERICO I/O
            throw new HttpClientErrorException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, getMessage("hint.file.invalid.format"));
        }

        if (hintList == null || hintList.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, getMessage("hint.file.empty"));
        }

        // Logica Admin (Lazy Creation)
        AdminEntity adminEntity = adminRepository.findById(adminEmail).orElse(null);
        if (adminEntity == null) {
            adminEntity = new AdminEntity();
            adminEntity.setEmail(adminEmail);
            adminEntity.setUsername(adminEmail);
            adminEntity.setName("Nome");
            adminEntity.setSurname("Cognome");
            adminEntity.setPassword("password");
            adminRepository.saveAndFlush(adminEntity);
        }

        // Verifica esistenza classi UT
        List<String> classNames = hintList.stream()
                .filter(h -> h.getType().equals(HintTypeEnum.CLASS))
                .map(h -> h.getClassUTName() != null ? h.getClassUTName() : null)
                .filter(name -> name != null && !name.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (!classNames.isEmpty()) {
            long existingClassCount = classUTRepository.countExistingClasses(classNames);
            if (existingClassCount != classNames.size()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, getMessage("hint.class.notfound"));
            }
        }

        // Mappa delle immagini
        Map<String, MultipartFile> imageMap = imageFiles.stream()
                .filter(img -> img.getOriginalFilename() != null && !img.getOriginalFilename().isEmpty())
                .collect(Collectors.toMap(MultipartFile::getOriginalFilename, img -> img));

        // Ciclo di salvataggio
        for (Hint hint : hintList) {

            HintEntity existingHint = hintRepository.findByContentAndTypeAndClassUtName(hint.getContent(), hint.getType(), hint.getClassUTName());
            if(existingHint != null) {
                throw new HttpClientErrorException(HttpStatus.CONFLICT, getMessage("hint.duplicate"));
            }

            HintEntity hintEntity = hintMapper.dtoToEntity(hint);

            // Validazione Content
            if (hintEntity.getContent() == null || hintEntity.getContent().trim().isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, getMessage("hint.content.missing"));
            }

            // Validazione Name
            if (hintEntity.getName() == null || hintEntity.getName().trim().isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, getMessage("hint.name.missing"));
            }

            HintEntity hintWithTopOrder;

            // Gestione Ordinamento
            if (hintEntity.getType() == HintTypeEnum.CLASS) {
                if (hintEntity.getClassUt() == null) {
                    throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, getMessage("hint.classutname.missing"));
                }
                hintWithTopOrder = hintRepository.findFirstByClassUtNameAndTypeOrderByOrderDesc(
                        hintEntity.getClassUt().getName(),
                        hintEntity.getType()
                ).orElse(null);

            } else if (hintEntity.getType() == HintTypeEnum.GENERIC) {
                hintEntity.setClassUt(null);
                hintWithTopOrder = hintRepository.findFirstByClassUtNameAndTypeOrderByOrderDesc(
                        null,
                        hintEntity.getType()
                ).orElse(null);
            } else {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, getMessage("hint.type.invalid"));
            }

            if (hintWithTopOrder != null) {
                hintEntity.setOrder(hintWithTopOrder.getOrder() + 1);
            } else {
                hintEntity.setOrder(1);
            }

            // Gestione Immagini
            if (hint.getImageUri() != null && !hint.getImageUri().isEmpty()) {

                String identifier = hint.getImageUri();
                MultipartFile imageFile = imageMap.get(identifier);

                if (imageFile != null) {
                    try {
                        String newFileName = System.currentTimeMillis() + "_" + identifier;
                        Path filePath = Paths.get(uploadDir, newFileName);
                        imageFile.transferTo(filePath.toFile());
                        hintEntity.setImageUri("/uploads/" + newFileName);

                    } catch (IOException e) {
                        log.error("Errore salvataggio immagine {}: {}", identifier, e.getMessage());
                        throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                                getMessage("hint.image.save.error", identifier));
                    }
                } else {
                    // Immagine dichiarata nel JSON ma non presente nell'upload
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                            getMessage("hint.image.missing.file")); // Assicurati che il testo di questa chiave sia generico o corretto (es: "URI immagine trovato nel JSON, ma file non allegato.")
                }
            }

            hintEntity.setAdmin(adminEntity);

            try {
                hintRepository.save(hintEntity);
            } catch (Exception e) {
                log.error("Errore DB Save: ", e);
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("hint.save.internal.error"));
            }
        }

        return getMessage("hint.upload.success");
    }

    private boolean isImageFile(String fileName) {
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".png") ||
                lowerCaseName.endsWith(".jpg") ||
                lowerCaseName.endsWith(".jpeg") ||
                lowerCaseName.endsWith(".gif");
    }

    @Override
    @Transactional
    public String updateHint(Long id, String name, String content, MultipartFile imageFile, String jwtToken) {
        jwtVerify(jwtToken);

        HintEntity hintEntity = hintRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, getMessage("hint.notfound.id", id)));

        if (name != null && !name.trim().isEmpty()) {
            hintEntity.setName(name);
        }
        if (content != null && !content.trim().isEmpty()) {
            hintEntity.setContent(content);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                if (hintEntity.getImageUri() != null) {
                    deleteImageFile(hintEntity.getImageUri());
                }

                String originalFilename = imageFile.getOriginalFilename();
                String safeFileName = (originalFilename != null) ? originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_") : "img";
                String newFileName = System.currentTimeMillis() + "_" + safeFileName;

                Path filePath = Paths.get(uploadDir, newFileName);
                imageFile.transferTo(filePath.toFile());

                hintEntity.setImageUri("/uploads/" + newFileName);

            } catch (IOException e) {
                log.error("Errore salvataggio nuova immagine per hint {}: {}", id, e.getMessage());
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("hint.image.update.error"));
            }
        }

        hintRepository.save(hintEntity);

        return getMessage("hint.update.success");
    }

    @Override
    public String deleteHintByClassUT(String classUT, String jwtToken) {
        jwtVerify(jwtToken);

        List<HintEntity> hintEntityList;
        HintTypeEnum type = classUT.equals("null") ? HintTypeEnum.GENERIC : HintTypeEnum.CLASS;

        if (type == HintTypeEnum.GENERIC) {
            hintEntityList = hintRepository.findByType(type);
        } else {
            hintEntityList = hintRepository.findByClassUtName(classUT);
        }

        if (CollectionUtils.isEmpty(hintEntityList)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, getMessage("hint.delete.class.notfound", classUT));
        }

        hintRepository.deleteAll(hintEntityList);

        for (HintEntity hint : hintEntityList) {
            deleteImageFile(hint.getImageUri());
        }

        return getMessage("hint.delete.success");
    }

    @Override
    public String deleteHintByClassUTAndOrder(String classUT, Integer order, String jwtToken) {
        jwtVerify(jwtToken);

        HintEntity hintEntity;
        HintTypeEnum type = classUT.equals("null") ? HintTypeEnum.GENERIC : HintTypeEnum.CLASS;

        if (type == HintTypeEnum.GENERIC) {
            hintEntity = hintRepository.findByTypeAndOrder(type, order);
        } else {
            hintEntity = hintRepository.findByClassUtNameAndOrder(classUT, order);
        }

        if (hintEntity == null) {
            // Costruiamo la parte dinamica del messaggio usando placeholder
            String target = type == HintTypeEnum.GENERIC
                    ? getMessage("hint.target.generic", order)
                    : getMessage("hint.target.class", classUT, order);

            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, getMessage("hint.delete.order.notfound", target));
        }

        hintRepository.delete(hintEntity);
        deleteImageFile(hintEntity.getImageUri());

        return getMessage("hint.delete.single.success");
    }

    private void deleteImageFile(String imageUri) {
        if (imageUri != null && !imageUri.trim().isEmpty() && imageUri.startsWith("/uploads/")) {
            try {
                String fileName = imageUri.substring("/uploads/".length());
                Path filePath = Paths.get(uploadDir, fileName);

                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    log.info("File immagine eliminato con successo: {}", filePath);
                } else {
                    log.warn("File immagine non trovato nel percorso fisico: {}", filePath);
                }
            } catch (IOException e) {
                log.error("Errore durante l'eliminazione del file immagine {}: {}", imageUri, e.getMessage());
            } catch (Exception e) {
                log.error("Errore generico durante l'eliminazione del file immagine {}: {}", imageUri, e.getMessage());
            }
        }
    }

    void jwtVerify(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty() || !jwtService.isJwtValid(jwtToken)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, getMessage("hint.jwt.invalid"));
        }
    }

    @Transactional
    public String deleteHintsByType(String type, String jwtToken) {
        jwtVerify(jwtToken);

        HintTypeEnum typeEnum;
        try {
            typeEnum = HintTypeEnum.valueOf(type);
        } catch (IllegalArgumentException e) {
            return getMessage("hint.type.enum.invalid", type);
        }

        List<HintEntity> hintsToDelete = hintRepository.findByType(typeEnum);

        if (hintsToDelete.isEmpty()) {
            return getMessage("hint.type.notfound", type);
        }
        for (HintEntity hintEntity : hintsToDelete) {
            if (hintEntity.getImageUri() != null && !hintEntity.getImageUri().isEmpty()) {
                deleteImageFile(hintEntity.getImageUri());
            }
        }

        hintRepository.deleteByType(typeEnum);

        return getMessage("hint.delete.type.success", type);
    }

    @Override
    @Transactional
    public void moveHint(Long id, String direction, String jwtToken) {
        jwtVerify(jwtToken);

        HintEntity currentHint = hintRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, getMessage("hint.notfound")));

        int currentOrder = currentHint.getOrder();
        int targetOrder;

        if ("UP".equalsIgnoreCase(direction)) {
            if (currentOrder <= 1) return;
            targetOrder = currentOrder - 1;
        } else if ("DOWN".equalsIgnoreCase(direction)) {
            targetOrder = currentOrder + 1;
        } else {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, getMessage("hint.move.direction.invalid"));
        }

        String className = (currentHint.getClassUt() != null) ? currentHint.getClassUt().getName() : null;

        HintEntity neighborHint = hintRepository.findByClassUtNameAndTypeAndOrder(className, currentHint.getType(), targetOrder)
                .orElse(null);

        if (neighborHint != null) {
            int tempOrder = -1;

            neighborHint.setOrder(tempOrder);
            hintRepository.saveAndFlush(neighborHint);

            currentHint.setOrder(targetOrder);
            hintRepository.saveAndFlush(currentHint);

            neighborHint.setOrder(currentOrder);
            hintRepository.save(neighborHint);
        }
    }
}