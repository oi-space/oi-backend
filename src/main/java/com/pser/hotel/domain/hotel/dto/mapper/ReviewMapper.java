package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.domain.ReviewImage;
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "grade", source = "grade")
    @Mapping(target = "detail", source = "detail")
    @Mapping(target = "reservation", source = "reservationId", qualifiedByName = "mapReservationIdToReservation")
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

    @Named("mapReservationIdToReservation")
    default Reservation mapReservationIdToReservation(Long reservationId) {
        if (reservationId == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        return reservation;
    }
}
