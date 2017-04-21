package com.io2.service;

import com.io2.model.Brand;
import com.io2.model.Sneaker;

import java.util.List;
import java.util.Map;

/**
 * Created by Niki on 2017-04-19.
 */

interface CollectionCreatorService {
    List<Brand> getAllBrands();

    Map<Float, String> getSizes();

    Boolean addSneaker(Sneaker sneaker);
}