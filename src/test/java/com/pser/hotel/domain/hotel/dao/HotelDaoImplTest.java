package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
public class HotelDaoImplTest {
    @Autowired
    HotelDao hotelDao;
    @Autowired
    FacilityDao facilityDao;
    @Autowired
    UserDao userDao;
    @Autowired
    EntityManager entityManager;
    User user;
    Hotel hotel;
    Facility facility;
    PageRequest pageRequest;
    HotelSearchRequest hotelSearchRequest;
    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);
        userDao.save(user);
        hotelDao.save(hotel);
        facilityDao.save(facility);
    }

    @Test
    @DisplayName("name 검색 테스트")
    public void nameSearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByName(hotel.getName());
        Slice<HotelResponse> nameResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(nameResponse.getContent()).isNotEmpty();
    }
    private HotelSearchRequest createSearchRequestByName(String name){
        return HotelSearchRequest.builder()
            .name(name)
            .build();
    }
    @Test
    @DisplayName("category 검색 테스트")
    public void categorySearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByCategory(hotel.getCategory());
        Slice<HotelResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }
    private HotelSearchRequest createSearchRequestByCategory(HotelCategoryEnum hotelCategoryEnum){
        return HotelSearchRequest.builder()
            .category(hotelCategoryEnum)
            .build();
    }
    @Test
    @DisplayName("province 검색 테스트")
    public void provinceSearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = creatSearchRequestByProvince(hotel.getProvince());
        Slice<HotelResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }
    private HotelSearchRequest creatSearchRequestByProvince(String province){
        return HotelSearchRequest.builder()
            .province(province)
            .build();
    }
    @Test
    @DisplayName("district 검색 테스트")
    public void districtSearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByDistrict(hotel.getDistrict());
        Slice<HotelResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }
    private HotelSearchRequest createSearchRequestByDistrict(String district){
        return HotelSearchRequest.builder()
            .district(district)
            .build();
    }
    @Test
    @DisplayName("detailedAddress 검색 테스트")
    public void detailedAddressTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByDetailedAddress(hotel.getDetailedAddress());
        Slice<HotelResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }
    private HotelSearchRequest createSearchRequestByDetailedAddress(String detailedAddress){
        return HotelSearchRequest.builder()
            .detailedAddress(detailedAddress)
            .build();
    }
    @Test
    @DisplayName("parkingLot 검색 테스트")
    public void parkingLotTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByParkingLot(hotel.getFacility().getParkingLot());
        Slice<HotelResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }
    private HotelSearchRequest createSearchRequestByParkingLot(Boolean parkingLot){
        return HotelSearchRequest.builder()
            .parkingLot(parkingLot)
            .build();
    }
    @Test
    @DisplayName("wifi 검색 테스트")
    public void wifiTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByWifi(hotel.getFacility().getWifi());
        Slice<HotelResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }
    private HotelSearchRequest createSearchRequestByWifi(Boolean wifi){
        return HotelSearchRequest.builder()
            .parkingLot(wifi)
            .build();
    }
    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }
}
