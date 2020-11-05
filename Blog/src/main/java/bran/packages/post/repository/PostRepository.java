package bran.packages.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bran.packages.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	Post findByFrontId(Long id);
	
	Post findByTitle(String title);
	
	void deleteByFrontId(Long id);
	
	Optional<List<Post>> findByCreatorUsername(String username);
	
}
