package es.uniovi.eii.paquetor.controllers;

import es.uniovi.eii.paquetor.entities.User;
import es.uniovi.eii.paquetor.entities.locations.City;
import es.uniovi.eii.paquetor.entities.locations.Location;
import es.uniovi.eii.paquetor.repositories.CitiesRepository;
import es.uniovi.eii.paquetor.repositories.LocationsRepository;
import es.uniovi.eii.paquetor.services.ParcelsService;
import es.uniovi.eii.paquetor.services.RolesService;
import es.uniovi.eii.paquetor.services.SecurityService;
import es.uniovi.eii.paquetor.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    @Autowired
    UsersService usersService;

    @Autowired
    SecurityService securityService;

    @Autowired
    RolesService rolesService;

    @Autowired
    ParcelsService parcelsService;

    @Autowired
    LocationsRepository locationsRepository;

    @Autowired
    CitiesRepository citiesRepository;

    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping(value = "/register")
    public String signup(@ModelAttribute("user") User user, Model model) {
        user.setRole(rolesService.getRoles()[0]);
        Location userLocation = user.getLocation();
        City userCity = userLocation.getCity();

        try {
            citiesRepository.save(userCity);
        } catch (Exception exc) {}

        locationsRepository.save(userLocation.setCity(userCity));
        user.setLocation(userLocation);
        usersService.addCustomer(user);
        securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
        return "redirect:home";
    }

    @GetMapping("/home")
    public String userHome(Model model) {
        User currentUser = usersService.getLoggedInUser();
        if (currentUser.getRole().equals(rolesService.getRoles()[0])) { // Customer
            model.addAttribute("sentParcels", parcelsService.getCustomerSentParcels(currentUser));
            model.addAttribute("receivedParcels", parcelsService.getCustomerReceivedParcels(currentUser));
            return "home";

        } else {
            return "redirect:/employee/home";
        }
    }
}