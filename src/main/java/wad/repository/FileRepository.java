package wad.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.FileObject;

public interface FileRepository extends JpaRepository<FileObject, Long> {

        FileObject findByArt(Long art);
}