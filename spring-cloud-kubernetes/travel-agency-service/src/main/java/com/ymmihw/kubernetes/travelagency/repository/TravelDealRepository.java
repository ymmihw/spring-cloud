package com.ymmihw.kubernetes.travelagency.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ymmihw.kubernetes.travelagency.model.TravelDeal;

public interface TravelDealRepository extends MongoRepository<TravelDeal, String> {

    public List<TravelDeal> findByDestination(String destination);

}