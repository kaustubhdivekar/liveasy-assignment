package com.liveasy.assignment.controller;

import com.liveasy.assignment.dto.LoadDto; // Create this DTO
import com.liveasy.assignment.entity.Load;
import com.liveasy.assignment.service.LoadService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper; // Or map manually
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/load") // Base path for load APIs
public class LoadController {

    @Autowired
    private LoadService loadService;

    @Autowired
    private ModelMapper modelMapper; // Bean configured elsewhere

    // POST /load → Create a new load
    @PostMapping
    public ResponseEntity<LoadDto> createLoad(@Valid @RequestBody LoadDto loadDto) {
        // Convert DTO to Entity
        Load loadRequest = modelMapper.map(loadDto, Load.class);
        Load createdLoad = loadService.createLoad(loadRequest);
        // Convert Entity to DTO
        LoadDto loadResponse = modelMapper.map(createdLoad, LoadDto.class);
        return new ResponseEntity<>(loadResponse, HttpStatus.CREATED);
    }

    // GET /load → Fetch loads (filter by shipperId, truckType, etc.)
    @GetMapping
    public ResponseEntity<List<LoadDto>> getLoads(
            @RequestParam(required = false) String shipperId,
            @RequestParam(required = false) String truckType /* Add other filters */) {
        List<Load> loads = loadService.getLoads(shipperId, truckType /* pass filters */);
        List<LoadDto> loadDtos = loads.stream()
                                     .map(load -> modelMapper.map(load, LoadDto.class))
                                     .collect(Collectors.toList());
        return ResponseEntity.ok(loadDtos);
    }

    // GET /load/{loadId} → Get load details
    @GetMapping("/{loadId}")
    public ResponseEntity<LoadDto> getLoadById(@PathVariable UUID loadId) {
        Load load = loadService.getLoadById(loadId);
        LoadDto loadDto = modelMapper.map(load, LoadDto.class);
        return ResponseEntity.ok(loadDto);
    }

    // PUT /load/{loadId} → Update load details
    @PutMapping("/{loadId}")
    public ResponseEntity<LoadDto> updateLoad(@PathVariable UUID loadId, @Valid @RequestBody LoadDto loadDto) {
        Load loadRequest = modelMapper.map(loadDto, Load.class);
        Load updatedLoad = loadService.updateLoad(loadId, loadRequest);
        LoadDto loadResponse = modelMapper.map(updatedLoad, LoadDto.class);
        return ResponseEntity.ok(loadResponse);
    }

    // DELETE /load/{loadId} → Delete a load
    @DeleteMapping("/{loadId}")
    public ResponseEntity<HttpStatus> deleteLoad(@PathVariable UUID loadId) {
        loadService.deleteLoad(loadId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Or HttpStatus.OK with a message
    }
}