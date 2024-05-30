package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.dto.ReviewSearchRequest;
import com.pser.hotel.global.common.request.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewDaoCustom {
    Page<Review> search(ReviewSearchRequest request, Pageable pageable);
}
