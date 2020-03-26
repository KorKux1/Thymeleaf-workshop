package co.edu.icesi.ci.thymeval.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.icesi.ci.thymeval.model.User;
import co.edu.icesi.ci.thymeval.model.User.firstValidator;
import co.edu.icesi.ci.thymeval.model.User.secondValidator;
import co.edu.icesi.ci.thymeval.service.UserService;

@Controller
public class UserController {

	UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users/")
	public String indexUser(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users/index";
	}
	
	@GetMapping("/users/add")
	public String addUser2(Model model) {
		model.addAttribute("user", new User());
		return "users/add-user1";
	}
	
	@PostMapping("/users/add")
	public String saveUser(@Validated({firstValidator.class}) User user, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action, Model model) {	
		model.addAttribute("genders", userService.getGenders());
		model.addAttribute("types", userService.getTypes());
		
		if(bindingResult.hasErrors()) {
			return "users/add-user1";
		}
		
		if (!action.equals("Cancel"))
			userService.save(user);
			return "users/add-user2";
	}
	
	@PostMapping("/users/add1")
	public String saveUser1(@Validated({secondValidator.class}) User user, BindingResult bindingResult, @RequestParam(value = "action", required = true) String action, Model model) {
		Optional<User> uOptional = userService.findById(user.getId());
		User user2 = uOptional.get();
		user.setEmail(user2.getEmail());
		user.setName(user2.getName());
		user.setBirthDate(user2.getBirthDate());
		
		if(bindingResult.hasErrors()) {
			return "users/add-user1";
		}
		
		if (!action.equals("Cancel"))
			userService.save(user);
		return "redirect:/users/";
	}

	@GetMapping("/users/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Optional<User> user = userService.findById(id);
		if (user == null)
			throw new IllegalArgumentException("Invalid user Id:" + id);
		model.addAttribute("user", user.get());
		model.addAttribute("genders", userService.getGenders());
		model.addAttribute("types", userService.getTypes());
		return "users/update-user";
	}

	@PostMapping("/users/edit/{id}")
	public String updateUser(@PathVariable("id") long id,
			@RequestParam(value = "action", required = true) String action, @Validated User user, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "users/update-user";
		}
		
		if (action != null && !action.equals("Cancel")) {
			userService.save(user);
		}
		return "redirect:/users/";
	}

	@GetMapping("/users/del/{id}")
	public String deleteUser(@PathVariable("id") long id) {
		User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userService.delete(user);
		return "redirect:/users/";
	}
}
