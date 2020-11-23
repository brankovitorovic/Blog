package bran.packages.post.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import bran.packages.post.dto.PostDTO;
import bran.packages.post.entity.Post;
import bran.packages.post.enums.CategoryEnum;
import bran.packages.post.mapper.PostMapper;
import bran.packages.post.repository.PostRepository;
import bran.packages.user.entity.User;
import bran.packages.user.exception.InvalidUserInfoException;
import bran.packages.user.mapper.UserMapper;
import bran.packages.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	
	private final PostMapper postMapper;
	
	private final UserRepository userRepository;
	
	private final UserMapper userMapper;
	
	@Transactional
	public PostDTO update(PostDTO postDTO){
		return postMapper.toDTO(postRepository.save(postMapper.fromDTO(postDTO)));
	}
	
	@Transactional
	public boolean delete(Long id){
		final User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		
		if( user.getId() != postRepository.findByFrontId(id).getCreator().getId()) {
			return false;
		}
		
		postRepository.deleteByFrontId(id);
		return true;
	}
	
	public List<PostDTO> findPostsFromUser(String username){
		return postMapper.toDTOList(postRepository.findByCreatorUsername(username).
				orElseThrow( () -> new InvalidUserInfoException("No posts from this user!")));
	}
	
	public Set<String> findAllWriters(){
		final List<PostDTO> posts = findAll();
		final Set<String> writers = new HashSet<>();

		posts.stream().forEach(post -> writers.add(post.getCreator().getUsername()));
				
		return writers;
	}
	
	@Transactional
	public PostDTO save(PostDTO postDTO) {
		
		User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		Post post = postMapper.fromDTO(postDTO);
		
		post.setCreator(user);
		post.setFrontId(generateFrontId());
		post.setDateCreated(Date.valueOf(LocalDate.now()));
		
		postRepository.save(post);
		
		return postMapper.toDTO(post);
	}
	
	public List<PostDTO> findByCategory(CategoryEnum categoryEnum){
		return postMapper.toDTOList(postRepository.findByCategory(categoryEnum));
	}
	
	public CategoryEnum[] getCategories(){
		return CategoryEnum.class.getEnumConstants();
	}
	
	public List<PostDTO> findAll(){
		return postMapper.toDTOList(postRepository.findAll());
	}
	
	private Long generateFrontId() { // getting unique frontId from current time in milliseconds
		return new java.util.Date().getTime();
	}
	
	
	
}
