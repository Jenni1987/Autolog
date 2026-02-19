package com.autolog.service;

import com.autolog.model.Document;
import com.autolog.model.Operation;
import com.autolog.repository.DocumentRepository;
import com.autolog.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepo;
    private final OperationRepository operationRepo;

    private final Path rootLocation = Paths.get("uploads");

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public DocumentService(DocumentRepository documentRepo,
                                  OperationRepository operationRepo) {
        this.documentRepo = documentRepo;
        this.operationRepo = operationRepo;

        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio uploads", e);
        }
    }

    public List<Document> findAllByUser(Integer userId) {
        return documentRepo.findAllByUserId(userId);
    }

    public Document findByIdForUser(Integer id, Integer userId) {
        return documentRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
    }

    public Document save(Document document,
                                MultipartFile file,
                                Integer operationId,
                                Integer userId) {

        Operation operation = operationRepo.findById(operationId)
                .filter(o -> o.getVehicle().getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Operación inválida"));

        document.setOperation(operation);

        handleFileUpload(document, file);

        return documentRepo.save(document);
    }

    public void update(Integer id,
                       Document newDoc,
                       MultipartFile file,
                       Integer operationId,
                       Integer userId) {

        Document existing = findByIdForUser(id, userId);

        Operation operation = operationRepo.findById(operationId)
                .filter(o -> o.getVehicle().getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Operación inválida"));

        existing.setFilename(newDoc.getFilename());
        existing.setOperation(operation);

        if (file != null && !file.isEmpty()) {
            deletePhysicalFile(existing.getFilepath());
            handleFileUpload(existing, file);
        }

        documentRepo.save(existing);
    }

    public void delete(Integer id, Integer userId) {
        Document doc = findByIdForUser(id, userId);
        deletePhysicalFile(doc.getFilepath());
        documentRepo.delete(doc);
    }

    private void handleFileUpload(Document document, MultipartFile file) {

        if (file == null || file.isEmpty()) return;

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("ERROR_SIZE");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("ERROR_EXTENSION");
        }

        String userFilename = document.getFilename();
        if (userFilename != null) {
            userFilename = userFilename.trim().replaceAll("(?i)\\.pdf$", "");
        }

        if (userFilename == null || userFilename.isBlank()) {
            userFilename = originalName.replaceAll("(?i)\\.pdf$", "");
        }

        try {
            String savedFilename = System.currentTimeMillis() + "_" + userFilename + ".pdf";
            Path destination = rootLocation.resolve(savedFilename);

            Files.copy(file.getInputStream(), destination,
                    StandardCopyOption.REPLACE_EXISTING);

            document.setFilename(userFilename);
            document.setFilepath(destination.toAbsolutePath().toString());
            document.setType(file.getContentType());

        } catch (IOException e) {
            throw new RuntimeException("ERROR_UPLOAD");
        }
    }

    private void deletePhysicalFile(String path) {
        if (path == null) return;
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException ignored) {}
    }
}
