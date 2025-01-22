package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.EntityManagerFactoryProvider;
import com.bookie.scrap.watcha.dto.WatchaBookEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class WatchaBookRepository implements WatchaRepository<WatchaBookEntity> {

    private static final WatchaBookRepository INSTANCE = new WatchaBookRepository();
    private final EntityManagerFactory emf;

    private WatchaBookRepository() {
        this.emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    public static WatchaBookRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<WatchaBookEntity> select(String code) {
        try (EntityManager em = emf.createEntityManager()) {
            WatchaBookEntity entity = em.find(WatchaBookEntity.class, code);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            log.error("An error occurred while selecting an entity with code: {}", code, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean insertOrUpdate(WatchaBookEntity targetEntity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            WatchaBookEntity existingEntity = em.createQuery(
                            "SELECT e FROM WatchaBookEntity e WHERE e.bookCode = :bookCode", WatchaBookEntity.class)
                    .setParameter("bookCode", targetEntity.getBookCode())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingEntity != null) {
                // 기존 엔티티가 존재하면 업데이트
                existingEntity.setBookTitle(targetEntity.getBookTitle());
                existingEntity.setBookSubtitle(targetEntity.getBookSubtitle());
                // 필요한 필드만 업데이트
                log.info("update: {}", existingEntity);
            } else {
                em.persist(targetEntity);
                log.info("insert: {}", targetEntity);
            }


            em.getTransaction().commit();

            return true;
        } catch (Exception e) {
            log.error("An error occurred while inserting or updating an entity: {}", targetEntity, e);
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...");
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback");
            }
            return false;
        } finally {
            em.close();
        }
    }
}
