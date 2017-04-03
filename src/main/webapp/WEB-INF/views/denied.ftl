<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<link rel="stylesheet" type="text/css" href="/resources/styles/css/style.css" />
</head>
<body>
	<div class="container">
		<header>
			<h1>
				Access Denied
				<div class="logout">
					<span id="currentUserLogin"> anonim </span> <a href="/logoutPage"> <i class="icon-off"></i>
					</a>
				</div>
			</h1>
		</header>
		<div class="alert alert-error">
			<div>
				<strong>Okay, Houston, we've had a problem here.</strong>
			</div>
			<ul>
				<li>Sorry, You don't have permission to this page</li>
			</ul>
		</div>
		<div class="form-horizontal">
			<div class="form-actions">
				<input class="btn" type="button" value="Back" onClick="history.back()">
			</div>
		</div>
	</div>
</body>
</html>
