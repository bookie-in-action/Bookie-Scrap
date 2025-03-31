package com.bookie.scrap.watcha.scheduler;


import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.db.redis.RedisConnectionProducer;
import com.bookie.scrap.common.db.redis.RedisSetManager;
import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseMetaDto;

import com.bookie.scrap.watcha.dto.WatchaBookcaseToBookDto;
import com.bookie.scrap.watcha.dto.WatchaCommentDto;
import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseToBookEntity;
import com.bookie.scrap.watcha.entity.WatchaCommentEntity;
import com.bookie.scrap.watcha.repository.WatchaBookMetaRepository;
import com.bookie.scrap.watcha.repository.WatchaBookToBookcaseMetasRepository;
import com.bookie.scrap.watcha.repository.WatchaBookcaseToBooksRepository;
import com.bookie.scrap.watcha.repository.WatchaCommentRepository;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.request.WatchaBookToBookcaseMetasRequestFactory;
import com.bookie.scrap.watcha.request.WatchaBookcaseToBooksRequestFactory;
import com.bookie.scrap.watcha.request.WatchaCommentRequestFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class WatchaJob implements Job {

    private static EntityManagerFactory emf;

    private final WatchaBookcaseToBooksRequestFactory     bookcaseToBookRequestFactory     = WatchaBookcaseToBooksRequestFactory.getInstance();
    private final WatchaBookToBookcaseMetasRequestFactory bookToBookcaseMetaRequestFactory = WatchaBookToBookcaseMetasRequestFactory.getInstance();
    private final WatchaBookMetaRequestFactory            bookMetaRequestFactory     = WatchaBookMetaRequestFactory.getInstance();
    private final WatchaCommentRequestFactory             commentRequestFactory     = WatchaCommentRequestFactory.getInstance();

    private final WatchaBookMetaRepository            bookMetaRepository     = WatchaBookMetaRepository.getInstance();
    private final WatchaBookToBookcaseMetasRepository bookToBookcaseMetasRepository = WatchaBookToBookcaseMetasRepository.getInstance();
    private final WatchaBookcaseToBooksRepository     bookcaseToBooksRepository = WatchaBookcaseToBooksRepository.getInstance();
    private final WatchaCommentRepository             commentRepository = WatchaCommentRepository.getInstance();

    private final RedisSetManager completeBookCodes     = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcode:complete");
    private final RedisSetManager undoneBookCodes       = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcode:undone");
    private final RedisSetManager completeBookcaseCodes = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcase:complete");
    private final RedisSetManager undoneBookcaseCodes   = new RedisSetManager(RedisConnectionProducer.getConn(), "bookcase:undone");

    private boolean isFirst = true;

    static {
        emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (isFirst && undoneBookCodes.size() == 0) {
            undoneBookCodes.addToSet("byLKj8M");
            isFirst = false;
        }

        while (undoneBookCodes.size() > 0) {
            processByBookCode(undoneBookCodes.get());
        }

        while (undoneBookcaseCodes.size() > 0) {
            processByBookcaseCode(undoneBookcaseCodes.get());
        }

        if (undoneBookCodes.size() > 0 || undoneBookcaseCodes.size() > 0) {
            this.execute();
        }

        close();
    }


    public void execute() throws JobExecutionException {

        if (isFirst && undoneBookCodes.size() == 0) {
            undoneBookCodes.addToSet("byLKj8M");
            isFirst = false;
        }

        while (undoneBookCodes.size() > 0) {
            processByBookCode(undoneBookCodes.get());
        }

        while (undoneBookcaseCodes.size() > 0) {
            processByBookcaseCode(undoneBookcaseCodes.get());
        }

        if (undoneBookCodes.size() > 0 || undoneBookcaseCodes.size() > 0) {
            this.execute();
        }

        close();
    }

    private void processByBookcaseCode(String bookcaseCode) {

        if (completeBookcaseCodes.isExist(bookcaseCode)) {
            return;
        }

        log.info("=> processByBookcaseCode bookcaseCode: {} process start", bookcaseCode);
        log.info("1. Insert Books In Bookcase");
        PageInfo bookPage = new PageInfo(1, 200);
        List<WatchaBookcaseToBookDto> books = new ArrayList<>();
        do {
            books = bookcaseToBookRequestFactory.createRequest(bookcaseCode, bookPage).execute();
            insertBooksInRedisAndDb(bookcaseCode, books);
            bookPage.nextPage();
        } while (!books.isEmpty());


    }

    private void processByBookCode(String bookCode) {

        if (completeBookCodes.isExist(bookCode)) {
            return;
        }

        log.info("=> processByBookCode bookCode: {} process start", bookCode);

        // book meta 저장
        log.info("1. Insert BookMeta");
        insertBookMetaInRedisAndDb(bookCode);

        // 코멘트 저장
        PageInfo commentPage = new WatchaRequestParam(1, 200, "", "");
        log.info("2. Insert book comment:{}", bookCode);
        List<WatchaCommentDto> commentDtos = new ArrayList<>();
        do {
            commentDtos = commentRequestFactory.createRequest(bookCode, commentPage).execute();
            insertCommentInDb(commentDtos);

            commentPage.nextPage();
        } while (!commentDtos.isEmpty());


        // bookcode -> bookcase 리스트 저장
        log.info("3. Insert BookcaseMetas that contain book:{}", bookCode);
        PageInfo bookcasePage = new PageInfo(1, 200);
        List<WatchaBookcaseMetaDto> bookcaseMetaDtos = new ArrayList<>();
        List<WatchaBookcaseMetaDto> sumBookcaseMetaDtos = new ArrayList<>();
        do {
            bookcaseMetaDtos = bookToBookcaseMetaRequestFactory.createRequest(bookCode, bookcasePage).execute();
            insertBookcaseMetasInRedisAndDb(bookCode, bookcaseMetaDtos);
            sumBookcaseMetaDtos.addAll(bookcaseMetaDtos);

            bookcasePage.nextPage();
        } while (!bookcaseMetaDtos.isEmpty());


        sumBookcaseMetaDtos.forEach(dto -> processByBookcaseCode(dto.getBookcaseCode()));

    }

    private void insertCommentInDb(List<WatchaCommentDto> commentDtos) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            List<WatchaCommentEntity> newCommentEntities = commentDtos.stream().map(WatchaCommentDto::toEntity).collect(Collectors.toList());
            commentRepository.insertOnlyNewComments(newCommentEntities, em);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...", e);
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback", e);
            }
        } finally {
            em.close();
        }
    }

    private void insertBookMetaInRedisAndDb(String bookCode) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            WatchaBookMetaDto bookMetaDto = bookMetaRequestFactory.createRequest(bookCode).execute();
            bookMetaRepository.insertOrUpdate(bookMetaDto.toEntity(), em);
            completeBookCodes.addToSet(bookCode);
            undoneBookCodes.deleteItem(bookCode);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...", e);
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback", e);
            }
        } finally {
            em.close();
        }
    }

    private void insertBookcaseMetasInRedisAndDb(String bookCode, List<WatchaBookcaseMetaDto> bookcaseMetaDtos) {

        if (bookcaseMetaDtos.isEmpty()) {
            return;
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            List<WatchaBookToBookcaseMetaEntity> bookcaseMetas = bookcaseMetaDtos.stream().map(WatchaBookcaseMetaDto::toEntity).collect(Collectors.toList());
            bookToBookcaseMetasRepository.insertOrUpdate(bookCode, bookcaseMetas, em);

            List<String> bookcaseCodes = bookcaseMetaDtos.stream().map(WatchaBookcaseMetaDto::getBookcaseCode).collect(Collectors.toList());

            undoneBookcaseCodes.addToSet(bookcaseCodes);
            completeBookcaseCodes.deleteItem(bookcaseCodes);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...", e);
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback", e);
            }
        } finally {
            em.close();
        }
    }

    private void insertBooksInRedisAndDb(String bookcaseCode, List<WatchaBookcaseToBookDto> bookDtos) {

        if (bookDtos.isEmpty()) {
            return;
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            List<WatchaBookcaseToBookEntity> books = bookDtos.stream().map(WatchaBookcaseToBookDto::toEntity).collect(Collectors.toList());

            bookcaseToBooksRepository.insertOrUpdate(bookcaseCode, books, em);

            List<String> bookCodes = bookDtos.stream().map(WatchaBookcaseToBookDto::getBookCode).collect(Collectors.toList());
            undoneBookCodes.addToSet(bookCodes);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...", e);
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback", e);
            }
        } finally {
            em.close();
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
