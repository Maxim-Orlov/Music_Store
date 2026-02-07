package com.max_orlov.music_albums_store.repository.dao;

import com.max_orlov.music_albums_store.model.Album;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AlbumDao {

    private static final String ORDER_BY_RATING = "a.currentRating";
    private static final String SELECT_BY_YEAR = "releaseYear";
    private static final String SELECT_BY_BAND_NAME = "bandName";
    private static final String HQL_QUERY = "from Album as a where a.releaseYear like :releaseYear " +
            "and a.band.bandName like :bandName order by %s";
    private static final String DESCEND = " desc";

    private final SessionFactory sessionFactory;

    public AlbumDao(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Transactional
    public List<Album> findAlbumByYearAndBand(String releaseYear, String bandName, String orderBy) {
        StringBuilder hql = new StringBuilder(String.format(HQL_QUERY, orderBy));
        if (orderBy.equals(ORDER_BY_RATING)) {
            hql.append(DESCEND);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<Album> query = session.createQuery(hql.toString(), Album.class);
        query.setParameter(SELECT_BY_YEAR, releaseYear);
        query.setParameter(SELECT_BY_BAND_NAME, bandName);
        List<Album> albumList = query.list();
        session.flush();
        session.clear();
        session.close();
        transaction.commit();
        return albumList;
    }

}
