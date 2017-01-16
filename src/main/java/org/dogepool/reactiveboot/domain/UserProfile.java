package org.dogepool.reactiveboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Mark Paluch
 */
@Value
@AllArgsConstructor
@Builder
public class UserProfile {

	@NonNull
	String avatarUrl;

	@NonNull
	String smallAvatarUrl;
}
