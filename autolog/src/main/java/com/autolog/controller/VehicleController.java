package com.autolog.controller;

import com.autolog.model.User;
import com.autolog.model.Vehicle;
import com.autolog.service.UserService;
import com.autolog.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/vehicles")
@SessionAttributes({ "user" })
public class VehicleController {

	private final VehicleService vehicleService;
	private final UserService userService;

	public VehicleController(VehicleService vehicleService, UserService userService) {
		this.vehicleService = vehicleService;
		this.userService = userService;
	}

	private User getUser(Model model, Principal principal) {
		User user = (User) model.getAttribute("user");
		if (user == null) {
			user = userService.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}
		return user;
	}

	@GetMapping
	public String list(Model model, Principal principal) {
		User user = getUser(model, principal);
		model.addAttribute("vehicles", vehicleService.getUserVehicles(user.getId()));
		return "vehicles/list";
	}

	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("vehicle", new Vehicle());
		return "vehicles/form";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model, Principal principal) {
		User user = getUser(model, principal);
		Vehicle vehicle = vehicleService.getVehicleByIdForUser(id, user.getId());
		model.addAttribute("vehicle", vehicle);
		return "vehicles/form";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute() Vehicle vehicle,
	                   BindingResult result,
	                   Principal principal,
	                   Model model) {

	    User user = getUser(model, principal);

	    if (result.hasErrors()) {
	        model.addAttribute("vehicle", vehicle);
	        return "vehicles/form";
	    }

	    vehicleService.save(vehicle, user);
	    return "redirect:/vehicles";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, Principal principal, Model model) {
		User user = getUser(model, principal);
		vehicleService.delete(id, user.getId());
		return "redirect:/vehicles";
	}
}
