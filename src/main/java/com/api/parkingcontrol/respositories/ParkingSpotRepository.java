package com.api.parkingcontrol.respositories;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> {


    boolean existsByApartmentAndBlock(String apartment, String block);

    boolean existsByParkingSpotNumber(String parkingSpotNumber);

    boolean existsByLicensePlateCar(String licencePlateCar);
}
