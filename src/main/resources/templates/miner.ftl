<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html
		PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Miner ${model.displayName}</title>
	<link rel="stylesheet" type="text/css" href="/css/semantic.min.css"/>
	<style type="text/css">
		.main.container {
			margin-top: 2em;
		}
	</style>
</head>
<body>

<div class="ui main container">
	<div class="ui card">
		<a class="image" href="#">
			<img src="${model.avatarUrl}" width=50 height=50/>
		</a>
		<div class="content">
			<div class="header">
				<div class="right floated author"><img src="${model.smallAvatarUrl}"
													   class="ui avatar image"/> ${model.nickname}
				</div>
            ${model.displayName}
			</div>
		</div>
		<div class="extra content">
    <span class="left floated like">
      Rank by Coins: ${model.rankByCoins}
    </span>
			<span class="right floated star">
      Rank by Hashrate: ${model.rankByHash}
    </span>
		</div>
	</div>
</div>
</body>
</html>
