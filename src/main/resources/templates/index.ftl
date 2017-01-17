<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html
		PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Welcome to ${poolName} dogecoin mining pool</title>
	<link rel="stylesheet" type="text/css" href="/css/semantic.min.css"/>
	<style type="text/css">
		.main.container {
			margin-top: 2em;
		}

		#filter-form {
			float: right;
		}
	</style>
</head>
<body>

<div class="ui main container">

	<h1 class="ui header">Welcome to ${poolName} dogecoin mining pool</h1>
	<p>${miningUserCount} users currently mining, for a global hashrate of ${gigaHashrate}
		GHash/s</p>
	<p>
	</p>
	<p>
	<div class="ui divider"></div>
	<div class="ui input" id="filter-form">
		<form action="/">
			<input name="hashrateFilter" type="number" placeholder="Hashrate greater"/>
			<button class="ui primary basic button" type="submit">Apply filter</button>
		</form>
	</div>
	<h2 class="ui header">TOP 10 Miners by Hashrate </h2>
	<div class="ui ordered list">

    <#list hashLadder as userStat>
		<div class="item">
			<div class="content">
				<a href="/miner/${userStat.userId}" class="header">${userStat.userId}</a>
				<div class="description">${userStat.hashrate} GHash/s</div>
			</div>
		</div>
    </#list>
	</div>

	<div class="ui divider"></div>
	<h2 class="ui header">TOP 10 Miners by Coins Found</h2>
	<div class="ui ordered list">
    <#list coinsLadder as userStat>
		<div class="item">
			<div class="content">
				<a href="/miner/${userStat.userId}" class="header">${userStat.userId}</a>
				<div class="description">${userStat.totalCoinsMined} dogecoins</div>
			</div>
		</div>
    </#list>
	</div>
</div>


</body>
</html>
