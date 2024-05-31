package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.global.config.QueryDslConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
@DisplayName("Reservation 엔티티 테스트")
@Slf4j
public class ReviewTest {

    @Test
    public void test(){}
}
