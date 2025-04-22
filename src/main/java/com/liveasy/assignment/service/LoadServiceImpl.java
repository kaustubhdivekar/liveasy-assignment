package com.liveasy.assignment.service;

import com.liveasy.assignment.entity.Load;
import com.liveasy.assignment.entity.LoadStatus;
import com.liveasy.assignment.exception.ResourceNotFoundException;
import com.liveasy.assignment.repository.LoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// ... other imports

import java.util.List;
import java.util.UUID;

@Service
public class LoadServiceImpl implements LoadService {

    @Autowired
    private LoadRepository loadRepository;
    // Potentially inject BookingRepository if deleteLoad needs to handle related bookings

    @Override
    @Transactional // Ensure atomicity
    public Load createLoad(Load load) {
        load.setStatus(LoadStatus.POSTED); // Ensure default status
        // Add any other validation or logic before saving
        return loadRepository.save(load);
    }

    @Override
    public List<Load> getLoads(String shipperId, String truckType /*...*/) {
        // Implement filtering logic using repository methods or Specifications
        if (shipperId != null && truckType != null) {
            return loadRepository.findByShipperIdAndTruckType(shipperId, truckType);
        } else if (shipperId != null) {
            return loadRepository.findByShipperId(shipperId);
        } else if (truckType != null) {
            return loadRepository.findByTruckType(truckType);
        } else {
            return loadRepository.findAll();
        }
        // Consider using Spring Data JPA Specifications for more complex/dynamic filtering
    }

    @Override
    public Load getLoadById(UUID loadId) {
        return loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", loadId));
    }

    @Override
    @Transactional
    public Load updateLoad(UUID loadId, Load loadDetails) {
        Load existingLoad = getLoadById(loadId);
        // Update fields - Be careful not to overwrite status or datePosted unintentionally
        existingLoad.setShipperId(loadDetails.getShipperId());
        existingLoad.setFacility(loadDetails.getFacility());
        existingLoad.setProductType(loadDetails.getProductType());
        existingLoad.setTruckType(loadDetails.getTruckType());
        existingLoad.setNoOfTrucks(loadDetails.getNoOfTrucks());
        existingLoad.setWeight(loadDetails.getWeight());
        existingLoad.setComment(loadDetails.getComment());
        // Add validation if necessary (e.g., cannot update a CANCELLED load?)
        return loadRepository.save(existingLoad);
    }

    @Override
    @Transactional
    public void deleteLoad(UUID loadId) {
        Load load = getLoadById(loadId);
        // Business Rule: Should deleting a load also delete associated bookings?
        // Or should it only be allowed if there are no non-rejected bookings?
        // Or should it just cancel the load? The requirements don't specify this,
        // but deleting related data is common. Let's assume simple deletion for now.
        // If related bookings should be handled, inject BookingRepository and delete them first.
        loadRepository.delete(load);
        // Alternative: Mark as cancelled instead of hard delete?
        // load.setStatus(LoadStatus.CANCELLED);
        // loadRepository.save(load);
    }

     @Override
     @Transactional
     public void updateLoadStatus(UUID loadId, LoadStatus status) {
         Load load = getLoadById(loadId);
         load.setStatus(status);
         loadRepository.save(load);
     }
}