package com.autolog.controller;

import com.autolog.model.Operation;
import com.autolog.model.User;
import com.autolog.model.Vehicle;
import com.autolog.service.OperationService;
import com.autolog.service.UserService;
import com.autolog.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/operations")
@SessionAttributes("user")
public class OperationController {

    private final OperationService operationService;
    private final VehicleService vehicleService;
    private final UserService userService;

    public OperationController(OperationService operationService,
                               VehicleService vehicleService,
                               UserService userService) {
        this.operationService = operationService;
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

    @GetMapping({"/new", "/edit/{id}"})
    public String form(@PathVariable(required = false) Integer id,
                       @RequestParam(required = false) Integer vehicleId,
                       Model model,
                       Principal principal) {

        User user = getUser(model, principal);

        Operation operation;
        if (id != null) {
            operation = operationService.getOperationForUser(id, user.getId());
            model.addAttribute("fixedVehicle", true); 
        } else {
            operation = new Operation();
            if (vehicleId != null) {
                Vehicle vehicle = vehicleService.getVehicleByIdForUser(vehicleId, user.getId());
                operation.setVehicle(vehicle);
                model.addAttribute("fixedVehicle", true);
            } else {
                model.addAttribute("fixedVehicle", false);
            }
        }

        model.addAttribute("operation", operation);

        List<Vehicle> vehicles = vehicleService.getUserVehicles(user.getId());
        model.addAttribute("vehicles", vehicles);

        return "operations/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute() Operation operation,
                       BindingResult result,
                       @RequestParam(required = false) Integer vehicleId,
                       Model model,
                       Principal principal) {

        User user = getUser(model, principal);

        if (vehicleId != null) {
            Vehicle vehicle = vehicleService.getVehicleByIdForUser(vehicleId, user.getId());
            operation.setVehicle(vehicle);
        }

        if (result.hasErrors()) {
            model.addAttribute("vehicles", vehicleService.getUserVehicles(user.getId()));
            return "operations/form";
        }

        operationService.save(operation, operation.getVehicle().getId(), user.getId());
        return "redirect:/operations";
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        User user = getUser(model, principal);
        model.addAttribute("operations", operationService.getOperationsForUser(user.getId()));
        return "operations/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model, Principal principal) {
        User user = getUser(model, principal);
        operationService.delete(id, user.getId());
        return "redirect:/operations";
    }
}
