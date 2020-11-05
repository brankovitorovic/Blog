package bran.packages.post.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import bran.packages.post.dto.PostDTO;
import bran.packages.post.entity.Post;
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
		//TODO provere i ovde i delete
		return postMapper.toDTO(postRepository.save(postMapper.fromDTO(postDTO)));
	}
	
	@Transactional
	public void delete(Long id){
		postRepository.deleteByFrontId(id);
	}
	
	public List<PostDTO> findPostsFromUser(String username){
		return postMapper.toDTOList(postRepository.findByCreatorUsername(username).
				orElseThrow( () -> new InvalidUserInfoException("bset")));
	}
	
	@Transactional
	public PostDTO save(PostDTO postDTO) {
		//TODO exeptions
		
		User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		Post post = postMapper.fromDTO(postDTO);
		
		post.setCreator(user);
		post.setFrontId(generateFrontId());
		post.setDateCreated(Date.valueOf(LocalDate.now()));
		
		postRepository.save(post);
		
		return postMapper.toDTO(post);
	}
	
	public List<PostDTO> findAll(){
		return postMapper.toDTOList(postRepository.findAll());
	}
	
	private Long generateFrontId() { // getting unique frontId from current time in milliseconds
		return new java.util.Date().getTime();
	}
	
	
	
}
