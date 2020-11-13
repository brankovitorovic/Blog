package bran.packages.user.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import bran.packages.security.JWTConstants;
import bran.packages.user.dto.UserDTO;
import bran.packages.user.dto.UserRoleDTO;
import bran.packages.user.entity.User;
import bran.packages.user.exception.InvalidUserInfoException;
import bran.packages.user.exception.UserNotFoundException;
import bran.packages.user.mapper.UserMapper;
import bran.packages.user.repository.UserRepository;
import bran.packages.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final UserRoleRepository userRoleRepository;

	private final UserMapper mapper;

	@Transactional
	public void deleteById(UUID existingId) {
		if (userNotFound(existingId))
			throw new UserNotFoundException(String.format("User with id: %d wasn't found!", existingId));

		userRepository.deleteByFrontId(existingId);
	}

	private boolean userNotFound(UUID existingId) {
		return findAll().stream().filter(s -> s.getFrontId().equals(existingId)).findFirst().isEmpty();
	}

	@Transactional
	public String save(UserDTO request) {
		if (usernameExists(request))
			throw new InvalidUserInfoException(
					String.format("User with username: %s already exists!", request.getUsername()));

		if (invalidUserRoles(request))
			throw new InvalidUserInfoException("Invalid user roles!");

		request.setPassword(hashPassword(request.getPassword()));

		request.setFrontId(UUID.randomUUID());

		Set<UserRoleDTO> roles = new HashSet<UserRoleDTO>(); // we will set every new save to be user
		UserRoleDTO role = new UserRoleDTO();
		role.setId(Long.valueOf("2"));
		role.setName("USER");
		roles.add(role);
		request.setRoles(roles);
		
		User user = userRepository.save(mapper.fromDTO(request));
		
		
		return createToken(user);
	}

	private boolean usernameExists(UserDTO user) {
		return userRepository.findByUsername(user.getUsername()).isPresent();
	}

	private boolean invalidUserRoles(UserDTO request) {
		return request.getRoles().stream().anyMatch(this::roleNotFound);
	}

	private boolean roleNotFound(UserRoleDTO role) {
		return userRoleRepository.findById(role.getId()).isEmpty();
	}

	private String hashPassword(String plainText) {
		return BCrypt.hashpw(plainText, BCrypt.gensalt());
	}

	
	@Transactional
	public String update(UUID existingId, UserDTO request) {
		if(! findByFrontUUID(existingId).getUsername().equals(request.getUsername())) {
			if (usernameExists(request))
				throw new InvalidUserInfoException(
						String.format("User with username: %s already exists!", request.getUsername()));
		}
		if (invalidUserRoles(request))
			throw new InvalidUserInfoException("Invalid user roles!");

		if (request.isPasswordPlain())
			request.setPassword(hashPassword(request.getPassword()));

		final User user = userRepository.findByFrontId(request.getFrontId());
		final User userForSave = mapper.fromDTO(request);
		userForSave.setId(user.getId());
		final User response = userRepository.save(userForSave);
		
		return createToken(response);
	}

	public String updateToken(UserDTO userDTO) {
		return createToken(userRepository.findByFrontId(userDTO.getFrontId()));
	}
	
	public UserDTO findByUsername(String username) {
		return userRepository.findByUsername(username).map(mapper::toDTO).orElseThrow(
				() -> new UserNotFoundException(String.format("User with username: %s wasn't found!", username)));
	}

	public UserDTO findByFrontUUID(UUID existingId) {
		return findAll().stream().filter(s -> s.getFrontId().equals(existingId)).findFirst().orElseThrow(
				() -> new UserNotFoundException(String.format("User with id: %s wasn't found!", existingId)));
	}

	public List<UserDTO> findAll() {
		return mapper.toDTOList(userRepository.findAll());
	}

	private String createToken(User user) { // when user change username or password and when token expire so it can be updated 
		
		String rolesToken = String.format("[ROLE_%s]",user.getRoles().stream().findFirst().get().getName() );
		
		String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("Role", rolesToken )
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JWTConstants.SECRET_KEY));
		
		return JWTConstants.TOKEN_PREFIX + token;
	}
	
	
}
