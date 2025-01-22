package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.EntityManagerFactoryProvider;
import com.bookie.scrap.watcha.dto.WatchaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WatchaHibenateImpl<T extends WatchaEntity> implements WatchaRepository<T> {

    private static final Map<Class<? extends WatchaEntity>, WatchaHibenateImpl<?>> INSTANCES = new ConcurrentHashMap<>();
    private final EntityManagerFactory emf;

    private final Class<T> entityClass;

    private WatchaHibenateImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    @SuppressWarnings("unchecked")
    public static <T extends WatchaEntity> WatchaHibenateImpl<T> getInstance(Class<T> entityClass) {
        return (WatchaHibenateImpl<T>) INSTANCES.computeIfAbsent(entityClass, WatchaHibenateImpl::new);
    }

    @Override
    public Optional<T> select(String pk) {
        try (EntityManager em = emf.createEntityManager()) {
            T entity = em.find(this.entityClass, pk);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            emf.close();
        }
    }

    @Override
    public boolean insertOrUpdate(T targetEntity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            T existingEntity = em.find(this.entityClass, targetEntity.getSnowflakeId());

            if (existingEntity != null) {
                em.merge(targetEntity);
                log.info("update: {}", targetEntity);
            } else {
                em.persist(targetEntity);
                log.info("insert: {}", targetEntity);
            }

            em.getTransaction().commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }
}
