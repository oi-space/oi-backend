package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.QReview;
import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.dto.request.ReviewSearchRequest;
import com.pser.hotel.domain.model.GradeEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReviewDaoImpl implements ReviewDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> search(ReviewSearchRequest request, Pageable pageable) {
        QReview review = QReview.review;

        BooleanBuilder searchCondition = buildSearchCondition(review, request);

        List<Review> reviews = queryFactory
                .selectFrom(review)
                .where(searchCondition)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long count = queryFactory
                .select(review.count())
                .from(review)
                .where(searchCondition)
                .fetchOne();

        return new PageImpl<>(reviews, pageable, count != null ? count : 0L);
    }

    private BooleanBuilder buildSearchCondition(QReview review, ReviewSearchRequest request) {
        return new BooleanBuilder()
                .and(matchKeyword(review, request.getKeyword()))
                .and(matchCreatedAfter(review, request.getCreatedAfter()))
                .and(matchCreatedBefore(review, request.getCreatedBefore()))
                .and(matchUpdatedAfter(review, request.getUpdatedAfter()))
                .and(matchUpdatedBefore(review, request.getUpdatedBefore()))
                .and(matchRating(review, request.getRating())) // Integer로 반환된 값을 사용
                .and(matchContent(review, request.getContent()))
                .and(matchCreatedAt(review, request.getCreatedAt()))
                .and(matchUpdatedAt(review, request.getUpdatedAt()));
    }

    private BooleanExpression matchKeyword(QReview review, String keyword) {
        if (keyword == null) {
            return null;
        }
        return review.detail.containsIgnoreCase(keyword);
    }

    private BooleanExpression matchCreatedAfter(QReview review, LocalDateTime createdAfter) {
        if (createdAfter == null) {
            return null;
        }
        return review.createdAt.after(createdAfter)
                .or(review.createdAt.eq(createdAfter));
    }

    private BooleanExpression matchCreatedBefore(QReview review, LocalDateTime createdBefore) {
        if (createdBefore == null) {
            return null;
        }
        return review.createdAt.before(createdBefore)
                .or(review.createdAt.eq(createdBefore));
    }

    private BooleanExpression matchUpdatedAfter(QReview review, LocalDateTime updatedAfter) {
        if (updatedAfter == null) {
            return null;
        }
        return review.updatedAt.after(updatedAfter)
                .or(review.updatedAt.eq(updatedAfter));
    }

    private BooleanExpression matchUpdatedBefore(QReview review, LocalDateTime updatedBefore) {
        if (updatedBefore == null) {
            return null;
        }
        return review.updatedAt.before(updatedBefore)
                .or(review.updatedAt.eq(updatedBefore));
    }

    private BooleanExpression matchRating(QReview review, Integer rating) {
        if (rating == null) {
            return null;
        }
        GradeEnum gradeEnum = GradeEnum.getByValue(rating);
        if (gradeEnum == null) {
            return null;
        }
        return review.grade.eq(gradeEnum);
    }

    private BooleanExpression matchContent(QReview review, String content) {
        if (content == null) {
            return null;
        }
        return review.detail.eq(content);
    }

    private BooleanExpression matchCreatedAt(QReview review, LocalDateTime createdAt) {
        if (createdAt == null) {
            return null;
        }
        LocalDateTime startOfDay = LocalDateTime.of(createdAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return review.createdAt.between(startOfDay, endOfDay)
                .or(review.createdAt.eq(startOfDay));
    }

    private BooleanExpression matchUpdatedAt(QReview review, LocalDateTime updatedAt) {
        if (updatedAt == null) {
            return null;
        }
        LocalDateTime startOfDay = LocalDateTime.of(updatedAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return review.updatedAt.between(startOfDay, endOfDay)
                .or(review.updatedAt.eq(startOfDay));
    }
}
