package binar.box.repository;

import binar.box.domain.Lock;
import binar.box.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LockRepository extends BaseJpaRepository<Lock, Long> {

	List<Lock> findByUserAndPaidFalse(User user);

	void deleteByUserAndDeleteToken(User user, String token);

	Optional<Lock> findByUserAndId(User user, long id);

	List<Lock> findByUserInAndPrivateLockFalse(List<User> applicationFriends);

//	@Query(value = "SELECT l.id,l.longitude,l.latitude,l.message,l.font_size,l.font_style,l.font_color,l.paid,l.private_lock,l.lock_color,l.lock_section_id,l.lock_type_id,l.user_id,l.delete_token,l.created_date,l.last_modified_date,l.panel_id FROM lock_entity AS l \r\n"
//			+ "INNER JOIN user AS u ON u.id = l.user_id\r\n"
//			+ "WHERE l.panel_id = :panelId AND l.private_lock=0 OR (l.user_id = :userId AND l.private_lock=1) OR (u.facebook_id NOT IN(:facebookFriendsIds) AND l.private_lock=1)", nativeQuery = true)
//	List<Lock> findUserPanelLocksAndHidePrivateFriendsLocks(@Param("userId") String userId,
//			@Param("panelId") long panelId, @Param("facebookFriendsIds") List<String> facebookFriendsIds);

//	List<Lock> findByPanelIdAndPaidTrue(Long id);

	List<Lock> findAllByUserIdAndPrivateLockFalse(List<String> facebookUserFriends);

	List<Lock> findByUser(User user);

	@Query(value = "SELECT locks.id,message,font_size,font_style,font_color,paid,private_lock,lock_color,lock_section_id\r\n"
			+ "			,lock_template_id,user_id,delete_token,locks.created_date,locks.last_modified_date FROM locks\r\n"
			+ "			INNER JOIN user ON user.id=locks.user_id  WHERE user.country=:country AND  RAND()  AND user.id NOT IN(:userIds)  AND locks.private_lock=0  LIMIT :limit", nativeQuery = true)
	List<Lock> findLocksRandomByCountry(@Param("limit") int limit, @Param("userIds") List<String> userId,
			@Param("country") String country);

	List<Lock> findByUserId(String UserId);

}
