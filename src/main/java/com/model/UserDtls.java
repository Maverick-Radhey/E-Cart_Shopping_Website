package com.model;



import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserDtls {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String number;
	
	private String email;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String pincode;
	
	private String password;
	
	private String profile_image;

	private String role;

	private Boolean isEnable;
	
	private Boolean accountNonLocked;
	
	private Integer failedAttemp;
	
	private Date lockTime;
	
	private String resetToken;

	public UserDtls() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDtls(int id, String name, String number, String email, String address, String city, String state,
			String pincode, String password, String profile_image, String role, Boolean isEnable,
			Boolean accountNonLocked, Integer failedAttemp, Date lockTime, String resetToken) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
		this.email = email;
		this.address = address;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.password = password;
		this.profile_image = profile_image;
		this.role = role;
		this.isEnable = isEnable;
		this.accountNonLocked = accountNonLocked;
		this.failedAttemp = failedAttemp;
		this.lockTime = lockTime;
		this.resetToken = resetToken;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfile_image() {
		return profile_image;
	}

	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Integer getFailedAttemp() {
		return failedAttemp;
	}

	public void setFailedAttemp(Integer failedAttemp) {
		this.failedAttemp = failedAttemp;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	@Override
	public String toString() {
		return "UserDtls [id=" + id + ", name=" + name + ", number=" + number + ", email=" + email + ", address="
				+ address + ", city=" + city + ", state=" + state + ", pincode=" + pincode + ", password=" + password
				+ ", profile_image=" + profile_image + ", role=" + role + ", isEnable=" + isEnable
				+ ", accountNonLocked=" + accountNonLocked + ", failedAttemp=" + failedAttemp + ", lockTime=" + lockTime
				+ ", resetToken=" + resetToken + "]";
	}



	
}
