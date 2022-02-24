package serialize;

import com.fasterxml.jackson.annotation.JsonProperty;

public  class JSONTests{

public static class User {
	@JsonProperty
	public String firstName;

	@JsonProperty
	public String lastName;

	@JsonProperty
	public short age;

	public User() {
	}

	public User(String firstName, String lastName, short age) {
		//this(firstName, lastName, age, null);
	}
}
}