package com.service.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuctionItemRepository extends CrudRepository<AuctionItem, Integer> {

    List<AuctionItem> findAllByStatusEquals(String status);
}
