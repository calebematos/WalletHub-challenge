package com.ef.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ef.model.Duration;
import com.ef.model.LogFile;

public class DataBaseService {

	public static void saveLog(LogFile log) {

		EntityManager em = createEntityManager();
		em.getTransaction().begin();
		em.persist(log);
		em.getTransaction().commit();
		em.close();
	}

	public static List<LogFile> searchInLog(LocalDateTime startDate, String duration, int threshold) {

		EntityManager em = createEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<LogFile> criteria = builder.createQuery(LogFile.class);
		Root<LogFile> root = criteria.from(LogFile.class);

		criteria.select(root);

		List<Predicate> predicates = new ArrayList<>();

		if (startDate != null) {
			LocalDateTime endDate = startDate;
			if (Duration.DAILY.getDesc().equals(duration))
				endDate = startDate.plusHours(24);
			if (Duration.HOURLY.getDesc().equals(duration))
				endDate = startDate.plusHours(1);

			predicates.add(builder.between(root.<LocalDateTime>get("date"), startDate, endDate));

		}
		criteria.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<LogFile> query = em.createQuery(criteria);
		return query.getResultList();
	}

	private static EntityManager createEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
		return emf.createEntityManager();
	}
}
