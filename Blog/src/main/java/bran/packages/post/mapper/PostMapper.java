package bran.packages.post.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import bran.packages.post.dto.PostDTO;
import bran.packages.post.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

	PostDTO toDTO(Post post);
	
	Post fromDTO(PostDTO postDTO);
	
	List<PostDTO> toDTOList(List<Post> posts);
	
}
