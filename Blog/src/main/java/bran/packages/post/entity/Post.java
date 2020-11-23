package bran.packages.post.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import bran.packages.post.enums.CategoryEnum;
import bran.packages.user.entity.User;
import lombok.Data;

@Data
@Entity
@Table(name = "posts")
public class Post implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long frontId;
	
	@ManyToOne
	@JoinColumn()
	private User creator;
	
	@Column(unique = true, nullable = false,length = 50)
	private String title;
	
	@Column(length = 64000)
	private String body;
	
	private Date dateCreated;
	
	private CategoryEnum category;
	
	private String url;
	
}
