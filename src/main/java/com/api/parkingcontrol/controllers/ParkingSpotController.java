package com.api.parkingcontrol.controllers;


import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import net.bytebuddy.TypeCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*" , maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    @Autowired
    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){

        if(parkingSpotService.existByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Lincense Plate Car is Already un use!@");
        }
        if(parkingSpotService.existByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is Already un use!");
        }
        if(parkingSpotService.existByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is Already un use!");
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0 , size = 10,sort ="id",direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional =  parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot nof found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional <ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id , @RequestBody @Valid ParkingSpotDto parkingSpotDto){

        //segue abaixo duas formas de fazer a atualização
        //        var parkingSpotModel = parkingSpotService.findById(id);
//        if(!parkingSpotModel.isPresent()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found!");
//        }
//        BeanUtils.copyProperties(parkingSpotDto,parkingSpotModel);
//        parkingSpotModel.get().setAlterationDate(LocalDateTime.now(ZoneId.of("UTC")));
//        parkingSpotService.save(parkingSpotModel.get());
//        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel.get());

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found!");
        }
        var parkingSpotModel = parkingSpotModelOptional.get();
        parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
        parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
        parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
        parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
        parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
        parkingSpotModel.setApartment(parkingSpotDto.getApartment());
        parkingSpotModel.setBlock(parkingSpotDto.getBlock());
        parkingSpotModel.setResponsibleName(parkingSpotDto.getResponsibleName());
        parkingSpotService.save(parkingSpotModel);

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel);
    }

}
