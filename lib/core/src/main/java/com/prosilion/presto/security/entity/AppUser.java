package com.prosilion.presto.security.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Extendable/customizable user, indirectly bound to Spring Security Authentication
 * and Authorization user via join service AuthUserServiceImpl
 */
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "appuser")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AppUser implements CustomizableAppUser<AppUser> {

  @EqualsAndHashCode.Exclude
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public abstract AppUser getInstantiatedCustomAppUserType();
}
