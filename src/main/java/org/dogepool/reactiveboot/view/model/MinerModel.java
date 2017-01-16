/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dogepool.reactiveboot.view.model;

import lombok.Value;
import org.dogepool.reactiveboot.domain.User;
import org.dogepool.reactiveboot.domain.UserStat;

/**
 * @author Mark Paluch
 */
@Value
public class MinerModel {

	String displayName;

	String nickname;

	String bio;

	long rankByCoins;

	long rankByHash;

	String avatarUrl;

	String smallAvatarUrl;

	public static MinerModel of(User user, UserStat userStat) {

		MinerModel minerModel = new MinerModel(user.getDisplayName(), user.getNickname(),
				null, userStat.getRankByCoins(), userStat.getRankByHash(), user
						.getUserProfile().getAvatarUrl(), user.getUserProfile()
						.getSmallAvatarUrl());
		return minerModel;
	}
}
