package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.util.SortUtils;

@Repository
public class CustoomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Customer> getCusotmers(int theSortField) {

		String theFieldName;

		switch (theSortField) {

		case SortUtils.FIRST_NAME:
			theFieldName = "firstName";
			break;
		case SortUtils.LAST_NAME:
			theFieldName = "lastName";
			break;
		case SortUtils.EMAIL:
			theFieldName = "email";
			break;
		default:
			theFieldName = "lastName";

		}
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// create a query ... sort by last name
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by " + theFieldName, Customer.class);

		// get the list of customers from the query
		List<Customer> customers = theQuery.getResultList();

		// return the results

		return customers;
	}

	@Override
	public List<Customer> searchCusotmers(String theSearchName) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Customer> theQuery = null;
		if (theSearchName != null && theSearchName.trim().length() > 0) {
			// search for firstName or lastName ... case insensitive
			theQuery = currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		} else {
			// theSearchName is empty ... so just get all customers
			theQuery = currentSession.createQuery("from Customer", Customer.class);

		}
		// get the list of customers from the query
		List<Customer> customers = theQuery.getResultList();

		// return the results

		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {

		// get current hibernate session

		Session currentSession = sessionFactory.getCurrentSession();

		// save/update the customer ... finally LOL

		currentSession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {

		// get current hibernate session

		Session currentSession = sessionFactory.getCurrentSession();

		// now retrieve/read from database using the primary key

		Customer theCustomer = currentSession.get(Customer.class, theId);
//		Customer theCustomer = currentSession.get(Customer.class, Integer.toString(theId));
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {

		// get current hibernate session

		Session currentSession = sessionFactory.getCurrentSession();

		// delete the customer ... finally LOL

		Query theQuery = currentSession.createQuery("delete from Customer where id=:customerId");

		theQuery.setParameter("customerId", theId);

		theQuery.executeUpdate();

//		currentSession.delete(theId);
	}

}
