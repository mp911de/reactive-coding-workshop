package org.dogepool.reactiveboot.view.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Mark Paluch
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinerModel {

	String smallAvatarUrl;

	String displayName;

	String nickname;

	String bio;

	String avatarUrl;

	long rankByCoins;

	long rankByHash;

}
