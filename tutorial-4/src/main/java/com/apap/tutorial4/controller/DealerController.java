package com.apap.tutorial4.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.apap.tutorial4.model.*;
import com.apap.tutorial4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DealerController {

	@Autowired
	private DealerService dealerService;
	
	@Autowired
	private CarService carService;
	
	@RequestMapping("/")
	private String home() {
		return "home";
	}
	
	@RequestMapping(value = "/dealer/add" , method = RequestMethod.GET)
	private String add (Model model) {
		model.addAttribute("dealer", new DealerModel());
		return "addDealer";
	}
	
	@RequestMapping(value = "/dealer/add" , method = RequestMethod.POST)
	private String addDealerSubmit(@ModelAttribute DealerModel dealer) {
		dealerService.addDealer(dealer);
		return "add";
	}
	
	@RequestMapping(value = "/dealer/view", method = RequestMethod.GET)
	private String view(String dealerId, Model model) {
		DealerModel dealer = null;
		List<CarModel> listCar = null;
		if(dealerService.getDealerDetailById(Long.parseLong(dealerId)).isPresent()) {
			dealer = dealerService.getDealerDetailById(Long.parseLong(dealerId)).get();
			listCar = dealer.getListCar();
			Collections.sort(listCar, priceComparator);
		}
		model.addAttribute("listCar", listCar);
		model.addAttribute("deal", dealer);
		model.addAttribute("dealId", dealerId);
		return "viewDealer";
	}
	
	@RequestMapping(value = "/dealer/delete", method =RequestMethod.GET)
	private String delete(String dealerId, Model model) {
		if(dealerService.getDealerDetailById(Long.parseLong(dealerId)).isPresent()) {
			DealerModel dealer = dealerService.getDealerDetailById(Long.parseLong(dealerId)).get();
			if (dealer.getListCar().isEmpty()) {
				dealerService.deleteDealer(dealer);
				return "delete";
			}
			else {
				List<CarModel> listCar = dealer.getListCar();
				for (CarModel car : listCar) {
					carService.deleteCar(car);
					dealerService.deleteDealer(dealer);
					return "delete";
				}
			}
		}
		return "error";
	}
	
	@RequestMapping(value="/dealer/update/{dealerId}", method = RequestMethod.GET)
	private String updateDealer(@PathVariable(value="dealerId") Long dealerId, Model model) {
		DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
		model.addAttribute("deal", dealer);
		return "update-dealer";
	}
	
	@RequestMapping(value="/dealer/update/{dealerId}", method = RequestMethod.POST)
	private String update(@PathVariable(value="dealerId") Long dealerId, @ModelAttribute Optional<DealerModel> deal) {
		dealerService.updateDealer(deal, dealerId);
		return "update";
	}
	
	@RequestMapping(value="/dealer/view-all", method= RequestMethod.GET)
	private String viewAllCar(Model model) {
		List<DealerModel> listDealer = dealerService.getAllDealer();
		
		model.addAttribute("listDealer", listDealer);
		return "viewAll";
	}

	CarModel car;
	public static Comparator<CarModel> priceComparator = new Comparator<CarModel>() {
		public int compare(CarModel o1, CarModel o2) {
			Long price1 = o1.getPrice();
			Long price2 = o2.getPrice();
			return price1.compareTo(price2);
		}
	};
}