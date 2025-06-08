package com.bookie.scrap.common.springconfig;

import com.bookie.scrap.watcha.request.book.bookmeta.rdb.BookMetaRdbRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.bookie.scrap", // JPA Repo 포함된 상위 패키지
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        BookMetaRdbRepository.class,
                }
        )
)
public class JpaConfig {
}
