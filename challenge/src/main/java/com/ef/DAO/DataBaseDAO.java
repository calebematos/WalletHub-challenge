package com.ef.DAO;

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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ef.model.Duration;
import com.ef.model.LogFile;

public class DataBaseDAO {

	public static void saveLog(LogFile log, EntityManager em) {

		em.getTransaction().begin();
		em.persist(log);
		em.getTransaction().commit();
		
	}

	public static List<LogFile> searchInLog(String startDateParam, String duration, int threshold)
			throws ParseException {

		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
		Date date = dtf.parse(startDateParam);
		LocalDateTime startDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		EntityManager em = createEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<LogFile> criteria = builder.createQuery(LogFile.class);
		Root<LogFile> root = criteria.from(LogFile.class);

		criteria.select(root);

		Predicate[] predicates = getPredicates(builder, root, startDate, duration);

		criteria.where(predicates);
		criteria.groupBy(root.get("ip"));
		criteria.having(builder.gt(builder.count(root), threshold));

		TypedQuery<LogFile> query = em.createQuery(criteria);
		return query.getResultList();
	}

	private static Predicate[] getPredicates(CriteriaBuilder builder, Root<LogFile> root, LocalDateTime startDate,
			String duration) {
		List<Predicate> predicates = new ArrayList<>();
		if (startDate != null) {
			LocalDateTime endDate = startDate;
			if (Duration.DAILY.getDesc().equals(duration))
				endDate = startDate.plusHours(24);
			if (Duration.HOURLY.getDesc().equals(duration))
				endDate = startDate.plusHours(1);

			predicates.add(builder.between(root.get("date"), startDate, endDate));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	public static EntityManager createEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
		return emf.createEntityManager();
	}
}
