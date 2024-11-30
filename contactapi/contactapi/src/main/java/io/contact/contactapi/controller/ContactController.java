package io.contact.contactapi.controller;

import io.contact.contactapi.domain.Contact;
import io.contact.contactapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(path = "/contacts")
@RequiredArgsConstructor
public class ContactController {


    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<Page<Contact>> getAllContacts(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(contactService.getAllContacts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable String id) {
        return ResponseEntity.ok(contactService.getContact(id));
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.createContact(contact));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        Contact contact = contactService.getContact(id);
        contactService.deleteContact(contact);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable String id, @RequestParam MultipartFile file) {
        return ResponseEntity.ok(contactService.uploadPhoto(id, file));
    }


    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.home") + "/Downloads/uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header("Content-Type", "image/jpeg")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file", e);
        }
    }

}
