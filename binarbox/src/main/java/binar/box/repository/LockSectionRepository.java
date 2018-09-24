package binar.box.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import binar.box.domain.LockSection;

/**
 * Created by Timis Nicu Alexandru on 18-Apr-18.
 */
public interface LockSectionRepository extends JpaRepository<LockSection, Long> {

	List<LockSection> findBySection(char letter);
}
