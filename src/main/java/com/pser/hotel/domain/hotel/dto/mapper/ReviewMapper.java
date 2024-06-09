package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.domain.ReviewImage;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.ReviewCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "room", source = "request", qualifiedByName = "mapRoom")
    @Mapping(target = "reviewImages", source = "imageUrls", qualifiedByName = "mapUrlsToReviewImages")
    Review toEntity(ReviewCreateRequest request);

    @Mapping(target = "imageUrls", source = "reviewImages", qualifiedByName = "mapReviewImagesToUrls")
    ReviewResponse toResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateReviewFromDto(ReviewUpdateRequest request, @MappingTarget Review review);

    @Named("mapReviewImagesToUrls")
    default List<String> mapReviewImagesToUrls(List<ReviewImage> reviewImages) {
        if (reviewImages == null) {
            return null;
        }
        return reviewImages.stream()
                .map(ReviewImage::getFileUrl)
                .collect(Collectors.toList());
    }

    @Named("mapUrlsToReviewImages")
    default List<ReviewImage> mapUrlsToReviewImages(List<String> urls) {
        if (urls == null) {
            return null;
        }
        return urls.stream()
                .map(ReviewImage::new)
                .collect(Collectors.toList());
    }

    @Named("mapRoom")
    default Room mapRoom(ReviewCreateRequest request) {
        // roomId를 사용하여 Room 객체를 조회하는 로직을 구현합니다.
        // 예: Room room = roomRepository.findById(request.getRoomId()).orElseThrow(...);
        // return room;

        // 예제 코드에서는 Room 객체를 직접 생성하여 반환합니다.
        Room room = new Room();
        room.setId(request.getRoomId());
        return room;
    }
}
