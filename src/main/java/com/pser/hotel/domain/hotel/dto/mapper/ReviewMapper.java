package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.domain.ReviewImage;
import com.pser.hotel.domain.hotel.dto.request.ReviewCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    Review toEntity(ReviewCreateRequest request);

    @Mapping(target = "imageUrls", expression = "java(mapReviewImagesToUrls(review.getReviewImages()))")
    ReviewResponse toResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateReviewFromDto(ReviewUpdateRequest request, @MappingTarget Review review);

    default List<String> mapReviewImagesToUrls(List<ReviewImage> reviewImages) {
        if (reviewImages == null) {
            return null;
        }
        return reviewImages.stream().map(ReviewImage::getUrl).collect(Collectors.toList());
    }
}
