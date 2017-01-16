package org.dogepool.reactiveboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Mark Paluch
 */
@AllArgsConstructor
@Value
@Builder
@Document
public class User {

	String id;

	@NonNull
	String nickname;

	@NonNull
	String displayName;

	@NonNull
	UserProfile userProfile;
}
