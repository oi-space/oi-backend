package com.pser.hotel.domain.sample.dao;

import com.pser.hotel.domain.sample.domain.QSample;
import com.pser.hotel.domain.sample.domain.Sample;
import com.pser.hotel.domain.sample.dto.SampleSearchRequest;
import com.pser.hotel.global.common.request.SearchQuery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class SampleDaoImpl implements SampleDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Sample> search(SampleSearchRequest request, SearchQuery searchQuery, Pageable pageable) {
        QSample sample = QSample.sample;
        List<Sample> samples = queryFactory
                .selectFrom(sample)
                .where(buildSearchCondition(searchQuery), buildRequestCondition(request))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        Long count = queryFactory
                .select(sample.count())
                .from(sample)
                .where(buildSearchCondition(searchQuery), buildRequestCondition(request))
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return new PageImpl<>(samples, pageable, count);
    }

    private BooleanBuilder buildSearchCondition(SearchQuery searchQuery) {
        return new BooleanBuilder()
                .and(matchKeyword(searchQuery.getKeyword()))
                .and(matchCreatedAfter(searchQuery.getCreatedAfter()))
                .and(matchCreatedBefore(searchQuery.getCreatedBefore()))
                .and(matchUpdatedAfter(searchQuery.getUpdatedAfter()))
                .and(matchUpdatedBefore(searchQuery.getUpdatedBefore()));
    }

    private BooleanBuilder buildRequestCondition(SampleSearchRequest request) {
        return new BooleanBuilder()
                .and(matchTitle(request.getTitle()))
                .and(matchContent(request.getContent()))
                .and(matchCreatedAt(request.getCreatedAt()))
                .and(matchUpdatedAt(request.getUpdatedAt()));
    }

    private BooleanExpression matchKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        QSample sample = QSample.sample;
        return sample.title.containsIgnoreCase(keyword)
                .or(sample.content.containsIgnoreCase(keyword));
    }

    private BooleanExpression matchCreatedAfter(LocalDateTime createdAfter) {
        if (createdAfter == null) {
            return null;
        }
        QSample sample = QSample.sample;
        return sample.createdAt.after(createdAfter)
                .or(sample.createdAt.eq(createdAfter));
    }

    private BooleanExpression matchCreatedBefore(LocalDateTime createdBefore) {
        if (createdBefore == null) {
            return null;
        }
        QSample sample = QSample.sample;
        return sample.createdAt.before(createdBefore)
                .or(sample.createdAt.eq(createdBefore));
    }

    private BooleanExpression matchUpdatedAfter(LocalDateTime updatedAfter) {
        if (updatedAfter == null) {
            return null;
        }
        QSample sample = QSample.sample;
        return sample.updatedAt.after(updatedAfter)
                .or(sample.updatedAt.eq(updatedAfter));
    }

    private BooleanExpression matchUpdatedBefore(LocalDateTime updatedBefore) {
        if (updatedBefore == null) {
            return null;
        }
        QSample sample = QSample.sample;
        return sample.updatedAt.before(updatedBefore)
                .or(sample.updatedAt.eq(updatedBefore));
    }

    private BooleanExpression matchTitle(String title) {
        if (title == null) {
            return null;
        }
        QSample sample = QSample.sample;
        return sample.title.eq(title);
    }

    private BooleanExpression matchContent(String content) {
        if (content == null) {
            return null;
        }
        QSample sample = QSample.sample;
        return sample.content.eq(content);
    }

    private BooleanExpression matchCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            return null;
        }
        QSample sample = QSample.sample;
        LocalDateTime start = LocalDateTime.of(createdAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = start.plusDays(1);
        return sample.createdAt.between(start, end)
                .or(sample.createdAt.eq(start));
    }

    private BooleanExpression matchUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            return null;
        }
        QSample sample = QSample.sample;
        LocalDateTime start = LocalDateTime.of(updatedAt.toLocalDate(), LocalTime.MIN);
        LocalDateTime end = start.plusDays(1);
        return sample.updatedAt.between(start, end)
                .or(sample.updatedAt.eq(start));
    }
}
