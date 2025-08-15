package com.dbiz.app.userservice.repository;

import java.util.List;
import java.util.Optional;

import com.dbiz.app.userservice.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbiz.app.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {
	
	Optional<User> findByUserName(final String username);

	@Query("SELECT u FROM User u WHERE u.userName = :username AND u.tenantId= :tenantId and u.isActive= 'Y'")
	Optional<User> findByUserNameAndDTenantId(final String username, final Integer tenantId);

	Optional<User> findByUserId(@Param("userId") Integer userId);

	User findByErpUserId(Integer userId);

	@Query("SELECT u From User u where u.userId = :userId")
	Optional<User> findById(Integer userId);

	List<User>findAllByPhone(String phone);

	User findByEmail(String email);

	Optional<User> findByUserIdAndTenantId(Integer userId, Integer tenantId);

	Optional<User> findByGoogleIdAndTenantId(String googleId,Integer tenantId);

	Optional<User> findByFacebookIdAndTenantId(String facebookId,Integer tenantId);

	Optional<User> findByZaloIdAndTenantId(String zaloId,Integer tenantId);

	Optional<User> findByAppleIdAndTenantId(String appleId,Integer tenantId);

	Optional<User> findByUserNameAndTenantId(String username,Integer tenantId);

	List<User> findByPhoneAndTenantId(String phone,Integer tenantId);

	List<User> findByEmailAndTenantId(String email,Integer tenantId);
	List<User> findByEmailAndUserNameAndTenantId(String email,String userName,Integer tenantId);
	List<User> findByPhoneOrUserNameAndTenantId(String phone,String userName,Integer tenantId);

	Optional<User>findByErpUserName(String erpUserName);

	Optional<User> findByClientIdAndClientSecretAndGrantType(
			String clientId,
			String clientSecret,
			String grantType
	);

	Optional<User> findByClientId(
			String clientId
	);
}
