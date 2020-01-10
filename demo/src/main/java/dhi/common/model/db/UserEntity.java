package dhi.common.model.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_USER")
public class UserEntity {

	@Id
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="USER_NM")
	private String userName;
	
	@Column(name="USER_PWD")
	private String userPassword;
	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="ENABLED")
	private Boolean enabled;
	
	@Column(name="REG_DATE")
	private Date regDate;
	
	@Column(name="PWD_UPDATE_YN")
	private String passwordUpdateYn;
	
	@Column(name="DEFAULT_P_UNIT_ID")
	private String defaultPlantUnitId;
	
	public UserEntity() {}
	
	public UserEntity(String userId, String userName, String roleId, Boolean enabled, String defaultPlantUnitId, Date regDate)
	{
		this.userId = userId;
		this.userName = userName;
		this.roleId = roleId;
		this.enabled = enabled;
		this.defaultPlantUnitId = defaultPlantUnitId;
		this.regDate = regDate;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getPasswordUpdateYn() {
		return passwordUpdateYn;
	}

	public void setPasswordUpdateYn(String passwordUpdateYn) {
		this.passwordUpdateYn = passwordUpdateYn;
	}		
	
	public String getDefaultPlantUnitId() {
		return defaultPlantUnitId;
	}

	public void setDefaultPlantUnitId(String defaultPlantUnitId) {
		this.defaultPlantUnitId = defaultPlantUnitId;
	}

	public Collection<GrantedAuthority> getAuthorities()
	{
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(this.roleId));
		
		return authorities;
	}
}
