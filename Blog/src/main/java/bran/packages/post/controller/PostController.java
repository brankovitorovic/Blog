package bran.packages.post.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bran.packages.post.dto.PostDTO;
import bran.packages.post.enums.CategoryEnum;
import bran.packages.post.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/post/")
public class PostController {

	private final PostService postService;
	
	@GetMapping("all")
	public ResponseEntity<List<PostDTO>> findAll(){
		return new ResponseEntity<>(postService.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("all/category={categoryEnum}")
	public ResponseEntity<List<PostDTO>> findByCategory(@PathVariable CategoryEnum categoryEnum){
		return new ResponseEntity<>(postService.findByCategory(categoryEnum),HttpStatus.OK);
	}
	
	@PostMapping("save")
	public ResponseEntity<PostDTO> save(@Valid @RequestBody PostDTO postDTO) {
		return new ResponseEntity<>(postService.save(postDTO),HttpStatus.OK); 
	}
	
	@GetMapping("all/writers")
	public ResponseEntity<Set<String>> getWriters(){
		return new ResponseEntity<>(postService.findAllWriters(),HttpStatus.OK);
	}
	
	@GetMapping("all/user={username}")
	public ResponseEntity<List<PostDTO>> findPostsFromUser(@PathVariable String username){
		return new ResponseEntity<>(postService.findPostsFromUser(username),HttpStatus.OK);
	}
	
	@GetMapping("delete/id={id}") // Google chrome does not allow delete method on localhost
	public ResponseEntity<String> delete(@PathVariable Long id){
		postService.delete(id);
		return new ResponseEntity<>("Successfully deleted.",HttpStatus.OK);
	}
	
	@PostMapping("update")
	public ResponseEntity<PostDTO> update(@RequestBody PostDTO postDTO){
		return new ResponseEntity<>(postService.update(postDTO),HttpStatus.OK);
	}
	
	@GetMapping("all/categories")
	public CategoryEnum[] getCategories(){
		return postService.getCategories();
	}
	
}
