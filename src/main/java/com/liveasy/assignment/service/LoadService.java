package com.liveasy.assignment.service;


import com.liveasy.assignment.entity.LoadStatus;
import com.liveasy.assignment.entity.Load;
import java.util.List;
import java.util.UUID;
// ... other imports


public interface LoadService {
    Load createLoad(Load load);
    List<Load> getLoads(String shipperId, String truckType /* add other filters */);
    Load getLoadById(UUID loadId);
    Load updateLoad(UUID loadId, Load loadDetails);
    void deleteLoad(UUID loadId);
    // Internal method (package-private or protected) to update status if needed by BookingService
    void updateLoadStatus(UUID loadId, LoadStatus status);
}