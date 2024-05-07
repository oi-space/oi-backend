package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
public class UserDaoTest {
    @Autowired
    UserDao userDao;
    @Autowired
    EntityManager entityManager;
    User user;
    @BeforeEach
    public void setUp() {
        user = Utils.createUser();
        userDao.save(user);
    }
    @Test
    @DisplayName("findById 테스트")
    public void findByIdTest() {
        User findUser = userDao.findById(user.getId()).get();
        Assertions.assertThat(findUser.getId()).isEqualTo(user.getId());
    }
}
