package com.ef.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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

	public static List<LogFile> searchInLog(String startDateParam, String duration, int threshold) throws ParseException {

		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = dtf.parse(startDateParam);
		LocalDateTime startDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

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

		Expression<String> groupByExp = root.get("ip").as(String.class);
		Expression<Long> countExp = builder.count(groupByExp);

		CriteriaQuery<LogFile> select = criteria.multiselect(groupByExp, countExp);
		criteria.groupBy(groupByExp);
		criteria.having(builder.gt(builder.count(root), threshold));

		TypedQuery<LogFile> query = em.createQuery(select);
		return query.getResultList();
	}

	private static EntityManager createEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
		return emf.createEntityManager();
	}
}
