package co.edu.icesi.ci.thymeval.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class User {
	
	public interface firstValidator {};
	
	public interface secondValidator {};
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;
	
	@NotBlank(groups=firstValidator.class)
	@Size(min=2, groups=firstValidator.class)
	private String name;
	
	@Email(groups=firstValidator.class)
	@NotBlank(groups=firstValidator.class)
	private String email;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(groups=firstValidator.class)
	@Past(groups=firstValidator.class)
	private LocalDate birthDate;
	
	@NotNull(groups=secondValidator.class)
	private UserType type;
	
	@NotNull(groups=secondValidator.class)
	private UserGender gender;
	
//	@OneToMany
//	private List<Appointment> appointments;
}
