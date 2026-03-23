package model;

/**
 * Represents a seller.
 */
public class Seller {
	private String documentType;
	private long id;
	private String firstName;
	private String lastName;

    public Seller(String documentType, long id, String firstName, String lastName) {
        this.documentType = documentType;
    	this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
