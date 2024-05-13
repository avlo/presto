package com.prosilion.presto.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Join table between AuthDetailsUser (used for auth & auth) and AppUser (used for
 * application specific business logic.
 */
@Getter
@NoArgsConstructor
@Entity(name = "appuser_authuser")
public class AppUserAuthUser implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long appUserId;
	private String authUserName;

	public AppUserAuthUser(@NonNull Long appUserId, @NonNull String authUserName) {
		this.appUserId = appUserId;
		this.authUserName = authUserName;
	}
}
