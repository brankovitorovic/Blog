package bran.packages.post.dto;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import bran.packages.post.enums.CategoryEnum;
import bran.packages.user.dto.UserDTO;
import lombok.Data;

@Data
public class PostDTO {

	@NotNull
	@NotBlank
	private Long frontId;

	@NotNull
	@NotBlank
	private UserDTO creator;
	
	@NotNull
	@NotBlank
	private String title;

	@NotNull
	@NotBlank
	private String body;
	
	private Date dateCreated;
	
	private CategoryEnum category;
	
	private String url;
	
}
