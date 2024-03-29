package com.bpwizard.configjdbc.core.security.userstore.entity;


import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.bpwizard.configjdbc.core.security.AuthProvider;
import com.bpwizard.configjdbc.core.security.UserUtils;
import com.bpwizard.configjdbc.core.security.model.UserDto;
import com.bpwizard.configjdbc.core.validation.Captcha;
import com.bpwizard.configjdbc.core.validation.Password;
import com.bpwizard.configjdbc.core.validation.UniqueEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * Base class for User entity
 */
//@Entity
//@Table(name="usr", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter @Setter
@NoArgsConstructor
public class User<ID extends Serializable> extends SpringEntity<ID> implements SpringUser<ID> {

    private static final long serialVersionUID = 2862969164717685466L;
    public static final int NAME_MIN = 1;
    public static final int NAME_MAX = 50;


    public User(String email, String password, String name, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Getter @Setter @ToString
    public static class Tag implements Serializable {

        private static final long serialVersionUID = -2129078111926834670L;
        private String name;
    }

    // email
    @JsonView(UserUtils.SignupInput.class)
    @UniqueEmail(groups = {UserUtils.SignUpValidation.class})
    // @Column(nullable = false, unique=true, length = UserUtils.EMAIL_MAX)
    protected String email;

    // password
    @JsonView(UserUtils.SignupInput.class)
    @Password(groups = {UserUtils.SignUpValidation.class, UserUtils.ChangeEmailValidation.class})
    // @Column(nullable = false) // no length because it will be encrypted
    protected String password;

    protected String salt;

    protected String newPassword;

    // @Temporal(TemporalType.TIMESTAMP)
    protected Date lockExpirationTime;

    protected int attempts;

    //	// roles collection
//	@ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name="usr_role", joinColumns=@JoinColumn(name="user_id"))
//    @Column(name="role")
//	protected Set<String> roles = new HashSet<>();
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "user_role",
//	    joinColumns = @JoinColumn(name = "user_id"),
//	    inverseJoinColumns = @JoinColumn(name = "role_id"))
    protected Set<String> roles = new HashSet<>();

    //	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "tenant_membership",
//    joinColumns = { @JoinColumn(name = "user_id", referencedColumnName="id", nullable=true) },
//    inverseJoinColumns = { @JoinColumn(name = "tenant_id", referencedColumnName="id", nullable=true) })
    protected Set<String> tenants;

    @JsonView(UserUtils.SignupInput.class)
    // @NotBlank(message = "{blank.name}", groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    // @Size(min=NAME_MIN, max=NAME_MAX, groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    // @Column(nullable = false, length = NAME_MAX)
    // @Column(length = NAME_MAX)
    protected String firstName;


    @JsonView(UserUtils.SignupInput.class)
    // @NotBlank(message = "{blank.name}", groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    // @Size(min=NAME_MIN, max=NAME_MAX, groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    // @Column(nullable = false, length = NAME_MAX)
    // @Column(length = NAME_MAX)
    protected String lastName;


    // in the email-change process, temporarily stores the new email
    @UniqueEmail(groups = {UserUtils.ChangeEmailValidation.class})
    // @Column(length = UserUtils.EMAIL_MAX)
    protected String newEmail;

    // A JWT issued before this won't be valid
    // @Column(nullable = false)
    @JsonIgnore
    protected long credentialsUpdatedMillis = System.currentTimeMillis();

    // holds reCAPTCHA response while signing up
    // @Transient
    @JsonView(UserUtils.SignupInput.class)
    @Captcha(groups = {UserUtils.SignUpValidation.class})
    protected String captchaResponse;

    @JsonView(UserUtils.SignupInput.class)
    @NotBlank(message = "{blank.name}", groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    @Size(min=NAME_MIN, max=NAME_MAX, groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    // @Column(nullable = false, length = NAME_MAX)
    protected String name;

    private String imageUrl;
    // @Column(nullable = false)
    protected Boolean emailVerified = false;

    // @NotNull
    // @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.local;

    private String providerId;

    public final boolean hasRole(String role) {
        return roles.stream().anyMatch(r -> r.equals(role));
    }

    /**
     * Called by spring security permission evaluator
     * to check whether the current-user has the given permission
     * on this entity.
     */
    @Override
    public boolean hasPermission(UserDto currentUser, String permission) {
        return UserUtils.hasPermission(getId(), currentUser, permission);
    }

    /**
     * A convenient toString method
     */
    @Override
    public String toString() {
        return "AbstractUser [email=" + email + ", roles=" + roles + "]";
    }

    /**
     * Makes a User DTO
     */
    public UserDto toUserDto() {

        UserDto userDto = new UserDto();

        userDto.setId(getId().toString());
        userDto.setUsername(email);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPassword(password);
        userDto.setEmail(email);
        userDto.setEmailVerified(this.emailVerified);
        userDto.setImageUrl(this.imageUrl);
        userDto.setProvider((null == this.provider) ? null : this.provider.name());
        userDto.setProviderId(this.providerId);


        // roles would be org.hibernate.collection.internal.PersistentSet,
        // which is not in another microservices not having Hibernate.
        // So, let's convert it to HashSet
        Set<String> userRoles = (this.roles == null || this.roles.size() == 0) ?
                Collections.emptySet()
                : this.getRoles();
//				this.roles.stream();
        //.map(role -> new SpringGrantedAuthority("ROLE_" + role))
//				.map(Role::getName)
//				.collect(Collectors.toCollection(() ->
//					new HashSet<String>(this.roles.size())));

        userDto.setRoles(userRoles);

        userDto.setTag(toTag());

        userDto.initialize();

        return userDto;
    }

    public Tag toTag() {

        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }

}
