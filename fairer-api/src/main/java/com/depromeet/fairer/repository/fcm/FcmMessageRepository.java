package com.depromeet.fairer.repository.fcm;

import com.depromeet.fairer.domain.fcm.FcmMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmMessageRepository extends JpaRepository<FcmMessage, Long> {

}
