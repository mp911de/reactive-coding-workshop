package org.dogepool.reactiveboot.view.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.dogepool.reactiveboot.domain.UserStat;

/**
 * @author Mark Paluch
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndexModel {

	List<UserStat> hashLadder;

	List<UserStat> coinsLadder;

	String poolName;

	int miningUserCount;

	Double gigaHashrate;

}
