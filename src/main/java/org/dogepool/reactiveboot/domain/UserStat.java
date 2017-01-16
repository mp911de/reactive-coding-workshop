package org.dogepool.reactiveboot.domain;

import lombok.Value;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Mark Paluch
 */
@Value
@Document
public class UserStat {

	String userId;

	double hashrate;

	long totalCoinsMined;
}
