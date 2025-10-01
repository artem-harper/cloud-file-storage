package cloudFileStorage.cloudfilestorage.repository;

import cloudFileStorage.cloudfilestorage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
