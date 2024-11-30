package io.contact.contactapi.service;

import io.contact.contactapi.domain.Contact;
import io.contact.contactapi.repositary.ContactRepositary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepositary contactRepositary;

    public Page<Contact> getAllContacts(@RequestParam int page, @RequestParam int size) {
        return contactRepositary.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContact(String id) {
        return contactRepositary.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Contact createContact(Contact contact) {
        return contactRepositary.save(contact);
    }

    public void deleteContact(Contact contact) {
        contactRepositary.delete(contact);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Saving Picture for user ID: {}", id);
        Contact contact = getContact(id);
        String photoUrl = photoFunction(id, file);
        contact.setImage(photoUrl);
        contactRepositary.save(contact);
        return photoUrl;
    }

    private String photoFunction(String id, MultipartFile image) {
        String fileName = id + "." + Optional.ofNullable(image.getOriginalFilename())
                .map(name -> name.substring(name.lastIndexOf('.') + 1))
                .orElse("png");

        try {
            Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/uploads/")
                    .toAbsolutePath()
                    .normalize();

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(image.getInputStream(), targetLocation, REPLACE_EXISTING);

            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/contacts/image/" + fileName)
                    .toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image", exception);
        }
    }
}
