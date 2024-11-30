package io.contact.contactapi.repositary;

import io.contact.contactapi.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepositary extends JpaRepository<Contact, String> {
}
