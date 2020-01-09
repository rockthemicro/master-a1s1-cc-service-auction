package com.service.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BidRepository extends CrudRepository<Bid, Integer> {

    List<Bid> findAllByUserNameEquals(String userName);
    List<Bid> findAllByAuctionItemIdEquals(Integer auctionItemId);
}
