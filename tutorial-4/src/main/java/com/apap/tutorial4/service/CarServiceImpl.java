package com.apap.tutorial4.service;

import java.util.Optional;
import com.apap.tutorial4.model.CarModel;
import com.apap.tutorial4.repository.CarDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public class CarServiceImpl implements CarService{
	
	@Autowired
	private CarDb carDb;
	
	@Override
	public void addCar(CarModel car) {
		carDb.save(car);
		
	}
	
	@Override
	public void deleteCar(CarModel car) {
		carDb.delete(car);
		
	}
	
	@Override
	public Optional<CarModel> getCarDetailById(Long id){
		return carDb.findById(id);
	}
}
