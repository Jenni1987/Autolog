package com.autolog.controller;

import com.autolog.model.Document;
import com.autolog.service.DocumentService;
import com.autolog.service.OperationService;
import com.autolog.service.UserService;
import com.autolog.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/documents")
@SessionAttributes({ "user" })
public class DocumentController {

	private final DocumentService documentService;
	private final OperationService operationService;
	private final UserService userService;

	public DocumentController(DocumentService documentService, OperationService operationService,
			UserService userService) {
		this.documentService = documentService;
		this.operationService = operationService;
		this.userService = userService;
	}

	private User getUser(Model model, Principal principal) {
		User user = (User) model.getAttribute("user");
		if (user == null && principal != null) {
			user = userService.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}
		return user;
	}

	@GetMapping
	public String list(Model model, Principal principal) {
		User user = getUser(model, principal);

		model.addAttribute("documents", documentService.findAllByUser(user.getId()));

		return "documents/list";
	}

	@GetMapping("/new")
	public String newForm(@RequestParam(required = false) Integer operationId,
	                      Model model, Principal principal) {
	    User user = getUser(model, principal);

	    Document doc = new Document();
	    model.addAttribute("document", doc);

	    var operations = operationService.getOperationsForUser(user.getId());
	    model.addAttribute("operations", operations);

	    if (operationId != null) {
	        operations.stream()
	                  .filter(op -> op.getId().equals(operationId))
	                  .findFirst()
	                  .ifPresent(doc::setOperation);
	    }

	    return "documents/form";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute Document document, @RequestParam Integer operationId,
			@RequestParam MultipartFile file, Model model, Principal principal) {

		User user = getUser(model, principal);

		try {
			documentService.save(document, file, operationId, user.getId());
			return "redirect:/documents";

		} catch (RuntimeException ex) {

			if ("ERROR_SIZE".equals(ex.getMessage())) {
				model.addAttribute("fileError", "El archivo supera el tamaño máximo permitido (10MB).");
			} else if ("ERROR_EXTENSION".equals(ex.getMessage())) {
				model.addAttribute("fileError", "Solo se permiten archivos en formato PDF (.pdf).");
			} else {
				model.addAttribute("fileError", "Error al subir el archivo.");
			}

			model.addAttribute("operations", operationService.getOperationsForUser(user.getId()));

			return "documents/form";
		}
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model, Principal principal) {

		User user = getUser(model, principal);

		model.addAttribute("document", documentService.findByIdForUser(id, user.getId()));

		model.addAttribute("operations", operationService.getOperationsForUser(user.getId()));

		return "documents/form";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Integer id, @ModelAttribute Document document,
			@RequestParam Integer operationId, @RequestParam MultipartFile file, Model model, Principal principal) {

		User user = getUser(model, principal);

		try {
			documentService.update(id, document, file, operationId, user.getId());
			return "redirect:/documents";

		} catch (RuntimeException ex) {

			if ("ERROR_SIZE".equals(ex.getMessage())) {
				model.addAttribute("fileError", "El archivo supera el tamaño máximo permitido (10MB).");
			} else if ("ERROR_EXTENSION".equals(ex.getMessage())) {
				model.addAttribute("fileError", "Solo se permiten archivos en formato PDF (.pdf).");
			} else {
				model.addAttribute("fileError", "Error al subir el archivo.");
			}

			model.addAttribute("document", documentService.findByIdForUser(id, user.getId()));

			model.addAttribute("operations", operationService.getOperationsForUser(user.getId()));

			return "documents/form";
		}
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, Model model, Principal principal) {

		User user = getUser(model, principal);

		documentService.delete(id, user.getId());

		return "redirect:/documents";
	}

	@GetMapping("/view/{id}")
	public ResponseEntity<Resource> view(@PathVariable Integer id, Model model, Principal principal) throws Exception {

		User user = getUser(model, principal);

		Document document = documentService.findByIdForUser(id, user.getId());

		Path path = Paths.get(document.getFilepath());
		Resource resource = new UrlResource(path.toUri());

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable Integer id, Model model, Principal principal)
			throws Exception {

		User user = getUser(model, principal);

		Document document = documentService.findByIdForUser(id, user.getId());

		Path path = Paths.get(document.getFilepath());
		Resource resource = new UrlResource(path.toUri());

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFilename() + "\"")
				.body(resource);
	}

}
