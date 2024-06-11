package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.domain.ReviewImage;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.ReviewCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {

    @Mapping(target = "room", source = "request", qualifiedByName = "mapRoom")
    @Mapping(target = "reviewImages", source = "imageUrls", qualifiedByName = "mapUrlsToReviewImages")
    @Mapping(target = "reviewerName", source = "reviewerName")
    @Mapping(target = "profileImageUrl", source = "profileImageUrl")
    @Mapping(target = "hotelId", source = "hotelId")
    Review toEntity(ReviewCreateRequest request);

    @Mapping(target = "imageUrls", source = "reviewImages", qualifiedByName = "mapReviewImagesToUrls")
    @Mapping(target = "reviewerName", source = "reviewerName")
    @Mapping(target = "profileImageUrl", source = "profileImageUrl")
    @Mapping(target = "hotelId", source = "hotelId")
    ReviewResponse toResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "reviewerName", source = "reviewerName")
    @Mapping(target = "profileImageUrl", source = "profileImageUrl")
    @Mapping(target = "hotelId", source = "hotelId")
    @Mapping(target = "room", source = "roomId", qualifiedByName = "mapRoomId")
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
        throw new UnsupportedOperationException("Room mapping logic is not implemented.");
    }

    @Named("mapRoomId")
    default Room mapRoomId(Long roomId) {
        throw new UnsupportedOperationException("Room mapping logic is not implemented.");
    }
}
