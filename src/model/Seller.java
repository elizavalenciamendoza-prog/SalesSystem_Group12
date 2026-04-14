package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a seller.
 */
public class Seller {
	private String documentType;
	private long id;
	private String firstName;
	private String lastName;
	private List<Sale> sales;

    public Seller(String documentType, long id, String firstName, String lastName) {
        this.documentType = documentType;
    	this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
		this.sales = new ArrayList<>();
    }

	public String getDocumentType() {
		return documentType;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void addSale(Sale sale) {
		this.sales.add(sale);
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void printInfo() {
		System.out.println("Tipo de Documento: " + documentType + " | ID: " + id + " | Nombre: " + firstName + " " + lastName);
		
		if (!sales.isEmpty()) {
			System.out.println("Ventas:");
			for (Sale sale : sales) {
				sale.printInfo();
			}
		}
	}
}
