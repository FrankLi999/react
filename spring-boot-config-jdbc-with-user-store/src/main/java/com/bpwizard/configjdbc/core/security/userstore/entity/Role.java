package com.bpwizard.configjdbc.core.security.userstore.entity;


import java.util.Set;

import com.bpwizard.configjdbc.core.security.UserUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Table(name="role", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Getter @Setter
@NoArgsConstructor
public class Role extends SpringEntity<Long> {

    private static final long serialVersionUID = -450270638531584010L;
    public static final int NAME_MIN = 1;
    public static final int NAME_MAX = 50;

    @JsonView(UserUtils.SignupInput.class)
    @NotBlank(message = "{blank.name}", groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    @Size(min=NAME_MIN, max=NAME_MAX, groups = {UserUtils.SignUpValidation.class, UserUtils.UpdateValidation.class})
    // @Column(nullable = false, length = NAME_MAX)
    protected String name;

    // @Column(nullable = false, length = NAME_MAX)
    protected String type = "SYSTEM";

    // @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    protected Set<String> users;
    protected Set<String> roles;
    // @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(name = "tenant_membership",
    //  joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="id", nullable=true) },
    //  inverseJoinColumns = { @JoinColumn(name = "tenant_id", referencedColumnName="id", nullable=true) })
    protected Set<String> tenants;
}
