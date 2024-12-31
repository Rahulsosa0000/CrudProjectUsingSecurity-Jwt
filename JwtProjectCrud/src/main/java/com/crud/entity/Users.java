package com.crud.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


	

@Entity
@Table(name = "users2")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;

	private String username;

	private String password;

	@Column(name = "mobile_no")
	private String mobileNo;
	
	private String token; 
	

	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Users(Integer id, String username, String password, String mobileNo,String token) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.mobileNo = mobileNo;
		this.token = token;
    

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    


}
