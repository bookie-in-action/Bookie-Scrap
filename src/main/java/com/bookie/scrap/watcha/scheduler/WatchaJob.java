package com.bookie.scrap.watcha.scheduler;


import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.db.redis.RedisConnectionProducer;
import com.bookie.scrap.common.db.redis.RedisSetManager;
import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.dto.WatchaBookcaseMetaDto;
import com.bookie.scrap.watcha.dto.WatchaBookcaseToBookDTO;

import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseToBookEntity;
import com.bookie.scrap.watcha.repository.WatchaBookMetaRepository;
import com.bookie.scrap.watcha.repository.WatchaBookToBookcaseMetasRepository;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.request.WatchaBookToBookcaseMetasRequestFactory;
import com.bookie.scrap.watcha.request.WatchaBookcaseToBooksRequestFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class WatchaJob implements Job {

    private static EntityManagerFactory emf;

    private final WatchaBookcaseToBooksRequestFactory bookcaseToBookRequestFactory     = WatchaBookcaseToBooksRequestFactory.getInstance();
    private final WatchaBookToBookcaseMetasRequestFactory bookTobookcaseMetaRequestFactory = WatchaBookToBookcaseMetasRequestFactory.getInstance();
    private final WatchaBookMetaRequestFactory     bookMetaRequestFactory     = WatchaBookMetaRequestFactory.getInstance();
//    private final WatchaCommentRequestFactory           commentRequestFactory     = WatchaCommentRequestFactory.getInstance();

    private final WatchaBookMetaRepository     bookMetaRepository     = WatchaBookMetaRepository.getInstance();
    private final WatchaBookToBookcaseMetasRepository bookToBookcaseMetaRepository = WatchaBookToBookcaseMetasRepository.getInstance();
//    private final WatchaBookcaseMetaRepository bookcaseRepository = WatchaBookcaseRepository.getInstance();
//    private final WatchaCommentRepository commentRepository = WatchaCommentRepository.getInstance();

    private final RedisSetManager completeBookCodes     = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcode:complete");
    private final RedisSetManager undoneBookCodes       = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcode:undone");
    private final RedisSetManager completeBookcaseCodes = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcase:complete");
    private final RedisSetManager undoneBookcaseCodes   = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcase:undone");

    static {
        emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (undoneBookCodes.size() == 0) {
            undoneBookCodes.addToSet("byLKj8M");
        }

        while (undoneBookCodes.size() > 0) {
            processByBookCode(undoneBookCodes.get());
        }

        while (undoneBookcaseCodes.size() > 0) {
            processByBookcaseCode(undoneBookcaseCodes.get());
        }

        close();
    }

    public void execute() throws JobExecutionException {

        if (undoneBookCodes.size() == 0) {
            undoneBookCodes.addToSet("byLKj8M");
        }

        while (undoneBookCodes.size() > 0) {
            processByBookCode(undoneBookCodes.get());
        }

        while (undoneBookcaseCodes.size() > 0) {
            processByBookcaseCode(undoneBookcaseCodes.get());
        }

        close();
    }

    private void processByBookcaseCode(String bookcaseCode) {

        if (completeBookcaseCodes.isExist(bookcaseCode)) {
            return;
        }

        log.debug("bookcaseCode: {} process start", bookcaseCode);

        PageInfo bookPage = new PageInfo(1, 20);
        List<WatchaBookcaseToBookDTO> bookCodeDtos = Collections.emptyList();
        do {
            bookCodeDtos = bookcaseToBookRequestFactory.createRequest(bookcaseCode, bookPage).execute();
            insertBookcaseInRedisAndDb(bookCodeDtos);
            bookPage.nextPage();
        } while (!bookCodeDtos.isEmpty());
    }

    private void processByBookCode(String bookCode) {

        if (completeBookCodes.isExist(bookCode)) {
            return;
        }

        log.debug("bookcode: {} process start", bookCode);

        // book meta 저장
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            WatchaBookMetaDto bookMetaDto = bookMetaRequestFactory.createRequest(bookCode).execute();
            bookMetaRepository.insertOrUpdate(bookMetaDto.toEntity(), em);
            completeBookCodes.addToSet(bookCode);
            undoneBookCodes.deleteItem(bookCode);
            em.getTransaction().commit();
        }

        // 코멘트 저장
        PageInfo commentInfo = new PageInfo(1, 12);


        // bookcode -> bookcase 리스트 저장
        PageInfo bookcasePage = new PageInfo(1, 20);
        List<WatchaBookcaseMetaDto> bookcaseMetaDtos = Collections.emptyList();
        do {
            bookcaseMetaDtos = bookTobookcaseMetaRequestFactory.createRequest(bookCode, bookcasePage).execute();
            insertBookcaseMetasInRedisAndDb(bookCode, bookcaseMetaDtos);
            bookcasePage.nextPage();

            processByBookcaseCode(undoneBookcaseCodes.get());
        } while (!bookcaseMetaDtos.isEmpty());

    }

    private void insertBookcaseMetasInRedisAndDb(String bookCode, List<WatchaBookcaseMetaDto> bookcaseMetaDtos) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<WatchaBookToBookcaseMetaEntity> bookcaseMetas = bookcaseMetaDtos.stream().map(WatchaBookcaseMetaDto::toEntity).collect(Collectors.toList());
            bookToBookcaseMetaRepository.insertOrUpdate(bookCode, bookcaseMetas, em);

            List<String> bookcaseCodes = bookcaseMetaDtos.stream().map(WatchaBookcaseMetaDto::getBookcaseCode).collect(Collectors.toList());

            undoneBookcaseCodes.addToSet(bookcaseCodes);
            completeBookcaseCodes.deleteItem(bookcaseCodes);
            em.getTransaction().commit();
        }
    }

    private void insertBookcaseInRedisAndDb(List<WatchaBookcaseToBookDTO> bookCodeDtos) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<WatchaBookcaseToBookEntity> bookcase = bookCodeDtos.stream().map(WatchaBookcaseToBookDTO::toEntity).collect(Collectors.toList());
//            bookcaseRepository.insertOrUpdate(bookcase, em);

            List<String> bookCodes = bookCodeDtos.stream().map(WatchaBookcaseToBookDTO::getBookCode).collect(Collectors.toList());
            undoneBookCodes.addToSet(bookCodes);
            em.getTransaction().commit();
        }
    }

    /**
     * redis 커넥션 정리
     */
    private void close() {
        completeBookCodes.close();
        completeBookcaseCodes.close();
        undoneBookcaseCodes.close();
        undoneBookCodes.close();
    }
}
