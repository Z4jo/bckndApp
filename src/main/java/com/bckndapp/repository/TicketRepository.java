package com.bckndapp.repository;

import com.bckndapp.entity.Projection;
import com.bckndapp.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

import javax.sql.rowset.CachedRowSet;
import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
	List<Ticket> findAllByProjection(Projection projection);

}
