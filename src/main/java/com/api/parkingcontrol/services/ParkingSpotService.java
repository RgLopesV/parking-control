package com.api.parkingcontrol.services;


import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.respositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    @Autowired
    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository){
        this.parkingSpotRepository = parkingSpotRepository;
    }


    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
         return parkingSpotRepository.save(parkingSpotModel);
    }
    public Optional<ParkingSpotModel> find(ParkingSpotModel parkingSpotModel){
        Optional optional = parkingSpotRepository.findById(parkingSpotModel.getId());
        return optional;
    }
    @Transactional
    public void delete(ParkingSpotModel parkingSpotModel){
        parkingSpotRepository.delete(parkingSpotModel);
    }



    public boolean existByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment,block);
    }

    public boolean existByParkingSpotNumber(String parkingSpotNumber) {
        return  parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existByLicensePlateCar(String licencePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licencePlateCar);
    }

    public List<ParkingSpotModel> findAll() {
        return parkingSpotRepository.findAll();
    }

    public Optional<ParkingSpotModel> findById(UUID id) {
        return parkingSpotRepository.findById(id);
    }
}