package com.luv2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.service.CustomerService;
import com.luv2code.springdemo.util.SortUtils;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	// need to inject the customer dao
	@Autowired
	CustomerService customerService;

	@GetMapping("/list")
	public String listCutomer(Model theModel, @RequestParam(required = false) String sort) {

		// get the customers from the customer service

		List<Customer> theCustomers = null;

		if (sort != null) {
			int theSortField = Integer.parseInt(sort);
			theCustomers = customerService.getCustomers(theSortField);
		} else {
			int theSortField = SortUtils.FIRST_NAME;
			theCustomers = customerService.getCustomers(theSortField);
		}

		// add the cusotmers to the model

		theModel.addAttribute("customers", theCustomers);

		return "list-customers";
	}

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		Customer theCustomer = new Customer();
		theModel.addAttribute("customer", theCustomer);
		return "customer-form";
	}

	@GetMapping("/search")
	public String searchCustomers(@RequestParam("theSearchName") String theSearchName, Model theModel) {

		List<Customer> theCustomers = customerService.searchCusotmers(theSearchName);
		theModel.addAttribute("customers", theCustomers);
		return "list-customers";
	}

	@PostMapping("/saveCustomer")
	String saveCustomer(@ModelAttribute("customer") Customer theCustomer) {

		// save the customer using our service
		customerService.saveCustomer(theCustomer);

		return "redirect:/customer/list";
	}

	@GetMapping("/delete")
	String deleteCustomer(@RequestParam("customerId") int theId, Model theModel) {

		// delete the customer using our service
		customerService.deleteCustomer(theId);

		return "redirect:/customer/list";
	}

	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int theId, Model theModel) {

		// get the customer from the database

		Customer theCustomer = customerService.getCustomer(theId);

		// set customer ad model attribute to pre-populate the form

		theModel.addAttribute("customer", theCustomer);

		// send over to our form

		return "customer-form";
	}
}
