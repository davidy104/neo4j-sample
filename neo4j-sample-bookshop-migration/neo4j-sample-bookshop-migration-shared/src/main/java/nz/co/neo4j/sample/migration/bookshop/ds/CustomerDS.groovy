package nz.co.neo4j.sample.migration.bookshop.ds;

import nz.co.neo4j.sample.migration.bookshop.NotFoundException
import nz.co.neo4j.sample.migration.bookshop.data.Customer

public interface CustomerDS {
	Customer createCustomer(Customer customer)
	Customer getCustomerByName(String lastName,String firstName)
	Customer getCustomerById(Long customerId)
	void deleteCustomer(Long customerId)throws NotFoundException
	Customer updateCustomer(Long customerId,Customer updateCustomer)throws NotFoundException
}
